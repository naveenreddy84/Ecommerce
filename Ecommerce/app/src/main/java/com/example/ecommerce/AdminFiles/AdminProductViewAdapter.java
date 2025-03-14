package com.example.ecommerce.AdminFiles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce.AdminFiles.AdminNavbarFiles.HomeNavbar.Smartphone;
import com.example.ecommerce.R;

import java.util.List;

public class AdminProductViewAdapter extends RecyclerView.Adapter<AdminProductViewAdapter.ViewHolder> {

    private Context context;
    private List<Smartphone> smartphoneList;

    public AdminProductViewAdapter(Context context, List<Smartphone> smartphoneList) {
        this.context = context;
        this.smartphoneList = smartphoneList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_productview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Smartphone smartphone = smartphoneList.get(position);
        holder.nameTextView.setText(smartphone.getName());
        holder.priceTextView.setText("₹" + smartphone.getPrice());
        holder.descriptionTextView.setText(smartphone.getDescription());


        Glide.with(context)
                .load(smartphone.getImageUrl())
                .placeholder(R.drawable.ic_placeholder_image)
                .into(holder.productImageView);
    }

    @Override
    public int getItemCount() {
        return smartphoneList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, priceTextView, descriptionTextView;
        ImageView productImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.productName);
            priceTextView = itemView.findViewById(R.id.productPrice);
            descriptionTextView = itemView.findViewById(R.id.productDescription);
            productImageView = itemView.findViewById(R.id.productImage);
        }
    }
}
