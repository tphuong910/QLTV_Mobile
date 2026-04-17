package com.BTCK.qltv.muontra;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

// Màn hình quản lý mượn trả sách: xem danh sách, tạo phiếu mượn, trả sách, xóa
public class MuonTraActivity extends AppCompatActivity {

    private EditText edtTimKiem;
    private ImageButton btnThem;
    private ImageView imgBack;
    private ListView lvMuonTra;
    private ArrayList<MuonTra> listMuonTra;
    private ArrayAdapter<MuonTra> adapter;
    private MuonTraQuery muonTraQuery;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muon_tra);

        edtTimKiem = findViewById(R.id.edtTimKiem);
        btnThem = findViewById(R.id.btnThem);
        imgBack = findViewById(R.id.imgBackMT);
        lvMuonTra = findViewById(R.id.lvMuonTra);

        muonTraQuery = new MuonTraQuery(this);
        listMuonTra = new ArrayList<>();

        setupListView();
        loadData("");

        // Sự kiện quay lại
        if (imgBack != null) {
            imgBack.setOnClickListener(v -> finish());
        }

        // Tìm kiếm theo từ khóa khi gõ
        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadData(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Bấm nút thêm → mở dialog mượn sách
        btnThem.setOnClickListener(v -> showDialogThem());

        // Bấm vào phiếu mượn → hiển thị tùy chọn (trả sách / xóa)
        lvMuonTra.setOnItemClickListener((parent, view, position, id) -> {
            MuonTra selected = listMuonTra.get(position);
            showDialogOptions(selected);
        });
    }

    // Load lại danh sách phiếu mượn từ DB
    private void loadData(String keyword) {
        listMuonTra.clear();
        listMuonTra.addAll(muonTraQuery.layDanhSach(keyword));
        adapter.notifyDataSetChanged();
    }

    // Gán adapter cho ListView, hiển thị mã phiếu + tên đọc giả + ngày mượn + trạng thái
    private void setupListView() {
        adapter = new ArrayAdapter<MuonTra>(this, R.layout.item_muon_tra, listMuonTra) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.item_muon_tra, null);
                }
                TextView tvHienThi = convertView.findViewById(R.id.tvHienThi);
                MuonTra mt = listMuonTra.get(position);
                String tenDG = mt.getTenDG() != null ? mt.getTenDG() : mt.getMaDG();
                
                // Lấy chi tiết sách để hiển thị thêm thông tin ngày mượn/ngày trả
                String chiTietSach = muonTraQuery.layChiTietMuonTra(mt.getMaMT());
                
                tvHienThi.setText(mt.getMaMT() + " - " + tenDG
                        + "\n" + chiTietSach
                        + "\nMượn: " + mt.getNgayMuon() + " | Hạn trả: " + mt.getHanTra()
                        + "\nTrạng thái: " + mt.getTrangThai());
                return convertView;
            }
        };
        lvMuonTra.setAdapter(adapter);
    }

    // Dialog hiển thị chi tiết phiếu mượn và các tùy chọn
    private void showDialogOptions(MuonTra mt) {
        // Lấy thông tin sách đã mượn
        String chiTiet = muonTraQuery.layChiTietMuonTra(mt.getMaMT());
        String tenDG = mt.getTenDG() != null ? mt.getTenDG() : mt.getMaDG();

        // Xây dựng nội dung chi tiết phiếu mượn
        StringBuilder msg = new StringBuilder();
        msg.append("Đọc giả: ").append(tenDG);
        msg.append("\nNgày mượn: ").append(mt.getNgayMuon());
        msg.append("\nHạn trả: ").append(mt.getHanTra());
        msg.append("\nTrạng thái: ").append(mt.getTrangThai());
        if (chiTiet != null && !chiTiet.isEmpty()) {
            msg.append("\n\nSách đã mượn:\n").append(chiTiet);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Phiếu mượn " + mt.getMaMT())
                .setMessage(msg.toString());

        // Nút trả sách chỉ hiện khi chưa trả
        if ("Chưa trả".equals(mt.getTrangThai())) {
            builder.setPositiveButton("Trả sách", (dialog, which) -> showDialogTraSach(mt));
        }

        builder.setNeutralButton("Xóa", (dialog, which) -> xoaMuonTra(mt));
        builder.setNegativeButton("Đóng", null);
        builder.show();
    }

    // Dialog tạo phiếu mượn sách mới
    private void showDialogThem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_muon_tra, null);
        builder.setView(view);
        builder.setTitle("Mượn Sách");

        EditText edtMaMT = view.findViewById(R.id.edtMaMT);
        Spinner spnDocGia = view.findViewById(R.id.spnDocGia);
        Spinner spnSach = view.findViewById(R.id.spnSach);
        EditText edtSoLuong = view.findViewById(R.id.edtSoLuong);
        EditText edtNgayMuon = view.findViewById(R.id.edtNgayMuon);
        EditText edtHanTra = view.findViewById(R.id.edtHanTra);

        // Tự sinh mã phiếu mượn
        edtMaMT.setText(muonTraQuery.taoMaMuonTraMoi());
        edtMaMT.setEnabled(false);

        // Ngày mượn mặc định là hôm nay
        edtNgayMuon.setText(sdf.format(new Date()));
        edtNgayMuon.setFocusable(false);

        // Hạn trả mặc định 14 ngày sau
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 14);
        edtHanTra.setText(sdf.format(cal.getTime()));
        edtHanTra.setFocusable(false);
        edtHanTra.setOnClickListener(v -> showDatePicker(edtHanTra));

        // Nạp danh sách đọc giả có thẻ hợp lệ vào Spinner
        ArrayList<String[]> danhSachDG = muonTraQuery.layDocGiaCoTheHopLe();
        if (danhSachDG.isEmpty()) {
            Toast.makeText(this, "Không có đọc giả nào có thẻ thư viện hợp lệ!", Toast.LENGTH_LONG).show();
            return;
        }
        ArrayList<String> displayDG = new ArrayList<>();
        for (String[] dg : danhSachDG) {
            displayDG.add(dg[0] + " - " + dg[1]);
        }
        ArrayAdapter<String> adapterDG = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, displayDG);
        adapterDG.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDocGia.setAdapter(adapterDG);

        // Nạp danh sách sách còn tồn kho vào Spinner
        ArrayList<String[]> danhSachSach = muonTraQuery.layDanhSachSachConTon();
        if (danhSachSach.isEmpty()) {
            Toast.makeText(this, "Không có sách nào còn trong kho!", Toast.LENGTH_LONG).show();
            return;
        }
        ArrayList<String> displaySach = new ArrayList<>();
        for (String[] s : danhSachSach) {
            displaySach.add(s[0] + " - " + s[1] + " (Còn: " + s[2] + ")");
        }
        ArrayAdapter<String> adapterSach = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, displaySach);
        adapterSach.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSach.setAdapter(adapterSach);

        edtSoLuong.setText("1");

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String maMT = edtMaMT.getText().toString().trim();
            String hanTra = edtHanTra.getText().toString().trim();
            String soLuongStr = edtSoLuong.getText().toString().trim();

            if (spnDocGia.getSelectedItemPosition() < 0 || spnSach.getSelectedItemPosition() < 0) {
                Toast.makeText(this, "Vui lòng chọn đọc giả và sách!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (soLuongStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập số lượng!", Toast.LENGTH_SHORT).show();
                return;
            }

            int soLuong;
            try {
                soLuong = Integer.parseInt(soLuongStr);
                if (soLuong <= 0) {
                    Toast.makeText(this, "Số lượng phải lớn hơn 0!", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số lượng không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            String maDG = danhSachDG.get(spnDocGia.getSelectedItemPosition())[0];
            String maSach = danhSachSach.get(spnSach.getSelectedItemPosition())[0];
            int soLuongTon = Integer.parseInt(danhSachSach.get(spnSach.getSelectedItemPosition())[2]);

            if (soLuong > soLuongTon) {
                Toast.makeText(this, "Số lượng mượn vượt quá số lượng tồn kho (" + soLuongTon + ")!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy mã nhân viên từ phiên đăng nhập
            SharedPreferences prefs = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
            String maNV = prefs.getString("MaUser", "NV001");

            MuonTra mt = new MuonTra(maMT, maDG, maNV,
                    sdf.format(new Date()), hanTra, "Đang mượn");

            if (muonTraQuery.themMuonTra(mt, maSach, soLuong)) {
                Toast.makeText(this, "Mượn sách thành công!", Toast.LENGTH_SHORT).show();
                loadData(edtTimKiem.getText().toString());
            } else {
                Toast.makeText(this, "Mượn sách thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    // Dialog chọn tình trạng sách khi trả
    private void showDialogTraSach(MuonTra mt) {
        String chiTiet = muonTraQuery.layChiTietMuonTra(mt.getMaMT());
        String[] tinhTrangArr = {"Tốt", "Hư hỏng nhẹ", "Hư hỏng nặng"};

        // Tạo layout hiển thị chi tiết sách và radio chọn tình trạng
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(60, 30, 60, 10);

        TextView tvInfo = new TextView(this);
        tvInfo.setTextSize(15);
        tvInfo.setText("Sách trả:\n" + chiTiet + "\n\nChọn tình trạng sách:");
        layout.addView(tvInfo);

        android.widget.RadioGroup radioGroup = new android.widget.RadioGroup(this);
        for (int i = 0; i < tinhTrangArr.length; i++) {
            android.widget.RadioButton rb = new android.widget.RadioButton(this);
            rb.setText(tinhTrangArr[i]);
            rb.setId(i);
            radioGroup.addView(rb);
        }
        layout.addView(radioGroup);

        new AlertDialog.Builder(this)
                .setTitle("Trả sách - " + mt.getMaMT())
                .setView(layout)
                .setPositiveButton("Xác nhận", (dialog, which) -> {
                    int selected = radioGroup.getCheckedRadioButtonId();
                    if (selected < 0) {
                        Toast.makeText(this, "Vui lòng chọn tình trạng sách!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (muonTraQuery.traSach(mt.getMaMT(), tinhTrangArr[selected])) {
                        Toast.makeText(this, "Trả sách thành công!", Toast.LENGTH_SHORT).show();
                        loadData(edtTimKiem.getText().toString());
                    } else {
                        Toast.makeText(this, "Trả sách thất bại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
    // Mở DatePicker cho người dùng chọn ngày
    private void showDatePicker(EditText target) {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            cal.set(year, month, dayOfMonth);
            target.setText(sdf.format(cal.getTime()));
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    // Xác nhận và xóa phiếu mượn
    private void xoaMuonTra(MuonTra mt) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc muốn xóa phiếu mượn này không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    if (muonTraQuery.xoaMuonTra(mt.getMaMT())) {
                        Toast.makeText(this, "Đã xóa!", Toast.LENGTH_SHORT).show();
                        loadData(edtTimKiem.getText().toString());
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }
}
