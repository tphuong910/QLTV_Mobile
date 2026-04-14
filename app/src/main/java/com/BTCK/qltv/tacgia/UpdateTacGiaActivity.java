package com.BTCK.qltv.tacgia;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;

public class UpdateTacGiaActivity extends AppCompatActivity {

    private EditText etMaTG, etTenTG, etNamSinhTG, etGioiTinhTG, etQuocTichTG;
    private Button btnCapNhat;
    private ImageView imgClose;
    private TextView tvTitle;
    private TacGiaQuery tacGiaQuery;
    private String maTG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tac_gia);

        etMaTG = findViewById(R.id.etMaTG);
        etTenTG = findViewById(R.id.etTenTG);
        etNamSinhTG = findViewById(R.id.etNamSinhTG);
        etGioiTinhTG = findViewById(R.id.etGioiTinhTG);
        etQuocTichTG = findViewById(R.id.etQuocTichTG);
        btnCapNhat = findViewById(R.id.btnLuuTG);
        imgClose = findViewById(R.id.imgCloseAddTG);
        tvTitle = findViewById(R.id.tvTitleTG);

        tvTitle.setText("Sửa Thông Tin Tác Giả");
        btnCapNhat.setText("CẬP NHẬT");

        tacGiaQuery = new TacGiaQuery(this);

        imgClose.setOnClickListener(v -> finish());

        // Lấy dữ liệu từ Intent
        maTG = getIntent().getStringExtra("MaTG");
        etMaTG.setText(maTG);
        etTenTG.setText(getIntent().getStringExtra("TenTG"));
        etNamSinhTG.setText(getIntent().getStringExtra("NamSinh"));
        etGioiTinhTG.setText(getIntent().getStringExtra("GioiTinh"));
        etQuocTichTG.setText(getIntent().getStringExtra("QuocTich"));

        btnCapNhat.setOnClickListener(v -> {
            String ten = etTenTG.getText().toString().trim();
            String namSinh = etNamSinhTG.getText().toString().trim();
            String gioiTinh = etGioiTinhTG.getText().toString().trim();
            String quocTich = etQuocTichTG.getText().toString().trim();

            if (ten.isEmpty() || namSinh.isEmpty() || gioiTinh.isEmpty() || quocTich.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (tacGiaQuery.capNhatTacGia(maTG, ten, namSinh, gioiTinh, quocTich)) {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}