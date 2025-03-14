package com.example.ecommerce.AdminFiles;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce.AdminFiles.AdminNavbarFiles.HomeNavbar.Smartphone;
import com.example.ecommerce.R;

import java.util.List;

public class AdminProductUpdateAdapter extends RecyclerView.Adapter<AdminProductUpdateAdapter.ViewHolder> {

    private Context context;
    private List<Smartphone> smartphoneList;

    public AdminProductUpdateAdapter(Context context, List<Smartphone> smartphoneList) {
        this.context = context;
        this.smartphoneList = smartphoneList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_update_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Smartphone smartphone = smartphoneList.get(position);

        holder.productName.setText(smartphone.getName());
        holder.productPrice.setText("₹" + smartphone.getPrice());
        holder.productDescription.setText(smartphone.getDescription());


        Glide.with(context)
                .load(smartphone.getImageUrl())
                .placeholder(R.drawable.ic_placeholder_image)
                .into(holder.productImage);


        holder.updateButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, AdminProductUpdateFinalPage.class);
            intent.putExtra("smartphone", smartphone);
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return smartphoneList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice, productDescription;
        ImageView productImage;
        Button updateButton;

        public ViewHolder(View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productDescription = itemView.findViewById(R.id.productDescription);
            productImage = itemView.findViewById(R.id.productImage);
            updateButton = itemView.findViewById(R.id.updateButton);
        }
    }
}

