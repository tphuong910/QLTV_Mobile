package com.BTCK.qltv.nhanvien;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;


import java.util.ArrayList;
import java.util.List;

public class NhanVienActivity extends AppCompatActivity {

    EditText edtSearch;
    ImageView btnAdd;
    ListView lvData;

    NhanVienQuery nhanVienQuery;

    // Mảng lưu danh sách gốc
    List<NhanVien> listGoc = new ArrayList<>();

    // Mảng lưu danh sách khi tìm kiếm
    List<NhanVien> listHienThi = new ArrayList<>();

    // Mảng chuỗi hiển thị lên ListView
    List<String> listHienThiString = new ArrayList<>();
    ArrayAdapter<String> adapter;

    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhan_vien);

        // Ánh xạ (Tìm view theo ID)
        edtSearch = findViewById(R.id.edtSearch);
        btnAdd = findViewById(R.id.btnAdd);
        lvData = findViewById(R.id.lvData);
        nhanVienQuery = new NhanVienQuery(this);

        // Sự kiện ấn nút Back
        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Thiết lập Adapter cho ListView với mảng chuỗi
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listHienThiString);
        lvData.setAdapter(adapter);

        loadData();

        // Sự kiện tìm kiếm (Gõ chữ tới đâu hiển thị tới đó)
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Chuyển sang màn hình thêm khi bấm nút Add
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NhanVienActivity.this, AddNhanVienActivity.class);
                startActivity(intent);
            }
        });

        // Đăng ký Context menu (Menu giữ đè)
        registerForContextMenu(lvData);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(); // Tải lại danh sách sau khi quay về trang này
    }

    private void loadData() {
        // Xóa hết rồi chép mảng vào
        listGoc.clear();
        listGoc.addAll(nhanVienQuery.layDanhSachNhanVien());

        // Gọi hàm lọc để đổ danh sách ra
        filterData(edtSearch.getText().toString());
    }

    private void filterData(String keyword) {
        listHienThi.clear();
        listHienThiString.clear();

        // Xử lý chuỗi tìm kiếm (đổi về chữ in thường)
        String tuKhoa = keyword;
        if (tuKhoa == null) {
            tuKhoa = "";
        }
        tuKhoa = tuKhoa.trim().toLowerCase();

        // Chạy vòng lặp for cơ bản duyệt mảng NhanVien
        for (int i = 0; i < listGoc.size(); i++) {
            NhanVien nv = listGoc.get(i);

            String tenNV = nv.getTenNV();
            if (tenNV == null) {
                tenNV = "";
            }

            // So sánh tên (đã in thường) với từ khóa
            if (tuKhoa.equals("") || tenNV.toLowerCase().contains(tuKhoa)) {
                listHienThi.add(nv);

                // Mảng chuỗi hiển thị chữ
                String chuoiCanHien = nv.getMaNV() + " - " + tenNV + " (" + nv.getVaiTro() + ")";
                listHienThiString.add(chuoiCanHien);
            }
        }

        // Báo Adapter cập nhật hiển thị mảng
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        selectedPosition = info.position;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (selectedPosition < 0 || selectedPosition >= listHienThi.size()) {
            return super.onContextItemSelected(item);
        }

        // Lấy nhân viên đang được click
        NhanVien nvEdit = listHienThi.get(selectedPosition);

        if (item.getItemId() == R.id.menu_update) {
            // Chuyển sang màng hình Update
            Intent intent = new Intent(NhanVienActivity.this, UpdateNhanVienActivity.class);

            // Đẩy dữ liệu qua bằng intent
            intent.putExtra("maNV", nvEdit.getMaNV());
            intent.putExtra("tenNV", nvEdit.getTenNV());
            intent.putExtra("queQuan", nvEdit.getQueQuan());
            intent.putExtra("gioiTinh", nvEdit.getGioiTinh());
            intent.putExtra("namSinh", nvEdit.getNamSinh());
            intent.putExtra("vaiTro", nvEdit.getVaiTro());
            intent.putExtra("email", nvEdit.getEmail());
            intent.putExtra("sdt", nvEdit.getSdt());
            intent.putExtra("user", nvEdit.getUser());
            intent.putExtra("pass", nvEdit.getPass());

            startActivity(intent);

        } else if (item.getItemId() == R.id.menu_delete) {
            // Xoá
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Xác nhận xóa");
            builder.setMessage("Bạn có chắc xóa Nhân viên này?");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    boolean kq = nhanVienQuery.xoaNhanVien(nvEdit.getMaNV());
                    if (kq == true) {
                        Toast.makeText(NhanVienActivity.this, "Đã xóa!", Toast.LENGTH_SHORT).show();
                        loadData(); // Tải lại danh sách
                    } else {
                        Toast.makeText(NhanVienActivity.this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("Không", null);
            builder.show();
        }

        return true;
    }
}