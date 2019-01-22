package es.ulpgc.eii.pea.practica3.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import es.ulpgc.eii.pea.practica3.R;


public class ProductFragment extends Fragment {

    TextView nameInput;
    TextView descriptionInput;
    TextView priceInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        nameInput = view.findViewById(R.id.name_input);
        descriptionInput = view.findViewById(R.id.description_input);
        priceInput = view.findViewById(R.id.price_input);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public String productName() {
        return nameInput.getText().toString();
    }

    public String productDescription() {
        return descriptionInput.getText().toString();
    }

    public String productPrice() {
        return priceInput.getText().toString();
    }

    public void productName(String productName) {
        nameInput.setText(productName);
    }

    public void productDescription(String productDescription) {
        descriptionInput.setText(productDescription);
    }

    public void productPrice(String productPrice) {
        priceInput.setText(productPrice);
    }
}
