package es.ulpgc.eii.pea.practica3;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import es.ulpgc.eii.pea.practica3.entities.Customer;
import es.ulpgc.eii.pea.practica3.entities.Entity;
import es.ulpgc.eii.pea.practica3.http.soap.SoapCustomerLoader;
import es.ulpgc.eii.pea.practica3.interfaces.EntityListFragment;
import es.ulpgc.eii.pea.practica3.interfaces.EntityLoader;
import es.ulpgc.eii.pea.practica3.interfaces.OnListFragmentInteractionListener;

public class CustomersFragment extends Fragment implements EntityLoader, EntityListFragment {

    private OnListFragmentInteractionListener mListener;
    private List<? extends Entity> customers;
    private RecyclerView recyclerView;
    private CustomersRecyclerViewAdapter customersRecyclerViewAdapter;

    public CustomersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loadEntities();
        return recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_customers_list, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnListFragmentInteractionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void loadEntitiesCallback(List<Entity> customers) {
        if (customers == null) return;

        this.customers = customers;
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(customersRecyclerViewAdapter = new CustomersRecyclerViewAdapter((List<Customer>) this.customers, mListener));
    }

    @Override
    public void loadEntities() {
        new SoapCustomerLoader(getContext(), this).execute();
    }

    @Override
    public void notifyDataSetChanged() {
        customersRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void removeEntity(Entity entity) {
        customers.remove(entity);
    }

}
