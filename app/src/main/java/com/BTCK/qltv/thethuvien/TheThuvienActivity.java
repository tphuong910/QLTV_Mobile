package com.BTCK.qltv.thethuvien;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// Màn hình quản lý thẻ thư viện: xem danh sách, tìm kiếm, thêm, sửa, xóa
public class TheThuvienActivity extends AppCompatActivity {

    private EditText edtTimKiem;
    private ImageButton btnThem;
    private ListView lvTheThuvien;
    private ArrayList<TheThuvien> listThe;
    private ArrayAdapter<TheThuvien> adapter;
    private TheThuvienQuery theThuvienQuery;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_thu_vien);

        edtTimKiem = findViewById(R.id.edtTimKiem);
        btnThem = findViewById(R.id.btnThem);
        lvTheThuvien = findViewById(R.id.lvTheThuvien);

        theThuvienQuery = new TheThuvienQuery(this);
        listThe = new ArrayList<>();

        setupListView();
        loadData("");

        // Tìm kiếm theo từ khóa khi người dùng gõ
        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadData(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Bấm nút thêm → mở dialog thêm thẻ mới
        btnThem.setOnClickListener(v -> showDialogThemSua(null));

        // Bấm vào item → hiển thị tùy chọn sửa / xóa
        lvTheThuvien.setOnItemClickListener((parent, view, position, id) -> {
            TheThuvien selected = listThe.get(position);
            showDialogOptions(selected);
        });
    }

    // Load lại danh sách thẻ từ DB theo từ khóa
    private void loadData(String keyword) {
        listThe.clear();
        listThe.addAll(theThuvienQuery.layDanhSach(keyword));
        adapter.notifyDataSetChanged();
    }

    // Gán adapter cho ListView, hiển thị mã thẻ + tên đọc giả + ngày cấp + trạng thái
    private void setupListView() {
        adapter = new ArrayAdapter<TheThuvien>(this, R.layout.item_the_thu_vien, listThe) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.item_the_thu_vien, null);
                }
                TextView tvHienThi = convertView.findViewById(R.id.tvHienThi);
                TheThuvien the = listThe.get(position);
                String tenDG = the.getTenDG() != null ? the.getTenDG() : "";
                tvHienThi.setText(the.getMaThe() + " - " + tenDG + " (" + the.getMaDG() + ")"
                        + "\nCấp: " + the.getNgayCap() + " | Hết hạn: " + the.getNgayHetHan()
                        + "\n" + the.getTrangThai());
                return convertView;
            }
        };
        lvTheThuvien.setAdapter(adapter);
    }

    // Dialog tùy chọn khi bấm vào 1 thẻ: Sửa hoặc Xóa
    private void showDialogOptions(TheThuvien the) {
        String[] options = {"Sửa", "Xóa"};
        new AlertDialog.Builder(this)
                .setTitle(the.getMaThe() + " - " + (the.getTenDG() != null ? the.getTenDG() : the.getMaDG()))
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showDialogThemSua(the);
                    } else {
                        xoaThe(the);
                    }
                }).show();
    }

    // Dialog thêm / sửa thẻ thư viện. Nếu the == null thì là thêm mới
    private void showDialogThemSua(TheThuvien the) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_the_thu_vien, null);
        builder.setView(view);

        EditText edtMaThe = view.findViewById(R.id.edtMaThe);
        Spinner spnDocGia = view.findViewById(R.id.spnDocGia);
        EditText edtNgayCap = view.findViewById(R.id.edtNgayCap);
        EditText edtNgayHetHan = view.findViewById(R.id.edtNgayHetHan);
        Spinner spnTrangThai = view.findViewById(R.id.spnTrangThai);

        // Nạp danh sách đọc giả vào Spinner (chọ chứ không nhập tay)
        ArrayList<String[]> danhSachDG = theThuvienQuery.layDanhSachDocGia();
        ArrayList<String> displayDG = new ArrayList<>();
        for (String[] dg : danhSachDG) {
            displayDG.add(dg[0] + " - " + dg[1]);
        }
        ArrayAdapter<String> adapterDG = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, displayDG);
        adapterDG.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDocGia.setAdapter(adapterDG);

        // Nạp danh sách trạng thái vào Spinner
        String[] trangThaiArr = {"Còn hiệu lực", "Hết hiệu lực"};
        ArrayAdapter<String> adapterTT = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, trangThaiArr);
        adapterTT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTrangThai.setAdapter(adapterTT);

        // Cho chọn ngày bằng DatePicker, không nhập tay
        edtNgayCap.setFocusable(false);
        edtNgayHetHan.setFocusable(false);
        edtNgayCap.setOnClickListener(v -> showDatePicker(edtNgayCap));
        edtNgayHetHan.setOnClickListener(v -> showDatePicker(edtNgayHetHan));

        boolean isEdit = (the != null);

        if (isEdit) {
            builder.setTitle("Sửa Thẻ Thư Viện");
            edtMaThe.setText(the.getMaThe());
            edtMaThe.setEnabled(false);
            edtNgayCap.setText(the.getNgayCap());
            edtNgayHetHan.setText(the.getNgayHetHan());

            // Chọ đúng đọc giả trong Spinner khi sửa
            for (int i = 0; i < danhSachDG.size(); i++) {
                if (danhSachDG.get(i)[0].equals(the.getMaDG())) {
                    spnDocGia.setSelection(i);
                    break;
                }
            }

            // Chọ đúng trạng thái trong Spinner khi sửa
            for (int i = 0; i < trangThaiArr.length; i++) {
                if (trangThaiArr[i].equals(the.getTrangThai())) {
                    spnTrangThai.setSelection(i);
                    break;
                }
            }
        } else {
            builder.setTitle("Thêm Thẻ Thư Viện");
            edtMaThe.setText(theThuvienQuery.taoMaTheMoi());
            edtMaThe.setEnabled(false);
            edtNgayCap.setText(sdf.format(new Date()));
            // Mặc định hết hạn sau 1 năm kể từ ngày cấp
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, 1);
            edtNgayHetHan.setText(sdf.format(cal.getTime()));
            spnTrangThai.setSelection(0); // "Còn hiệu lực"
        }

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String maThe2 = edtMaThe.getText().toString().trim();
            String ngayCap = edtNgayCap.getText().toString().trim();
            String ngayHetHan = edtNgayHetHan.getText().toString().trim();
            String trangThai = spnTrangThai.getSelectedItem().toString();

            if (spnDocGia.getSelectedItemPosition() < 0 || danhSachDG.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn đọc giả!", Toast.LENGTH_SHORT).show();
                return;
            }

            String maDG = danhSachDG.get(spnDocGia.getSelectedItemPosition())[0];

            if (ngayCap.isEmpty() || ngayHetHan.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isEdit) {
                the.setMaDG(maDG);
                the.setNgayCap(ngayCap);
                the.setNgayHetHan(ngayHetHan);
                the.setTrangThai(trangThai);
                if (theThuvienQuery.suaThe(the)) {
                    Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            } else {
                TheThuvien newThe = new TheThuvien(maThe2, maDG, ngayCap, ngayHetHan, trangThai);
                if (theThuvienQuery.themThe(newThe)) {
                    Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
            loadData(edtTimKiem.getText().toString());
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    // Mở DatePicker để người dùng chọn ngày
    private void showDatePicker(EditText target) {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            cal.set(year, month, dayOfMonth);
            target.setText(sdf.format(cal.getTime()));
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    // Xác nhận và xóa thẻ thư viện
    private void xoaThe(TheThuvien the) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc muốn xóa thẻ thư viện này không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    if (theThuvienQuery.xoaThe(the.getMaThe())) {
                        Toast.makeText(this, "Đã xóa!", Toast.LENGTH_SHORT).show();
                        loadData(edtTimKiem.getText().toString());
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }
}
