package com.BTCK.qltv.sach;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddSachActivity extends AppCompatActivity {

    EditText edtTenSach, edtSoLuong, edtNamXB, edtMaTL, edtMaTG, edtMaNXB, edtMaNN, edtMaViTri;
    Button btnSaveSach;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sach);

        edtTenSach = findViewById(R.id.edtTenSach);
        edtSoLuong = findViewById(R.id.edtSoLuong);
        edtNamXB = findViewById(R.id.edtNamXB);
        edtMaTL = findViewById(R.id.edtMaTL);
        edtMaTG = findViewById(R.id.edtMaTG);
        edtMaNXB = findViewById(R.id.edtMaNXB);
        edtMaNN = findViewById(R.id.edtMaNN);
        edtMaViTri = findViewById(R.id.edtMaViTri);
        btnSaveSach = findViewById(R.id.btnSaveSach);

        database = FirebaseDatabase.getInstance().getReference("sach");

        btnSaveSach.setOnClickListener(v -> {
            String ten = edtTenSach.getText().toString();
            String maTL = edtMaTL.getText().toString();
            String maTG = edtMaTG.getText().toString();
            String maNXB = edtMaNXB.getText().toString();
            String maNN = edtMaNN.getText().toString();
            String maViTri = edtMaViTri.getText().toString();
            String strSoLuong = edtSoLuong.getText().toString();
            String strNamXB = edtNamXB.getText().toString();

            if (ten.isEmpty() || maTL.isEmpty() || strSoLuong.isEmpty() || strNamXB.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            int soLuong = Integer.parseInt(strSoLuong);
            int namXB = Integer.parseInt(strNamXB);

            String bookId = database.push().getKey();
            Sach newSach = new Sach(bookId, maTG, maNXB, maTL, ten, maNN, maViTri, namXB, soLuong);

            database.child(bookId).setValue(newSach);

            Toast.makeText(this, "Thêm sách thành công!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}