package es.ulpgc.eii.pea.practica3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import es.ulpgc.eii.pea.practica3.dialogs.DeleteEntityConfirmDialog;
import es.ulpgc.eii.pea.practica3.dialogs.EntityHasOrdersDialog;
import es.ulpgc.eii.pea.practica3.dialogs.FillEveryFieldDialog;
import es.ulpgc.eii.pea.practica3.dialogs.HttpErrorDialog;
import es.ulpgc.eii.pea.practica3.entities.Customer;
import es.ulpgc.eii.pea.practica3.entities.Entity;
import es.ulpgc.eii.pea.practica3.entities.Order;
import es.ulpgc.eii.pea.practica3.entities.Product;
import es.ulpgc.eii.pea.practica3.fragments.OrderFragment;
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
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

public class OrderActivity extends AppCompatActivity implements EntityDeleter, EntityEditor, EntityCreator {

    private Order order;
    private Order oldOrder;
    private Customer selectedCustomer;
    private Product selectedProduct;
    private OrderFragment orderFragment;
    private Menu menu;
    private boolean shouldUnedit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        orderFragment = (OrderFragment) getFragmentManager().findFragmentById(R.id.fragment_order);
        setQuantityInputListener();
        setSearchButtonsListeners();

        if ((order = (Order) getIntent().getSerializableExtra("entity")) == null) return;
        selectedCustomer = order.customer();
        selectedProduct = order.product();
        setOrderInUI();
        oldOrder = (Order) order.clone();
    }

    @Override
    public void onBackPressed() {
        navigateUpFromSameTask(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.entity_navigation_drawer, menu);
        if (order == null) menu.findItem(R.id.delete_button).setVisible(false);
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
                deleteOrder();
                return true;
            }
        });
    }

    private void setSaveButtonListener(MenuItem saveButton) {
        saveButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (fieldsAreIncomplete())
                    new FillEveryFieldDialog(OrderActivity.this).showDialog();
                else if (order == null) createOrder();
                else editOrder();
                return true;
            }
        });
    }

    private boolean fieldsAreIncomplete() {
        try {
            parseInt(orderFragment.orderQuantity());
        } catch (NumberFormatException e) {
            return true;
        }

        return orderFragment.orderCode().isEmpty() ||
                selectedCustomer == null ||
                selectedProduct == null;
    }

    private void createOrder() {
        order = new Order(
                orderFragment.orderCode(),
                orderFragment.orderDate(),
                parseInt(orderFragment.orderQuantity()),
                selectedCustomer,
                selectedProduct
        );
        new JsonEntityCreator<>(getApplicationContext(), Order.class, this, order).execute();
    }

    private void editOrder() {
        oldOrder = (Order) order.clone();
        setOrderFromUI();
        if (order.equals(oldOrder))
            makeText(this, R.string.noChanges, Toast.LENGTH_SHORT).show();
        else
            new JsonEntityEditor<>(getApplicationContext(), Order.class, this, order).execute();
    }

    private void editOldOrder() {
        shouldUnedit = false;
        setOldOrderInUI();
        order = (Order) oldOrder.clone();
        new JsonEntityEditor<>(getApplicationContext(), Order.class, this, oldOrder).execute();
    }

    private void deleteOrder() {
        new DeleteEntityConfirmDialog(this, this, order).showDialog();
    }

    @Override
    public void confirmDeletion(Entity entity) {
        new JsonEntityDeleter<>(getApplicationContext(), Order.class, this, entity).execute();
    }

    private void setOrderInUI() {
        orderFragment.orderCode(order == null ? "" : order.code());
        if (order != null) orderFragment.orderDate(order.date());
        orderFragment.orderCustomer(order == null ? "" : order.customer().name());
        orderFragment.orderProduct(order == null ? "" : order.product().name());
        orderFragment.orderQuantity(order == null ? "" : valueOf(order.quantity()));
        orderFragment.orderPrice(order == null ? "" : orderTotalPrice());
    }

    private void setOldOrderInUI() {
        orderFragment.orderCode(oldOrder.code());
        orderFragment.orderDate(oldOrder.date());
        orderFragment.orderCustomer(oldOrder.customer().name());
        orderFragment.orderProduct(oldOrder.product().name());
        orderFragment.orderQuantity(valueOf(oldOrder.quantity()));
        orderFragment.orderPrice(valueOf(oldOrder.product().price() * (float) oldOrder.quantity()));
    }

    private void setOrderFromUI() {
        order.code(orderFragment.orderCode());
        order.date(orderFragment.orderDate());
        order.quantity(parseInt(orderFragment.orderQuantity()));
        order.customer(selectedCustomer);
        order.product(selectedProduct);
    }

    @Override
    public void createEntityCallback(final Entity entity) {
        if (entity == null) {
            new HttpErrorDialog(this).showDialog();
            order = null;
            return;
        }

        menu.findItem(R.id.delete_button).setVisible(true);
        oldOrder = (Order) order.clone();
        make(findViewById(R.id.order_activity), R.string.entityCreated, LENGTH_LONG).setAction(R.string.undo, new OnClickListener() {
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
            order = oldOrder;
            return;
        }
        if (shouldUnedit) showUndoSnackbar();
        else showUndoneSnackbar();
    }

    private void showUndoneSnackbar() {
        shouldUnedit = true;
        make(findViewById(R.id.order_activity), R.string.undone, LENGTH_LONG).show();
    }

    private void showUndoSnackbar() {
        make(findViewById(R.id.order_activity), R.string.entityEdited, LENGTH_LONG).setAction(R.string.undo, new OnClickListener() {
            @Override
            public void onClick(View view) {
                editOldOrder();
            }
        }).show();
    }

    @Override
    public void deleteEntityCallback(Entity entity, boolean hasAssociatedOrders) {
        if(entity == null){
            new HttpErrorDialog(this).showDialog();
            return;
        }

        order = null;
        oldOrder = null;
        selectedCustomer = null;
        selectedProduct = null;
        menu.findItem(R.id.delete_button).setVisible(false);
        setOrderInUI();
        makeText(this, R.string.entityDeleted, Toast.LENGTH_SHORT).show();
    }

    private void setQuantityInputListener() {
        orderFragment.quatityInput().addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                orderFragment.orderPrice(
                        orderFragment.orderQuantity().isEmpty() || selectedProduct == null ? "" : orderTotalPrice()
                );
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if ((requestCode == getResources().getInteger(R.integer.request_selectable)) && (resultCode == RESULT_OK) && (intent != null)) {
            selectEntity((Entity) intent.getSerializableExtra("customer"));
        }
    }

    public void selectEntity(Entity entity) {
        if (entity instanceof Customer) {
            this.selectedCustomer = (Customer) entity;
            orderFragment.orderCustomer(selectedCustomer.name());
        } else {
            this.selectedProduct = (Product) entity;
            orderFragment.orderProduct(selectedProduct.name());
            orderFragment.orderPrice(orderFragment.orderQuantity().isEmpty() ? "" : orderTotalPrice());
        }
    }

    private String orderTotalPrice() {
        return valueOf(selectedProduct.price() * (float) parseInt(orderFragment.orderQuantity()));
    }

    private void setSearchButtonsListeners() {
        orderFragment.searchCustomerButton().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        new Intent(OrderActivity.this, SelectActivity.class)
                                .putExtra("entityClass", Customer.class),
                        getResources().getInteger(R.integer.request_selectable));
            }
        });

        orderFragment.searchProductButton().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        new Intent(OrderActivity.this, SelectActivity.class)
                                .putExtra("entityClass", Product.class),
                        getResources().getInteger(R.integer.request_selectable));
            }
        });
    }

}
