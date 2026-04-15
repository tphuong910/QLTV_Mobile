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

    // Lấy danh sách tất cả ngôn ngữ
    public List<NgonNgu> layTatCaNgonNgu() {
        List<NgonNgu> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ngonngu", null);
        while (cursor.moveToNext()) {
            list.add(new NgonNgu(cursor.getString(0), cursor.getString(1)));
        }
        cursor.close();
        return list;
    }

    // Thêm ngôn ngữ mới
    public boolean themNgonNgu(String ma, String ten) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaNN", ma);
        values.put("TenNN", ten);
        return db.insert("ngonngu", null, values) > 0;
    }

    // Cập nhật ngôn ngữ
    public boolean capNhatNgonNgu(String ma, String tenMoi) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenNN", tenMoi);
        return db.update("ngonngu", values, "MaNN = ?", new String[]{ma}) > 0;
    }

    // Hàm tự động tạo mã ngôn ngữ mới
    public String taoMaNNMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Lấy mã ngôn ngữ cuối cùng, sắp xếp giảm dần để lấy mã lớn nhất
        Cursor cursor = db.rawQuery("SELECT MaNN FROM ngonngu ORDER BY MaNN DESC LIMIT 1", null);

        String maMoi = "NN001"; // Mã mặc định nếu chưa có ngôn ngữ nào

        if (cursor.moveToFirst()) {
            String maCuoi = cursor.getString(0);
            if (maCuoi != null && maCuoi.length() > 2) {
                try {
                    // Cắt chữ NN ở đầu, lấy phần số phía sau và cộng thêm 1
                    int so = Integer.parseInt(maCuoi.substring(2));
                    so++;
                    // Định dạng lại thành chữ NN và 3 chữ số (VD: NN010)
                    maMoi = String.format("NN%03d", so);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        cursor.close();
        return maMoi;
    }
    // Xóa ngôn ngữ
    public boolean xoaNgonNgu(String maNN) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("ngonngu", "MaNN = ?", new String[]{maNN}) > 0;
    }
}
