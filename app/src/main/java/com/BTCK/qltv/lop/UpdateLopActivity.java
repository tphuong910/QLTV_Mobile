package com.BTCK.qltv.lop;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;

import java.util.ArrayList;
import java.util.List;

public class UpdateLopActivity extends AppCompatActivity {

    private EditText etMaLop, etTenLop;
    private Spinner spnKhoa;
    private Button btnCapNhat;
    private ImageView imgClose;
    private TextView tvTitle;
    private LopQuery lopQuery;
    private List<String[]> listKhoa;
    private String maLop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lop);

        etMaLop = findViewById(R.id.etMaLopAdd);
        etTenLop = findViewById(R.id.etTenLopAdd);
        spnKhoa = findViewById(R.id.spnKhoaAdd);
        btnCapNhat = findViewById(R.id.btnLuuLop);
        imgClose = findViewById(R.id.imgCloseAddLop);
        tvTitle = findViewById(R.id.tvTitleLopAdd);

        tvTitle.setText("Sửa Thông Tin Lớp");
        btnCapNhat.setText("CẬP NHẬT");

        lopQuery = new LopQuery(this);
        
        setupSpinner();

        // Lấy dữ liệu từ Intent
        maLop = getIntent().getStringExtra("MaLop");
        String tenLop = getIntent().getStringExtra("TenLop");
        String maKhoa = getIntent().getStringExtra("MaKhoa");

        etMaLop.setText(maLop);
        etTenLop.setText(tenLop);
        etMaLop.setEnabled(false);
        
        // Set selected khoa in spinner
        for (int i = 0; i < listKhoa.size(); i++) {
            if (listKhoa.get(i)[0].equals(maKhoa)) {
                spnKhoa.setSelection(i);
                break;
            }
        }

        imgClose.setOnClickListener(v -> finish());

        btnCapNhat.setOnClickListener(v -> {
            String ten = etTenLop.getText().toString().trim();
            int selectedPos = spnKhoa.getSelectedItemPosition();
            String maKhoaMoi = listKhoa.get(selectedPos)[0];

            if (ten.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên lớp", Toast.LENGTH_SHORT).show();
                return;
            }

            if (lopQuery.capNhatLop(maLop, ten, maKhoaMoi)) {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSpinner() {
        listKhoa = lopQuery.layDanhSachKhoa();
        List<String> tenKhoaList = new ArrayList<>();
        for (String[] khoa : listKhoa) {
            tenKhoaList.add(khoa[1]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenKhoaList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnKhoa.setAdapter(adapter);
    }
}
