package com.BTCK.qltv.tacgia;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;

public class AddTacGiaActivity extends AppCompatActivity {

    private EditText etMaTG, etTenTG, etNamSinhTG, etGioiTinhTG, etQuocTichTG;
    private Button btnLuu;
    private ImageView imgClose;
    private TacGiaQuery tacGiaQuery;
    private String maMoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tac_gia);

        etMaTG = findViewById(R.id.etMaTG);
        etTenTG = findViewById(R.id.etTenTG);
        etNamSinhTG = findViewById(R.id.etNamSinhTG);
        etGioiTinhTG = findViewById(R.id.etGioiTinhTG);
        etQuocTichTG = findViewById(R.id.etQuocTichTG);
        btnLuu = findViewById(R.id.btnLuuTG);
        imgClose = findViewById(R.id.imgCloseAddTG);

        tacGiaQuery = new TacGiaQuery(this);
        
        // Tự động sinh mã mới và hiển thị
        maMoi = tacGiaQuery.taoMaTGMoi();
        etMaTG.setText(maMoi);
        etMaTG.setEnabled(false); // Khóa không cho người dùng chỉnh sửa

        imgClose.setOnClickListener(v -> finish());

        btnLuu.setOnClickListener(v -> {
            String ten = etTenTG.getText().toString().trim();
            String namSinh = etNamSinhTG.getText().toString().trim();
            String gioiTinh = etGioiTinhTG.getText().toString().trim();
            String quocTich = etQuocTichTG.getText().toString().trim();

            if (ten.isEmpty() || namSinh.isEmpty() || gioiTinh.isEmpty() || quocTich.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (tacGiaQuery.themTacGia(maMoi, ten, namSinh, gioiTinh, quocTich)) {
                Toast.makeText(this, "Thêm tác giả thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
