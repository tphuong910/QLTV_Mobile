package com.BTCK.qltv.khoa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.BTCK.qltv.R;

public class UpdateKhoaActivity extends AppCompatActivity {
    EditText edtMaKhoa, edtTenKhoa;
    Button btnUpdateKhoa;
    KhoaQuery khoaQuery;
    String maKhoaCanSua = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_khoa);

        edtMaKhoa = findViewById(R.id.edtMaKhoa);
        edtTenKhoa = findViewById(R.id.edtTenKhoa);
        btnUpdateKhoa = findViewById(R.id.btnUpdateKhoa);
        khoaQuery = new KhoaQuery(this);

        Intent intent = getIntent();
        if (intent != null) {
            maKhoaCanSua = intent.getStringExtra("maKhoa");
            edtMaKhoa.setText(maKhoaCanSua);
            edtTenKhoa.setText(intent.getStringExtra("tenKhoa"));
        }

        btnUpdateKhoa.setOnClickListener(v -> {
            String tenMoi = edtTenKhoa.getText().toString().trim();
            if (tenMoi.isEmpty()) {
                Toast.makeText(this, "Không để trống tên", Toast.LENGTH_SHORT).show();
                return;
            }
            if (khoaQuery.capNhatKhoa(new Khoa(maKhoaCanSua, tenMoi))) {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}