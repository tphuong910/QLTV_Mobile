package com.BTCK.qltv.khoa;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.BTCK.qltv.R;

public class AddKhoaActivity extends AppCompatActivity {
    EditText edtMaKhoa, edtTenKhoa;
    Button btnSaveKhoa;
    ImageButton btnBack;
    KhoaQuery khoaQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_khoa);

        edtMaKhoa = findViewById(R.id.edtMaKhoa);
        edtTenKhoa = findViewById(R.id.edtTenKhoa);
        btnSaveKhoa = findViewById(R.id.btnSaveKhoa);
        btnBack = findViewById(R.id.btnBackAddKhoa);
        
        khoaQuery = new KhoaQuery(this);

        btnBack.setOnClickListener(v -> finish());
        
        edtMaKhoa.setText(khoaQuery.taoMaKhoaMoi());

        btnSaveKhoa.setOnClickListener(v -> {
            String ma = edtMaKhoa.getText().toString();
            String ten = edtTenKhoa.getText().toString().trim();

            if (ten.isEmpty()) {
                Toast.makeText(this, "Tên Khoa không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            if (khoaQuery.themKhoa(new Khoa(ma, ten))) {
                Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
