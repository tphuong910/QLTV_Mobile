package com.BTCK.qltv.kesach;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;

import java.util.ArrayList;

public class KeSachActivity extends AppCompatActivity {

    private EditText edtTimKiem;
    private ImageButton btnThem, btnBack;
    private ListView lvKeSach;
    private ArrayList<KeSach> listKeSach;
    private ArrayAdapter<KeSach> adapter;
    private KeSachQuery keSachQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ke_sach);

        edtTimKiem = findViewById(R.id.edtTimKiem);
        btnThem = findViewById(R.id.btnThem);
        btnBack = findViewById(R.id.btnBackKeSach);
        lvKeSach = findViewById(R.id.lvKeSach);

        keSachQuery = new KeSachQuery(this);
        listKeSach = new ArrayList<>();

        // Nút quay lại
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        setupListView();
        loadData("");

        // Chức năng Tìm Kiếm
        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadData(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Chức năng Thêm Mới
        btnThem.setOnClickListener(v -> showDialogThêmSua(null));

        // Xử lý Click vào Item để Sửa / Xóa
        lvKeSach.setOnItemClickListener((parent, view, position, id) -> {
            KeSach selected = listKeSach.get(position);
            showDialogOptions(selected);
        });
    }

    private void loadData(String keyword) {
        listKeSach.clear();
        listKeSach.addAll(keSachQuery.layDanhSachKeSach(keyword));
        adapter.notifyDataSetChanged();
    }

    private void setupListView() {
        adapter = new ArrayAdapter<KeSach>(this, R.layout.item_list_advanced, android.R.id.text1, listKeSach) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(android.R.id.text1);
                KeSach ks = listKeSach.get(position);
                text.setText(ks.getTenKe() + " (" + ks.getMaViTri() + ")");
                return view;
            }
        };
        lvKeSach.setAdapter(adapter);
    }

    private void showDialogOptions(KeSach ks) {
        String[] options = {"Sửa", "Xóa"};
        new AlertDialog.Builder(this)
                .setTitle("Tùy chọn: " + ks.getTenKe())
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showDialogThêmSua(ks);
                    } else if (which == 1) {
                        xoaKeSach(ks);
                    }
                }).show();
    }

    private void showDialogThêmSua(KeSach ks) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_ke_sach, null);
        builder.setView(view);

        EditText edtMaViTri = view.findViewById(R.id.edtMaViTri);
        EditText edtTenKe = view.findViewById(R.id.edtTenKe);
        EditText edtMoTa = view.findViewById(R.id.edtMoTa);

        boolean isEdit = (ks != null);

        if (isEdit) {
            builder.setTitle("Sửa Kệ Sách");
            edtMaViTri.setText(ks.getMaViTri());
            edtMaViTri.setEnabled(false);
            edtTenKe.setText(ks.getTenKe());
            edtMoTa.setText(ks.getMoTa());
        } else {
            builder.setTitle("Thêm Kệ Sách");
        }

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String ma = edtMaViTri.getText().toString().trim();
            String ten = edtTenKe.getText().toString().trim();
            String mt = edtMoTa.getText().toString().trim();

            if (ma.isEmpty() || ten.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isEdit) {
                ks.setTenKe(ten);
                ks.setMoTa(mt);
                if (keSachQuery.suaKeSach(ks)) {
                    Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    loadData(edtTimKiem.getText().toString());
                }
            } else {
                KeSach newKs = new KeSach(ma, ten, mt);
                if (keSachQuery.themKeSach(newKs)) {
                    Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                    loadData(edtTimKiem.getText().toString());
                } else {
                    Toast.makeText(this, "Mã vị trí đã tồn tại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void xoaKeSach(KeSach ks) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc muốn xóa kệ sách này không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    if (keSachQuery.xoaKeSach(ks.getMaViTri())) {
                        Toast.makeText(this, "Đã xóa!", Toast.LENGTH_SHORT).show();
                        loadData(edtTimKiem.getText().toString());
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }
}
