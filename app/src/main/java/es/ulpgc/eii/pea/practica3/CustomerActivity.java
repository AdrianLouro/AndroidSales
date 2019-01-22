package es.ulpgc.eii.pea.practica3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import es.ulpgc.eii.pea.practica3.dialogs.DeleteEntityConfirmDialog;
import es.ulpgc.eii.pea.practica3.dialogs.EntityHasOrdersDialog;
import es.ulpgc.eii.pea.practica3.dialogs.FillEveryFieldDialog;
import es.ulpgc.eii.pea.practica3.dialogs.HttpErrorDialog;
import es.ulpgc.eii.pea.practica3.entities.Customer;
import es.ulpgc.eii.pea.practica3.entities.Entity;
import es.ulpgc.eii.pea.practica3.fragments.CustomerFragment;
import es.ulpgc.eii.pea.practica3.http.soap.SoapCustomerCreator;
import es.ulpgc.eii.pea.practica3.http.soap.SoapCustomerDeleter;
import es.ulpgc.eii.pea.practica3.http.soap.SoapCustomerEditor;
import es.ulpgc.eii.pea.practica3.interfaces.EntityCreator;
import es.ulpgc.eii.pea.practica3.interfaces.EntityDeleter;
import es.ulpgc.eii.pea.practica3.interfaces.EntityEditor;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.support.design.widget.Snackbar.make;
import static android.support.v4.app.NavUtils.navigateUpFromSameTask;
import static android.widget.Toast.makeText;

public class CustomerActivity extends AppCompatActivity implements EntityDeleter, EntityEditor, EntityCreator {

    private Customer customer;
    private Customer oldCustomer;
    private CustomerFragment customerFragment;
    private Menu menu;
    private boolean shouldUnedit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        customerFragment = (CustomerFragment) getFragmentManager().findFragmentById(R.id.fragment_customer);

        if ((customer = (Customer) getIntent().getSerializableExtra("entity")) == null) return;
        setCustomerInUI();
        oldCustomer = (Customer) customer.clone();
    }

    @Override
    public void onBackPressed() {
        navigateUpFromSameTask(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.entity_navigation_drawer, menu);
        if (customer == null) menu.findItem(R.id.delete_button).setVisible(false);
        this.menu = menu;
        setSaveButtonListener(menu.findItem(R.id.save_button));
        setDeleteButtonListener(menu.findItem(R.id.delete_button));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) navigateUpFromSameTask(this);
        return true;
    }

    private void setDeleteButtonListener(MenuItem deleteButton) {
        deleteButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                deleteCustomer();
                return true;
            }
        });
    }

    private void setSaveButtonListener(MenuItem saveButton) {
        saveButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (fieldsAreIncomplete())
                    new FillEveryFieldDialog(CustomerActivity.this).showDialog();
                else if (customer == null) createCustomer();
                else editCustomer();
                return true;
            }
        });
    }

    private boolean fieldsAreIncomplete() {
        return customerFragment.customerName().isEmpty() || customerFragment.customerAddress().isEmpty();
    }

    private void createCustomer() {
        new SoapCustomerCreator(getApplicationContext(), this).execute(customer = new Customer(
                customerFragment.customerName(),
                customerFragment.customerAddress()
        ));
    }

    private void editCustomer() {
        oldCustomer = (Customer) customer.clone();
        setCustomerFromUI();
        if (customer.equals(oldCustomer))
            makeText(this, R.string.noChanges, Toast.LENGTH_SHORT).show();
        else
            new SoapCustomerEditor(getApplicationContext(), this).execute(customer);
    }

    private void editOldCustomer() {
        shouldUnedit = false;
        setOldCustomerInUI();
        customer = (Customer) oldCustomer.clone();
        new SoapCustomerEditor(getApplicationContext(), this).execute(oldCustomer);
    }

    private void deleteCustomer() {
        new DeleteEntityConfirmDialog(this, this, customer).showDialog();
    }

    @Override
    public void confirmDeletion(Entity entity) {
        new SoapCustomerDeleter(getApplicationContext(), this).execute((Customer) entity);
    }

    private void setCustomerInUI() {
        customerFragment.customerName(customer == null ? "" : customer.name());
        customerFragment.customerAddress(customer == null ? "" : customer.address());
    }

    private void setOldCustomerInUI() {
        customerFragment.customerName(oldCustomer.name());
        customerFragment.customerAddress(oldCustomer.address());
    }

    private void setCustomerFromUI() {
        customer.name(customerFragment.customerName());
        customer.address(customerFragment.customerAddress());
    }

    @Override
    public void createEntityCallback(final Entity entity) {
        if (entity == null) {
            new HttpErrorDialog(this).showDialog();
            customer = null;
            return;
        }

        menu.findItem(R.id.delete_button).setVisible(true);
        oldCustomer = (Customer) customer.clone();
        make(findViewById(R.id.customer_activity), R.string.entityCreated, LENGTH_LONG).setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDeletion(entity);
            }
        }).show();
    }

    @Override
    public void editEntityCallback(Entity entity) {
        if (entity == null) {
            new HttpErrorDialog(this).showDialog();
            customer = oldCustomer;
            return;
        }

        if (shouldUnedit) showUndoSnackbar();
        else showUndoneSnackbar();
    }

    private void showUndoneSnackbar() {
        shouldUnedit = true;
        make(findViewById(R.id.customer_activity), R.string.undone, LENGTH_LONG).show();
    }

    private void showUndoSnackbar() {
        make(findViewById(R.id.customer_activity), R.string.entityEdited, LENGTH_LONG).setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editOldCustomer();
            }
        }).show();
    }

    @Override
    public void deleteEntityCallback(Entity entity, boolean hasAssociatedOrders) {
        if (entity == null) {
            if (hasAssociatedOrders) new EntityHasOrdersDialog(this).showDialog();
            else new HttpErrorDialog(this).showDialog();
            return;
        }

        customer = null;
        oldCustomer = null;
        menu.findItem(R.id.delete_button).setVisible(false);
        setCustomerInUI();
        makeText(this, R.string.entityDeleted, Toast.LENGTH_SHORT).show();
    }

}
