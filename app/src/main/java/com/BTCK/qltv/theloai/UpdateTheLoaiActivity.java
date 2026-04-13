package com.BTCK.qltv.theloai;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;

public class UpdateTheLoaiActivity extends AppCompatActivity {

    EditText edtMaTL, edtTenTL;
    Button btnSaveTheLoai;
    TheLoaiQuery theLoaiQuery;
    String maCu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_the_loai);

        edtMaTL = findViewById(R.id.edtMaTL);
        edtTenTL = findViewById(R.id.edtTenTL);
        btnSaveTheLoai = findViewById(R.id.btnSaveTheLoai);

        theLoaiQuery = new TheLoaiQuery(this);

        maCu = getIntent().getStringExtra("maTL");
        edtMaTL.setText(getIntent().getStringExtra("maTL"));
        edtTenTL.setText(getIntent().getStringExtra("tenTL"));

        btnSaveTheLoai.setOnClickListener(v -> {
            String ma = edtMaTL.getText().toString().trim(); // Mã không cho sửa
            String ten = edtTenTL.getText().toString().trim();

            if (ten.isEmpty()) {
                Toast.makeText(this, "Tên Thể loại không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            if (maCu == null || maCu.isEmpty()) {
                Toast.makeText(this, "Không tìm thấy thể loại cần sửa", Toast.LENGTH_SHORT).show();
                return;
            }

            TheLoai theLoaiMoi = new TheLoai(ma, ten);
            boolean updated = theLoaiQuery.suaTheLoai(maCu, theLoaiMoi);

            if (updated) {
                Toast.makeText(this, "Đã cập nhật Thể Loại", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}