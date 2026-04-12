package com.BTCK.qltv.login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RecoverPasswordActivity extends AppCompatActivity {

    EditText edtUser, edtEmail, edtNewPassword;
    Button btnConfirm, btnCancel;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        edtUser = findViewById(R.id.edtRecoverUser);
        edtEmail = findViewById(R.id.edtRecoverEmail);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        btnConfirm = findViewById(R.id.btnConfirmRecover);
        btnCancel = findViewById(R.id.btnCancelRecover);

        // Kết nối bảng "nhanvien" trong Firebase
        database = FirebaseDatabase.getInstance().getReference("nhanvien");

        btnCancel.setOnClickListener(v -> {
            finish(); // Đóng màn hình này
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
        // Tìm tên đăng nhập
        Query checkUser = database.orderByChild("User").equalTo(username);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Chạy vòng lặp lấy thông tin user
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String dbEmail = userSnapshot.child("Email").getValue(String.class);

                        // Kiểm tra email có khớp không
                        if (dbEmail != null && dbEmail.equalsIgnoreCase(email)) {
                            // Cập nhật mật khẩu mới
                            userSnapshot.getRef().child("Pass").setValue(newPassword)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(RecoverPasswordActivity.this, "Đổi mật khẩu thành công! Hãy đăng nhập lại.", Toast.LENGTH_LONG).show();
                                            finish(); // Quay lại đăng nhập
                                        } else {
                                            Toast.makeText(RecoverPasswordActivity.this, "Lỗi cập nhật mật khẩu!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(RecoverPasswordActivity.this, "Email không đúng!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(RecoverPasswordActivity.this, "Không tìm thấy tên đăng nhập!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RecoverPasswordActivity.this, "Lỗi cơ sở dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
