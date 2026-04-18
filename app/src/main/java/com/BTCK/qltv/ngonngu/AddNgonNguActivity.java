package com.BTCK.qltv.ngonngu;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;

public class AddNgonNguActivity extends AppCompatActivity {

    private EditText etMaNN, etTenNN;
    private Button btnLuuNN;
    private ImageView imgClose;
    private NgonNguQuery ngonNguQuery;
    private String maNgonNguMoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ngon_ngu);

        etMaNN = findViewById(R.id.etMaNNNN);
        etTenNN = findViewById(R.id.etTenNNNN);
        btnLuuNN = findViewById(R.id.btnLuuNN);
        imgClose = findViewById(R.id.imgCloseAddNN);

        ngonNguQuery = new NgonNguQuery(this);

        imgClose.setOnClickListener(v -> finish());

        maNgonNguMoi = ngonNguQuery.taoMaNNMoi();
        etMaNN.setText(maNgonNguMoi);
        etMaNN.setEnabled(false);

        btnLuuNN.setOnClickListener(v -> {
            String ten = etTenNN.getText().toString().trim();

            if (ten.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên ngôn ngữ", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isSuccess = ngonNguQuery.themNgonNgu(maNgonNguMoi, ten);
            if (isSuccess) {
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
