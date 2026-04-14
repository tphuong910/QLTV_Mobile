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

public class UpdateSachActivity extends AppCompatActivity {

    EditText edtTenSach, edtSoLuong, edtNamXB;
    Spinner spnMaTL, spnMaTG, spnMaNXB, spnMaNN, spnMaViTri;
    Button btnSaveSach;
    SachQuery sachQuery;
    String maSach;

    // Các danh sách dùng để lưu Mã tương ứng với dữ liệu trên Spinner
    ArrayList<String> dsMaTL = new ArrayList<>();
    ArrayList<String> dsMaTG = new ArrayList<>();
    ArrayList<String> dsMaNXB = new ArrayList<>();
    ArrayList<String> dsMaNN = new ArrayList<>();
    ArrayList<String> dsMaViTri = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sach);

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

        maSach = getIntent().getStringExtra("maSach");
        edtTenSach.setText(getIntent().getStringExtra("tenSach"));
        edtSoLuong.setText(String.valueOf(getIntent().getIntExtra("soLuong", 0)));
        edtNamXB.setText(String.valueOf(getIntent().getIntExtra("namXB", 0)));

        // Load dữ liệu lên Spinner và lưu mã vào các danh sách song song
        loadSpinnerData(spnMaTL, "theloai", "MaTL", "TenTL", dsMaTL, getIntent().getStringExtra("maTL"));
        loadSpinnerData(spnMaTG, "tacgia", "MaTG", "TenTG", dsMaTG, getIntent().getStringExtra("maTG"));
        loadSpinnerData(spnMaNXB, "nhaxuatban", "MaNXB", "TenNXB", dsMaNXB, getIntent().getStringExtra("maNXB"));
        loadSpinnerData(spnMaNN, "ngonngu", "MaNN", "TenNN", dsMaNN, getIntent().getStringExtra("maNN"));
        loadSpinnerData(spnMaViTri, "kesach", "MaViTri", "TenKe", dsMaViTri, getIntent().getStringExtra("maViTri"));

        btnSaveSach.setOnClickListener(v -> {
            String ten = edtTenSach.getText().toString().trim();
            String strSoLuong = edtSoLuong.getText().toString().trim();
            String strNamXB = edtNamXB.getText().toString().trim();

            if (maSach == null || maSach.isEmpty()) {
                Toast.makeText(this, "Không tìm thấy mã sách!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (ten.isEmpty() || strSoLuong.isEmpty() || strNamXB.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy Mã (ID) từ các danh sách thông qua vị trí mà người dùng chọn trên Spinner
            String maTL = dsMaTL.get(spnMaTL.getSelectedItemPosition());
            String maTG = dsMaTG.get(spnMaTG.getSelectedItemPosition());
            String maNXB = dsMaNXB.get(spnMaNXB.getSelectedItemPosition());
            String maNN = dsMaNN.get(spnMaNN.getSelectedItemPosition());
            String maViTri = dsMaViTri.get(spnMaViTri.getSelectedItemPosition());

            int soLuongMoi;
            int namXBMoi;
            try {
                soLuongMoi = Integer.parseInt(strSoLuong);
                namXBMoi = Integer.parseInt(strNamXB);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số lượng và năm xuất bản phải là số!", Toast.LENGTH_SHORT).show();
                return;
            }

            Sach sachMoi = new Sach(maSach, maTG, maNXB, maTL, ten, maNN, maViTri, namXBMoi, soLuongMoi);
            boolean updated = sachQuery.suaSach(sachMoi);

            if (updated) {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSpinnerData(Spinner spinner, String tableName, String idCol, String nameCol, ArrayList<String> dsMa, String selectedId) {
        SQLiteHelper dbHelper = new SQLiteHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + idCol + ", " + nameCol + " FROM " + tableName, null);

        ArrayList<String> dsTen = new ArrayList<>();
        dsMa.clear();
        int selectedIndex = 0;
        int currentIndex = 0;

        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String name = cursor.getString(1);

            dsMa.add(id);   // Lưu Mã (ID) vào danh sách ẩn
            dsTen.add(name); // Lưu Tên vào danh sách để hiện lên Spinner

            if (selectedId != null && id.equals(selectedId)) {
                selectedIndex = currentIndex;
            }
            currentIndex++;
        }

        cursor.close();
        db.close();
        dbHelper.close();

        if (dsMa.isEmpty()) {
            dsMa.add("");
            dsTen.add("-- Trống --");
        }

        // Tạo ArrayAdapter cơ bản kiểu chuỗi giống như trong video hướng dẫn
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dsTen);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (selectedId != null) {
            spinner.setSelection(selectedIndex);
        }
    }
}