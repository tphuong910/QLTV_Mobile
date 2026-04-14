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

public class AddLopActivity extends AppCompatActivity {

    private EditText etMaLop, etTenLop;
    private Spinner spnKhoa;
    private Button btnLuu;
    private ImageView imgClose;
    private TextView tvTitle;
    private LopQuery lopQuery;
    private List<String[]> listKhoa;
    private String maMoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lop);

        etMaLop = findViewById(R.id.etMaLopAdd);
        etTenLop = findViewById(R.id.etTenLopAdd);
        spnKhoa = findViewById(R.id.spnKhoaAdd);
        btnLuu = findViewById(R.id.btnLuuLop);
        imgClose = findViewById(R.id.imgCloseAddLop);
        tvTitle = findViewById(R.id.tvTitleLopAdd);

        lopQuery = new LopQuery(this);
        
        setupSpinner();
        
        maMoi = lopQuery.taoMaLopMoi();
        etMaLop.setText(maMoi);
        etMaLop.setEnabled(false);

        imgClose.setOnClickListener(v -> finish());

        btnLuu.setOnClickListener(v -> {
            String ten = etTenLop.getText().toString().trim();
            int selectedPos = spnKhoa.getSelectedItemPosition();
            String maKhoa = listKhoa.get(selectedPos)[0];

            if (ten.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên lớp", Toast.LENGTH_SHORT).show();
                return;
            }

            if (lopQuery.themLop(maMoi, ten, maKhoa)) {
                Toast.makeText(this, "Thêm lớp thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
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
