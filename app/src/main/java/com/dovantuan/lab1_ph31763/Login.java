package com.dovantuan.lab1_ph31763;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {

    TextView txtchuacotaikoan,tvQuenMk;
    TextInputLayout txttk, txtmk;
    Button btndangnhap;
    private ProgressDialog progressDialog;
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            // neu user da dang nhap vao tu phien truoc thi su dung user luon
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtchuacotaikoan = findViewById(R.id.tvChuaCoTk);
        txttk = findViewById(R.id.edtemaillogin);
        txtmk = findViewById(R.id.edtPassword_login);
        tvQuenMk = findViewById(R.id.tvQuenMk);
        btndangnhap = findViewById(R.id.btndangnhap);
        progressDialog = new ProgressDialog(this);

        Intent intent = getIntent();
        if (intent != null) {
            String emailFromSignUp = intent.getStringExtra("email");
            String passwordFromSignUp = intent.getStringExtra("password");

            if (emailFromSignUp != null && passwordFromSignUp != null) {
                // Set email and password in the respective TextInputLayouts
                txttk.getEditText().setText(emailFromSignUp);
                txtmk.getEditText().setText(passwordFromSignUp);
            }
        }

        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangnhap();
            }
        });

        txtchuacotaikoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chuyensangsingup();
            }
        });

        tvQuenMk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });
    }

    private void chuyensangsingup() {
        Intent intent = new Intent(Login.this, SignUp.class);
        startActivity(intent);
    }

    private void dangnhap() {
        String email = txttk.getEditText().getText().toString().trim();
        String password = txtmk.getEditText().getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            // Display a message to the user indicating that both email and password are required
            // You can customize this part based on your needs
            Toast.makeText(this, "Email và mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Sẽ mất một lúc vui lòng chờ");
        progressDialog.show();

        // Perform the login action using Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // You may want to redirect to another activity or perform other actions here
                            Intent intent = new Intent(Login.this, ListView.class);
                            startActivity(intent);
                            finish(); // Close the current Login activity

                            Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Đăng nhập thất bại. Kiểm tra lại email và mật khẩu.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void forgotPassword() {
        String email = txttk.getEditText().getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Login.this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}