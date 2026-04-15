package com.BTCK.qltv.khachhang;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;
import com.BTCK.qltv.login.LoginActivity;
import com.BTCK.qltv.muontra.MuonTra;
import com.BTCK.qltv.muontra.MuonTraQuery;
import com.BTCK.qltv.ngonngu.NgonNgu;
import com.BTCK.qltv.ngonngu.NgonNguQuery;
import com.BTCK.qltv.nhaxuatban.NhaXuatBan;
import com.BTCK.qltv.nhaxuatban.NhaXuatBanQuery;
import com.BTCK.qltv.sach.Sach;
import com.BTCK.qltv.sach.SachQuery;
import com.BTCK.qltv.tacgia.TacGia;
import com.BTCK.qltv.tacgia.TacGiaQuery;
import com.BTCK.qltv.theloai.TheLoai;
import com.BTCK.qltv.theloai.TheLoaiQuery;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeKhachHangActivity extends AppCompatActivity {

    private TextView tvWelcome, tvTitle;
    private ImageView imgMenu;
    private EditText edtSearch;
    private ListView lvMain;
    private TabLayout tabLayout;
    private Spinner spnFilterTheLoai;
    private FloatingActionButton fabMuonSach;

    private SachQuery sachQuery;
    private TacGiaQuery tacGiaQuery;
    private NhaXuatBanQuery nxbQuery;
    private NgonNguQuery ngonNguQuery;
    private MuonTraQuery muonTraQuery;
    private TheLoaiQuery theLoaiQuery;

    private ArrayList<Object> currentList = new ArrayList<>();
    private ArrayAdapter<Object> adapter;
    private int currentTab = 0;
    private String maDG;
    private List<TheLoai> listTheLoai;
    private String currentSelectedMaTL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_khach_hang);

        initViews();
        initQueries();
        loadUserInfo();
        setupTabLayout();
        setupSpinner();
        setupListView();
        loadData("");

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadData(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        imgMenu.setOnClickListener(v -> showPopupMenu());
        
        fabMuonSach.setOnClickListener(v -> {
            tabLayout.getTabAt(0).select(); // Chuyển về tab Sách để mượn
            Toast.makeText(this, "Hãy chọn sách bạn muốn mượn từ danh sách!", Toast.LENGTH_SHORT).show();
        });
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcomeKH);
        tvTitle = findViewById(R.id.tvTitleKH);
        imgMenu = findViewById(R.id.imgMenuKH);
        edtSearch = findViewById(R.id.edtSearchKH);
        lvMain = findViewById(R.id.lvMainKH);
        tabLayout = findViewById(R.id.tabLayoutKH);
        spnFilterTheLoai = findViewById(R.id.spnFilterTheLoaiKH);
        fabMuonSach = findViewById(R.id.fabMuonSachKH);
    }

    private void initQueries() {
        sachQuery = new SachQuery(this);
        tacGiaQuery = new TacGiaQuery(this);
        nxbQuery = new NhaXuatBanQuery(this);
        ngonNguQuery = new NgonNguQuery(this);
        muonTraQuery = new MuonTraQuery(this);
        theLoaiQuery = new TheLoaiQuery(this);
    }

    private void loadUserInfo() {
        SharedPreferences prefs = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String ten = prefs.getString("TenUser", "Khách hàng");
        maDG = prefs.getString("MaUser", "");
        tvWelcome.setText("Xin chào, " + ten);
    }

    private void setupTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition();
                edtSearch.setText("");
                updateUIForTab();
                loadData("");
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void updateUIForTab() {
        spnFilterTheLoai.setVisibility(currentTab == 0 ? View.VISIBLE : View.GONE);
        fabMuonSach.setVisibility(currentTab == 5 ? View.VISIBLE : View.GONE);
        
        switch (currentTab) {
            case 0: tvTitle.setText("Danh sách sách hiện có"); break;
            case 1: tvTitle.setText("Danh sách thể loại"); break;
            case 2: tvTitle.setText("Danh sách tác giả"); break;
            case 3: tvTitle.setText("Danh sách nhà xuất bản"); break;
            case 4: tvTitle.setText("Danh sách ngôn ngữ"); break;
            case 5: tvTitle.setText("Sách bạn đang mượn"); break;
        }
    }

    private void setupSpinner() {
        listTheLoai = theLoaiQuery.layDanhSachTheLoai();
        ArrayList<String> tenTLList = new ArrayList<>();
        tenTLList.add("Tất cả thể loại");
        for (TheLoai tl : listTheLoai) tenTLList.add(tl.getTenTL());

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenTLList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFilterTheLoai.setAdapter(spinnerAdapter);

        spnFilterTheLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) currentSelectedMaTL = "";
                else currentSelectedMaTL = listTheLoai.get(position - 1).getMaTL();
                loadData(edtSearch.getText().toString());
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadData(String keyword) {
        currentList.clear();
        String lowerKeyword = keyword.toLowerCase().trim();

        switch (currentTab) {
            case 0: // Sách
                List<Sach> sList = sachQuery.layDanhSach(keyword);
                for (Sach s : sList) {
                    if (currentSelectedMaTL.isEmpty() || s.getMaTL().equals(currentSelectedMaTL)) {
                        currentList.add(s);
                    }
                }
                break;
            case 1: // Thể loại
                List<TheLoai> tlList = theLoaiQuery.layDanhSachTheLoai();
                for (TheLoai tl : tlList) {
                    if (tl.getTenTL().toLowerCase().contains(lowerKeyword)) currentList.add(tl);
                }
                break;
            case 2: // Tác giả
                List<TacGia> tgList = tacGiaQuery.layTatCaTacGia();
                for (TacGia tg : tgList) {
                    if (tg.getTenTG().toLowerCase().contains(lowerKeyword)) currentList.add(tg);
                }
                break;
            case 3: // NXB
                List<NhaXuatBan> nxbList = nxbQuery.layDanhSachNXB();
                for (NhaXuatBan nxb : nxbList) {
                    if (nxb.getTenNXB().toLowerCase().contains(lowerKeyword)) currentList.add(nxb);
                }
                break;
            case 4: // Ngôn ngữ
                List<NgonNgu> nnList = ngonNguQuery.layTatCaNgonNgu();
                for (NgonNgu nn : nnList) {
                    if (nn.getTenNN().toLowerCase().contains(lowerKeyword)) currentList.add(nn);
                }
                break;
            case 5: // Mượn trả
                ArrayList<MuonTra> mtList = muonTraQuery.layDanhSachTheoKhachHang(maDG);
                for (MuonTra mt : mtList) {
                    String chiTiet = muonTraQuery.layChiTietMuonTra(mt.getMaMT());
                    if (chiTiet.toLowerCase().contains(lowerKeyword)) currentList.add(mt);
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    private void setupListView() {
        adapter = new ArrayAdapter<Object>(this, android.R.layout.simple_list_item_2, android.R.id.text1, currentList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);

                Object item = currentList.get(position);
                if (item instanceof Sach) {
                    Sach s = (Sach) item;
                    text1.setText(s.getTenSach());
                    text2.setText("Tác giả: " + s.getMaTG() + " | SL: " + s.getSoLuong());
                } else if (item instanceof TheLoai) {
                    TheLoai tl = (TheLoai) item;
                    text1.setText(tl.getTenTL());
                    text2.setText("Mã: " + tl.getMaTL());
                } else if (item instanceof TacGia) {
                    TacGia tg = (TacGia) item;
                    text1.setText(tg.getTenTG());
                    text2.setText("Quốc tịch: " + tg.getQuocTich());
                } else if (item instanceof NhaXuatBan) {
                    NhaXuatBan nxb = (NhaXuatBan) item;
                    text1.setText(nxb.getTenNXB());
                    text2.setText("Địa chỉ: " + nxb.getDiaChi());
                } else if (item instanceof NgonNgu) {
                    NgonNgu nn = (NgonNgu) item;
                    text1.setText(nn.getTenNN());
                    text2.setText("Mã: " + nn.getMaNN());
                } else if (item instanceof MuonTra) {
                    MuonTra mt = (MuonTra) item;
                    String chiTiet = muonTraQuery.layChiTietMuonTra(mt.getMaMT());
                    text1.setText(chiTiet);
                    text2.setText("Mượn: " + mt.getNgayMuon() + " | Hạn: " + mt.getHanTra() + " | " + mt.getTrangThai());
                }
                return view;
            }
        };
        lvMain.setAdapter(adapter);

        lvMain.setOnItemClickListener((parent, view, position, id) -> {
            Object item = currentList.get(position);
            if (currentTab == 0 && item instanceof Sach) {
                showBorrowDialog((Sach) item);
            } else if (currentTab == 5 && item instanceof MuonTra) {
                MuonTra mt = (MuonTra) item;
                if (!mt.getTrangThai().equals("Đã trả")) showReturnDialog(mt);
            }
        });
    }

    private void showBorrowDialog(Sach sach) {
        if (sach.getSoLuong() <= 0) {
            Toast.makeText(this, "Sách này đã hết!", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mượn sách: " + sach.getTenSach());

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_muon_tra_khach_hang, null);
        TextView tvNgayMuon = dialogView.findViewById(R.id.tvNgayMuonKH);
        EditText edtHanTra = dialogView.findViewById(R.id.edtHanTraKH);

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        tvNgayMuon.setText("Ngày mượn: " + today);

        edtHanTra.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                edtHanTra.setText(date);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        builder.setView(dialogView);
        builder.setPositiveButton("Xác nhận mượn", (dialog, which) -> {
            String hanTra = edtHanTra.getText().toString().trim();
            if (hanTra.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn hạn trả!", Toast.LENGTH_SHORT).show();
                return;
            }

            MuonTra mt = new MuonTra();
            mt.setMaMT(muonTraQuery.taoMaMuonTraMoi());
            mt.setMaDG(maDG);
            mt.setMaNV("NV001");
            mt.setNgayMuon(today);
            mt.setHanTra(hanTra);
            mt.setTrangThai("Đang mượn");

            if (muonTraQuery.themMuonTra(mt, sach.getMaSach(), 1)) {
                Toast.makeText(this, "Đã gửi yêu cầu mượn sách!", Toast.LENGTH_SHORT).show();
                loadData(edtSearch.getText().toString());
            } else {
                Toast.makeText(this, "Mượn sách thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void showReturnDialog(MuonTra mt) {
        new AlertDialog.Builder(this)
                .setTitle("Trả sách")
                .setMessage("Bạn muốn trả sách này?")
                .setPositiveButton("Xác nhận trả", (dialog, which) -> {
                    if (muonTraQuery.traSach(mt.getMaMT(), "Bình thường")) {
                        Toast.makeText(this, "Trả sách thành công!", Toast.LENGTH_SHORT).show();
                        loadData("");
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, imgMenu);
        popupMenu.getMenu().add("Đăng xuất");
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("Đăng xuất")) {
                getSharedPreferences("UserSession", Context.MODE_PRIVATE).edit().clear().apply();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
            return true;
        });
        popupMenu.show();
    }
}
