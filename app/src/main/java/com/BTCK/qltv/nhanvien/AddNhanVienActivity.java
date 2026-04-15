package com.BTCK.qltv.nhanvien;

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

public class AddNhanVienActivity extends AppCompatActivity {

    EditText edtTenNV, edtQueQuan, edtNamSinh, edtEmail, edtSdt, edtUser, edtPass;
    Spinner spnGioiTinh, spnVaiTro;
    Button btnSave, btnCancel;

    NhanVienQuery nhanVienQuery;

    // Dữ liệu mảng đơn giản cho cửa sổ Spinner
    String[] arrGioiTinh = {"Nam", "Nữ"};
    String[] arrVaiTro = {"Thủ thư", "Quản lý"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nhanvien);

        // Ánh xạ id (findViewById)
        edtTenNV = findViewById(R.id.edtTenNV);
        edtQueQuan = findViewById(R.id.edtQueQuan);
        edtNamSinh = findViewById(R.id.edtNamSinh);
        edtEmail = findViewById(R.id.edtEmail);
        edtSdt = findViewById(R.id.edtSdt);
        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);

        spnGioiTinh = findViewById(R.id.spnGioiTinh);
        spnVaiTro = findViewById(R.id.spnVaiTro);

        btnSave = findViewById(R.id.btnSave);
        btnCancel=findViewById(R.id.btnCancel);

        nhanVienQuery = new NhanVienQuery(this);

        // Nút lùi về
        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Thiết lập Adapter cho 2 Spinner
        ArrayAdapter<String> adapterGioiTinh = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrGioiTinh);
        adapterGioiTinh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGioiTinh.setAdapter(adapterGioiTinh);

        ArrayAdapter<String> adapterVaiTro = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrVaiTro);
        adapterVaiTro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnVaiTro.setAdapter(adapterVaiTro);

        // Bấm lưu
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy nội dung từ các Edit Text
                String ten = edtTenNV.getText().toString().trim();
                String que = edtQueQuan.getText().toString().trim();
                String nam = edtNamSinh.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String sdt = edtSdt.getText().toString().trim();
                String user = edtUser.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();

                // Kiểm tra rỗng
                if (ten.equals("") || sdt.equals("") || user.equals("") || pass.equals("")) {
                    Toast.makeText(AddNhanVienActivity.this, "Vui lòng nhập đủ thông tin bắt buộc!", Toast.LENGTH_SHORT).show();
                    return; // Ngừng tiếp tục chạy
                }

                // Lấy giá trị trực tiếp từ mảng bằng cách truy cập vị trí người dùng đã chọn
                int viTriGioiTinh = spnGioiTinh.getSelectedItemPosition();
                String gioiTinh = arrGioiTinh[viTriGioiTinh];

                int viTriVaiTro = spnVaiTro.getSelectedItemPosition();
                String vaiTro = arrVaiTro[viTriVaiTro];

                // Sinh mã mới và khởi tạo Đối tượng NV
                String maNV = nhanVienQuery.taoMaNhanVienMoi();
                NhanVien nv = new NhanVien(maNV, ten, que, gioiTinh, nam, vaiTro, email, sdt, user, pass);

                // Insert (Thêm) vào Database
                boolean ketQua = nhanVienQuery.themNhanVien(nv);

                if (ketQua == true) {
                    Toast.makeText(AddNhanVienActivity.this, "Thêm thành công! Mã: " + maNV, Toast.LENGTH_SHORT).show();
                    finish(); // Trở về
                } else {
                    Toast.makeText(AddNhanVienActivity.this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
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