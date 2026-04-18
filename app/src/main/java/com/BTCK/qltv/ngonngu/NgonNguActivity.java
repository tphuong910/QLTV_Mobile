package com.BTCK.qltv.ngonngu;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NgonNguActivity extends AppCompatActivity {

    private ListView lvNgonNgu;
    private EditText etTimKiem;
    private FloatingActionButton fabThem;
    private ImageView imgBack;

    private NgonNguQuery ngonNguQuery;
    private List<NgonNgu> listNgonNgu;
    private NgonNguAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngon_ngu);

        // Ánh xạ các thành phần giao diện
        lvNgonNgu = findViewById(R.id.lvNgonNgu);
        etTimKiem = findViewById(R.id.etTimKiemNN);
        fabThem = findViewById(R.id.fabThemNN);
        imgBack = findViewById(R.id.imgBackNN);

        ngonNguQuery = new NgonNguQuery(this);

        loadData("");

        if (imgBack != null) {
            imgBack.setOnClickListener(v -> finish());
        }

        //nút thêm mới
        fabThem.setOnClickListener(v -> {
            // Chuyển sang Activity thêm ngôn ngữ
            Intent intent = new Intent(NgonNguActivity.this, AddNgonNguActivity.class);
            startActivity(intent);
        });

        // Xử lý tìm kiếm
        etTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        registerForContextMenu(lvNgonNgu);
    }

    private void loadData(String keyword) {
        listNgonNgu = ngonNguQuery.layDanhSach(keyword);
        adapter = new NgonNguAdapter(this, listNgonNgu);
        lvNgonNgu.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(etTimKiem.getText().toString());
    }

    // Khởi tạo menu khi nhấn giữ vào một dòng trong danh sách
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (info == null) return super.onContextItemSelected(item);
        
        NgonNgu nn = listNgonNgu.get(info.position);

        //trang sửa
        if (item.getItemId() == R.id.menu_update) {
            Intent intent = new Intent(NgonNguActivity.this, UpdateNgonNguActivity.class);
            intent.putExtra("MaNN", nn.getMaNN());
            intent.putExtra("TenNN", nn.getTenNN());
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menu_delete) {
            confirmDelete(nn);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void confirmDelete(NgonNgu nn) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa ngôn ngữ: " + nn.getTenNN() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    if (ngonNguQuery.xoaNgonNgu(nn.getMaNN())) {
                        Toast.makeText(this, "Đã xóa thành công!", Toast.LENGTH_SHORT).show();
                        loadData(etTimKiem.getText().toString());
                    } else {
                        Toast.makeText(this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
