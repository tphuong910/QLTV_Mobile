package com.BTCK.qltv.ngonngu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.BTCK.qltv.database.SQLiteHelper;
import java.util.ArrayList;
import java.util.List;

public class NgonNguQuery {
    private final SQLiteHelper dbHelper;

    public NgonNguQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public List<NgonNgu> layDanhSach(String keyword) {
        List<NgonNgu> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM ngonngu";
        String[] args = null;
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += " WHERE TenNN LIKE ? OR MaNN LIKE ?";
            args = new String[]{"%" + keyword + "%", "%" + keyword + "%"};
        }
        Cursor cursor = db.rawQuery(sql, args);
        while (cursor.moveToNext()) {
            list.add(new NgonNgu(cursor.getString(0), cursor.getString(1)));
        }
        cursor.close();
        return list;
    }

    public List<NgonNgu> layTatCaNgonNgu() {
        return layDanhSach("");
    }

    public boolean themNgonNgu(String ma, String ten) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaNN", ma);
        values.put("TenNN", ten);
        return db.insert("ngonngu", null, values) > 0;
    }

    public boolean capNhatNgonNgu(String ma, String tenMoi) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenNN", tenMoi);
        return db.update("ngonngu", values, "MaNN = ?", new String[]{ma}) > 0;
    }

    public String taoMaNNMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaNN FROM ngonngu ORDER BY MaNN DESC LIMIT 1", null);
        String maMoi = "NN001";
        if (cursor.moveToFirst()) {
            String maCuoi = cursor.getString(0);
            if (maCuoi != null && maCuoi.length() > 2) {
                try {
                    int so = Integer.parseInt(maCuoi.substring(2));
                    so++;
                    maMoi = String.format("NN%03d", so);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        cursor.close();
        return maMoi;
    }

    public boolean xoaNgonNgu(String maNN) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("ngonngu", "MaNN = ?", new String[]{maNN}) > 0;
    }
}
