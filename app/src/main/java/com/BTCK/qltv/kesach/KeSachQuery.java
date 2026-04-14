package com.BTCK.qltv.kesach;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.BTCK.qltv.database.SQLiteHelper;
import java.util.ArrayList;

public class KeSachQuery {
    private SQLiteHelper dbHelper;

    public KeSachQuery(Context context) {
        dbHelper = new SQLiteHelper(context); // Sử dụng class SQLiteHelper của bạn
    }

    // 1. Lấy danh sách hoặc Tìm kiếm
    public ArrayList<KeSach> layDanhSachKeSach(String tuKhoa) {
        ArrayList<KeSach> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM kesach";
        String[] selectionArgs = null;

        if (tuKhoa != null && !tuKhoa.trim().isEmpty()) {
            query += " WHERE TenKe LIKE ? OR MaViTri LIKE ?";
            selectionArgs = new String[]{"%" + tuKhoa + "%", "%" + tuKhoa + "%"};
        }

        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (cursor.moveToFirst()) {
            do {
                list.add(new KeSach(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // 2. Thêm
    public boolean themKeSach(KeSach ks) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaViTri", ks.getMaViTri());
        values.put("TenKe", ks.getTenKe());
        values.put("MoTa", ks.getMoTa());
        long result = db.insert("kesach", null, values);
        return result != -1;
    }

    // 3. Sửa
    public boolean suaKeSach(KeSach ks) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenKe", ks.getTenKe());
        values.put("MoTa", ks.getMoTa());
        int result = db.update("kesach", values, "MaViTri=?", new String[]{ks.getMaViTri()});
        return result > 0;
    }

    // 4. Xóa
    public boolean xoaKeSach(String MaViTri) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete("kesach", "MaViTri=?", new String[]{MaViTri});
        return result > 0;
    }
}
