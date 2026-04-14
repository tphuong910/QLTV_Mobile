package com.BTCK.qltv.ngonngu;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;

public class UpdateNgonNguActivity extends AppCompatActivity {

    private EditText etMaNN, etTenNN;
    private Button btnCapNhat;
    private ImageView imgClose;
    private TextView tvTitle;
    private NgonNguQuery ngonNguQuery;
    private String maNN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ngon_ngu); // Dùng chung layout với Add

        etMaNN = findViewById(R.id.etMaNNNN);
        etTenNN = findViewById(R.id.etTenNNNN);
        btnCapNhat = findViewById(R.id.btnLuuNN);
        imgClose = findViewById(R.id.imgCloseAddNN);
        tvTitle = findViewById(R.id.tvTitleNN);

        tvTitle.setText("Sửa Thông Tin");
        btnCapNhat.setText("CẬP NHẬT");

        ngonNguQuery = new NgonNguQuery(this);

        // Sự kiện đóng
        imgClose.setOnClickListener(v -> finish());

        // Lấy dữ liệu từ Intent
        maNN = getIntent().getStringExtra("MaNN");
        String tenNN = getIntent().getStringExtra("TenNN");

        etMaNN.setText(maNN);
        etTenNN.setText(tenNN);
        etMaNN.setEnabled(false);

        btnCapNhat.setOnClickListener(v -> {
            String tenMoi = etTenNN.getText().toString().trim();
            if (tenMoi.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên ngôn ngữ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (ngonNguQuery.capNhatNgonNgu(maNN, tenMoi)) {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
