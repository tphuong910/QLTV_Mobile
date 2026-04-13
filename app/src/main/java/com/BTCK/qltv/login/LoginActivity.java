package com.BTCK.qltv.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnLogin;
    TextView tvForgotPassword;
    TaiKhoanQuery taiKhoanQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        taiKhoanQuery = new TaiKhoanQuery(this);

        btnLogin.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                performLogin(username, password);
            }
        });

        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RecoverPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void performLogin(String username, String password) {
        TaiKhoanQuery.UserInfo userInfo = taiKhoanQuery.dangNhap(username, password);
        if (userInfo == null) {
            Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
        saveSession(userInfo.tenNhanVien, userInfo.vaiTro);

        Intent intent = new Intent(LoginActivity.this, com.BTCK.qltv.dashboard.DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveSession(String ten, String vaiTro) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TenNV", ten);
        editor.putString("VaiTro", vaiTro);
        editor.apply();
    }
}