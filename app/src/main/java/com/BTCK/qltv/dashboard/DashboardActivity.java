package com.BTCK.qltv.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;
import com.BTCK.qltv.login.LoginActivity;
import com.BTCK.qltv.sach.SachActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    TextView tvAppName, tvRole;
    ImageView imgMenu;
    ListView lvModules;
    List<Module> moduleList;
    ModuleAdapter adapter;

    // Khai báo các TextView thống kê
    TextView tvTotalBooks, tvTotalCategories, tvBorrowedBooks, tvOverdueBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvAppName = findViewById(R.id.tvAppName);
        tvRole = findViewById(R.id.tvRole);
        imgMenu = findViewById(R.id.imgMenu);
        lvModules = findViewById(R.id.lvModules);

        tvTotalBooks = findViewById(R.id.tvTotalBooks);
        tvTotalCategories = findViewById(R.id.tvTotalCategories);
        tvBorrowedBooks = findViewById(R.id.tvBorrowedBooks);
        tvOverdueBooks = findViewById(R.id.tvOverdueBooks);

        loadUserProfile();
        loadStatistics();
        setupListView();

        // Bắt sự kiện click vào Menu 3 gạch (Để Đăng xuất)
        imgMenu.setOnClickListener(v -> showPopupMenu());
    }

    private void loadUserProfile() {
        SharedPreferences prefs = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String tenNV = prefs.getString("TenNV", "Người dùng");
        String vaiTro = prefs.getString("VaiTro", "Chưa xác định");

        tvAppName.setText("Xin chào, " + tenNV);
        tvRole.setText("Vai trò: " + vaiTro);
    }

    private void loadStatistics() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        // 1. Lấy tổng số lượng sách
        database.child("sach").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvTotalBooks.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // 2. Lấy tổng số thể loại
        database.child("theloai").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvTotalCategories.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // 3. Lấy lượng sách đang mượn & quá hạn
        database.child("muontra").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int dangMuon = 0;
                int quaHan = 0;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date currentDate = new Date();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    String trangThai = ds.child("TrangThai").getValue(String.class);

                    if ("Chưa trả".equals(trangThai)) {
                        int soLuongTrongPhieu = 0;

                        // Cộng dồn số lượng sách trong mảng chiTiet
                        if (ds.hasChild("chiTiet")) {
                            for (DataSnapshot ct : ds.child("chiTiet").getChildren()) {
                                Integer sl = ct.child("SoLuong").getValue(Integer.class);
                                if (sl != null) {
                                    soLuongTrongPhieu += sl;
                                }
                            }
                        } else {
                            soLuongTrongPhieu = 1; // Fallback nếu phiếu lỗi không có chi tiết
                        }

                        dangMuon += soLuongTrongPhieu;

                        // Kiểm tra xem phiếu này có quá hạn không
                        String hanTraStr = ds.child("HanTra").getValue(String.class);
                        if (hanTraStr != null) {
                            try {
                                Date hanTraDate = sdf.parse(hanTraStr);
                                if (hanTraDate != null && hanTraDate.before(currentDate)) {
                                    // Ngày hạn trả < Ngày hiện tại -> Quá hạn
                                    quaHan += soLuongTrongPhieu;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                tvBorrowedBooks.setText(String.valueOf(dangMuon));
                tvOverdueBooks.setText(String.valueOf(quaHan));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setupListView() {
        moduleList = new ArrayList<>();
        moduleList.add(new Module("Quản lý sách", R.drawable.ic_book));
        moduleList.add(new Module("Quản lý thể loại", R.drawable.ic_category));
        moduleList.add(new Module("Quản lý tác giả", R.drawable.ic_author));
        moduleList.add(new Module("Quản lý nhà xuất bản", R.drawable.ic_publisher));
        moduleList.add(new Module("Quản lý độc giả", R.drawable.ic_reader));
        moduleList.add(new Module("Quản lý kệ sách", R.drawable.ic_bookshelf));
        moduleList.add(new Module("Quản lý ngôn ngữ", R.drawable.ic_language));
        moduleList.add(new Module("Quản lý mượn - trả sách", R.drawable.ic_borrow_return));

        adapter = new ModuleAdapter(this, moduleList);
        lvModules.setAdapter(adapter);

        lvModules.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(DashboardActivity.this, SachActivity.class));
                    break;
//                case 1:
//                    startActivity(new Intent(DashboardActivity.this, TheLoaiActivity.class));
//                    break;
                default:
                    Toast.makeText(this, "Chức năng đang phát triển!", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, imgMenu);
        popupMenu.getMenu().add("Đăng xuất");
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("Đăng xuất")) {
                SharedPreferences prefs = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
                prefs.edit().clear().apply();

                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                finish();
            }
            return true;
        });
        popupMenu.show();
    }
}