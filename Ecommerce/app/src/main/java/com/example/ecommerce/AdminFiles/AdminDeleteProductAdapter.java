package com.example.ecommerce.AdminFiles;



import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce.AdminFiles.AdminNavbarFiles.HomeNavbar.Smartphone;
import com.example.ecommerce.R;


import java.util.List;

public class AdminDeleteProductAdapter extends RecyclerView.Adapter<AdminDeleteProductAdapter.ViewHolder> {

    private Context context;
    private List<Smartphone> smartphoneList;
    private AdminDeleteProductPage adminDeleteProductPage;

    public AdminDeleteProductAdapter(Context context, List<Smartphone> smartphoneList) {
        this.context = context;
        this.smartphoneList = smartphoneList;
        this.adminDeleteProductPage = (AdminDeleteProductPage) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_delete_product_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Smartphone smartphone = smartphoneList.get(position);

        holder.productName.setText(smartphone.getName());
        holder.productPrice.setText("₹" + smartphone.getPrice());
        holder.productDescription.setText(smartphone.getDescription());

        String imageUrl = smartphone.getImageUrl();
        Glide.with(context)
                .load(Uri.parse(imageUrl))  // Use the content URI directly
                .placeholder(R.drawable.ic_placeholder_image)
                .into(holder.productImage);




        holder.deleteButton.setOnClickListener(v -> {
            String productId = smartphone.getProductId();
            adminDeleteProductPage.deleteProduct(productId);
        });
    }




    @Override
    public int getItemCount() {
        return smartphoneList.size();
    }





    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice, productDescription;
        ImageView productImage;
        Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productDescription = itemView.findViewById(R.id.productDescription);
            productImage = itemView.findViewById(R.id.productImage);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }



    }
}

