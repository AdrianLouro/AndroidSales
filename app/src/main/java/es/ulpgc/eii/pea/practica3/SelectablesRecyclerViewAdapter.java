package es.ulpgc.eii.pea.practica3;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import es.ulpgc.eii.pea.practica3.entities.Customer;
import es.ulpgc.eii.pea.practica3.entities.Entity;
import es.ulpgc.eii.pea.practica3.entities.Product;
import es.ulpgc.eii.pea.practica3.interfaces.OnListFragmentInteractionListener;

public class SelectablesRecyclerViewAdapter extends RecyclerView.Adapter<SelectablesRecyclerViewAdapter.ViewHolder> {

    private final List<Entity> selectables;
    private final OnListFragmentInteractionListener mListener;

    public SelectablesRecyclerViewAdapter(List<Entity> selectables, OnListFragmentInteractionListener listener) {
        this.selectables = selectables;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_selectables, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.entity = selectables.get(position);
        holder.name.setText(selectables.get(position) instanceof Customer ? ((Customer)selectables.get(position)).name() : ((Product)selectables.get(position)).name());
        holder.detail.setText(selectables.get(position) instanceof Customer ? ((Customer)selectables.get(position)).address() : ((Product)selectables.get(position)).description());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onListFragmentClick(holder.entity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectables.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView name;
        public final TextView detail;
        public Entity entity;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.name);
            detail = view.findViewById(R.id.detail);
        }
    }
}
