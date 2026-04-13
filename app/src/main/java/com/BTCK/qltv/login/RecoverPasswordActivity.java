package com.BTCK.qltv.login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;

public class RecoverPasswordActivity extends AppCompatActivity {

    EditText edtUser, edtEmail, edtNewPassword;
    Button btnConfirm, btnCancel;
    TaiKhoanQuery taiKhoanQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        edtUser = findViewById(R.id.edtRecoverUser);
        edtEmail = findViewById(R.id.edtRecoverEmail);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        btnConfirm = findViewById(R.id.btnConfirmRecover);
        btnCancel = findViewById(R.id.btnCancelRecover);
        taiKhoanQuery = new TaiKhoanQuery(this);

        btnCancel.setOnClickListener(v -> {
            finish();
        });

        btnConfirm.setOnClickListener(v -> {
            String user = edtUser.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String newPass = edtNewPassword.getText().toString().trim();

            if (user.isEmpty() || email.isEmpty() || newPass.isEmpty()) {
                Toast.makeText(RecoverPasswordActivity.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                updatePassword(user, email, newPass);
            }
        });
    }

    private void updatePassword(String username, String email, String newPassword) {
        boolean updated = taiKhoanQuery.doiMatKhau(username, email, newPassword);
        if (updated) {
            Toast.makeText(RecoverPasswordActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(RecoverPasswordActivity.this, "Sai tên đăng nhập hoặc email!", Toast.LENGTH_SHORT).show();
        }
    }
}
