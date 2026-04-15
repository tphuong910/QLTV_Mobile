package com.BTCK.qltv.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "qltv.db";
    // Giữ nguyên version 5 để không kích hoạt hàm onUpgrade làm mất dữ liệu thêm lần nữa
    private static final int DB_VERSION = 5; 

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Chỉ xóa bảng khi thực sự cần thiết (ví dụ thay đổi cấu trúc lớn)
        // Hiện tại ta giữ nguyên để bảo vệ dữ liệu người dùng nhập vào
        if (oldVersion < 5) {
            db.execSQL("DROP TABLE IF EXISTS phieudattruoc");
            db.execSQL("DROP TABLE IF EXISTS phieutra");
            db.execSQL("DROP TABLE IF EXISTS thethuvien");
            db.execSQL("DROP TABLE IF EXISTS chitietmuontra");
            db.execSQL("DROP TABLE IF EXISTS muontra");
            db.execSQL("DROP TABLE IF EXISTS sach");
            db.execSQL("DROP TABLE IF EXISTS docgia");
            db.execSQL("DROP TABLE IF EXISTS lop");
            db.execSQL("DROP TABLE IF EXISTS khoa");
            db.execSQL("DROP TABLE IF EXISTS ngonngu");
            db.execSQL("DROP TABLE IF EXISTS tacgia");
            db.execSQL("DROP TABLE IF EXISTS theloai");
            db.execSQL("DROP TABLE IF EXISTS kesach");
            db.execSQL("DROP TABLE IF EXISTS nhaxuatban");
            db.execSQL("DROP TABLE IF EXISTS nhanvien");
            onCreate(db);
        }
    }

    private void createTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS khoa (MaKhoa TEXT PRIMARY KEY, TenKhoa TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS lop (MaLop TEXT PRIMARY KEY, TenLop TEXT NOT NULL, MaKhoa TEXT, FOREIGN KEY(MaKhoa) REFERENCES khoa(MaKhoa))");
        db.execSQL("CREATE TABLE IF NOT EXISTS docgia (MaDG TEXT PRIMARY KEY, MaKhoa TEXT NOT NULL, MaLop TEXT NOT NULL, TenDG TEXT NOT NULL, NamSinh TEXT, GioiTinh TEXT NOT NULL, DiaChi TEXT NOT NULL, Email TEXT NOT NULL, Sdt TEXT NOT NULL, User TEXT, Pass TEXT, FOREIGN KEY(MaKhoa) REFERENCES khoa(MaKhoa), FOREIGN KEY(MaLop) REFERENCES lop(MaLop))");
        db.execSQL("CREATE TABLE IF NOT EXISTS kesach (MaViTri TEXT PRIMARY KEY, TenKe TEXT NOT NULL, MoTa TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS ngonngu (MaNN TEXT PRIMARY KEY, TenNN TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS tacgia (MaTG TEXT PRIMARY KEY, TenTG TEXT NOT NULL, NamSinh TEXT, GioiTinh TEXT NOT NULL, QuocTich TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS nhanvien (MaNV TEXT PRIMARY KEY, TenNV TEXT NOT NULL, QueQuan TEXT NOT NULL, GioiTinh TEXT NOT NULL, NamSinh TEXT NOT NULL, VaiTro TEXT NOT NULL, Email TEXT NOT NULL, Sdt TEXT NOT NULL, User TEXT NOT NULL UNIQUE, Pass TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS nhaxuatban (MaNXB TEXT PRIMARY KEY, TenNXB TEXT NOT NULL, DiaChi TEXT NOT NULL, Email TEXT NOT NULL, Sdt TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS theloai (MaTL TEXT PRIMARY KEY, TenTL TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS sach (MaSach TEXT PRIMARY KEY, MaTG TEXT NOT NULL, MaNXB TEXT NOT NULL, MaTL TEXT NOT NULL, TenSach TEXT NOT NULL, NamXB INTEGER NOT NULL, SoLuong INTEGER, MaNN TEXT NOT NULL, MaViTri TEXT NOT NULL, FOREIGN KEY(MaTG) REFERENCES tacgia(MaTG), FOREIGN KEY(MaNXB) REFERENCES nhaxuatban(MaNXB), FOREIGN KEY(MaTL) REFERENCES theloai(MaTL), FOREIGN KEY(MaNN) REFERENCES ngonngu(MaNN), FOREIGN KEY(MaViTri) REFERENCES kesach(MaViTri))");
        db.execSQL("CREATE TABLE IF NOT EXISTS muontra (MaMT TEXT PRIMARY KEY, MaDG TEXT NOT NULL, MaNV TEXT NOT NULL, NgayMuon TEXT, HanTra TEXT NOT NULL, TrangThai TEXT, FOREIGN KEY(MaDG) REFERENCES docgia(MaDG), FOREIGN KEY(MaNV) REFERENCES nhanvien(MaNV))");
        db.execSQL("CREATE TABLE IF NOT EXISTS chitietmuontra (MaMT TEXT NOT NULL, MaSach TEXT NOT NULL, SoLuong INTEGER NOT NULL, PRIMARY KEY(MaMT, MaSach), FOREIGN KEY(MaMT) REFERENCES muontra(MaMT), FOREIGN KEY(MaSach) REFERENCES sach(MaSach))");
        db.execSQL("CREATE TABLE IF NOT EXISTS phieudattruoc (MaPhieuDat TEXT PRIMARY KEY, MaDG TEXT NOT NULL, MaSach TEXT NOT NULL, NgayDat TEXT NOT NULL, TrangThai TEXT, NgayHetHan TEXT, FOREIGN KEY(MaDG) REFERENCES docgia(MaDG), FOREIGN KEY(MaSach) REFERENCES sach(MaSach))");
        db.execSQL("CREATE TABLE IF NOT EXISTS phieutra (MaPT TEXT PRIMARY KEY, MaMT TEXT NOT NULL, NgayTra TEXT, TinhTrang TEXT, FOREIGN KEY(MaMT) REFERENCES muontra(MaMT))");
        db.execSQL("CREATE TABLE IF NOT EXISTS thethuvien (MaThe TEXT PRIMARY KEY, MaDG TEXT NOT NULL, NgayCap TEXT NOT NULL, NgayHetHan TEXT NOT NULL, TrangThai TEXT NOT NULL, FOREIGN KEY(MaDG) REFERENCES docgia(MaDG))");
    }

    private void insertSampleData(SQLiteDatabase db) {
        db.execSQL("INSERT OR IGNORE INTO khoa (MaKhoa, TenKhoa) VALUES ('KH001', 'Công nghệ thông tin'), ('KH002', 'Cơ khí'), ('KH003', 'Ngoại ngữ')");
        db.execSQL("INSERT OR IGNORE INTO lop (MaLop, TenLop, MaKhoa) VALUES ('L001', '74DCHT21', 'KH001'), ('L002', '74DCHT22', 'KH001')");
        db.execSQL("INSERT OR IGNORE INTO docgia (MaDG, MaKhoa, MaLop, TenDG, NamSinh, GioiTinh, DiaChi, Email, Sdt, User, Pass) VALUES " +
            "('DG001', 'KH001', 'L001', 'Ngọc Bích', '2005', 'Nữ', 'Hà Nội', 'nb@gmail.com', '0393916176', 'kh1', '123')," +
            "('DG002', 'KH001', 'L001', 'Khách Hàng 2', '2004', 'Nam', 'Hà Nội', 'kh2@gmail.com', '0393916177', 'kh2', '123')");
        db.execSQL("INSERT OR IGNORE INTO ngonngu (MaNN, TenNN) VALUES ('NN001', 'Tiếng Việt'), ('NN002', 'Tiếng Anh')");
        db.execSQL("INSERT OR IGNORE INTO tacgia (MaTG, TenTG, NamSinh, GioiTinh, QuocTich) VALUES ('TG001', 'Nguyễn Nhật Ánh', '1955', 'Nam', 'Việt Nam'), ('TG002', 'Nam Cao', '1915', 'Nam', 'Việt Nam')");
        db.execSQL("INSERT OR IGNORE INTO nhaxuatban (MaNXB, TenNXB, DiaChi, Email, Sdt) VALUES ('NXB001', 'NXB Trẻ', 'TP.HCM', 'tre@nxb.vn', '0123456789'), ('NXB002', 'NXB Kim Đồng', 'Hà Nội', 'kd@nxb.vn', '0987654321')");
        db.execSQL("INSERT OR IGNORE INTO theloai (MaTL, TenTL) VALUES ('TL001', 'Văn học'), ('TL002', 'Giáo trình')");
        db.execSQL("INSERT OR IGNORE INTO kesach (MaViTri, TenKe, MoTa) VALUES ('VT001', 'Kệ A1', 'Tầng 1'), ('VT002', 'Kệ B1', 'Tầng 1')");
        db.execSQL("INSERT OR IGNORE INTO nhanvien (MaNV, TenNV, QueQuan, GioiTinh, NamSinh, VaiTro, Email, Sdt, User, Pass) VALUES ('NV001', 'Trần Hải', 'Hà Nội', 'Nam', '1999', 'Quản lý', 'hai@utt.edu.vn', '0987654321', 'nv1', '123')");
        db.execSQL("INSERT OR IGNORE INTO sach (MaSach, MaTG, MaNXB, MaTL, TenSach, NamXB, SoLuong, MaNN, MaViTri) VALUES " +
            "('S001', 'TG001', 'NXB001', 'TL001', 'Cho tôi xin một vé đi tuổi thơ', 2008, 10, 'NN001', 'VT001')," +
            "('S002', 'TG002', 'NXB002', 'TL001', 'Chí Phèo', 1941, 5, 'NN001', 'VT002')");
    }
}
