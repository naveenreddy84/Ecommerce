package com.example.ecommerce.AdminFiles;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

public class AdminProductViewPage extends AppCompatActivity {

    private RecyclerView AdminProductsViewrecyclerView;
    private AdminProductViewAdapter productAdapter;
    private List<Smartphone> smartphoneList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_view_page);


        databaseReference = FirebaseDatabase.getInstance().getReference("smartphones");


        AdminProductsViewrecyclerView = findViewById(R.id.AdminProductsViewrecyclerView);
        AdminProductsViewrecyclerView.setLayoutManager(new LinearLayoutManager(this));

        smartphoneList = new ArrayList<>();
        productAdapter = new AdminProductViewAdapter(this, smartphoneList);
        AdminProductsViewrecyclerView.setAdapter(productAdapter);


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
                        smartphoneList.add(smartphone);
                    }
                }

                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminProductViewPage.this, "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
