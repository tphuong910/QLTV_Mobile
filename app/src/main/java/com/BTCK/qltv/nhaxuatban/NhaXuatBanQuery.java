package com.BTCK.qltv.nhaxuatban;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

import com.BTCK.qltv.database.SQLiteHelper;

public class NhaXuatBanQuery {
    private final SQLiteHelper dbHelper;

    public NhaXuatBanQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    // 1. Lấy danh sách NXB
    public ArrayList<NhaXuatBan> layDanhSachNXB() {
        ArrayList<NhaXuatBan> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM nhaxuatban", null);

        if (cursor.moveToFirst()) {
            do {
                NhaXuatBan nxb = new NhaXuatBan();
                nxb.setMaNXB(cursor.getString(0));
                nxb.setTenNXB(cursor.getString(1));
                nxb.setDiaChi(cursor.getString(2));
                nxb.setEmail(cursor.getString(3));
                nxb.setSdt(cursor.getString(4));
                list.add(nxb);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // 2. Tự động sinh Mã NXB mới (Ví dụ: NXB017)
    public String taoMaNXBMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaNXB FROM nhaxuatban ORDER BY MaNXB DESC LIMIT 1", null);
        String maMoi = "NXB001";
        if (cursor.moveToFirst()) {
            String maCu = cursor.getString(0); // Lấy mã lớn nhất hiện tại, VD: NXB016
            int so = Integer.parseInt(maCu.substring(3)) + 1; // Cắt chữ NXB đi, lấy số cộng 1
            maMoi = String.format("NXB%03d", so); // Format lại thành chuỗi có 3 số
        }
        cursor.close();
        return maMoi;
    }

    // 3. Thêm NXB
    public boolean themNXB(NhaXuatBan nxb) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaNXB", nxb.getMaNXB());
        values.put("TenNXB", nxb.getTenNXB());
        values.put("DiaChi", nxb.getDiaChi());
        values.put("Email", nxb.getEmail());
        values.put("Sdt", nxb.getSdt());
        long result = db.insert("nhaxuatban", null, values);
        return result != -1; // Trả về true nếu thành công
    }

    // 4. Cập nhật NXB
    public boolean capNhatNXB(NhaXuatBan nxb) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenNXB", nxb.getTenNXB());
        values.put("DiaChi", nxb.getDiaChi());
        values.put("Email", nxb.getEmail());
        values.put("Sdt", nxb.getSdt());
        int result = db.update("nhaxuatban", values, "MaNXB = ?", new String[]{nxb.getMaNXB()});
        return result > 0;
    }

    // 5. Xóa NXB
    public boolean xoaNXB(String maNXB) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete("nhaxuatban", "MaNXB = ?", new String[]{maNXB});
        return result > 0;
    }
}