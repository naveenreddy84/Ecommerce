package com.example.ecommerce.users;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.EcommerceUser;
import com.example.ecommerce.Authentication.LoginPage;
import com.example.ecommerce.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

public class UserRegisterPage extends AppCompatActivity {

    TextView titleregister, loginLink;
    EditText username, registerEmail, registerPassword;
    Button registerBtn;
    DatabaseReference databaseReference;

    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_register_page);

        titleregister = findViewById(R.id.titleregister);
        loginLink = findViewById(R.id.loginLink);
        username = findViewById(R.id.username);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerBtn = findViewById(R.id.registerBtn);

        loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(UserRegisterPage.this, LoginPage.class);
            startActivity(intent);
            finish();
        });

        FirebaseApp.initializeApp(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        registerBtn.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = username.getText().toString().trim();
        String email = registerEmail.getText().toString().trim();
        String password = registerPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        String userId = firebaseUser != null ? firebaseUser.getUid() : "";


                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot usersSnapshot) {
                                boolean isFirstUser = !usersSnapshot.exists();


                                String role = isFirstUser ? "admin" : "user";


                                EcommerceUser user = new EcommerceUser(userId, name, email, password, role);


                                databaseReference.child(userId).setValue(user)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(UserRegisterPage.this, "Registration successful", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(UserRegisterPage.this, LoginPage.class);
                                            intent.putExtra("userId", userId);
                                            intent.putExtra("userName", name);
                                            intent.putExtra("userEmail", email);
                                            intent.putExtra("userRole", role);
                                            startActivity(intent);
                                            finish();
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(UserRegisterPage.this, "Failed to save user data", Toast.LENGTH_SHORT).show());
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                Toast.makeText(UserRegisterPage.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // If user creation fails
                        Toast.makeText(UserRegisterPage.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
