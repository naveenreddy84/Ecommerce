package com.example.ecommerce.AdminFiles.AdminNavbarFiles.HomeNavbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.EcommerceUser;
import com.example.ecommerce.R;
import com.example.ecommerce.users.UserAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ManageUsers extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private ArrayList<EcommerceUser> userList;

    public ManageUsers() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_list, container, false);


        recyclerView = view.findViewById(R.id.recyclerView_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        userList = new ArrayList<>();


        loadUsersFromDatabase();


        userAdapter = new UserAdapter((ArrayList<EcommerceUser>) userList, new UserAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                handleDeleteClick(position);
            }
        });


        recyclerView.setAdapter(userAdapter);

        return view;
    }

    //  method for deleting  the user
    private void handleDeleteClick(int position) {
        EcommerceUser user = userList.get(position);

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");


        usersRef.child(user.getId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "User deleted: " + user.getName(), Toast.LENGTH_SHORT).show();
                    userList.remove(position);
                    userAdapter.notifyItemRemoved(position);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to delete user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    // Load users from Firebase Database
    private void loadUsersFromDatabase() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    EcommerceUser user = userSnapshot.getValue(EcommerceUser.class);


                    if (user != null && "user".equals(user.getRole())) {
                        userList.add(user);
                    }
                }


                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error loading users: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
