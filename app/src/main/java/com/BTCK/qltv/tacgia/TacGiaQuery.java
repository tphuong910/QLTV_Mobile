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

    // Lấy toàn bộ danh sách tác giả
    public List<TacGia> layTatCaTacGia() {
        List<TacGia> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tacgia", null);
        while (cursor.moveToNext()) {
            list.add(new TacGia(
                    cursor.getString(0), // MaTG
                    cursor.getString(1), // TenTG
                    cursor.getString(2), // NamSinh
                    cursor.getString(3), // GioiTinh
                    cursor.getString(4)  // QuocTich
            ));
        }
        cursor.close();
        return list;
    }

    // Thêm tác giả mới
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

    // Cập nhật tác giả
    public boolean capNhatTacGia(String ma, String ten, String namSinh, String gioiTinh, String quocTich) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenTG", ten);
        values.put("NamSinh", namSinh);
        values.put("GioiTinh", gioiTinh);
        values.put("QuocTich", quocTich);
        return db.update("tacgia", values, "MaTG = ?", new String[]{ma}) > 0;
    }

    // Xóa tác giả
    public boolean xoaTacGia(String maTG) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("tacgia", "MaTG = ?", new String[]{maTG}) > 0;
    }

    // Tự động sinh mã tác giả mới
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
