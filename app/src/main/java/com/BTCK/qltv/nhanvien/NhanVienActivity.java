package com.BTCK.qltv.nhanvien;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NhanVienActivity extends AppCompatActivity {

    private ListView lvNhanVien;
    private FloatingActionButton fabAdd;
    private ImageView btnBack;
    private List<NhanVien> listNhanVien;
    private NhanVienAdapter adapter;
    private NhanVienQuery nhanVienQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhan_vien);

        initViews();
        nhanVienQuery = new NhanVienQuery(this);
        loadData();

        btnBack.setOnClickListener(v -> finish());
        fabAdd.setOnClickListener(v -> showNhanVienDialog(null));
    }

    private void initViews() {
        lvNhanVien = findViewById(R.id.lvNhanVien);
        fabAdd = findViewById(R.id.fabAdd);
        btnBack = findViewById(R.id.btnBack);
    }

    private void loadData() {
        listNhanVien = nhanVienQuery.layDanhSachNhanVien();
        adapter = new NhanVienAdapter(this, listNhanVien, new NhanVienAdapter.OnActionClickListener() {
            @Override
            public void onEdit(NhanVien nv) {
                showNhanVienDialog(nv);
            }

            @Override
            public void onDelete(NhanVien nv) {
                confirmDelete(nv);
            }
        });
        lvNhanVien.setAdapter(adapter);
    }

    private void showNhanVienDialog(NhanVien nv) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_nhan_vien, null);
        builder.setView(view);

        TextView tvDialogTitle = view.findViewById(R.id.tvDialogTitle);
        EditText etMaNV = view.findViewById(R.id.etMaNV);
        EditText etTenNV = view.findViewById(R.id.etTenNV);
        EditText etQueQuan = view.findViewById(R.id.etQueQuan);
        Spinner spnGioiTinh = view.findViewById(R.id.spnGioiTinh);
        EditText etNamSinh = view.findViewById(R.id.etNamSinh);
        Spinner spnVaiTro = view.findViewById(R.id.spnVaiTro);
        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etSdt = view.findViewById(R.id.etSdt);
        EditText etUser = view.findViewById(R.id.etUser);
        EditText etPass = view.findViewById(R.id.etPass);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnSave = view.findViewById(R.id.btnSave);

        AlertDialog dialog = builder.create();

        if (nv != null) {
            tvDialogTitle.setText("Sửa Nhân Viên");
            etMaNV.setText(nv.getMaNV());
            etTenNV.setText(nv.getTenNV());
            etQueQuan.setText(nv.getQueQuan());
            setSpinnerValue(spnGioiTinh, nv.getGioiTinh());
            etNamSinh.setText(nv.getNamSinh());
            setSpinnerValue(spnVaiTro, nv.getVaiTro());
            etEmail.setText(nv.getEmail());
            etSdt.setText(nv.getSdt());
            etUser.setText(nv.getUser());
            etPass.setText(nv.getPass());
        } else {
            tvDialogTitle.setText("Thêm Nhân Viên");
            etMaNV.setText(nhanVienQuery.taoMaNVMoi());
        }

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            String ma = etMaNV.getText().toString().trim();
            String ten = etTenNV.getText().toString().trim();
            String que = etQueQuan.getText().toString().trim();
            String gt = spnGioiTinh.getSelectedItem().toString();
            String ns = etNamSinh.getText().toString().trim();
            String vt = spnVaiTro.getSelectedItem().toString();
            String email = etEmail.getText().toString().trim();
            String sdt = etSdt.getText().toString().trim();
            String user = etUser.getText().toString().trim();
            String pass = etPass.getText().toString().trim();

            if (ten.isEmpty() || que.isEmpty() || ns.isEmpty() || email.isEmpty() || sdt.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            NhanVien newNv = new NhanVien(ma, ten, que, gt, ns, vt, email, sdt, user, pass);
            boolean success;
            if (nv == null) {
                success = nhanVienQuery.themNhanVien(newNv);
            } else {
                success = nhanVienQuery.suaNhanVien(newNv);
            }

            if (success) {
                Toast.makeText(this, "Thành công", Toast.LENGTH_SHORT).show();
                loadData();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void confirmDelete(NhanVien nv) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa nhân viên " + nv.getTenNV() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    if (nhanVienQuery.xoaNhanVien(nv.getMaNV())) {
                        Toast.makeText(this, "Đã xóa", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void setSpinnerValue(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}
