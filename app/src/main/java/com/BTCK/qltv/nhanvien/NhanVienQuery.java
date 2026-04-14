package com.BTCK.qltv.nhanvien;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.BTCK.qltv.database.SQLiteHelper;
import java.util.ArrayList;
import java.util.List;

public class NhanVienQuery {
    private final SQLiteHelper dbHelper;

    public NhanVienQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public List<NhanVien> layDanhSachNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM nhanvien", null);
        if (cursor.moveToFirst()) {
            do {
                NhanVien nv = new NhanVien(
                    cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7), cursor.getString(8),
                    cursor.getString(9)
                );
                list.add(nv);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean themNhanVien(NhanVien nv) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaNV", nv.getMaNV());
        values.put("TenNV", nv.getTenNV());
        values.put("QueQuan", nv.getQueQuan());
        values.put("GioiTinh", nv.getGioiTinh());
        values.put("NamSinh", nv.getNamSinh());
        values.put("VaiTro", nv.getVaiTro());
        values.put("Email", nv.getEmail());
        values.put("Sdt", nv.getSdt());
        values.put("User", nv.getUser());
        values.put("Pass", nv.getPass());
        return db.insert("nhanvien", null, values) != -1;
    }

    public boolean suaNhanVien(NhanVien nv) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenNV", nv.getTenNV());
        values.put("QueQuan", nv.getQueQuan());
        values.put("GioiTinh", nv.getGioiTinh());
        values.put("NamSinh", nv.getNamSinh());
        values.put("VaiTro", nv.getVaiTro());
        values.put("Email", nv.getEmail());
        values.put("Sdt", nv.getSdt());
        values.put("User", nv.getUser());
        values.put("Pass", nv.getPass());
        return db.update("nhanvien", values, "MaNV = ?", new String[]{nv.getMaNV()}) > 0;
    }

    public boolean xoaNhanVien(String maNV) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("nhanvien", "MaNV = ?", new String[]{maNV}) > 0;
    }

    public String taoMaNVMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaNV FROM nhanvien ORDER BY MaNV DESC LIMIT 1", null);
        String maMoi = "NV001";
        if (cursor.moveToFirst()) {
            String maCu = cursor.getString(0);
            int so = Integer.parseInt(maCu.substring(2)) + 1;
            maMoi = String.format("NV%03d", so);
        }
        cursor.close();
        return maMoi;
    }
}
