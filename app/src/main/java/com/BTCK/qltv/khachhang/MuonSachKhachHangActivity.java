package com.BTCK.qltv.khachhang;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;
import com.BTCK.qltv.muontra.MuonTra;
import com.BTCK.qltv.muontra.MuonTraQuery;
import com.BTCK.qltv.sach.Sach;
import com.BTCK.qltv.sach.SachQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MuonSachKhachHangActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private Spinner spnSach;
    private EditText edtSoLuong, edtHanTra;
    private Button btnXacNhan;

    private SachQuery sachQuery;
    private MuonTraQuery muonTraQuery;
    private List<Sach> listSach;
    private String maDG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muon_sach_khach_hang);

        initViews();
        initQueries();
        loadData();

        btnBack.setOnClickListener(v -> finish());

        edtHanTra.setOnClickListener(v -> showDatePicker());

        btnXacNhan.setOnClickListener(v -> performBorrow());
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBackMuonSach);
        spnSach = findViewById(R.id.spnSachMuonKH);
        edtSoLuong = findViewById(R.id.edtSoLuongMuonKH);
        edtHanTra = findViewById(R.id.edtHanTraKH);
        btnXacNhan = findViewById(R.id.btnXacNhanMuonKH);
        
        edtSoLuong.setText("1");
    }

    private void initQueries() {
        sachQuery = new SachQuery(this);
        muonTraQuery = new MuonTraQuery(this);
        SharedPreferences prefs = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        maDG = prefs.getString("MaUser", "");
    }

    private void loadData() {
        listSach = sachQuery.layDanhSachSach();
        ArrayList<String> tenSachList = new ArrayList<>();
        for (Sach s : listSach) {
            tenSachList.add(s.getMaSach() + " - " + s.getTenSach() + " (Còn: " + s.getSoLuong() + ")");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenSachList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSach.setAdapter(adapter);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            edtHanTra.setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void performBorrow() {
        if (spnSach.getSelectedItemPosition() < 0) return;
        
        Sach selectedSach = listSach.get(spnSach.getSelectedItemPosition());
        String strSoLuong = edtSoLuong.getText().toString().trim();
        String hanTra = edtHanTra.getText().toString().trim();

        if (strSoLuong.isEmpty() || hanTra.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        int soLuong = Integer.parseInt(strSoLuong);
        if (soLuong > selectedSach.getSoLuong()) {
            Toast.makeText(this, "Số lượng mượn vượt quá kho!", Toast.LENGTH_SHORT).show();
            return;
        }

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        MuonTra mt = new MuonTra(muonTraQuery.taoMaMuonTraMoi(), maDG, "NV001", today, hanTra, "Đang mượn");

        if (muonTraQuery.themMuonTra(mt, selectedSach.getMaSach(), soLuong)) {
            Toast.makeText(this, "Mượn sách thành công!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Mượn sách thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
