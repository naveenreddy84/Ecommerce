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

public class UserProductDetailsPage extends AppCompatActivity {




    TextView productName, productPrice, productDescription, quantityTextView;
    ImageView productImage;
    Button placeOrderButton, increaseQuantityButton, decreaseQuantityButton;
    String quantity = "1";
    double unitPrice = 0.0;
    double totalPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product_details_page);


        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        productImage = findViewById(R.id.productImage);
        placeOrderButton = findViewById(R.id.placeOrderButton);
        increaseQuantityButton = findViewById(R.id.increaseQuantityButton);
        decreaseQuantityButton = findViewById(R.id.decreaseQuantityButton);
        quantityTextView = findViewById(R.id.quantityTextView);


        Intent intent = getIntent();
        String name = intent.getStringExtra("productName");
        String price = intent.getStringExtra("productPrice");
        String description = intent.getStringExtra("productDescription");
        String imageUrl = intent.getStringExtra("productImageUrl");
       String productId = intent.getStringExtra("productId");

        productName.setText(name);
        productPrice.setText(price);
        productDescription.setText(description);


        try {
            unitPrice = Double.parseDouble(price.replace("$", ""));
            totalPrice = unitPrice * Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        updatePrice();


        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_placeholder_image)
                .into(productImage);


        increaseQuantityButton.setOnClickListener(v -> {

            int currentQuantity = Integer.parseInt(quantity);
            currentQuantity++;
            quantity = String.valueOf(currentQuantity);
            quantityTextView.setText(quantity);
            totalPrice = unitPrice * currentQuantity;
            updatePrice();
        });


        decreaseQuantityButton.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(quantity);
            if (currentQuantity > 1) {
                currentQuantity--;
                quantity = String.valueOf(currentQuantity);
                quantityTextView.setText(quantity);
                totalPrice = unitPrice * currentQuantity;
                updatePrice();
            }
        });


        placeOrderButton.setOnClickListener(v -> {
            Intent paymentIntent = new Intent(UserProductDetailsPage.this, StripePaymentPage.class);
            paymentIntent.putExtra("productName", name);
            paymentIntent.putExtra("productPrice", String.format("$%.2f", totalPrice));
            paymentIntent.putExtra("productDescription", description);
            paymentIntent.putExtra("productImageUrl", imageUrl);
            paymentIntent.putExtra("productQuantity", quantity);
            paymentIntent.putExtra("productId",productId);


            startActivity(paymentIntent);
        });
    }


    private void updatePrice() {
        productPrice.setText(String.format("$%.2f", totalPrice));
    }
}
