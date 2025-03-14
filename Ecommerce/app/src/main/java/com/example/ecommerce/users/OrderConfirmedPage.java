package com.example.ecommerce.users;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ecommerce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class OrderConfirmedPage extends AppCompatActivity {


    ImageView productImage;
    TextView productName, productPrice, productDescription,productquantity;
    Button backbutton;


    Intent intent;
    DatabaseReference db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmed_page);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();


        intent = getIntent();
        String name = intent.getStringExtra("productName");
        String price = intent.getStringExtra("productPrice");
        String description = intent.getStringExtra("productDescription");
        String productImageUrl = intent.getStringExtra("productImageUrl");
        String productId = intent.getStringExtra("productId");
       String  quantity = intent.getStringExtra("productQuantity");


        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        productImage = findViewById(R.id.productImage);
        backbutton = findViewById(R.id.backbutton);
        productquantity = findViewById(R.id.productquantity);

        productName.setText(name);
        productPrice.setText(price);
        productDescription.setText(description);
        productquantity.setText("quantity : " + quantity);



        Glide.with(this)
                .load(productImageUrl)
                .placeholder(R.drawable.ic_placeholder_image)
                .into(productImage);


        saveOrderToFirebase(productId, name, price,description,productImageUrl, quantity);


        backbutton.setOnClickListener(v -> openUserHome());
    }


    private void openUserHome() {
        Intent intent = new Intent(OrderConfirmedPage.this, UserDashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clears all the previous activites
        startActivity(intent);
        finish();
    }


    private void saveOrderToFirebase(String productId, String productName, String productPrice,String productDescription,String productImage,String quantity) {

        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        if (userId != null) {

            Map<String, Object> orderDetails = new HashMap<>();
            orderDetails.put("userId", userId);
            orderDetails.put("productId", productId);
            orderDetails.put("productName", productName);
            orderDetails.put("productPrice", productPrice);
            orderDetails.put("productDescription",productDescription);
            orderDetails.put("productImage",productImage);
            orderDetails.put("quantity", quantity);
            orderDetails.put("orderStatus", "Paid");


            String orderId = db.child("orders").push().getKey();
            if (orderId != null) {
                db.child("orders").child(orderId).setValue(orderDetails)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                Toast.makeText(OrderConfirmedPage.this, "Order confirmed!", Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(OrderConfirmedPage.this, "Failed to confirm order. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {

                Toast.makeText(OrderConfirmedPage.this, "Error generating order ID. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } else {

            Toast.makeText(OrderConfirmedPage.this, "You need to be logged in to place an order.", Toast.LENGTH_SHORT).show();
        }
    }
}

