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

import es.ulpgc.eii.pea.practica3.dialogs.HttpErrorDialog;
import es.ulpgc.eii.pea.practica3.entities.Entity;
import es.ulpgc.eii.pea.practica3.entities.Order;
import es.ulpgc.eii.pea.practica3.http.json.JsonEntityLoader;
import es.ulpgc.eii.pea.practica3.interfaces.EntityListFragment;
import es.ulpgc.eii.pea.practica3.interfaces.EntityLoader;
import es.ulpgc.eii.pea.practica3.interfaces.OnListFragmentInteractionListener;

public class OrdersFragment extends Fragment implements EntityLoader, EntityListFragment {

    private OnListFragmentInteractionListener mListener;
    private List<? extends Entity> orders;
    private RecyclerView recyclerView;
    private OrdersRecyclerViewAdapter ordersRecyclerViewAdapter;

    public OrdersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loadEntities();
        return recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_orders_list, container, false);
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
    public void loadEntitiesCallback(List<Entity> orders) {
        if(orders == null) return;

        this.orders = orders;
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(ordersRecyclerViewAdapter = new OrdersRecyclerViewAdapter((List<Order>) this.orders, mListener));
    }

    @Override
    public void loadEntities() {
        new JsonEntityLoader<>(getContext(), Order.class, this).execute();
    }

    @Override
    public void notifyDataSetChanged() {
        ordersRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void removeEntity(Entity entity) {
        orders.remove(entity);
    }

}
