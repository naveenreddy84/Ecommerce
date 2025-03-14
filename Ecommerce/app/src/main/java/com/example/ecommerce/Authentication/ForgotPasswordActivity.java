package com.example.ecommerce.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText resetemailEditText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        mAuth = FirebaseAuth.getInstance();


        resetemailEditText = findViewById(R.id.resetemailEditText);


        findViewById(R.id.resetPasswordButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }


    private void resetPassword() {
        String email = resetemailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            resetemailEditText.setError("Please enter your email address");
            resetemailEditText.requestFocus();
            return;
        }


        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        Intent intent = new Intent(ForgotPasswordActivity.this, LoginPage.class);
                        startActivity(intent);
                        Toast.makeText(ForgotPasswordActivity.this,
                                "Password reset email sent. Please check your inbox.",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else {

                        Toast.makeText(ForgotPasswordActivity.this,
                                "Failed to send password reset email: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
