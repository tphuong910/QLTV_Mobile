package com.BTCK.qltv.theloai;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.BTCK.qltv.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class TheLoaiQuery {

    private final SQLiteHelper dbHelper;

    public TheLoaiQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public List<TheLoai> layDanhSachTheLoai() {
        List<TheLoai> listTheLoai = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT MaTL, TenTL FROM theloai ORDER BY MaTL",
                null
        );

        while (cursor.moveToNext()) {
            TheLoai theLoai = new TheLoai();
            theLoai.setMaTL(cursor.getString(0));
            theLoai.setTenTL(cursor.getString(1));
            listTheLoai.add(theLoai);
        }

        cursor.close();
        return listTheLoai;
    }

    public boolean themTheLoai(TheLoai theLoai) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL("INSERT INTO theloai (MaTL, TenTL) VALUES (?, ?)",
                    new Object[]{theLoai.getMaTL(), theLoai.getTenTL()});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String taoMaTheLoaiMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaTL FROM theloai", null);

        int maxId = 0;
        while (cursor.moveToNext()) {
            String maHienTai = cursor.getString(0);
            if (maHienTai != null && maHienTai.startsWith("TL")) {
                try {
                    int id = Integer.parseInt(maHienTai.substring(2));
                    if (id > maxId) {
                        maxId = id;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        cursor.close();

        return String.format("TL%03d", maxId + 1);
    }

    public boolean suaTheLoai(String maCu, TheLoai theLoai) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL("UPDATE theloai SET MaTL = ?, TenTL = ? WHERE MaTL = ?",
                    new Object[]{theLoai.getMaTL(), theLoai.getTenTL(), maCu});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaTheLoai(String maTL) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM theloai WHERE MaTL = ?", new Object[]{maTL});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
