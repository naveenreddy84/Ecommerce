package com.example.ecommerce.AdminFiles.AdminNavbarFiles.HomeNavbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.ecommerce.AdminFiles.AdminDeleteProductPage;
import com.example.ecommerce.AdminFiles.AdminProductUpdatePage;
import com.example.ecommerce.AdminFiles.AdminProductViewPage;
import com.example.ecommerce.AdminFiles.AdminUploadPage;
import com.example.ecommerce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private TextView welcomeTextView;
    private DatabaseReference usersDatabase;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        welcomeTextView = view.findViewById(R.id.welcomeTextView);

        // Initialize buttons
        Button AddButton = view.findViewById(R.id.AddButton);
        Button ViewButton = view.findViewById(R.id.ViewButton);
        Button UpdateButton = view.findViewById(R.id.UpdateButton);
        Button DeleteButton = view.findViewById(R.id.DeleteButton);

        // Initialize Firebase Database reference
        usersDatabase = FirebaseDatabase.getInstance().getReference("Users");


        getAdminName();


        AddButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AdminUploadPage.class);
            startActivity(intent);
        });

        ViewButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AdminProductViewPage.class);
            startActivity(intent);
        });

        UpdateButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AdminProductUpdatePage.class);
            startActivity(intent);
        });

        DeleteButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AdminDeleteProductPage.class);
            startActivity(intent);
        });

        return view;
    }


    private void getAdminName() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        usersDatabase.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check if the user exists and has a role of "admin"
                if (dataSnapshot.exists()) {
                    String role = dataSnapshot.child("role").getValue(String.class);
                    if ("admin".equals(role)) {
                        String adminName = dataSnapshot.child("name").getValue(String.class);
                        welcomeTextView.setText("Welcome Admin, " + adminName + "!");
                    } else {
                        welcomeTextView.setText("Welcome User!");
                    }
                } else {
                    Log.e("HomeFragment", "User not found in the database.");
                    welcomeTextView.setText("Welcome Admin!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("HomeFragment", "Error retrieving admin data: " + databaseError.getMessage());
                welcomeTextView.setText("Error loading admin details.");
            }
        });
    }
}
