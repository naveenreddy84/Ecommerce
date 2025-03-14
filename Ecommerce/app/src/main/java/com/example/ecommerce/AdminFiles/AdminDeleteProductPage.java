package com.example.ecommerce.AdminFiles;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.AdminFiles.AdminNavbarFiles.HomeNavbar.Smartphone;
import com.example.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminDeleteProductPage extends AppCompatActivity {

    private RecyclerView AdmindeleterecyclerView;
    private AdminDeleteProductAdapter adminProductAdapter;
    private List<Smartphone> smartphoneList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_delete_product_page);

        AdmindeleterecyclerView = findViewById(R.id.AdmindeleterecyclerView);
        AdmindeleterecyclerView.setLayoutManager(new LinearLayoutManager(this));

        smartphoneList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("smartphones");

        adminProductAdapter = new AdminDeleteProductAdapter(this, smartphoneList);
        AdmindeleterecyclerView.setAdapter(adminProductAdapter);


        fetchProducts();
    }

    private void fetchProducts() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                smartphoneList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Smartphone smartphone = snapshot.getValue(Smartphone.class);
                    if (smartphone != null) {
                        if (smartphone.getImageUrl() != null && !smartphone.getImageUrl().isEmpty() && smartphone.getName() != null) {
                            smartphoneList.add(smartphone);
                        } else {
                            Log.w("AdminDeleteProductPage", "Invalid Smartphone data: " + snapshot.getKey());
                        }
                    } else {
                        Log.w("AdminDeleteProductPage", "Null smartphone object found at key: " + snapshot.getKey());
                    }
                }

                if (smartphoneList.isEmpty()) {
                    Toast.makeText(AdminDeleteProductPage.this, "No valid products found", Toast.LENGTH_SHORT).show();
                } else {
                    adminProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminDeleteProductPage.this, "Failed to load products: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void deleteProduct(String productId) {

        if (productId == null || productId.isEmpty()) {
            Toast.makeText(AdminDeleteProductPage.this, "Invalid Product ID", Toast.LENGTH_SHORT).show();
            return;
        }
        databaseReference.child(productId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AdminDeleteProductPage.this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AdminDeleteProductPage.this, "Failed to delete product", Toast.LENGTH_SHORT).show();
                    }
                });


        //
    }
}
