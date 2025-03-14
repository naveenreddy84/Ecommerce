package com.example.ecommerce.users;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecommerce.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserDashboard extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        if (bottomNavigationView == null) {
            Log.e("UserDashboard", "Bottom Navigation View is null!");
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_home) {
                openuserHome();
                return true;
            } else if (item.getItemId() == R.id.nav_orders) {
                openUsersordersFragment();
                return true;
            } else if (item.getItemId() == R.id.nav_cart) {
                openuserscartFragment();
                return true;
            }else if (item.getItemId() == R.id.nav_profile) {
                openuserprofileFragment();
                return true;
            }
            return false;
        });
        openuserHome();

        }


    private void   openuserHome(){

        UserHomeFragment userHomeFragment = new UserHomeFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, userHomeFragment)
                .addToBackStack(null)
                .commit();


    }
    private void   openUsersordersFragment(){

        UsersOrdersFragment ManageUsersOrdersFragment = new UsersOrdersFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, ManageUsersOrdersFragment)
                .addToBackStack(null)
                .commit();


    }

    private void  openuserscartFragment(){

        UserCartFragment ManageUsersCartFragment = new UserCartFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, ManageUsersCartFragment)
                .addToBackStack(null)
                .commit();


    }

    private void   openuserprofileFragment(){

        UsersProfileFragment ManageUserProfileFragment = new UsersProfileFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, ManageUserProfileFragment)
                .addToBackStack(null)
                .commit();


    }

    }




