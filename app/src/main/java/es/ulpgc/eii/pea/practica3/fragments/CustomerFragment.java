package es.ulpgc.eii.pea.practica3.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import es.ulpgc.eii.pea.practica3.R;


public class CustomerFragment extends Fragment {

    TextView nameInput;
    TextView addressInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer, container, false);
        nameInput = view.findViewById(R.id.name_input);
        addressInput = view.findViewById(R.id.address_input);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public String customerName() {
        return nameInput.getText().toString();
    }

    public String customerAddress() {
        return addressInput.getText().toString();
    }

    public void customerName(String customerName) {
        nameInput.setText(customerName);
    }

    public void customerAddress(String customerAddress) {
        addressInput.setText(customerAddress);
    }
}
