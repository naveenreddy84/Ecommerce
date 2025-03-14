package com.example.ecommerce.AdminFiles;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.AdminFiles.AdminNavbarFiles.HomeNavbar.HomeFragment;
import com.example.ecommerce.AdminFiles.AdminNavbarFiles.HomeNavbar.ManageUsers;
import com.example.ecommerce.R;
import com.example.ecommerce.AdminFiles.AdminNavbarFiles.HomeNavbar.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminDashboard extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);


        userRole = getIntent().getStringExtra("userRole");


        bottomNavigationView = findViewById(R.id.bottomNavigationView);




        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_home) {
                openAdminHome();
                return true;
            } else if (item.getItemId() == R.id.nav_users) {
                openManageUsersFragment();
                return true;
            } else if (item.getItemId() == R.id.nav_settings) {
                openSettingsFragment();
                return true;
            }
            return false;
        });

        openAdminHome();
    }

    private void  openAdminHome(){

        HomeFragment ManageHomeFragment = new HomeFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, ManageHomeFragment)
                .addToBackStack(null)
                .commit();


    }


            private void openManageUsersFragment() {
                // Creating an instance of the ManageUsers fragment
                ManageUsers manageUsersFragment = new ManageUsers();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentFrame, manageUsersFragment)
                        .addToBackStack(null)
                        .commit();
            }


     private void  openSettingsFragment(){

        SettingsFragment ManageSettingsFragment = new SettingsFragment();

         getSupportFragmentManager().beginTransaction()
                 .replace(R.id.contentFrame, ManageSettingsFragment)
                 .addToBackStack(null)
                 .commit();


    }

}
