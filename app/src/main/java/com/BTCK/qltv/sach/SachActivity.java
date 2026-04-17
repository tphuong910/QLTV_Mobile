package com.BTCK.qltv.sach;

import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
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

public class SachActivity extends AppCompatActivity {

    EditText edtSearch;
    ImageButton btnAdd;
    ListView lvData;

    SachQuery sachQuery;
    List<Sach> listGoc = new ArrayList<>();
    List<Sach> listHienThi = new ArrayList<>();

    ArrayAdapter<String> adapter;
    List<String> listHienThiString = new ArrayList<>();

    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sach);

        edtSearch = findViewById(R.id.edtSearch);
        btnAdd = findViewById(R.id.btnAdd);
        lvData = findViewById(R.id.lvData);
        sachQuery = new SachQuery(this);

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
            startActivity(new Intent(SachActivity.this, AddSachActivity.class));
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
        listGoc.addAll(sachQuery.layDanhSachSach());
        filterData(edtSearch.getText().toString().trim());
    }

    private void filterData(String keyword) {
        String tuKhoa = keyword == null ? "" : keyword.trim().toLowerCase(Locale.getDefault());

        listHienThi.clear();
        listHienThiString.clear();

        for (Sach sach : listGoc) {
            String tenSach = sach.getTenSach() == null ? "" : sach.getTenSach();
            if (tuKhoa.isEmpty() || tenSach.toLowerCase(Locale.getDefault()).contains(tuKhoa)) {
                listHienThi.add(sach);
                listHienThiString.add(sach.getMaSach() + " - " + tenSach + " (SL: " + sach.getSoLuong() + ")");
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

        Sach sachEdit = listHienThi.get(selectedPosition);

        if (item.getItemId() == R.id.menu_update) {
            Intent intent = new Intent(SachActivity.this, UpdateSachActivity.class);
            intent.putExtra("maSach", sachEdit.getMaSach());
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
                        boolean deleted = sachQuery.xoaSach(sachEdit.getMaSach());
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