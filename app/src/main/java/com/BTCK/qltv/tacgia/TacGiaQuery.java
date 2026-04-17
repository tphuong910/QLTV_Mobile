package com.BTCK.qltv.tacgia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.BTCK.qltv.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class TacGiaQuery {
    private final SQLiteHelper dbHelper;

    public TacGiaQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public List<TacGia> layTatCaTacGia() {
        return layDanhSach("");
    }

    public List<TacGia> layDanhSach(String keyword) {
        List<TacGia> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM tacgia";
        String[] args = null;
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += " WHERE TenTG LIKE ? OR QuocTich LIKE ? OR MaTG LIKE ?";
            args = new String[]{"%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%"};
        }
        Cursor cursor = db.rawQuery(sql, args);
        while (cursor.moveToNext()) {
            list.add(new TacGia(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            ));
        }
        cursor.close();
        return list;
    }

    public boolean themTacGia(String ma, String ten, String namSinh, String gioiTinh, String quocTich) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaTG", ma);
        values.put("TenTG", ten);
        values.put("NamSinh", namSinh);
        values.put("GioiTinh", gioiTinh);
        values.put("QuocTich", quocTich);
        return db.insert("tacgia", null, values) > 0;
    }

    public boolean capNhatTacGia(String ma, String ten, String namSinh, String gioiTinh, String quocTich) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenTG", ten);
        values.put("NamSinh", namSinh);
        values.put("GioiTinh", gioiTinh);
        values.put("QuocTich", quocTich);
        return db.update("tacgia", values, "MaTG = ?", new String[]{ma}) > 0;
    }

    public boolean xoaTacGia(String maTG) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("tacgia", "MaTG = ?", new String[]{maTG}) > 0;
    }

    public String taoMaTGMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaTG FROM tacgia ORDER BY MaTG DESC LIMIT 1", null);
        String maMoi = "TG001";
        if (cursor.moveToFirst()) {
            String maCuoi = cursor.getString(0);
            if (maCuoi != null && maCuoi.length() > 2) {
                try {
                    int so = Integer.parseInt(maCuoi.substring(2));
                    so++;
                    maMoi = String.format("TG%03d", so);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        cursor.close();
        return maMoi;
    }
}
