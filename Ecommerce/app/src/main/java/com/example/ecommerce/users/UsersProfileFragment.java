package com.example.ecommerce.users;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.ecommerce.Authentication.LoginPage;
import com.example.ecommerce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UsersProfileFragment extends Fragment {

    private TextView userNameTextView, userEmailTextView,contactUs,emailtext,aboutUs;
    private Button  logoutButton;
    private FirebaseAuth mAuth;
    private DatabaseReference Databaserefernece;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_profile, container, false);


        userNameTextView = view.findViewById(R.id.userNameTextView);
        userEmailTextView = view.findViewById(R.id.userEmailTextView);
        contactUs = view.findViewById(R.id.contactUs);
        aboutUs = view.findViewById(R.id.aboutUs);
        logoutButton = view.findViewById(R.id.logoutButton);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            Databaserefernece = FirebaseDatabase.getInstance().getReference("Users").child(userId);

            Databaserefernece.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Fetching data from the snapshot
                        String userName = snapshot.child("name").getValue(String.class);
                        String userEmail = snapshot.child("email").getValue(String.class);

                        // Set the values directly to the TextViews
                        userNameTextView.setText(userName);
                        userEmailTextView.setText(userEmail);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle error
                    Log.e("FirebaseError", "Failed to read user data: " + error.getMessage());
                }
            });
        } else {
            Log.e("FirebaseError", "User not logged in");
        }


        logoutButton.setOnClickListener(v -> {
            logout();
        });





        return view;
    }




    private void logout() {

        mAuth.signOut();


        Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(getContext(), LoginPage.class);
        startActivity(intent);
        getActivity().finish();

    }




}

