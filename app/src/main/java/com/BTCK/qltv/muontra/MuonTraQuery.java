package com.BTCK.qltv.muontra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.BTCK.qltv.database.SQLiteHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

// Lớp xử lý các thao tác CSDL liên quan đến mượn trả sách
public class MuonTraQuery {
    private final SQLiteHelper dbHelper;

    public MuonTraQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    // Lấy danh sách phiếu mượn, JOIN với docgia để lấy tên, hỗ trợ tìm kiếm
    public ArrayList<MuonTra> layDanhSach(String keyword) {
        ArrayList<MuonTra> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT mt.MaMT, mt.MaDG, mt.MaNV, mt.NgayMuon, mt.HanTra, mt.TrangThai, d.TenDG " +
                "FROM muontra mt LEFT JOIN docgia d ON mt.MaDG = d.MaDG";
        String[] args = null;
        if (keyword != null && !keyword.trim().isEmpty()) {
            query += " WHERE mt.MaMT LIKE ? OR d.TenDG LIKE ? OR mt.MaDG LIKE ?";
            args = new String[]{"%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%"};
        }
        query += " ORDER BY mt.MaMT DESC";
        Cursor cursor = db.rawQuery(query, args);
        while (cursor.moveToNext()) {
            MuonTra mt = new MuonTra();
            mt.setMaMT(cursor.getString(0));
            mt.setMaDG(cursor.getString(1));
            mt.setMaNV(cursor.getString(2));
            mt.setNgayMuon(cursor.getString(3));
            mt.setHanTra(cursor.getString(4));
            mt.setTrangThai(cursor.getString(5));
            mt.setTenDG(cursor.getString(6));
            list.add(mt);
        }
        cursor.close();
        return list;
    }

    // Lấy danh sách mượn trả của 1 khách hàng cụ thể
    public ArrayList<MuonTra> layDanhSachTheoKhachHang(String maDG) {
        ArrayList<MuonTra> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT mt.MaMT, mt.MaDG, mt.MaNV, mt.NgayMuon, mt.HanTra, mt.TrangThai, d.TenDG " +
                "FROM muontra mt LEFT JOIN docgia d ON mt.MaDG = d.MaDG WHERE mt.MaDG = ? ORDER BY mt.NgayMuon DESC";
        Cursor cursor = db.rawQuery(query, new String[]{maDG});
        while (cursor.moveToNext()) {
            MuonTra mt = new MuonTra();
            mt.setMaMT(cursor.getString(0));
            mt.setMaDG(cursor.getString(1));
            mt.setMaNV(cursor.getString(2));
            mt.setNgayMuon(cursor.getString(3));
            mt.setHanTra(cursor.getString(4));
            mt.setTrangThai(cursor.getString(5));
            mt.setTenDG(cursor.getString(6));
            list.add(mt);
        }
        cursor.close();
        return list;
    }

    // Tự sinh mã phiếu mượn dạng MT01, MT02,... dựa theo mã cuối trong DB
    public String taoMaMuonTraMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaMT FROM muontra ORDER BY MaMT DESC LIMIT 1", null);
        String maMoi = "MT01";
        if (cursor.moveToFirst()) {
            String maCuoi = cursor.getString(0);
            if (maCuoi != null && maCuoi.startsWith("MT")) {
                try {
                    int so = Integer.parseInt(maCuoi.substring(2));
                    maMoi = String.format(Locale.getDefault(), "MT%02d", so + 1);
                } catch (NumberFormatException ignored) {}
            }
        }
        cursor.close();
        return maMoi;
    }

    // Tự sinh mã phiếu trả dạng PT01, PT02,...
    private String taoMaPhieuTraMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaPT FROM phieutra ORDER BY MaPT DESC LIMIT 1", null);
        String maMoi = "PT01";
        if (cursor.moveToFirst()) {
            String maCuoi = cursor.getString(0);
            if (maCuoi != null && maCuoi.startsWith("PT")) {
                try {
                    int so = Integer.parseInt(maCuoi.substring(2));
                    maMoi = String.format(Locale.getDefault(), "PT%02d", so + 1);
                } catch (NumberFormatException ignored) {}
            }
        }
        cursor.close();
        return maMoi;
    }

    // Lấy danh sách đọc giả có thẻ thư viện còn hiệu lực và chưa hết hạn
    public ArrayList<String[]> layDocGiaCoTheHopLe() {
        ArrayList<String[]> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Cursor cursor = db.rawQuery(
                "SELECT DISTINCT d.MaDG, d.TenDG FROM docgia d " +
                        "INNER JOIN thethuvien t ON d.MaDG = t.MaDG " +
                        "WHERE t.TrangThai = 'Còn hiệu lực' AND t.NgayHetHan >= ?",
                new String[]{today});
        while (cursor.moveToNext()) {
            list.add(new String[]{cursor.getString(0), cursor.getString(1)});
        }
        cursor.close();
        return list;
    }

    // Lấy danh sách sách còn tồn kho (số lượng > 0)
    public ArrayList<String[]> layDanhSachSachConTon() {
        ArrayList<String[]> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT MaSach, TenSach, SoLuong FROM sach WHERE SoLuong > 0 ORDER BY MaSach", null);
        while (cursor.moveToNext()) {
            list.add(new String[]{cursor.getString(0), cursor.getString(1), cursor.getString(2)});
        }
        cursor.close();
        return list;
    }

    // Tạo phiếu mượn + chi tiết mượn + trừ số lượng sách tồn kho (dùng transaction)
    public boolean themMuonTra(MuonTra mt, String maSach, int soLuong) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            // Kiểm tra số lượng sách tồn kho
            Cursor cursor = db.rawQuery("SELECT SoLuong FROM sach WHERE MaSach = ?", new String[]{maSach});
            if (!cursor.moveToFirst()) {
                cursor.close();
                return false;
            }
            int soLuongTon = cursor.getInt(0);
            cursor.close();
            if (soLuongTon < soLuong) {
                return false;
            }

            // Thêm phiếu mượn
            ContentValues valuesMT = new ContentValues();
            valuesMT.put("MaMT", mt.getMaMT());
            valuesMT.put("MaDG", mt.getMaDG());
            valuesMT.put("MaNV", mt.getMaNV());
            valuesMT.put("NgayMuon", mt.getNgayMuon());
            valuesMT.put("HanTra", mt.getHanTra());
            valuesMT.put("TrangThai", mt.getTrangThai());
            if (db.insert("muontra", null, valuesMT) == -1) {
                return false;
            }

            // Thêm chi tiết mượn (sách nào, số lượng bao nhiêu)
            ContentValues valuesCT = new ContentValues();
            valuesCT.put("MaMT", mt.getMaMT());
            valuesCT.put("MaSach", maSach);
            valuesCT.put("SoLuong", soLuong);
            if (db.insert("chitietmuontra", null, valuesCT) == -1) {
                return false;
            }

            // Trừ số lượng sách đã cho mượn
            db.execSQL("UPDATE sach SET SoLuong = SoLuong - ? WHERE MaSach = ?",
                    new Object[]{soLuong, maSach});

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }
    }

    // Trả sách: cộng lại số lượng vào kho, cập nhật trạng thái và tạo phiếu trả
    public boolean traSach(String maMT, String tinhTrang) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            // Lấy chi tiết mượn để cộng lại số lượng vào kho
            Cursor cursor = db.rawQuery(
                    "SELECT MaSach, SoLuong FROM chitietmuontra WHERE MaMT = ?",
                    new String[]{maMT});
            while (cursor.moveToNext()) {
                String maSach = cursor.getString(0);
                int soLuong = cursor.getInt(1);
                db.execSQL("UPDATE sach SET SoLuong = SoLuong + ? WHERE MaSach = ?",
                        new Object[]{soLuong, maSach});
            }
            cursor.close();

            // Cập nhật trạng thái phiếu mượn thành "Đã trả"
            ContentValues values = new ContentValues();
            values.put("TrangThai", "Đã trả");
            db.update("muontra", values, "MaMT=?", new String[]{maMT});

            // Tạo phiếu trả
            String maPT = taoMaPhieuTraMoi();
            String ngayTra = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            ContentValues valuesPT = new ContentValues();
            valuesPT.put("MaPT", maPT);
            valuesPT.put("MaMT", maMT);
            valuesPT.put("NgayTra", ngayTra);
            valuesPT.put("TinhTrang", tinhTrang);
            db.insert("phieutra", null, valuesPT);

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }
    }

    // Xóa phiếu mượn và các bản ghi liên quan (phiếu trả, chi tiết mượn)
    public boolean xoaMuonTra(String maMT) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            // Xóa phiếu trả nếu có
            db.delete("phieutra", "MaMT=?", new String[]{maMT});
            // Xóa chi tiết mượn
            db.delete("chitietmuontra", "MaMT=?", new String[]{maMT});
            // Xóa phiếu mượn
            db.delete("muontra", "MaMT=?", new String[]{maMT});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }
    }

    // Lấy chi tiết sách đã mượn trong phiếu để hiển thị
    public String layChiTietMuonTra(String maMT) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        StringBuilder sb = new StringBuilder();
        Cursor cursor = db.rawQuery(
                "SELECT s.TenSach, ct.SoLuong FROM chitietmuontra ct " +
                        "LEFT JOIN sach s ON ct.MaSach = s.MaSach WHERE ct.MaMT = ?",
                new String[]{maMT});
        while (cursor.moveToNext()) {
            if (sb.length() > 0) sb.append("\n");
            sb.append("- ").append(cursor.getString(0)).append(" (x").append(cursor.getInt(1)).append(")");
        }
        cursor.close();
        return sb.toString();
    }
}
