package com.BTCK.qltv.sach;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;
import com.BTCK.qltv.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class AddSachActivity extends AppCompatActivity {

    EditText edtTenSach, edtSoLuong, edtNamXB;
    Spinner spnMaTL, spnMaTG, spnMaNXB, spnMaNN, spnMaViTri;
    Button btnSaveSach;

    SachQuery sachQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sach);

        edtTenSach = findViewById(R.id.edtTenSach);
        edtSoLuong = findViewById(R.id.edtSoLuong);
        edtNamXB = findViewById(R.id.edtNamXB);
        
        spnMaTL = findViewById(R.id.spnMaTL);
        spnMaTG = findViewById(R.id.spnMaTG);
        spnMaNXB = findViewById(R.id.spnMaNXB);
        spnMaNN = findViewById(R.id.spnMaNN);
        spnMaViTri = findViewById(R.id.spnMaViTri);
        
        btnSaveSach = findViewById(R.id.btnSaveSach);

        sachQuery = new SachQuery(this);

        // Load dữ liệu lên các Spinner
        loadSpinnerData(spnMaTL, "theloai", "MaTL", "TenTL", null);
        loadSpinnerData(spnMaTG, "tacgia", "MaTG", "TenTG", null);
        loadSpinnerData(spnMaNXB, "nhaxuatban", "MaNXB", "TenNXB", null);
        loadSpinnerData(spnMaNN, "ngonngu", "MaNN", "TenNN", null);
        loadSpinnerData(spnMaViTri, "kesach", "MaViTri", "TenKe", null);

        btnSaveSach.setOnClickListener(v -> {
            String ten = edtTenSach.getText().toString().trim();
            String strSoLuong = edtSoLuong.getText().toString().trim();
            String strNamXB = edtNamXB.getText().toString().trim();

            if (ten.isEmpty() || strSoLuong.isEmpty() || strNamXB.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin sách!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy ID từ Spinner
            String maTL = ((SpinnerItem) spnMaTL.getSelectedItem()).getId();
            String maTG = ((SpinnerItem) spnMaTG.getSelectedItem()).getId();
            String maNXB = ((SpinnerItem) spnMaNXB.getSelectedItem()).getId();
            String maNN = ((SpinnerItem) spnMaNN.getSelectedItem()).getId();
            String maViTri = ((SpinnerItem) spnMaViTri.getSelectedItem()).getId();

            int soLuong;
            int namXB;
            try {
                soLuong = Integer.parseInt(strSoLuong);
                namXB = Integer.parseInt(strNamXB);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số lượng và năm xuất bản phải là số!", Toast.LENGTH_SHORT).show();
                return;
            }

            String maSach = sachQuery.taoMaSachMoi();
            Sach sach = new Sach(maSach, maTG, maNXB, maTL, ten, maNN, maViTri, namXB, soLuong);

            boolean inserted = sachQuery.themSach(sach);
            if (inserted) {
                Toast.makeText(this, "Thêm sách thành công! Mã: " + maSach, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm sách thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSpinnerData(Spinner spinner, String tableName, String idCol, String nameCol, String selectedId) {
        SQLiteHelper dbHelper = new SQLiteHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + idCol + ", " + nameCol + " FROM " + tableName, null);

        List<SpinnerItem> list = new ArrayList<>();
        int selectedIndex = 0;
        int currentIndex = 0;

        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            list.add(new SpinnerItem(id, name));

            if (selectedId != null && id.equals(selectedId)) {
                selectedIndex = currentIndex;
            }
            currentIndex++;
        }

        cursor.close();
        db.close();
        dbHelper.close();

        if (list.isEmpty()) {
            list.add(new SpinnerItem("", "-- Trống --"));
        }

        ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (selectedId != null) {
            spinner.setSelection(selectedIndex);
        }
    }
}