package com.example.ecommerce.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;

public class SplashScreen extends AppCompatActivity {


    TextView splashscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);


        splashscreen = findViewById(R.id.splashscreen);


        new
                Handler().postDelayed(() -> {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                   startActivity(intent);
                      finish();
        }, 3000);
    }

    }
