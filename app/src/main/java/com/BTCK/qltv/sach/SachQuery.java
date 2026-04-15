package com.BTCK.qltv.sach;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.BTCK.qltv.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SachQuery {

    private final SQLiteHelper dbHelper;

    public SachQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    // Lấy toàn bộ danh sách sách (dành cho Admin)
    public List<Sach> layDanhSachSach() {
        return layDanhSach("");
    }

    // Lấy danh sách sách có lọc theo từ khóa (dành cho cả Admin và Khách hàng)
    public List<Sach> layDanhSach(String keyword) {
        List<Sach> listSach = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT s.MaSach, s.MaTG, s.MaNXB, s.MaTL, s.TenSach, s.MaNN, s.MaViTri, s.NamXB, s.SoLuong, tg.TenTG " +
                "FROM sach s JOIN tacgia tg ON s.MaTG = tg.MaTG ";
        
        String[] args = null;
        if (keyword != null && !keyword.isEmpty()) {
            query += "WHERE s.TenSach LIKE ? OR tg.TenTG LIKE ? ";
            args = new String[]{"%" + keyword + "%", "%" + keyword + "%"};
        }
        query += "ORDER BY s.MaSach";

        Cursor cursor = db.rawQuery(query, args);

        while (cursor.moveToNext()) {
            Sach sach = new Sach();
            sach.setMaSach(cursor.getString(0));
            sach.setMaTG(cursor.getString(9)); // Lấy tên tác giả thay vì mã để hiển thị
            sach.setMaNXB(cursor.getString(2));
            sach.setMaTL(cursor.getString(3));
            sach.setTenSach(cursor.getString(4));
            sach.setMaNN(cursor.getString(5));
            sach.setMaViTri(cursor.getString(6));
            sach.setNamXB(cursor.getInt(7));
            sach.setSoLuong(cursor.getInt(8));
            listSach.add(sach);
        }

        cursor.close();
        return listSach;
    }

    public String taoMaSachMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT MaSach FROM sach ORDER BY MaSach DESC LIMIT 1",
                null
        );

        String maMoi = "S001";
        if (cursor.moveToFirst()) {
            String maCuoi = cursor.getString(0);
            if (maCuoi != null && maCuoi.startsWith("S")) {
                try {
                    int so = Integer.parseInt(maCuoi.substring(1));
                    maMoi = String.format(Locale.getDefault(), "S%03d", so + 1);
                } catch (NumberFormatException ignored) {
                    maMoi = "S001";
                }
            }
        }

        cursor.close();
        return maMoi;
    }

    public boolean themSach(Sach sach) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL("INSERT INTO sach (MaSach, MaTG, MaNXB, MaTL, TenSach, NamXB, SoLuong, MaNN, MaViTri) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{sach.getMaSach(), sach.getMaTG(), sach.getMaNXB(), sach.getMaTL(), sach.getTenSach(), sach.getNamXB(), sach.getSoLuong(), sach.getMaNN(), sach.getMaViTri()});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean suaSach(Sach sach) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL("UPDATE sach SET MaTG = ?, MaNXB = ?, MaTL = ?, TenSach = ?, NamXB = ?, SoLuong = ?, MaNN = ?, MaViTri = ? WHERE MaSach = ?",
                    new Object[]{sach.getMaTG(), sach.getMaNXB(), sach.getMaTL(), sach.getTenSach(), sach.getNamXB(), sach.getSoLuong(), sach.getMaNN(), sach.getMaViTri(), sach.getMaSach()});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaSach(String maSach) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM sach WHERE MaSach = ?", new Object[]{maSach});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
