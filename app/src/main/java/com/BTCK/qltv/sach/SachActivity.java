package com.BTCK.qltv.sach;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SachActivity extends AppCompatActivity {

    EditText edtSearch;
    ImageButton btnAdd;
    ListView lvData;

    DatabaseReference mDatabase;
    List<Sach> listGoc = new ArrayList<>();
    List<Sach> listHienThi = new ArrayList<>();

    // Tạm dùng ArrayAdapter hiển thị Tên Sách + Số lượng
    ArrayAdapter<String> adapter;
    List<String> listHienThiString = new ArrayList<>();

    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sach); // Đã đổi layout riêng cho Sách

        edtSearch = findViewById(R.id.edtSearch);
        btnAdd = findViewById(R.id.btnAdd);
        lvData = findViewById(R.id.lvData);

        // Trỏ vào bảng "sach"
        mDatabase = FirebaseDatabase.getInstance().getReference("sach");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listHienThiString);
        lvData.setAdapter(adapter);

        loadData();

        // TÌM KIẾM THEO TÊN SÁCH
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

        // NÚT THÊM SÁCH
        btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(SachActivity.this, AddSachActivity.class));
        });

        // Đăng ký mở Menu để Sửa/Xóa giống kiểu MainActivity mà bạn cung cấp
        registerForContextMenu(lvData);
    }

    private void loadData() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listGoc.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Sach sach = data.getValue(Sach.class);
                    if (sach != null) {
                        sach.setId(data.getKey()); // Lấy S001, S002...
                        listGoc.add(sach);
                    }
                }
                filterData("");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SachActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterData(String keyword) {
        listHienThi.clear();
        listHienThiString.clear();
        for (Sach sach : listGoc) {
            if (keyword.isEmpty() || sach.getTenSach().toLowerCase().contains(keyword.toLowerCase())) {
                listHienThi.add(sach);
                // Hiển thị tên sách và số lượng trên ListView
                listHienThiString.add(sach.getTenSach() + " (SL: " + sach.getSoLuong() + ")");
            }
        }
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
        Sach sachEdit = listHienThi.get(selectedPosition);

        if (item.getItemId() == R.id.menu_update) {
            Intent intent = new Intent(SachActivity.this, UpdateSachActivity.class);
            // Đẩy dữ liệu cũ sang UpdateActivity
            intent.putExtra("id", sachEdit.getId());
            intent.putExtra("tenSach", sachEdit.getTenSach());
            intent.putExtra("soLuong", sachEdit.getSoLuong());
            intent.putExtra("namXB", sachEdit.getNamXB());
            intent.putExtra("maTL", sachEdit.getMaTL());
            intent.putExtra("maTG", sachEdit.getMaTG());
            intent.putExtra("maNXB", sachEdit.getMaNXB());
            intent.putExtra("maNN", sachEdit.getMaNN());
            intent.putExtra("maViTri", sachEdit.getMaViTri());
            startActivity(intent);

        } else if (item.getItemId() == R.id.menu_delete) {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc xóa Sách này?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        mDatabase.child(sachEdit.getId()).removeValue();
                        Toast.makeText(this, "Đã xóa!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Không", null)
                    .show();
        }
        return super.onContextItemSelected(item);
    }
}