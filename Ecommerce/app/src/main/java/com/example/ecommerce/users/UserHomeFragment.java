package com.example.ecommerce.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ecommerce.AdminFiles.AdminNavbarFiles.HomeNavbar.Smartphone;
import com.example.ecommerce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserHomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserProductAdapter userProductAdapter;
    private List<Smartphone> smartphoneList;
    private DatabaseReference databaseReference;


    public UserHomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        smartphoneList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("smartphones");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_home, container, false);


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        userProductAdapter = new UserProductAdapter(getContext(), smartphoneList);
        recyclerView.setAdapter(userProductAdapter);


        fetchProducts();

        return view;
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


                userProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getContext(), "Failed to load products: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}