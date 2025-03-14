package com.example.ecommerce.users;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce.R;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_display_orders, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        // Load image using Glide or Picasso (or any image loading library)
        Glide.with(holder.itemView.getContext())
                .load(order.getProductImage()) // URL or file path
                .into(holder.productImageView);

        holder.productNameTextView.setText(order.getProductName());
        holder.productPriceTextView.setText(order.getProductPrice());
        holder.quantityTextView.setText(order.getQuantity());
        holder.orderStatusTextView.setText(order.getOrderStatus());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImageView;
        private TextView productNameTextView;
        private TextView productPriceTextView;
        private TextView quantityTextView;
        private TextView orderStatusTextView;

        public OrderViewHolder(View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.product_image);
            productNameTextView = itemView.findViewById(R.id.product_name);
            productPriceTextView = itemView.findViewById(R.id.product_price);
            quantityTextView = itemView.findViewById(R.id.quantity);
            orderStatusTextView = itemView.findViewById(R.id.order_status);
        }
    }
}
