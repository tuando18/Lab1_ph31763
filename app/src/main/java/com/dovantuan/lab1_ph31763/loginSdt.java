package com.dovantuan.lab1_ph31763;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class loginSdt extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;

    private TextInputEditText edtOtp, edtPhone;

    private Button btnGetOtp, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sdt);

        edtPhone = findViewById(R.id.edtPhone);
        edtOtp = findViewById(R.id.edtOtp);
        btnGetOtp = findViewById(R.id.btnGetOtp);
        btnLogin = findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();

        btnGetOtp.setOnClickListener(view -> {
            String phoneNumber = edtPhone.getText().toString();
            if (phoneNumber.isEmpty()) {
                Toast.makeText(loginSdt.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                return;
            }

            getOtp(phoneNumber);
        });

        btnLogin.setOnClickListener(view -> {
            String code = edtOtp.getText().toString();
            if (code.isEmpty()) {
                Toast.makeText(loginSdt.this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
                return;
            }
            verifyOTP(code);
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                edtOtp.setText(phoneAuthCredential.getSmsCode());
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(loginSdt.this, "Lỗi xác thực số điện thoại", Toast.LENGTH_SHORT).show();
                // Bạn cũng có thể ghi log lỗi để debug
                Log.e("VerificationFailed", "onVerificationFailed: " + e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
            }
        };
    }

    private void getOtp(String phoneNumber) {
        btnGetOtp.setEnabled(false);
        btnGetOtp.setText("Đang xử lý...");
        PhoneAuthOptions options = PhoneAuthOptions
                .newBuilder(mAuth)
                .setPhoneNumber("+84" + phoneNumber)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .setTimeout(100L, TimeUnit.SECONDS)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

//    private void verifyOTP(String code) {
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
//        signInWithPhoneAuthCredential(credential);
//    }

    private void verifyOTP(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        btnLogin.setEnabled(false);
        btnLogin.setText("Đang xử lý...");
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(loginSdt.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = task.getResult().getUser();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        } else {
                            Toast.makeText(loginSdt.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(loginSdt.this, "Sai mã OTP", Toast.LENGTH_SHORT).show();
                            }
                        }
                        btnLogin.setEnabled(true);
                        btnLogin.setText("Login");
                    }
                });
    }
}
