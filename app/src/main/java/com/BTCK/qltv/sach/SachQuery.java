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

    public List<Sach> layDanhSachSach() {
        List<Sach> listSach = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT MaSach, MaTG, MaNXB, MaTL, TenSach, MaNN, MaViTri, NamXB, SoLuong FROM sach ORDER BY MaSach",
                null
        );

        while (cursor.moveToNext()) {
            Sach sach = new Sach();
            sach.setMaSach(cursor.getString(0));
            sach.setMaTG(cursor.getString(1));
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
        ContentValues values = new ContentValues();
        values.put("MaSach", sach.getMaSach());
        values.put("MaTG", sach.getMaTG());
        values.put("MaNXB", sach.getMaNXB());
        values.put("MaTL", sach.getMaTL());
        values.put("TenSach", sach.getTenSach());
        values.put("NamXB", sach.getNamXB());
        values.put("SoLuong", sach.getSoLuong());
        values.put("MaNN", sach.getMaNN());
        values.put("MaViTri", sach.getMaViTri());

        long row = db.insert("sach", null, values);
        return row != -1;
    }

    public boolean suaSach(Sach sach) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaTG", sach.getMaTG());
        values.put("MaNXB", sach.getMaNXB());
        values.put("MaTL", sach.getMaTL());
        values.put("TenSach", sach.getTenSach());
        values.put("NamXB", sach.getNamXB());
        values.put("SoLuong", sach.getSoLuong());
        values.put("MaNN", sach.getMaNN());
        values.put("MaViTri", sach.getMaViTri());

        int row = db.update("sach", values, "MaSach = ?", new String[]{sach.getMaSach()});
        return row > 0;
    }

    public boolean xoaSach(String maSach) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int row = db.delete("sach", "MaSach = ?", new String[]{maSach});
        return row > 0;
    }
}
