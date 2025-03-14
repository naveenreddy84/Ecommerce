package com.example.ecommerce.AdminFiles;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.AdminFiles.AdminNavbarFiles.HomeNavbar.Smartphone;
import com.example.ecommerce.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AdminUploadPage extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private String selectedImageUrl = "";
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_upload_page);

        storageReference = FirebaseStorage.getInstance().getReference("product_images");

        Button btnChooseImage = findViewById(R.id.btnChooseImage);
        Button btnUploadProduct = findViewById(R.id.btnUploadProduct);

        btnChooseImage.setOnClickListener(v -> openFileChooser());

        btnUploadProduct.setOnClickListener(v -> uploadProductToDatabase());
    }


    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ImageView imageViewProduct = findViewById(R.id.imageViewProduct);
            imageViewProduct.setImageURI(imageUri);
        }
    }


    private void uploadProductToDatabase() {
        if (imageUri != null) {

            String productId = FirebaseDatabase.getInstance().getReference("smartphones").push().getKey();
            StorageReference fileReference = storageReference.child(productId + ".jpg");

            // Uploading the image to Firebase Storage
            UploadTask uploadTask = fileReference.putFile(imageUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Geting the download URL of the uploaded image
                fileReference.getDownloadUrl().addOnSuccessListener(uri -> {

                    String productName = ((EditText) findViewById(R.id.editTextProductName)).getText().toString();
                    String productDescription = ((EditText) findViewById(R.id.editTextDescription)).getText().toString();
                    String productPrice = ((EditText) findViewById(R.id.editTextPrice)).getText().toString();


                    Smartphone product = new Smartphone(productId, productName, productDescription, productPrice, uri.toString());


                    if (productId != null) {
                        FirebaseDatabase.getInstance().getReference("smartphones").child(productId).setValue(product)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(AdminUploadPage.this, "Product uploaded successfully!", Toast.LENGTH_SHORT).show();

                                    // Redirect to Admin Dashboard
                                    Intent intent = new Intent(AdminUploadPage.this, AdminDashboard.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(AdminUploadPage.this, "Failed to upload product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(AdminUploadPage.this, "Failed to get image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(AdminUploadPage.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "Please select an image for the product", Toast.LENGTH_SHORT).show();
        }
    }
}
