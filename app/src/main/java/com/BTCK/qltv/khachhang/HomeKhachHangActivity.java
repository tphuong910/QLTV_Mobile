package com.BTCK.qltv.khachhang;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;
import com.BTCK.qltv.dashboard.DashboardQuery;
import com.BTCK.qltv.database.SQLiteHelper;
import com.BTCK.qltv.login.LoginActivity;
import com.BTCK.qltv.muontra.MuonTra;
import com.BTCK.qltv.muontra.MuonTraQuery;
import com.BTCK.qltv.sach.Sach;
import com.BTCK.qltv.sach.SachQuery;

import java.util.ArrayList;
import java.util.List;

public class HomeKhachHangActivity extends AppCompatActivity {

    private TextView tvWelcome, tvTotalBooks, tvBorrowedBooks;
    private ImageView imgMenu;
    private LinearLayout btnDanhSach, btnMuonSach, btnLichSu;
    
    private DashboardQuery dashboardQuery;
    private MuonTraQuery muonTraQuery;
    private SachQuery sachQuery;
    private SQLiteHelper dbHelper;
    private String maDG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_khach_hang);

        initViews();
        loadUserInfo();
        loadStatistics();
        setupClickListeners();

        imgMenu.setOnClickListener(v -> showPopupMenu());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStatistics();
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcomeKH);
        tvTotalBooks = findViewById(R.id.tvTotalBooksKH);
        tvBorrowedBooks = findViewById(R.id.tvBorrowedBooksKH);
        imgMenu = findViewById(R.id.imgMenuKH);
        
        btnDanhSach = findViewById(R.id.nutDanhSach);
        btnDanhSach.setEnabled(true);
        btnDanhSach.setAlpha(1f);

        btnMuonSach = findViewById(R.id.nutMuonSach);
        btnMuonSach.setEnabled(true);
        btnMuonSach.setAlpha(1f);

        btnLichSu = findViewById(R.id.nutLichSu);
        btnLichSu.setEnabled(true);
        btnLichSu.setAlpha(1f);

        dashboardQuery = new DashboardQuery(this);
        muonTraQuery = new MuonTraQuery(this);
        sachQuery = new SachQuery(this);
        dbHelper = new SQLiteHelper(this);
    }

    private void loadUserInfo() {
        SharedPreferences prefs = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String ten = prefs.getString("TenUser", "Khách hàng");
        maDG = prefs.getString("MaUser", "");
        tvWelcome.setText("Xin chào, " + ten);
    }

    private void loadStatistics() {
        tvTotalBooks.setText(String.valueOf(dashboardQuery.layTongSach()));
        ArrayList<MuonTra> listMT = muonTraQuery.layDanhSachTheoKhachHang(maDG);
        int count = 0;
        for (MuonTra mt : listMT) {
            if (mt.getTrangThai().equals("Đang mượn") || mt.getTrangThai().equals("Chưa trả")) {
                count++;
            }
        }
        tvBorrowedBooks.setText(String.valueOf(count));
    }

    private void setupClickListeners() {
        btnDanhSach.setOnClickListener(v -> showSachListDialog());
        
        btnMuonSach.setOnClickListener(v -> {
            startActivityForResult(new Intent(this, MuonSachKhachHangActivity.class), 100);
        });
        
        btnLichSu.setOnClickListener(v -> showMuonTraHistoryDialog());
    }

    private void showSachListDialog() {
        List<Sach> listS = sachQuery.layDanhSachSach();
        ArrayList<String> names = new ArrayList<>();
        for (Sach s : listS) names.add(s.getTenSach() + " (SL: " + s.getSoLuong() + ")");

        new AlertDialog.Builder(this)
                .setTitle("Danh sách sách")
                .setItems(names.toArray(new String[0]), (dialog, which) -> showSachDetails(listS.get(which)))
                .setPositiveButton("Đóng", null)
                .show();
    }

    private void showSachDetails(Sach sach) {
        String tenTL = getColumnValue("theloai", "TenTL", "MaTL", sach.getMaTL());
        String tenNXB = getColumnValue("nhaxuatban", "TenNXB", "MaNXB", sach.getMaNXB());
        String tenKe = getColumnValue("kesach", "TenKe", "MaViTri", sach.getMaViTri());
        String tenNN = getColumnValue("ngonngu", "TenNN", "MaNN", sach.getMaNN());

        String detail = "Thể loại: " + tenTL + "\nTác giả: " + sach.getMaTG() + 
                         "\nNXB: " + tenNXB + "\nNgôn ngữ: " + tenNN + 
                         "\nVị trí: " + tenKe + "\nNăm XB: " + sach.getNamXB() + 
                         "\nSố lượng: " + sach.getSoLuong();

        new AlertDialog.Builder(this)
                .setTitle(sach.getTenSach())
                .setMessage(detail)
                .setPositiveButton("Mượn ngay", (dialog, which) -> {
                    startActivityForResult(new Intent(this, MuonSachKhachHangActivity.class), 100);
                })
                .setNegativeButton("Đóng", null)
                .show();
    }

    private void showMuonTraHistoryDialog() {
        ArrayList<MuonTra> listMT = muonTraQuery.layDanhSachTheoKhachHang(maDG);
        ArrayList<String> info = new ArrayList<>();
        for (MuonTra mt : listMT) {
            String ct = muonTraQuery.layChiTietMuonTra(mt.getMaMT());
            info.add(ct + "\nNgày mượn: " + mt.getNgayMuon() + " | Trạng thái: " + mt.getTrangThai());
        }

        new AlertDialog.Builder(this)
                .setTitle("Lịch sử mượn trả")
                .setItems(info.toArray(new String[0]), (dialog, which) -> {
                    MuonTra selected = listMT.get(which);
                    if (!selected.getTrangThai().equals("Đã trả")) {
                        showReturnDialog(selected);
                    }
                })
                .setPositiveButton("Đóng", null)
                .show();
    }

    private void showReturnDialog(MuonTra mt) {
        new AlertDialog.Builder(this)
                .setTitle("Trả sách")
                .setMessage("Bạn muốn trả sách này?")
                .setPositiveButton("Xác nhận", (dialog, which) -> {
                    if (muonTraQuery.traSach(mt.getMaMT(), "Tốt")) {
                        Toast.makeText(this, "Trả sách thành công!", Toast.LENGTH_SHORT).show();
                        loadStatistics();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private String getColumnValue(String table, String targetCol, String idCol, String idVal) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + targetCol + " FROM " + table + " WHERE " + idCol + " = ?", new String[]{idVal});
        String res = idVal;
        if (c.moveToFirst()) res = c.getString(0);
        c.close();
        return res;
    }

    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, imgMenu);
        popupMenu.getMenu().add("Liên hệ quản lý");
        popupMenu.getMenu().add("Đăng xuất");
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("Đăng xuất")) {
                getSharedPreferences("UserSession", Context.MODE_PRIVATE).edit().clear().apply();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:0987654321"));
                startActivity(callIntent);
            }
            return true;
        });
        popupMenu.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) loadStatistics();
    }
}
