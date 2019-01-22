package es.ulpgc.eii.pea.practica3;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import es.ulpgc.eii.pea.practica3.entities.Order;
import es.ulpgc.eii.pea.practica3.interfaces.OnListFragmentInteractionListener;

public class OrdersRecyclerViewAdapter extends RecyclerView.Adapter<OrdersRecyclerViewAdapter.ViewHolder> {

    private final List<Order> orders;
    private final OnListFragmentInteractionListener mListener;

    public OrdersRecyclerViewAdapter(List<Order> orders, OnListFragmentInteractionListener listener) {
        this.orders = orders;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_orders, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.order = orders.get(position);
        holder.customerName.setText(orders.get(position).customer().name());
        holder.productName.setText(orders.get(position).product().name());
        holder.code.setText(orders.get(position).code());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onListFragmentClick(holder.order);
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mListener.onListFragmentLongClick(holder.order);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView customerName;
        public final TextView productName;
        public final TextView code;
        public Order order;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            customerName = view.findViewById(R.id.customer_name);
            productName = view.findViewById(R.id.product_name);
            code = view.findViewById(R.id.code);
        }
    }
}
