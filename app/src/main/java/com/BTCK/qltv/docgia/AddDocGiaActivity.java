package com.BTCK.qltv.docgia;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;

import java.util.List;

public class AddDocGiaActivity extends AppCompatActivity {

    EditText edtMaDG, edtTenDG, edtNamSinh, edtDiaChi, edtEmail, edtSoDienThoai;
    Spinner spnGioiTinh, spnKhoa, spnLop;
    Button btnSaveDG;
    ImageButton btnBack;
    DocGiaQuery docGiaQuery;

    List<String> listKhoa;
    List<String> listLop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doc_gia);

        // Ánh xạ View
        edtMaDG = findViewById(R.id.edtMaDG);
        edtTenDG = findViewById(R.id.edtTenDG);
        edtNamSinh = findViewById(R.id.edtNamSinh);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtEmail = findViewById(R.id.edtEmail);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        spnGioiTinh = findViewById(R.id.spnGioiTinh);
        spnKhoa = findViewById(R.id.spnKhoa);
        spnLop = findViewById(R.id.spnLop);
        btnSaveDG = findViewById(R.id.btnSaveDG);
        btnBack = findViewById(R.id.btnBackAddDG);

        docGiaQuery = new DocGiaQuery(this);

        // Nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // 1. Hiển thị mã tự động
        edtMaDG.setText(docGiaQuery.taoMaDGMoi());

        // 2. Load dữ liệu Spinner Giới tính
        String[] arrGioiTinh = {"Nam", "Nữ"};
        ArrayAdapter<String> adapterGT = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrGioiTinh);
        spnGioiTinh.setAdapter(adapterGT);

        // 3. Load dữ liệu Spinner Khoa
        listKhoa = docGiaQuery.layDanhSachKhoaSpinner();
        ArrayAdapter<String> adapterKhoa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listKhoa);
        spnKhoa.setAdapter(adapterKhoa);

        // 4. Bắt sự kiện khi chọn Khoa -> Load danh sách Lớp tương ứng
        spnKhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedKhoa = listKhoa.get(position);
                String maKhoa = selectedKhoa.split(" - ")[0]; // Cắt lấy "KH001"

                listLop = docGiaQuery.layDanhSachLopSpinner(maKhoa);
                ArrayAdapter<String> adapterLop = new ArrayAdapter<>(AddDocGiaActivity.this, android.R.layout.simple_spinner_dropdown_item, listLop);
                spnLop.setAdapter(adapterLop);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 5. Xử lý nút Lưu
        btnSaveDG.setOnClickListener(v -> {
            String ten = edtTenDG.getText().toString().trim();
            String namSinh = edtNamSinh.getText().toString().trim();
            String diaChi = edtDiaChi.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String sdt = edtSoDienThoai.getText().toString().trim();
            String gioiTinh = spnGioiTinh.getSelectedItem().toString();

            if (spnKhoa.getSelectedItem() == null || spnLop.getSelectedItem() == null) {
                Toast.makeText(this, "Vui lòng chọn Khoa và Lớp", Toast.LENGTH_SHORT).show();
                return;
            }
            String maKhoa = spnKhoa.getSelectedItem().toString().split(" - ")[0];
            String maLop = spnLop.getSelectedItem().toString().split(" - ")[0];

            if (ten.isEmpty() || namSinh.isEmpty()) {
                Toast.makeText(this, "Tên và Năm sinh không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            DocGia dg = new DocGia(docGiaQuery.taoMaDGMoi(), maKhoa, maLop, ten, namSinh, gioiTinh, diaChi, email, sdt);
            if (docGiaQuery.themDocGia(dg)) {
                Toast.makeText(this, "Đã thêm Độc giả thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
