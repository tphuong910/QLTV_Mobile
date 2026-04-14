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

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;
import com.BTCK.qltv.login.LoginActivity;
import com.BTCK.qltv.ngonngu.NgonNguActivity;
import com.BTCK.qltv.sach.SachActivity;
import com.BTCK.qltv.theloai.TheLoaiActivity;
import com.BTCK.qltv.tacgia.TacGiaActivity;
import com.BTCK.qltv.lop.LopActivity;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    TextView tvAppName, tvRole;
    ImageView imgMenu;
    ListView lvModules;
    List<Module> moduleList;
    ModuleAdapter adapter;
    DashboardQuery dashboardQuery;

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
        dashboardQuery = new DashboardQuery(this);

        loadUserProfile();
        loadStatistics();
        setupListView();

        imgMenu.setOnClickListener(v -> showPopupMenu());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStatistics();
    }

    private void loadUserProfile() {
        SharedPreferences prefs = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String tenNV = prefs.getString("TenNV", "Người dùng");
        String vaiTro = prefs.getString("VaiTro", "Chưa xác định");

        tvAppName.setText("Xin chào, " + tenNV);
        tvRole.setText("Vai trò: " + vaiTro);
    }

    private void loadStatistics() {
        tvTotalBooks.setText(String.valueOf(dashboardQuery.layTongSach()));
        tvTotalCategories.setText(String.valueOf(dashboardQuery.layTongTheLoai()));
        tvBorrowedBooks.setText(String.valueOf(dashboardQuery.laySachDangMuon()));
        tvOverdueBooks.setText(String.valueOf(dashboardQuery.laySachQuaHan()));
    }

    private void setupListView() {
        moduleList = new ArrayList<>();
        moduleList.add(new Module("Quản lý sách", R.drawable.ic_book));
        moduleList.add(new Module("Quản lý thể loại", R.drawable.ic_category));
        moduleList.add(new Module("Quản lý tác giả", R.drawable.ic_author));
        moduleList.add(new Module("Quản lý khoa", R.drawable.ic_department));
        moduleList.add(new Module("Quản lý lớp", R.drawable.ic_class));
        moduleList.add(new Module("Quản lý nhân viên", R.drawable.ic_employee));
        moduleList.add(new Module("Quản lý nhà xuất bản", R.drawable.ic_publisher));
        moduleList.add(new Module("Quản lý độc giả", R.drawable.ic_reader));
        moduleList.add(new Module("Quản lý kệ sách", R.drawable.ic_bookshelf));
        moduleList.add(new Module("Quản lý ngôn ngữ", R.drawable.ic_language));
        moduleList.add(new Module("Quản lý thẻ thư viện", R.drawable.ic_library));
        moduleList.add(new Module("Quản lý mượn - trả sách", R.drawable.ic_borrow_return));

        adapter = new ModuleAdapter(this, moduleList);
        lvModules.setAdapter(adapter);

        lvModules.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(DashboardActivity.this, SachActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(DashboardActivity.this, TheLoaiActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(DashboardActivity.this, TacGiaActivity.class));
                    break;
                case 4:
                    startActivity(new Intent(DashboardActivity.this, LopActivity.class));
                    break;
                case 9:
                    startActivity(new Intent(DashboardActivity.this, NgonNguActivity.class));
                    break;
                case 5: // Giả sử "Quản lý nhân viên" ở vị trí thứ 5 trong danh sách của bạn
                    startActivity(new Intent(DashboardActivity.this, NhanVienActivity.class));
                    break;
                case 8: // Vị trí của "Quản lý kệ sách" trong moduleList
                    startActivity(new Intent(DashboardActivity.this, KeSachActivity.class));
                    break;
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
