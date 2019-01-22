package es.ulpgc.eii.pea.practica3.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import es.ulpgc.eii.pea.practica3.R;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;


public class OrderFragment extends Fragment {

    TextView codeInput;
    DatePicker datePicker;
    TextView customerInput;
    TextView productInput;
    TextView quantityInput;
    TextView priceInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        codeInput = view.findViewById(R.id.code_input);
        datePicker = view.findViewById(R.id.date_picker);
        customerInput = view.findViewById(R.id.customer_input);
        productInput = view.findViewById(R.id.product_input);
        quantityInput = view.findViewById(R.id.quantity_input);
        priceInput = view.findViewById(R.id.price_input);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public TextView quatityInput() {
        return quantityInput;
    }

    public ImageView searchCustomerButton() {
        return getView().findViewById(R.id.search_customer_button);
    }

    public ImageView searchProductButton() {
        return getView().findViewById(R.id.search_product_button);
    }

    public String orderCode() {
        return codeInput.getText().toString();
    }

    public Date orderDate() {
        Calendar calendar = getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public String orderCustomer() {
        return customerInput.getText().toString();
    }

    public String orderQuantity() {
        return quantityInput.getText().toString();
    }

    public String orderProduct() {
        return productInput.getText().toString();
    }

    public String orderPrice() {
        return priceInput.getText().toString();
    }

    public void orderCode(String orderCode) {
        codeInput.setText(orderCode);
    }

    public void orderDate(Date orderDate) {
        Calendar calendar = getInstance();
        calendar.setTime(orderDate);
        datePicker.updateDate(calendar.get(YEAR), calendar.get(MONTH), calendar.get(DAY_OF_MONTH));
    }

    public void orderCustomer(String orderCustomer) {
        customerInput.setText(orderCustomer);
    }

    public void orderProduct(String orderProduct) {
        productInput.setText(orderProduct);
    }

    public void orderQuantity(String orderQuantity) {
        quantityInput.setText(orderQuantity);
    }

    public void orderPrice(String orderPrice) {
        priceInput.setText(orderPrice);
    }
}
