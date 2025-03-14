package com.example.ecommerce.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.AdminFiles.AdminDashboard;
import com.example.ecommerce.EcommerceUser;
import com.example.ecommerce.R;
import com.example.ecommerce.users.UserDashboard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

public class LoginPage extends AppCompatActivity {

    EditText emailField, passwordField;

    TextView forgotpasswordLink;
    Button loginButton;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);
        forgotpasswordLink =findViewById(R.id.forgotpasswordLink);


        mAuth = FirebaseAuth.getInstance();


        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        loginButton.setOnClickListener(v -> loginUser());
        forgotpasswordLink.setOnClickListener(v -> {
          Intent intent =  new Intent(LoginPage.this, ForgotPasswordActivity.class);
startActivity(intent);
//finish();


        });

    }



    private void loginUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser != null) {
                            String userId = currentUser.getUid();
                            getUserRoleAndNavigate(userId);
                        }
                    } else {

                        Toast.makeText(LoginPage.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getUserRoleAndNavigate(String userId) {

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    EcommerceUser user = dataSnapshot.getValue(EcommerceUser.class);
                    if (user != null) {
                        String userRole = user.getRole();
                        navigateBasedOnRole(userId, user.getName(), user.getEmail(), userRole);
                    } else {
                        Toast.makeText(LoginPage.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginPage.this, "No user data found for this email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("LoginPage", "Database Error: " + databaseError.getMessage());
                Toast.makeText(LoginPage.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateBasedOnRole(String userId, String userName, String userEmail, String userRole) {
        Intent intent;
        if ("admin".equals(userRole)) {
            intent = new Intent(LoginPage.this, AdminDashboard.class);
            startActivity(intent);
        } else if ("user".equals(userRole)) {
            intent = new Intent(LoginPage.this, UserDashboard.class);
        } else {
            Toast.makeText(LoginPage.this, "Unknown user role", Toast.LENGTH_SHORT).show();
            return;
        }

        intent.putExtra("userId", userId);
        intent.putExtra("userName", userName);
        intent.putExtra("userEmail", userEmail);
        intent.putExtra("userRole", userRole);
        startActivity(intent);
        finish();
    }
}
