package com.BTCK.qltv.theloai;

import android.app.AlertDialog;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TheLoaiActivity extends AppCompatActivity {

    EditText edtSearch;
    ImageButton btnAdd;
    ListView lvData;

    TheLoaiQuery theLoaiQuery;
    List<TheLoai> listGoc = new ArrayList<>();
    List<TheLoai> listHienThi = new ArrayList<>();

    ArrayAdapter<String> adapter;
    List<String> listHienThiString = new ArrayList<>();

    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_loai);

        edtSearch = findViewById(R.id.edtSearch);
        btnAdd = findViewById(R.id.btnAdd);
        lvData = findViewById(R.id.lvData);
        theLoaiQuery = new TheLoaiQuery(this);

        findViewById(R.id.imgBack).setOnClickListener(v -> finish());

        adapter = new ArrayAdapter<>(this, R.layout.item_list_advanced, android.R.id.text1, listHienThiString);
        lvData.setAdapter(adapter);

        loadData();

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

        btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(TheLoaiActivity.this, AddTheLoaiActivity.class));
        });

        registerForContextMenu(lvData);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        listGoc.clear();
        listGoc.addAll(theLoaiQuery.layDanhSachTheLoai());
        filterData(edtSearch.getText().toString().trim());
    }

    private void filterData(String keyword) {
        String tuKhoa = keyword == null ? "" : keyword.trim().toLowerCase(Locale.getDefault());

        listHienThi.clear();
        listHienThiString.clear();

        for (TheLoai tl : listGoc) {
            String tenTL = tl.getTenTL() == null ? "" : tl.getTenTL();
            if (tuKhoa.isEmpty() || tenTL.toLowerCase(Locale.getDefault()).contains(tuKhoa)) {
                listHienThi.add(tl);
                listHienThiString.add(tenTL + " (" + tl.getMaTL() + ")");
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
        if (selectedPosition < 0 || selectedPosition >= listHienThi.size()) {
            return super.onContextItemSelected(item);
        }

        TheLoai tlEdit = listHienThi.get(selectedPosition);

        if (item.getItemId() == R.id.menu_update) {
            Intent intent = new Intent(TheLoaiActivity.this, UpdateTheLoaiActivity.class);
            intent.putExtra("maTL", tlEdit.getMaTL());
            intent.putExtra("tenTL", tlEdit.getTenTL());
            startActivity(intent);

        } else if (item.getItemId() == R.id.menu_delete) {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc xóa Thể Loại này?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        boolean deleted = theLoaiQuery.xoaTheLoai(tlEdit.getMaTL());
                        if (deleted) {
                            Toast.makeText(this, "Đã xóa!", Toast.LENGTH_SHORT).show();
                            loadData();
                        } else {
                            Toast.makeText(this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Không", null)
                    .show();
        }

        return true;
    }
}
