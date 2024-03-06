package com.dovantuan.lab1_ph31763;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ManHinhCho extends AppCompatActivity {
    private Button btnLogEmail, btnLogPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_hinh_cho);
        btnLogEmail = findViewById(R.id.btnEmail);
        btnLogPhone = findViewById(R.id.btnSdt);

        btnLogEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEmail();
            }
        });

        btnLogPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSdt();
            }
        });
    }

    private void loginEmail() {
        Intent intent = new Intent(ManHinhCho.this, SignUp.class);
        startActivity(intent);
    }

    private void loginSdt() {
        Intent intent = new Intent(ManHinhCho.this, LoginOTPActivity.class);
        startActivity(intent);
    }
}