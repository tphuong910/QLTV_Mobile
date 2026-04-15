package com.BTCK.qltv.nhaxuatban;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;

public class UpdateNhaXuatBanActivity extends AppCompatActivity {

    EditText edtMaNXB, edtTenNXB, edtDiaChi, edtEmail, edtSoDienThoai;
    Button btnUpdateNXB;
    NhaXuatBanQuery nhaXuatBanQuery;

    String maNXBCanSua = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nha_xuat_ban);

        edtMaNXB = findViewById(R.id.edtMaNXB);
        edtTenNXB = findViewById(R.id.edtTenNXB);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtEmail = findViewById(R.id.edtEmail);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        btnUpdateNXB = findViewById(R.id.btnUpdateNXB);

        nhaXuatBanQuery = new NhaXuatBanQuery(this);

        // 1. Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            maNXBCanSua = intent.getStringExtra("maNXB");
            edtMaNXB.setText(maNXBCanSua);
            edtTenNXB.setText(intent.getStringExtra("tenNXB"));
            edtDiaChi.setText(intent.getStringExtra("diaChi"));
            edtEmail.setText(intent.getStringExtra("email"));
            edtSoDienThoai.setText(intent.getStringExtra("sdt"));
        }

        // 2. Xử lý nút Cập nhật
        btnUpdateNXB.setOnClickListener(v -> {
            String tenMoi = edtTenNXB.getText().toString().trim();
            String diaChiMoi = edtDiaChi.getText().toString().trim();
            String emailMoi = edtEmail.getText().toString().trim();
            String sdtMoi = edtSoDienThoai.getText().toString().trim();

            if (tenMoi.isEmpty()) {
                Toast.makeText(this, "Tên Nhà xuất bản không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            if (maNXBCanSua != null && !maNXBCanSua.isEmpty()) {
                NhaXuatBan nxbUpdate = new NhaXuatBan(maNXBCanSua, tenMoi, diaChiMoi, emailMoi, sdtMoi);
                boolean updated = nhaXuatBanQuery.capNhatNXB(nxbUpdate);

                if (updated) {
                    Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Lỗi: Không tìm thấy Mã NXB!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}