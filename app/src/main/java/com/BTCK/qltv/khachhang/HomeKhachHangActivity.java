package com.BTCK.qltv.khachhang;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;
import com.BTCK.qltv.database.SQLiteHelper;
import com.BTCK.qltv.login.LoginActivity;
import com.BTCK.qltv.muontra.MuonTra;
import com.BTCK.qltv.muontra.MuonTraQuery;
import com.BTCK.qltv.sach.Sach;
import com.BTCK.qltv.sach.SachQuery;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class HomeKhachHangActivity extends AppCompatActivity {

    private TextView tvWelcome, tvTitle;
    private ImageView imgMenu;
    private EditText edtSearch;
    private ListView lvMain;
    private TabLayout tabLayout;
    private FloatingActionButton fabMuonSach;

    private SachQuery sachQuery;
    private MuonTraQuery muonTraQuery;
    private SQLiteHelper dbHelper;

    private ArrayList<Object> currentList = new ArrayList<>();
    private ArrayAdapter<Object> adapter;
    private int currentTab = 0;
    private String maDG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_khach_hang);

        initViews();
        initQueries();
        loadUserInfo();
        setupTabLayout();
        setupListView();
        loadData("");

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadData(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        imgMenu.setOnClickListener(v -> showPopupMenu());
        
        fabMuonSach.setOnClickListener(v -> {
            // Mở trang mượn sách mới (Activity riêng)
            Intent intent = new Intent(HomeKhachHangActivity.this, MuonSachKhachHangActivity.class);
            startActivityForResult(intent, 100);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            loadData(""); // Load lại khi mượn thành công
        }
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcomeKH);
        tvTitle = findViewById(R.id.tvTitleKH);
        imgMenu = findViewById(R.id.imgMenuKH);
        edtSearch = findViewById(R.id.edtSearchKH);
        lvMain = findViewById(R.id.lvMainKH);
        tabLayout = findViewById(R.id.tabLayoutKH);
        fabMuonSach = findViewById(R.id.fabMuonSachKH);
    }

    private void initQueries() {
        sachQuery = new SachQuery(this);
        muonTraQuery = new MuonTraQuery(this);
        dbHelper = new SQLiteHelper(this);
    }

    private void loadUserInfo() {
        SharedPreferences prefs = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String ten = prefs.getString("TenUser", "Khách hàng");
        maDG = prefs.getString("MaUser", "");
        tvWelcome.setText("Xin chào, " + ten);
    }

    private void setupTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition();
                edtSearch.setText("");
                updateUIForTab();
                loadData("");
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void updateUIForTab() {
        fabMuonSach.setVisibility(currentTab == 1 ? View.VISIBLE : View.GONE);
        if (currentTab == 0) tvTitle.setText("Danh sách sách hiện có");
        else tvTitle.setText("Lịch sử mượn trả sách");
    }

    private void loadData(String keyword) {
        currentList.clear();
        if (currentTab == 0) {
            currentList.addAll(sachQuery.layDanhSach(keyword));
        } else {
            currentList.addAll(muonTraQuery.layDanhSachTheoKhachHang(maDG));
        }
        adapter.notifyDataSetChanged();
    }

    private void setupListView() {
        adapter = new ArrayAdapter<Object>(this, android.R.layout.simple_list_item_2, android.R.id.text1, currentList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);

                Object item = currentList.get(position);
                if (item instanceof Sach) {
                    Sach s = (Sach) item;
                    text1.setText(s.getTenSach());
                    text2.setText("Tác giả: " + s.getMaTG() + " | SL: " + s.getSoLuong());
                } else if (item instanceof MuonTra) {
                    MuonTra mt = (MuonTra) item;
                    String chiTiet = muonTraQuery.layChiTietMuonTra(mt.getMaMT());
                    text1.setText(chiTiet);
                    text2.setText("Mượn: " + mt.getNgayMuon() + " | Hạn: " + mt.getHanTra() + " | " + mt.getTrangThai());
                }
                return view;
            }
        };
        lvMain.setAdapter(adapter);

        lvMain.setOnItemClickListener((parent, view, position, id) -> {
            Object item = currentList.get(position);
            if (currentTab == 0 && item instanceof Sach) {
                showSachDetails((Sach) item);
            }
        });
    }

    private void showSachDetails(Sach sach) {
        String tenTL = getColumnValue("theloai", "TenTL", "MaTL", sach.getMaTL());
        String tenNXB = getColumnValue("nhaxuatban", "TenNXB", "MaNXB", sach.getMaNXB());
        String tenKe = getColumnValue("kesach", "TenKe", "MaViTri", sach.getMaViTri());
        String tenNN = getColumnValue("ngonngu", "TenNN", "MaNN", sach.getMaNN());

        StringBuilder sb = new StringBuilder();
        sb.append("Thể loại: ").append(tenTL).append("\n")
          .append("Tác giả: ").append(sach.getMaTG()).append("\n")
          .append("Nhà xuất bản: ").append(tenNXB).append("\n")
          .append("Ngôn ngữ: ").append(tenNN).append("\n")
          .append("Vị trí: ").append(tenKe).append("\n")
          .append("Năm XB: ").append(sach.getNamXB()).append("\n")
          .append("Số lượng hiện có: ").append(sach.getSoLuong());

        new AlertDialog.Builder(this)
                .setTitle(sach.getTenSach())
                .setMessage(sb.toString())
                .setPositiveButton("Đóng", null)
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
        popupMenu.getMenu().add("Đăng xuất");
        popupMenu.setOnMenuItemClickListener(item -> {
            getSharedPreferences("UserSession", Context.MODE_PRIVATE).edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        });
        popupMenu.show();
    }
}
