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
import es.ulpgc.eii.pea.practica3.entities.Entity;
import es.ulpgc.eii.pea.practica3.entities.Product;
import es.ulpgc.eii.pea.practica3.fragments.ProductFragment;
import es.ulpgc.eii.pea.practica3.http.json.JsonEntityCreator;
import es.ulpgc.eii.pea.practica3.http.json.JsonEntityDeleter;
import es.ulpgc.eii.pea.practica3.http.json.JsonEntityEditor;
import es.ulpgc.eii.pea.practica3.interfaces.EntityCreator;
import es.ulpgc.eii.pea.practica3.interfaces.EntityDeleter;
import es.ulpgc.eii.pea.practica3.interfaces.EntityEditor;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.support.design.widget.Snackbar.make;
import static android.support.v4.app.NavUtils.navigateUpFromSameTask;
import static android.widget.Toast.makeText;
import static java.lang.Float.parseFloat;
import static java.lang.String.valueOf;

public class ProductActivity extends AppCompatActivity implements EntityDeleter, EntityEditor, EntityCreator {

    private Product product;
    private Product oldProduct;
    private ProductFragment productFragment;
    private Menu menu;
    private boolean shouldUnedit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        productFragment = (ProductFragment) getFragmentManager().findFragmentById(R.id.fragment_product);

        if ((product = (Product) getIntent().getSerializableExtra("entity")) == null) return;
        setProductInUI();
        oldProduct = (Product) product.clone();
    }

    @Override
    public void onBackPressed() {
        navigateUpFromSameTask(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.entity_navigation_drawer, menu);
        if (product == null) menu.findItem(R.id.delete_button).setVisible(false);
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
                deleteProduct();
                return true;
            }
        });
    }

    private void setSaveButtonListener(MenuItem saveButton) {
        saveButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (fieldsAreIncomplete())
                    new FillEveryFieldDialog(ProductActivity.this).showDialog();
                else if (product == null) createProduct();
                else editProduct();
                return true;
            }
        });
    }

    private boolean fieldsAreIncomplete() {
        try {
            parseFloat(productFragment.productPrice());
        } catch (NumberFormatException e) {
            return true;
        }
        return productFragment.productName().isEmpty() || productFragment.productDescription().isEmpty();
    }

    private void createProduct() {
        new JsonEntityCreator<>(getApplicationContext(), Product.class, this, product = new Product(
                productFragment.productName(),
                productFragment.productDescription(),
                parseFloat(productFragment.productPrice())
        )).execute();
    }

    private void editProduct() {
        oldProduct = (Product) product.clone();
        setProductFromUI();
        if (product.equals(oldProduct))
            makeText(this, R.string.noChanges, Toast.LENGTH_SHORT).show();
        else
            new JsonEntityEditor<>(getApplicationContext(), Product.class, this, product).execute();
    }

    private void editOldProduct() {
        shouldUnedit = false;
        setOldProductInUI();
        product = (Product) oldProduct.clone();
        new JsonEntityEditor<>(getApplicationContext(), Product.class, this, oldProduct).execute();
    }

    private void deleteProduct() {
        new DeleteEntityConfirmDialog(this, this, product).showDialog();
    }

    @Override
    public void confirmDeletion(Entity entity) {
        new JsonEntityDeleter<>(getApplicationContext(), Product.class, this, entity).execute();
    }

    private void setProductInUI() {
        productFragment.productName(product == null ? "" : product.name());
        productFragment.productDescription(product == null ? "" : product.description());
        productFragment.productPrice(product == null ? "" : valueOf(product.price()));
    }

    private void setOldProductInUI() {
        productFragment.productName(oldProduct.name());
        productFragment.productDescription(oldProduct.description());
        productFragment.productPrice(valueOf(oldProduct.price()));
    }

    private void setProductFromUI() {
        product.name(productFragment.productName());
        product.description(productFragment.productDescription());
        product.price(parseFloat(productFragment.productPrice()));
    }

    @Override
    public void createEntityCallback(final Entity entity) {
        if (entity == null) {
            new HttpErrorDialog(this).showDialog();
            product = null;
            return;
        }

        menu.findItem(R.id.delete_button).setVisible(true);
        oldProduct = (Product) product.clone();
        make(findViewById(R.id.product_activity), R.string.entityCreated, LENGTH_LONG).setAction(R.string.undo, new View.OnClickListener() {
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
            product = oldProduct;
            return;
        }

        if (shouldUnedit) showUndoSnackbar();
        else showUndoneSnackbar();
    }

    private void showUndoneSnackbar() {
        shouldUnedit = true;
        make(findViewById(R.id.product_activity), R.string.undone, LENGTH_LONG).show();
    }

    private void showUndoSnackbar() {
        make(findViewById(R.id.product_activity), R.string.entityEdited, LENGTH_LONG).setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editOldProduct();
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

        product = null;
        oldProduct = null;
        menu.findItem(R.id.delete_button).setVisible(false);
        setProductInUI();
        makeText(this, R.string.entityDeleted, Toast.LENGTH_SHORT).show();
    }

}
