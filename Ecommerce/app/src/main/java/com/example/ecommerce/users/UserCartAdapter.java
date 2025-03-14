package com.example.ecommerce.users;

import android.content.Context;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Iterator;
import java.util.List;

public class UserCartAdapter extends RecyclerView.Adapter<UserCartAdapter.ViewHolder> {

    private Context context;
    private List<Smartphone> cartProductsList;

    DatabaseReference Database;

    FirebaseAuth mAuth;


    public UserCartAdapter(Context context, List<Smartphone> cartProductsList) {
        this.context = context;
        this.cartProductsList = cartProductsList;

        mAuth = FirebaseAuth.getInstance();
        Database = FirebaseDatabase.getInstance().getReference();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Smartphone smartphone = cartProductsList.get(position);




        holder.productName.setText(smartphone.getName());
        holder.productPrice.setText("$" + smartphone.getPrice());
        holder.productDescription.setText(smartphone.getDescription());


        Glide.with(context)
                .load(smartphone.getImageUrl())
                .placeholder(R.drawable.ic_placeholder_image)  // Default placeholder image
                .into(holder.productImage);


        holder.removeButton.setOnClickListener(v -> {
            String productId = smartphone.getProductId();
            if (productId != null && !productId.isEmpty()) {
                removeProduct(productId);
            }
        });

    }

    private void removeProduct(String productId) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            if (productId != null && !productId.isEmpty()) {

                DatabaseReference productRef = Database.child("cart-items").child(userId).child(productId);


                productRef.removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (Smartphone smartphone : cartProductsList) {
                            if (smartphone.getProductId().equals(productId)) {
                                cartProductsList.remove(smartphone);
                                notifyDataSetChanged();
                                break;
                            }
                        }
                        Toast.makeText(context, "Product removed from cart", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to remove product from cart", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return cartProductsList.size();
    }












    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice, productDescription;
        ImageView productImage;
        Button removeButton;

        public ViewHolder(View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productDescription = itemView.findViewById(R.id.productDescription);
            productImage = itemView.findViewById(R.id.productImage);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
