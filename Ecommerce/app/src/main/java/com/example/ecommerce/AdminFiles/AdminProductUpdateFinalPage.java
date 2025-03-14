package com.example.ecommerce.AdminFiles;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ecommerce.AdminFiles.AdminNavbarFiles.HomeNavbar.HomeFragment;
import com.example.ecommerce.AdminFiles.AdminNavbarFiles.HomeNavbar.Smartphone;
import com.example.ecommerce.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AdminProductUpdateFinalPage extends AppCompatActivity {

    private EditText nameEditText, priceEditText, descriptionEditText;
    private ImageView productImageView;
    private Button updateButton;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Smartphone smartphone;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_update_final_page);


        databaseReference = FirebaseDatabase.getInstance().getReference("smartphones");
        storageReference = FirebaseStorage.getInstance().getReference("product_images");


        nameEditText = findViewById(R.id.nameEditText);
        priceEditText = findViewById(R.id.priceEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        productImageView = findViewById(R.id.productImageView);
        updateButton = findViewById(R.id.updateButton);


        smartphone = (Smartphone) getIntent().getSerializableExtra("smartphone");


        nameEditText.setText(smartphone.getName());
        priceEditText.setText(smartphone.getPrice());
        descriptionEditText.setText(smartphone.getDescription());
        Glide.with(this).load(smartphone.getImageUrl()).into(productImageView);


        productImageView.setOnClickListener(v -> openImagePicker());


        updateButton.setOnClickListener(v -> updateProductDetails());
    }

    private void openImagePicker() {
        //  for Opening  the gallery to pick an image
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            // Getting  the selected image URI
            selectedImageUri = data.getData();
            productImageView.setImageURI(selectedImageUri);
        }
    }

    private void updateProductDetails() {
        String updatedName = nameEditText.getText().toString();
        String updatedPrice = priceEditText.getText().toString();
        String updatedDescription = descriptionEditText.getText().toString();


        if (selectedImageUri != null) {
            // If a new image is selected, upload it to Firebase Storage
            uploadImageAndUpdateProduct(updatedName, updatedPrice, updatedDescription);
        } else {
            smartphone.setName(updatedName);
            smartphone.setPrice(updatedPrice);
            smartphone.setDescription(updatedDescription);

            // Updating the product in Firebase database
            databaseReference.child(smartphone.getProductId()).setValue(smartphone)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AdminProductUpdateFinalPage.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminProductUpdateFinalPage.this, HomeFragment.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AdminProductUpdateFinalPage.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void uploadImageAndUpdateProduct(String updatedName, String updatedPrice, String updatedDescription) {
        // Creating a reference to the storage location for the new image
        StorageReference imageRef = storageReference.child(System.currentTimeMillis() + ".jpg");

        // Get the image InputStream from the selected URI
        ContentResolver contentResolver = getContentResolver();
        try {
            InputStream imageStream = contentResolver.openInputStream(selectedImageUri);
            byte[] imageData = getBytes(imageStream);

            // Upload the image
            UploadTask uploadTask = imageRef.putBytes(imageData);
            uploadTask.addOnSuccessListener(taskSnapshot -> {

                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();


                    smartphone.setName(updatedName);
                    smartphone.setPrice(updatedPrice);
                    smartphone.setDescription(updatedDescription);
                    smartphone.setImageUrl(imageUrl);

                    // Updating  the product in Firebase database
                    databaseReference.child(smartphone.getProductId()).setValue(smartphone)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(AdminProductUpdateFinalPage.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(AdminProductUpdateFinalPage.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                            });
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(AdminProductUpdateFinalPage.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error while reading the image", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to convert InputStream to byte array
    private byte[] getBytes(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
