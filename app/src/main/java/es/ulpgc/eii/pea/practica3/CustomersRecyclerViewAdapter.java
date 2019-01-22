package es.ulpgc.eii.pea.practica3;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import es.ulpgc.eii.pea.practica3.entities.Customer;
import es.ulpgc.eii.pea.practica3.interfaces.OnListFragmentInteractionListener;

public class CustomersRecyclerViewAdapter extends RecyclerView.Adapter<CustomersRecyclerViewAdapter.ViewHolder> {

    private final List<Customer> customers;
    private final OnListFragmentInteractionListener mListener;

    public CustomersRecyclerViewAdapter(List<Customer> customers, OnListFragmentInteractionListener listener) {
        this.customers = customers;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_customers, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.customer = customers.get(position);
        holder.name.setText(customers.get(position).name());
        holder.address.setText(customers.get(position).address());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onListFragmentClick(holder.customer);
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mListener.onListFragmentLongClick(holder.customer);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView name;
        public final TextView address;
        public Customer customer;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.name);
            address = view.findViewById(R.id.address);
        }
    }
}
