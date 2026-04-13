package com.BTCK.qltv.dashboard;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.BTCK.qltv.database.SQLiteHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DashboardQuery {

    private final SQLiteHelper dbHelper;

    public DashboardQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public int layTongSach() {
        return laySoLuong("SELECT COUNT(*) FROM sach");
    }

    public int layTongTheLoai() {
        return laySoLuong("SELECT COUNT(*) FROM theloai");
    }

    public int laySachDangMuon() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT MaMT FROM muontra WHERE TrangThai = ?",
                new String[]{"Chưa trả"}
        );

        int tong = 0;
        while (cursor.moveToNext()) {
            String maMT = cursor.getString(0);
            tong += laySoLuongTrongPhieu(db, maMT);
        }

        cursor.close();
        return tong;
    }

    public int laySachQuaHan() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT MaMT, HanTra FROM muontra WHERE TrangThai = ?",
                new String[]{"Chưa trả"}
        );

        int tongQuaHan = 0;
        Date ngayHienTai = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        while (cursor.moveToNext()) {
            String maMT = cursor.getString(0);
            String hanTra = cursor.getString(1);

            if (hanTra == null) {
                continue;
            }

            try {
                Date ngayHan = sdf.parse(hanTra);
                if (ngayHan != null && ngayHan.before(ngayHienTai)) {
                    tongQuaHan += laySoLuongTrongPhieu(db, maMT);
                }
            } catch (ParseException ignored) {
            }
        }

        cursor.close();
        return tongQuaHan;
    }

    private int laySoLuong(String sql) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        int tong = 0;
        if (cursor.moveToFirst()) {
            tong = cursor.getInt(0);
        }

        cursor.close();
        return tong;
    }

    private int laySoLuongTrongPhieu(SQLiteDatabase db, String maMT) {
        Cursor cursor = db.rawQuery(
                "SELECT SUM(SoLuong) FROM chitietmuontra WHERE MaMT = ?",
                new String[]{maMT}
        );

        int soLuong = 0;
        if (cursor.moveToFirst()) {
            soLuong = cursor.isNull(0) ? 0 : cursor.getInt(0);
        }

        cursor.close();

        if (soLuong <= 0) {
            return 1;
        }
        return soLuong;
    }
}
