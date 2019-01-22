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

import es.ulpgc.eii.pea.practica3.entities.Entity;
import es.ulpgc.eii.pea.practica3.entities.Product;
import es.ulpgc.eii.pea.practica3.http.json.JsonEntityLoader;
import es.ulpgc.eii.pea.practica3.interfaces.EntityListFragment;
import es.ulpgc.eii.pea.practica3.interfaces.EntityLoader;
import es.ulpgc.eii.pea.practica3.interfaces.OnListFragmentInteractionListener;

public class ProductsFragment extends Fragment implements EntityLoader, EntityListFragment {

    private OnListFragmentInteractionListener mListener;
    private List<? extends Entity> products;
    private RecyclerView recyclerView;
    private ProductsRecyclerViewAdapter productsRecyclerViewAdapter;

    public ProductsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loadEntities();
        return recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_products_list, container, false);
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
    public void loadEntitiesCallback(List<Entity> products) {
        if (products == null) return;

        this.products = products;
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(productsRecyclerViewAdapter = new ProductsRecyclerViewAdapter((List<Product>) this.products, mListener));
    }

    @Override
    public void loadEntities() {
        new JsonEntityLoader<>(getContext(), Product.class, this).execute();
    }

    @Override
    public void notifyDataSetChanged() {
        productsRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void removeEntity(Entity entity) {
        products.remove(entity);
    }

}
