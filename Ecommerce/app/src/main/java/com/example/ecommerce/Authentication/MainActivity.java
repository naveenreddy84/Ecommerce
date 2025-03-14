package com.example.ecommerce.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;
import com.example.ecommerce.users.UserRegisterPage;

public class MainActivity extends AppCompatActivity {

    TextView title;

    Button userbtn,adminbtn,gotoregisterpage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        title = findViewById(R.id.title);
        userbtn = findViewById(R.id.userbtn);
        adminbtn = findViewById(R.id.adminbtn);
        gotoregisterpage = findViewById(R.id.gotoregisterpage);



        gotoregisterpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserRegisterPage.class);
                startActivity(intent);
//                finish();
            }
        });
        adminbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginPage.class);
                startActivity(intent);
//                finish();
            }
        });
        userbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginPage.class);
                startActivity(intent);
//                finish();
            }
        });








    }
}