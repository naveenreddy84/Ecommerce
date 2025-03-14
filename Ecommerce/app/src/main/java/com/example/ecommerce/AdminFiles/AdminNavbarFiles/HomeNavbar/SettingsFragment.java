package com.example.ecommerce.AdminFiles.AdminNavbarFiles.HomeNavbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Authentication.LoginPage;
import com.example.ecommerce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SettingsAdapter settingsAdapter;
    private List<String> settingsList;

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;

    private String userName,email;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        View view = inflater.inflate(R.layout.fragment_settings, container, false);


        recyclerView = view.findViewById(R.id.settingsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        settingsList = new ArrayList<>();
        settingsList.add("Logout");



        settingsAdapter = new SettingsAdapter(settingsList, position -> {
            handleSettingItemClick(position);
        });
        recyclerView.setAdapter(settingsAdapter);

        return view;
    }


    private void handleSettingItemClick(int position) {
        switch (position) {
            case 0:

                logout();
                break;
            case 1:

                Toast.makeText(getContext(), "Change Password clicked", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(getContext(), "Notification Settings clicked", Toast.LENGTH_SHORT).show();
                break;


            default:
                break;
        }
    }



    private void logout() {

        mAuth.signOut();


        Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(getContext(), LoginPage.class);
        startActivity(intent);
        getActivity().finish();

    }

}
