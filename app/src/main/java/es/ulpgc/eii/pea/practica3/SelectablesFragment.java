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
import es.ulpgc.eii.pea.practica3.entities.Product;
import es.ulpgc.eii.pea.practica3.http.json.JsonEntityLoader;
import es.ulpgc.eii.pea.practica3.interfaces.EntityListFragment;
import es.ulpgc.eii.pea.practica3.interfaces.EntityLoader;
import es.ulpgc.eii.pea.practica3.interfaces.OnListFragmentInteractionListener;

public class SelectablesFragment extends Fragment implements EntityLoader, EntityListFragment {

    private OnListFragmentInteractionListener mListener;
    private List<? extends Entity> selectables;
    private RecyclerView recyclerView;
    private SelectablesRecyclerViewAdapter selectablesRecyclerViewAdapter;
    private Class entityClass;

    public SelectablesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_selectables_list, container, false);
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
    public void loadEntitiesCallback(List<Entity> entities) {
        if (entities == null) return;

        this.selectables = entities;
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(selectablesRecyclerViewAdapter = new SelectablesRecyclerViewAdapter((List<Entity>) this.selectables, mListener));
    }

    @Override
    public void loadEntities() {
        new JsonEntityLoader<>(
                getContext(),
                entityClass.getName().equals(Customer.class.getName()) ? Customer.class : Product.class,
                this
        ).execute();
    }

    @Override
    public void notifyDataSetChanged() {
        selectablesRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void removeEntity(Entity entity) {
        selectables.remove(entity);
    }

    public void entityClass(Class entityClass){
        this.entityClass = entityClass;

    }

}
