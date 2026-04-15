package com.BTCK.qltv.docgia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;

import java.util.List;

public class UpdateDocGiaActivity extends AppCompatActivity {

    EditText edtMaDG, edtTenDG, edtNamSinh, edtDiaChi, edtEmail, edtSoDienThoai;
    Spinner spnGioiTinh, spnKhoa, spnLop;
    Button btnUpdateDG;
    DocGiaQuery docGiaQuery;

    List<String> listKhoa;
    List<String> listLop;
    String maDGCanSua = "", maKhoaCu = "", maLopCu = "";
    boolean isFirstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_doc_gia);

        // Ánh xạ
        edtMaDG = findViewById(R.id.edtMaDG);
        edtTenDG = findViewById(R.id.edtTenDG);
        edtNamSinh = findViewById(R.id.edtNamSinh);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtEmail = findViewById(R.id.edtEmail);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        spnGioiTinh = findViewById(R.id.spnGioiTinh);
        spnKhoa = findViewById(R.id.spnKhoa);
        spnLop = findViewById(R.id.spnLop);
        btnUpdateDG = findViewById(R.id.btnUpdateDG);

        docGiaQuery = new DocGiaQuery(this);

        // Setup Spinners cơ bản
        String[] arrGioiTinh = {"Nam", "Nữ"};
        ArrayAdapter<String> adapterGT = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrGioiTinh);
        spnGioiTinh.setAdapter(adapterGT);

        listKhoa = docGiaQuery.layDanhSachKhoaSpinner();
        ArrayAdapter<String> adapterKhoa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listKhoa);
        spnKhoa.setAdapter(adapterKhoa);

        // Bắt sự kiện chọn Khoa để load Lớp
        spnKhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String maKhoa = listKhoa.get(position).split(" - ")[0];
                listLop = docGiaQuery.layDanhSachLopSpinner(maKhoa);
                ArrayAdapter<String> adapterLop = new ArrayAdapter<>(UpdateDocGiaActivity.this, android.R.layout.simple_spinner_dropdown_item, listLop);
                spnLop.setAdapter(adapterLop);

                // Nếu là lần đầu mở form, tự động set lại Lớp cũ
                if (isFirstLoad && !maLopCu.isEmpty() && listLop != null) {
                    for (int i = 0; i < listLop.size(); i++) {
                        if (listLop.get(i).startsWith(maLopCu)) {
                            spnLop.setSelection(i);
                            break;
                        }
                    }
                    isFirstLoad = false; // Đánh dấu đã load xong
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Nhận dữ liệu Intent
        Intent intent = getIntent();
        if (intent != null) {
            maDGCanSua = intent.getStringExtra("maDG");
            maKhoaCu = intent.getStringExtra("maKhoa");
            maLopCu = intent.getStringExtra("maLop");

            edtMaDG.setText(maDGCanSua);
            edtTenDG.setText(intent.getStringExtra("tenDG"));
            edtNamSinh.setText(intent.getStringExtra("namSinh"));
            edtDiaChi.setText(intent.getStringExtra("diaChi"));
            edtEmail.setText(intent.getStringExtra("email"));
            edtSoDienThoai.setText(intent.getStringExtra("sdt"));

            // Set giới tính cũ
            String gtCu = intent.getStringExtra("gioiTinh");
            if (gtCu != null && gtCu.equals("Nữ")) spnGioiTinh.setSelection(1);

            // Set khoa cũ
            for (int i = 0; i < listKhoa.size(); i++) {
                if (listKhoa.get(i).startsWith(maKhoaCu)) {
                    spnKhoa.setSelection(i);
                    break;
                }
            }
        }

        // Xử lý Cập nhật
        btnUpdateDG.setOnClickListener(v -> {
            String tenMoi = edtTenDG.getText().toString().trim();
            if (tenMoi.isEmpty()) {
                Toast.makeText(this, "Tên Độc giả không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            String maKhoa = spnKhoa.getSelectedItem().toString().split(" - ")[0];
            String maLop = spnLop.getSelectedItem().toString().split(" - ")[0];
            String gioiTinh = spnGioiTinh.getSelectedItem().toString();

            DocGia dgUpdate = new DocGia(maDGCanSua, maKhoa, maLop, tenMoi,
                    edtNamSinh.getText().toString(), gioiTinh, edtDiaChi.getText().toString(),
                    edtEmail.getText().toString(), edtSoDienThoai.getText().toString());

            if (docGiaQuery.capNhatDocGia(dgUpdate)) {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}