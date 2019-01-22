package es.ulpgc.eii.pea.practica3;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import es.ulpgc.eii.pea.practica3.entities.Product;
import es.ulpgc.eii.pea.practica3.interfaces.OnListFragmentInteractionListener;

public class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.ViewHolder> {

    private final List<Product> products;
    private final OnListFragmentInteractionListener mListener;

    public ProductsRecyclerViewAdapter(List<Product> products, OnListFragmentInteractionListener listener) {
        this.products = products;
        mListener = listener;
    }

    @Override
    public ProductsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductsRecyclerViewAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_products, parent, false));
    }

    @Override
    public void onBindViewHolder(final ProductsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.product = products.get(position);
        holder.name.setText(products.get(position).name());
        holder.description.setText(products.get(position).description());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onListFragmentClick(holder.product);
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mListener.onListFragmentLongClick(holder.product);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView name;
        public final TextView description;
        public Product product;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
        }
    }
}
