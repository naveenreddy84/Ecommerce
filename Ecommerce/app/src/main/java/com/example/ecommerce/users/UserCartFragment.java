package com.example.ecommerce.users;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.AdminFiles.AdminNavbarFiles.HomeNavbar.Smartphone;
import com.example.ecommerce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserCartFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserCartAdapter userCartAdapter;
    private List<Smartphone> cartProductsList;
    private FirebaseAuth mAuth;
    private DatabaseReference cartItemsRef;
    private DatabaseReference productsRef;

    public UserCartFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_cart, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartProductsList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();


        cartItemsRef = FirebaseDatabase.getInstance().getReference("cart-items");


        productsRef = FirebaseDatabase.getInstance().getReference("smartphones");

        userCartAdapter = new UserCartAdapter(getContext(), cartProductsList);
        recyclerView.setAdapter(userCartAdapter);

        fetchCartItems();

        return view;
    }

    private void fetchCartItems() {
        String userId = mAuth.getCurrentUser().getUid();

        if (userId != null) {

            cartItemsRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    cartProductsList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String productId = snapshot.getKey();
                        fetchProductDetails(productId);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Failed to load cart: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void fetchProductDetails(String productId) {
        productsRef.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Smartphone smartphone = dataSnapshot.getValue(Smartphone.class);
                if (smartphone != null) {
                    cartProductsList.add(smartphone);
                    userCartAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load product: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
