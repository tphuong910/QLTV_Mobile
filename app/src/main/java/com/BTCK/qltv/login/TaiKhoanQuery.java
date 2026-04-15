package com.BTCK.qltv.login;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.BTCK.qltv.database.SQLiteHelper;

public class TaiKhoanQuery {

    private final SQLiteHelper dbHelper;

    public TaiKhoanQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public static class UserInfo {
        public final String id;
        public final String ten;
        public final String vaiTro; // "Staff" hoặc "Khách hàng"

        public UserInfo(String id, String ten, String vaiTro) {
            this.id = id;
            this.ten = ten;
            this.vaiTro = vaiTro;
        }
    }

    public UserInfo dangNhap(String user, String pass) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        // 1. Kiểm tra trong bảng nhanvien (Admin/Staff)
        Cursor cursorNV = db.rawQuery(
                "SELECT MaNV, TenNV, VaiTro FROM nhanvien WHERE User = ? AND Pass = ?",
                new String[]{user, pass}
        );

        if (cursorNV.moveToFirst()) {
            UserInfo userInfo = new UserInfo(
                cursorNV.getString(0), 
                cursorNV.getString(1), 
                cursorNV.getString(2)
            );
            cursorNV.close();
            return userInfo;
        }
        cursorNV.close();

        // 2. Kiểm tra trong bảng docgia (Khách hàng)
        // Lưu ý: Tên cột trong DB là User và Pass (phân biệt hoa thường trong một số trường hợp)
        Cursor cursorDG = db.rawQuery(
                "SELECT MaDG, TenDG FROM docgia WHERE User = ? AND Pass = ?",
                new String[]{user, pass}
        );

        if (cursorDG.moveToFirst()) {
            UserInfo userInfo = new UserInfo(
                cursorDG.getString(0), 
                cursorDG.getString(1), 
                "Khách hàng"
            );
            cursorDG.close();
            return userInfo;
        }
        cursorDG.close();

        return null;
    }

    public boolean doiMatKhau(String user, String email, String passMoi) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            // Cập nhật cho nhân viên
            db.execSQL("UPDATE nhanvien SET Pass = ? WHERE User = ? AND Email = ?", new Object[]{passMoi, user, email});
            // Cập nhật cho độc giả (khách hàng)
            db.execSQL("UPDATE docgia SET Pass = ? WHERE User = ? AND Email = ?", new Object[]{passMoi, user, email});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
