package com.BTCK.qltv.khoa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

import com.BTCK.qltv.database.SQLiteHelper;

public class KhoaQuery {
    private final SQLiteHelper dbHelper;

    public KhoaQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public ArrayList<Khoa> layDanhSachKhoa() {
        ArrayList<Khoa> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM khoa", null);

        if (cursor.moveToFirst()) {
            do {
                list.add(new Khoa(cursor.getString(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public String taoMaKhoaMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaKhoa FROM khoa ORDER BY MaKhoa DESC LIMIT 1", null);
        String maMoi = "KH001";
        if (cursor.moveToFirst()) {
            String maCu = cursor.getString(0);
            try {
                int so = Integer.parseInt(maCu.substring(2)) + 1; // Cắt chữ KH
                maMoi = String.format("KH%03d", so);
            } catch (Exception e) { maMoi = "KH999"; }
        }
        cursor.close();
        return maMoi;
    }

    public boolean themKhoa(Khoa khoa) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaKhoa", khoa.getMaKhoa());
        values.put("TenKhoa", khoa.getTenKhoa());
        return db.insert("khoa", null, values) != -1;
    }

    public boolean capNhatKhoa(Khoa khoa) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenKhoa", khoa.getTenKhoa());
        return db.update("khoa", values, "MaKhoa = ?", new String[]{khoa.getMaKhoa()}) > 0;
    }

    public boolean xoaKhoa(String maKhoa) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("khoa", "MaKhoa = ?", new String[]{maKhoa}) > 0;
    }
}