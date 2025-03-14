package com.example.ecommerce.users;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProductAdapter extends RecyclerView.Adapter<UserProductAdapter.ViewHolder> {

    private Context context;
    private List<Smartphone> smartphoneList;

    private FirebaseAuth mAuth;
    private DatabaseReference Database;






    public UserProductAdapter(Context context, List<Smartphone> smartphoneList) {
        this.context = context;
        this.smartphoneList = smartphoneList;
        mAuth = FirebaseAuth.getInstance();
        Database = FirebaseDatabase.getInstance().getReference("cart-items");

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_userproduct, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Smartphone smartphone = smartphoneList.get(position);






        holder.productName.setText(smartphone.getName());
        holder.productPrice.setText("$" + smartphone.getPrice());
        holder.productDescription.setText(smartphone.getDescription());


        Glide.with(context)
                .load(smartphone.getImageUrl())
                .placeholder(R.drawable.ic_placeholder_image)
                .into(holder.productImage);

        holder.buyButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserProductDetailsPage.class);
            intent.putExtra("productName", smartphone.getName());
            intent.putExtra("productPrice", "$" + smartphone.getPrice());
            intent.putExtra("productDescription", smartphone.getDescription());
            intent.putExtra("productImageUrl", smartphone.getImageUrl());
            intent.putExtra("productId",smartphone.getProductId());
            context.startActivity(intent);
        });


        String productId = smartphone.getProductId();


        holder.addToCartButton.setOnClickListener(v -> {
            addToCart(productId);
            notifyDataSetChanged();
        });
    }



    private void addToCart(String productId) {
        String userId = mAuth.getCurrentUser().getUid();

        if (userId != null) {
            Database.child(userId).child(productId).setValue("Added")
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(context, "Please log in to add items to cart", Toast.LENGTH_SHORT).show();
        }
    }






    @Override
    public int getItemCount() {
        return smartphoneList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView productName, productPrice, productDescription;
        ImageView productImage;
        Button addToCartButton,buyButton;

        public ViewHolder(View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productDescription = itemView.findViewById(R.id.productDescription);
            productImage = itemView.findViewById(R.id.productImage);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
            buyButton = itemView.findViewById(R.id.buyButton);
        }
    }
}
