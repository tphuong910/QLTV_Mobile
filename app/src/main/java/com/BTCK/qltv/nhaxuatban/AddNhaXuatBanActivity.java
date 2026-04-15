package com.BTCK.qltv.nhaxuatban;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;

public class AddNhaXuatBanActivity extends AppCompatActivity {

    EditText edtMaNXB, edtTenNXB, edtDiaChi, edtEmail, edtSoDienThoai;
    Button btnSaveNXB;
    NhaXuatBanQuery nhaXuatBanQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nha_xuat_ban);

        edtMaNXB = findViewById(R.id.edtMaNXB);
        edtTenNXB = findViewById(R.id.edtTenNXB);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtEmail = findViewById(R.id.edtEmail);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        btnSaveNXB = findViewById(R.id.btnSaveNXB);

        nhaXuatBanQuery = new NhaXuatBanQuery(this);

        // Hiển thị mã tự động lên UI
        String maTuDong = nhaXuatBanQuery.taoMaNXBMoi();
        edtMaNXB.setText(maTuDong);

        btnSaveNXB.setOnClickListener(v -> {
            String ma = nhaXuatBanQuery.taoMaNXBMoi();
            String ten = edtTenNXB.getText().toString().trim();
            String diaChi = edtDiaChi.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String sdt = edtSoDienThoai.getText().toString().trim();

            if (ten.isEmpty()) {
                Toast.makeText(this, "Tên NXB không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            NhaXuatBan nxb = new NhaXuatBan(ma, ten, diaChi, email, sdt);
            boolean inserted = nhaXuatBanQuery.themNXB(nxb);

            if (inserted) {
                Toast.makeText(this, "Đã thêm NXB: " + ma, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}