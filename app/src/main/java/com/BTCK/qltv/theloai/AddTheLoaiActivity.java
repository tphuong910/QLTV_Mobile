package com.BTCK.qltv.theloai;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;

public class AddTheLoaiActivity extends AppCompatActivity {

    EditText edtMaTL, edtTenTL;
    Button btnSaveTheLoai;
    ImageButton btnBack;
    TheLoaiQuery theLoaiQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_the_loai);

        edtMaTL = findViewById(R.id.edtMaTL);
        edtTenTL = findViewById(R.id.edtTenTL);
        btnSaveTheLoai = findViewById(R.id.btnSaveTheLoai);
        btnBack = findViewById(R.id.btnBackAddTL);

        theLoaiQuery = new TheLoaiQuery(this);

        // Nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Hiển thị mã tự động lên UI
        String maTuDong = theLoaiQuery.taoMaTheLoaiMoi();
        edtMaTL.setText(maTuDong);

        btnSaveTheLoai.setOnClickListener(v -> {
            String ma = edtMaTL.getText().toString();
            String ten = edtTenTL.getText().toString().trim();

            if (ten.isEmpty()) {
                Toast.makeText(this, "Tên không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            TheLoai theLoai = new TheLoai(ma, ten);
            boolean inserted = theLoaiQuery.themTheLoai(theLoai);

            if (inserted) {
                Toast.makeText(this, "Đã thêm Thể Loại thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
