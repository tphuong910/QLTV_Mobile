package com.BTCK.qltv.lop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.BTCK.qltv.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class LopQuery {
    private final SQLiteHelper dbHelper;

    public LopQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public List<Lop> layDanhSach(String keyword) {
        List<Lop> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT lop.*, khoa.TenKhoa FROM lop JOIN khoa ON lop.MaKhoa = khoa.MaKhoa";
        String[] args = null;
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += " WHERE TenLop LIKE ? OR MaLop LIKE ? OR khoa.TenKhoa LIKE ?";
            args = new String[]{"%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%"};
        }
        Cursor cursor = db.rawQuery(sql, args);
        while (cursor.moveToNext()) {
            list.add(new Lop(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            ));
        }
        cursor.close();
        return list;
    }

    public List<Lop> layTatCaLop() {
        return layDanhSach("");
    }

    public boolean themLop(String ma, String ten, String maKhoa) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaLop", ma);
        values.put("TenLop", ten);
        values.put("MaKhoa", maKhoa);
        return db.insert("lop", null, values) > 0;
    }

    public boolean capNhatLop(String ma, String ten, String maKhoa) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenLop", ten);
        values.put("MaKhoa", maKhoa);
        return db.update("lop", values, "MaLop = ?", new String[]{ma}) > 0;
    }

    public boolean xoaLop(String maLop) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("lop", "MaLop = ?", new String[]{maLop}) > 0;
    }

    public String taoMaLopMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaLop FROM lop ORDER BY MaLop DESC LIMIT 1", null);
        String maMoi = "L001";
        if (cursor.moveToFirst()) {
            String maCuoi = cursor.getString(0);
            if (maCuoi != null && maCuoi.length() > 1) {
                try {
                    int so = Integer.parseInt(maCuoi.substring(1));
                    so++;
                    maMoi = String.format("L%03d", so);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        cursor.close();
        return maMoi;
    }

    public List<String[]> layDanhSachKhoa() {
        List<String[]> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM khoa", null);
        while (cursor.moveToNext()) {
            list.add(new String[]{cursor.getString(0), cursor.getString(1)});
        }
        cursor.close();
        return list;
    }
}
