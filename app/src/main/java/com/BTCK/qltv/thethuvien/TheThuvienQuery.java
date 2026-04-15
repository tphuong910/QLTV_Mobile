package com.BTCK.qltv.thethuvien;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.BTCK.qltv.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.Locale;

// Lớp xử lý các thao tác CSDL liên quan đến thẻ thư viện
public class TheThuvienQuery {
    private final SQLiteHelper dbHelper;

    public TheThuvienQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    // Lấy danh sách thẻ thư viện, JOIN với bảng docgia để lấy tên, hỗ trợ tìm kiếm
    public ArrayList<TheThuvien> layDanhSach(String keyword) {
        ArrayList<TheThuvien> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT t.MaThe, t.MaDG, t.NgayCap, t.NgayHetHan, t.TrangThai, d.TenDG " +
                "FROM thethuvien t LEFT JOIN docgia d ON t.MaDG = d.MaDG";
        String[] args = null;
        if (keyword != null && !keyword.trim().isEmpty()) {
            query += " WHERE t.MaThe LIKE ? OR d.TenDG LIKE ? OR t.MaDG LIKE ?";
            args = new String[]{"%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%"};
        }
        query += " ORDER BY t.MaThe";
        Cursor cursor = db.rawQuery(query, args);
        while (cursor.moveToNext()) {
            TheThuvien the = new TheThuvien();
            the.setMaThe(cursor.getString(0));
            the.setMaDG(cursor.getString(1));
            the.setNgayCap(cursor.getString(2));
            the.setNgayHetHan(cursor.getString(3));
            the.setTrangThai(cursor.getString(4));
            the.setTenDG(cursor.getString(5));
            list.add(the);
        }
        cursor.close();
        return list;
    }

    // Tự sinh mã thẻ mới dạng TTV001, TTV002,... dựa theo mã cuối cùng trong DB
    public String taoMaTheMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaThe FROM thethuvien ORDER BY MaThe DESC LIMIT 1", null);
        String maMoi = "TTV001";
        if (cursor.moveToFirst()) {
            String maCuoi = cursor.getString(0);
            if (maCuoi != null && maCuoi.startsWith("TTV")) {
                try {
                    int so = Integer.parseInt(maCuoi.substring(3));
                    maMoi = String.format(Locale.getDefault(), "TTV%03d", so + 1);
                } catch (NumberFormatException ignored) {}
            }
        }
        cursor.close();
        return maMoi;
    }

    // Thêm thẻ thư viện mới vào CSDL
    public boolean themThe(TheThuvien the) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaThe", the.getMaThe());
        values.put("MaDG", the.getMaDG());
        values.put("NgayCap", the.getNgayCap());
        values.put("NgayHetHan", the.getNgayHetHan());
        values.put("TrangThai", the.getTrangThai());
        return db.insert("thethuvien", null, values) != -1;
    }

    // Cập nhật thông tin thẻ thư viện theo mã thẻ
    public boolean suaThe(TheThuvien the) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaDG", the.getMaDG());
        values.put("NgayCap", the.getNgayCap());
        values.put("NgayHetHan", the.getNgayHetHan());
        values.put("TrangThai", the.getTrangThai());
        return db.update("thethuvien", values, "MaThe=?", new String[]{the.getMaThe()}) > 0;
    }

    // Xóa thẻ thư viện theo mã
    public boolean xoaThe(String maThe) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("thethuvien", "MaThe=?", new String[]{maThe}) > 0;
    }

    // Lấy danh sách đọc giả (MaDG, TenDG) để đổ vào Spinner
    public ArrayList<String[]> layDanhSachDocGia() {
        ArrayList<String[]> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaDG, TenDG FROM docgia ORDER BY MaDG", null);
        while (cursor.moveToNext()) {
            list.add(new String[]{cursor.getString(0), cursor.getString(1)});
        }
        cursor.close();
        return list;
    }
}
