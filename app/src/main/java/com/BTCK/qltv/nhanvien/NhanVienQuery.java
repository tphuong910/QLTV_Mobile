package com.BTCK.qltv.nhanvien;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.BTCK.qltv.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class NhanVienQuery {

    private SQLiteHelper dbHelper;

    public NhanVienQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public List<NhanVien> layDanhSachNhanVien() {
        List<NhanVien> listNhanVien = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT MaNV, TenNV, QueQuan, GioiTinh, NamSinh, VaiTro, Email, SDT, [User], Pass FROM nhanvien ORDER BY MaNV", null);

        while (cursor.moveToNext() == true) {
            NhanVien nv = new NhanVien();
            nv.setMaNV(cursor.getString(0));
            nv.setTenNV(cursor.getString(1));
            nv.setQueQuan(cursor.getString(2));
            nv.setGioiTinh(cursor.getString(3));
            nv.setNamSinh(cursor.getString(4));
            nv.setVaiTro(cursor.getString(5));
            nv.setEmail(cursor.getString(6));
            nv.setSdt(cursor.getString(7));
            nv.setUser(cursor.getString(8));
            nv.setPass(cursor.getString(9));

            listNhanVien.add(nv);
        }
        cursor.close();

        return listNhanVien;
    }

    public String taoMaNhanVienMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaNV FROM nhanvien ORDER BY MaNV DESC LIMIT 1", null);

        String maMoi = "NV001"; // Mã mặc định bắt đầu nếu bảng trống

        if (cursor.moveToFirst() == true) {
            String maCuoi = cursor.getString(0);

            if (maCuoi != null && maCuoi.startsWith("NV")) {
                // Tách chuỗi "NV001" lấy phần số "001" rẽ sang Int
                String phanSo = maCuoi.substring(2);
                try {
                    int so = Integer.parseInt(phanSo);
                    so = so + 1; // Tăng mã lên 1

                    // Nối thủ công thay vì dùng String.format phức tạp
                    if (so < 10) {
                        maMoi = "NV00" + so;
                    } else if (so < 100) {
                        maMoi = "NV0" + so;
                    } else {
                        maMoi = "NV" + so;
                    }
                } catch (Exception e) {
                    maMoi = "NV001";
                }
            }
        }
        cursor.close();
        return maMoi;
    }

    public boolean themNhanVien(NhanVien nv) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            Object[] data = new Object[]{
                    nv.getMaNV(), nv.getTenNV(), nv.getQueQuan(), nv.getGioiTinh(),
                    nv.getNamSinh(), nv.getVaiTro(), nv.getEmail(), nv.getSdt(),
                    nv.getUser(), nv.getPass()
            };
            db.execSQL("INSERT INTO nhanvien (MaNV, TenNV, QueQuan, GioiTinh, NamSinh, VaiTro, Email, SDT, [User], Pass) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean suaNhanVien(NhanVien nv) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            Object[] data = new Object[]{
                    nv.getTenNV(), nv.getQueQuan(), nv.getGioiTinh(), nv.getNamSinh(),
                    nv.getVaiTro(), nv.getEmail(), nv.getSdt(), nv.getUser(),
                    nv.getPass(), nv.getMaNV()
            };
            db.execSQL("UPDATE nhanvien SET TenNV = ?, QueQuan = ?, GioiTinh = ?, NamSinh = ?, VaiTro = ?, Email = ?, SDT = ?, [User] = ?, Pass = ? WHERE MaNV = ?", data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean xoaNhanVien(String maNV) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            Object[] data = new Object[]{maNV};
            db.execSQL("DELETE FROM nhanvien WHERE MaNV = ?", data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}