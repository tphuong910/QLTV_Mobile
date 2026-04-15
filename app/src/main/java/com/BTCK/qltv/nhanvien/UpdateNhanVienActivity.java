package com.BTCK.qltv.nhanvien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;

public class UpdateNhanVienActivity extends AppCompatActivity {

    EditText edtTenNV, edtQueQuan, edtNamSinh, edtEmail, edtSdt, edtUser, edtPass;
    Spinner spnGioiTinh, spnVaiTro;
    Button btnUpdate, btnCancel;

    NhanVienQuery nhanVienQuery;
    String maNV;

    // Dữ liệu mảng đơn giản
    String[] arrGioiTinh = {"Nam", "Nữ"};
    String[] arrVaiTro = {"Thủ thư", "Quản lý"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nhanvien);

        edtTenNV = findViewById(R.id.edtTenNV);
        edtQueQuan = findViewById(R.id.edtQueQuan);
        edtNamSinh = findViewById(R.id.edtNamSinh);
        edtEmail = findViewById(R.id.edtEmail);
        edtSdt = findViewById(R.id.edtSdt);
        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);

        spnGioiTinh = findViewById(R.id.spnGioiTinh);
        spnVaiTro = findViewById(R.id.spnVaiTro);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);

        nhanVienQuery = new NhanVienQuery(this);

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Đọc dữ liệu Intent gửi qua
        Intent intent = getIntent();
        maNV = intent.getStringExtra("maNV");
        edtTenNV.setText(intent.getStringExtra("tenNV"));
        edtQueQuan.setText(intent.getStringExtra("queQuan"));
        edtNamSinh.setText(intent.getStringExtra("namSinh"));
        edtEmail.setText(intent.getStringExtra("email"));
        edtSdt.setText(intent.getStringExtra("sdt"));
        edtUser.setText(intent.getStringExtra("user"));
        edtPass.setText(intent.getStringExtra("pass"));

        String gioiTinhCu = intent.getStringExtra("gioiTinh");
        String vaiTroCu = intent.getStringExtra("vaiTro");

        // Thiết lập Adapter
        ArrayAdapter<String> adapterGioiTinh = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrGioiTinh);
        adapterGioiTinh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGioiTinh.setAdapter(adapterGioiTinh);

        // So sánh chuỗi (thủ công) để gán vị trí Spinner Chọn
        if (gioiTinhCu != null && gioiTinhCu.equals("Nữ")) {
            spnGioiTinh.setSelection(1);
        } else {
            spnGioiTinh.setSelection(0); // Nam
        }

        ArrayAdapter<String> adapterVaiTro = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrVaiTro);
        adapterVaiTro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnVaiTro.setAdapter(adapterVaiTro);

        if (vaiTroCu != null && vaiTroCu.equals("Quản lý")) {
            spnVaiTro.setSelection(1);
        } else {
            spnVaiTro.setSelection(0); // Thủ thư
        }

        // Bấm Cập nhật
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edtTenNV.getText().toString().trim();
                String que = edtQueQuan.getText().toString().trim();
                String nam = edtNamSinh.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String sdt = edtSdt.getText().toString().trim();
                String user = edtUser.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();

                if (maNV == null || maNV.equals("")) {
                    Toast.makeText(UpdateNhanVienActivity.this, "Lỗi mã!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (ten.equals("") || sdt.equals("") || user.equals("") || pass.equals("")) {
                    Toast.makeText(UpdateNhanVienActivity.this, "Vui lòng nhập đủ bắt buộc!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String gioiTinhMoi = arrGioiTinh[spnGioiTinh.getSelectedItemPosition()];
                String vaiTroMoi = arrVaiTro[spnVaiTro.getSelectedItemPosition()];

                NhanVien nvMoi = new NhanVien(maNV, ten, que, gioiTinhMoi, nam, vaiTroMoi, email, sdt, user, pass);

                boolean ketQua = nhanVienQuery.suaNhanVien(nvMoi);

                if (ketQua == true) {
                    Toast.makeText(UpdateNhanVienActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UpdateNhanVienActivity.this, "Thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }
}