package com.BTCK.qltv.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.BTCK.qltv.database.SQLiteHelper;

public class TaiKhoanQuery {

    private final SQLiteHelper dbHelper;

    public TaiKhoanQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public static class UserInfo {
        public final String tenNhanVien;
        public final String vaiTro;

        public UserInfo(String tenNhanVien, String vaiTro) {
            this.tenNhanVien = tenNhanVien;
            this.vaiTro = vaiTro;
        }
    }

    public UserInfo dangNhap(String user, String pass) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT TenNV, VaiTro FROM nhanvien WHERE User = ? AND Pass = ? LIMIT 1",
                new String[]{user, pass}
        );

        UserInfo userInfo = null;
        if (cursor.moveToFirst()) {
            userInfo = new UserInfo(cursor.getString(0), cursor.getString(1));
        }

        cursor.close();
        return userInfo;
    }

    public boolean doiMatKhau(String user, String email, String passMoi) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Pass", passMoi);

        int row = db.update(
                "nhanvien",
                values,
                "User = ? AND Email = ?",
                new String[]{user, email}
        );
        return row > 0;
    }
}
