package com.BTCK.qltv.docgia;

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

public class DocGiaActivity extends AppCompatActivity {

    EditText edtSearch;
    ImageButton btnAdd;
    ListView lvData;

    DocGiaQuery docGiaQuery;
    List<DocGia> listGoc = new ArrayList<>();
    List<DocGia> listHienThi = new ArrayList<>();
    ArrayAdapter<String> adapter;
    List<String> listHienThiString = new ArrayList<>();
    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_gia);

        edtSearch = findViewById(R.id.edtSearch);
        btnAdd = findViewById(R.id.btnAdd);
        lvData = findViewById(R.id.lvData);
        docGiaQuery = new DocGiaQuery(this);

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

        btnAdd.setOnClickListener(v -> startActivity(new Intent(DocGiaActivity.this, AddDocGiaActivity.class)));
        registerForContextMenu(lvData);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        listGoc.clear();
        listGoc.addAll(docGiaQuery.layDanhSachDocGia());
        filterData(edtSearch.getText().toString().trim());
    }

    private void filterData(String keyword) {
        String tuKhoa = keyword == null ? "" : keyword.trim().toLowerCase(Locale.getDefault());
        listHienThi.clear();
        listHienThiString.clear();

        for (DocGia dg : listGoc) {
            String ten = dg.getTenDG() == null ? "" : dg.getTenDG();
            if (tuKhoa.isEmpty() || ten.toLowerCase(Locale.getDefault()).contains(tuKhoa) || dg.getMaDG().toLowerCase().contains(tuKhoa)) {
                listHienThi.add(dg);
                listHienThiString.add(ten + " (" + dg.getMaDG() + ")\nLớp: " + dg.getMaLop() + " - SĐT: " + dg.getSdt());
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
        if (selectedPosition < 0 || selectedPosition >= listHienThi.size()) return super.onContextItemSelected(item);

        DocGia dgEdit = listHienThi.get(selectedPosition);

        if (item.getItemId() == R.id.menu_update) {
            Intent intent = new Intent(DocGiaActivity.this, UpdateDocGiaActivity.class);
            intent.putExtra("maDG", dgEdit.getMaDG());
            intent.putExtra("tenDG", dgEdit.getTenDG());
            intent.putExtra("maKhoa", dgEdit.getMaKhoa());
            intent.putExtra("maLop", dgEdit.getMaLop());
            intent.putExtra("namSinh", dgEdit.getNamSinh());
            intent.putExtra("gioiTinh", dgEdit.getGioiTinh());
            intent.putExtra("diaChi", dgEdit.getDiaChi());
            intent.putExtra("email", dgEdit.getEmail());
            intent.putExtra("sdt", dgEdit.getSdt());
            startActivity(intent);
        } else if (item.getItemId() == R.id.menu_delete) {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc xóa Độc giả này?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        if (docGiaQuery.xoaDocGia(dgEdit.getMaDG())) {
                            Toast.makeText(this, "Đã xóa!", Toast.LENGTH_SHORT).show();
                            loadData();
                        }
                    })
                    .setNegativeButton("Không", null).show();
        }
        return true;
    }
}