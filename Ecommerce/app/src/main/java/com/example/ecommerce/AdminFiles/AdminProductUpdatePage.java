package com.example.ecommerce.AdminFiles;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class AdminProductUpdatePage extends AppCompatActivity {

    private RecyclerView AdminUpdaterecyclerView;
    private AdminProductUpdateAdapter adminProductUpdateAdapter;
    private List<Smartphone> smartphoneList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_update_page);


        databaseReference = FirebaseDatabase.getInstance().getReference("smartphones");


        AdminUpdaterecyclerView = findViewById(R.id.AdminUpdaterecyclerView);
        AdminUpdaterecyclerView.setLayoutManager(new LinearLayoutManager(this));

        smartphoneList = new ArrayList<>();


        adminProductUpdateAdapter = new AdminProductUpdateAdapter(this, smartphoneList);

        AdminUpdaterecyclerView.setAdapter(adminProductUpdateAdapter);

        fetchProducts();
    }


    private void fetchProducts() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                smartphoneList.clear();  // to clear  previous data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Smartphone smartphone = snapshot.getValue(Smartphone.class);
                    if (smartphone != null) {
                        smartphoneList.add(smartphone);
                    }
                }

                adminProductUpdateAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminProductUpdatePage.this, "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
