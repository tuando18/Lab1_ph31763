package com.dovantuan.lab1_ph31763;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private TextInputLayout edtEmail, edtMatKhau;
    private Button btnDangKy;
    private TextView btnDangNhap;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

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
        setContentView(R.layout.activity_sign_up);

//        anhXa();
        edtEmail = findViewById(R.id.edtemail);
        edtMatKhau = findViewById(R.id.edtPassword_login);
        btnDangKy = findViewById(R.id.btndangki);
        btnDangNhap = findViewById(R.id.tvDangNhap);
        progressDialog = new ProgressDialog(this);

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangKy();
            }
        });
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangNhap();
            }
        });
    }

    private void anhXa() {
        edtEmail = findViewById(R.id.edtemail);
        edtMatKhau = findViewById(R.id.edtPassword_login);
        btnDangKy = findViewById(R.id.btndangki);
        btnDangNhap = findViewById(R.id.tvDangNhap);
        progressDialog = new ProgressDialog(this);
    }

    private void dangNhap() {
        Intent intent = new Intent(this, Login.class);

        String email = edtEmail.getEditText().getText().toString().trim();
        String password = edtMatKhau.getEditText().getText().toString().trim();

        intent.putExtra("email", email);
        intent.putExtra("password", password);

        startActivity(intent);
    }


    private void dangKy() {


        String email = edtEmail.getEditText().getText().toString().trim();
        String password = edtMatKhau.getEditText().getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Bạn chưa nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Sẽ mất một lúc vui lòng chờ");
        progressDialog.show();

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(getApplicationContext(), "Đăng ký thành công: " + user.getEmail(), Toast.LENGTH_LONG).show();

                            dangNhap();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}