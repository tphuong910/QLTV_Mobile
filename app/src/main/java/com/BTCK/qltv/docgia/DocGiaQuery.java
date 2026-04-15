package com.BTCK.qltv.docgia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

import com.BTCK.qltv.database.SQLiteHelper;

public class DocGiaQuery {
    private final SQLiteHelper dbHelper;

    public DocGiaQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    // 1. Lấy danh sách Độc giả
    public ArrayList<DocGia> layDanhSachDocGia() {
        ArrayList<DocGia> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM docgia", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new DocGia(
                        cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // 2. Tạo Mã Độc giả tự động (VD: DG008)
    public String taoMaDGMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaDG FROM docgia ORDER BY MaDG DESC LIMIT 1", null);
        String maMoi = "DG001";
        if (cursor.moveToFirst()) {
            String maCu = cursor.getString(0);
            try {
                int so = Integer.parseInt(maCu.substring(2)) + 1;
                maMoi = String.format("DG%03d", so);
            } catch (Exception e) { maMoi = "DG999"; }
        }
        cursor.close();
        return maMoi;
    }

    // 3. Thêm / Sửa / Xóa Độc giả
    public boolean themDocGia(DocGia dg) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaDG", dg.getMaDG());
        values.put("MaKhoa", dg.getMaKhoa());
        values.put("MaLop", dg.getMaLop());
        values.put("TenDG", dg.getTenDG());
        values.put("NamSinh", dg.getNamSinh());
        values.put("GioiTinh", dg.getGioiTinh());
        values.put("DiaChi", dg.getDiaChi());
        values.put("Email", dg.getEmail());
        values.put("Sdt", dg.getSdt());
        return db.insert("docgia", null, values) != -1;
    }

    public boolean capNhatDocGia(DocGia dg) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaKhoa", dg.getMaKhoa());
        values.put("MaLop", dg.getMaLop());
        values.put("TenDG", dg.getTenDG());
        values.put("NamSinh", dg.getNamSinh());
        values.put("GioiTinh", dg.getGioiTinh());
        values.put("DiaChi", dg.getDiaChi());
        values.put("Email", dg.getEmail());
        values.put("Sdt", dg.getSdt());
        return db.update("docgia", values, "MaDG = ?", new String[]{dg.getMaDG()}) > 0;
    }

    public boolean xoaDocGia(String maDG) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("docgia", "MaDG = ?", new String[]{maDG}) > 0;
    }

    // --- CÁC HÀM HỖ TRỢ ĐỔ DỮ LIỆU LÊN SPINNER ---
    public List<String> layDanhSachKhoaSpinner() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT MaKhoa, TenKhoa FROM khoa", null);
        while (c.moveToNext()) {
            list.add(c.getString(0) + " - " + c.getString(1)); // Dạng: KH001 - Công nghệ thông tin
        }
        c.close();
        return list;
    }

    public List<String> layDanhSachLopSpinner(String maKhoa) {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Lấy lớp theo đúng mã khoa đã chọn
        Cursor c = db.rawQuery("SELECT MaLop, TenLop FROM lop WHERE MaKhoa=?", new String[]{maKhoa});
        while (c.moveToNext()) {
            list.add(c.getString(0) + " - " + c.getString(1)); // Dạng: L001 - 74DCHT21
        }
        c.close();
        return list;
    }
}