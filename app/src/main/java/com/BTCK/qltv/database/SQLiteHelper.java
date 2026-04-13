package com.BTCK.qltv.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "qltv.db";
    private static final int DB_VERSION = 2;

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

    private void createTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS khoa (" +
            "MaKhoa TEXT PRIMARY KEY," +
            "TenKhoa TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS lop (" +
            "MaLop TEXT PRIMARY KEY," +
            "TenLop TEXT NOT NULL," +
            "MaKhoa TEXT," +
            "FOREIGN KEY(MaKhoa) REFERENCES khoa(MaKhoa))");

        db.execSQL("CREATE TABLE IF NOT EXISTS docgia (" +
            "MaDG TEXT PRIMARY KEY," +
            "MaKhoa TEXT NOT NULL," +
            "MaLop TEXT NOT NULL," +
            "TenDG TEXT NOT NULL," +
            "NamSinh TEXT," +
            "GioiTinh TEXT NOT NULL," +
            "DiaChi TEXT NOT NULL," +
            "Email TEXT NOT NULL," +
            "Sdt TEXT NOT NULL," +
            "FOREIGN KEY(MaKhoa) REFERENCES khoa(MaKhoa)," +
            "FOREIGN KEY(MaLop) REFERENCES lop(MaLop))");

        db.execSQL("CREATE TABLE IF NOT EXISTS kesach (" +
            "MaViTri TEXT PRIMARY KEY," +
            "TenKe TEXT NOT NULL," +
            "MoTa TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS ngonngu (" +
            "MaNN TEXT PRIMARY KEY," +
            "TenNN TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS tacgia (" +
            "MaTG TEXT PRIMARY KEY," +
            "TenTG TEXT NOT NULL," +
            "NamSinh TEXT," +
            "GioiTinh TEXT NOT NULL," +
            "QuocTich TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS nhanvien (" +
                "MaNV TEXT PRIMARY KEY," +
                "TenNV TEXT NOT NULL," +
            "QueQuan TEXT NOT NULL," +
            "GioiTinh TEXT NOT NULL," +
            "NamSinh TEXT NOT NULL," +
                "VaiTro TEXT NOT NULL," +
                "Email TEXT NOT NULL," +
            "Sdt TEXT NOT NULL," +
                "User TEXT NOT NULL UNIQUE," +
                "Pass TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS nhaxuatban (" +
            "MaNXB TEXT PRIMARY KEY," +
            "TenNXB TEXT NOT NULL," +
            "DiaChi TEXT NOT NULL," +
            "Email TEXT NOT NULL," +
            "Sdt TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS theloai (" +
                "MaTL TEXT PRIMARY KEY," +
                "TenTL TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS sach (" +
                "MaSach TEXT PRIMARY KEY," +
                "MaTG TEXT NOT NULL," +
                "MaNXB TEXT NOT NULL," +
                "MaTL TEXT NOT NULL," +
                "TenSach TEXT NOT NULL," +
                "NamXB INTEGER NOT NULL," +
            "SoLuong INTEGER," +
                "MaNN TEXT NOT NULL," +
            "MaViTri TEXT NOT NULL," +
            "FOREIGN KEY(MaTG) REFERENCES tacgia(MaTG)," +
            "FOREIGN KEY(MaNXB) REFERENCES nhaxuatban(MaNXB)," +
            "FOREIGN KEY(MaTL) REFERENCES theloai(MaTL)," +
            "FOREIGN KEY(MaNN) REFERENCES ngonngu(MaNN)," +
            "FOREIGN KEY(MaViTri) REFERENCES kesach(MaViTri))");

        db.execSQL("CREATE TABLE IF NOT EXISTS muontra (" +
                "MaMT TEXT PRIMARY KEY," +
                "MaDG TEXT NOT NULL," +
                "MaNV TEXT NOT NULL," +
                "NgayMuon TEXT," +
                "HanTra TEXT NOT NULL," +
            "TrangThai TEXT," +
            "FOREIGN KEY(MaDG) REFERENCES docgia(MaDG)," +
            "FOREIGN KEY(MaNV) REFERENCES nhanvien(MaNV))");

        db.execSQL("CREATE TABLE IF NOT EXISTS chitietmuontra (" +
                "MaMT TEXT NOT NULL," +
                "MaSach TEXT NOT NULL," +
            "SoLuong INTEGER NOT NULL," +
            "PRIMARY KEY(MaMT, MaSach)," +
            "FOREIGN KEY(MaMT) REFERENCES muontra(MaMT)," +
            "FOREIGN KEY(MaSach) REFERENCES sach(MaSach))");

        db.execSQL("CREATE TABLE IF NOT EXISTS phieudattruoc (" +
            "MaPhieuDat TEXT PRIMARY KEY," +
            "MaDG TEXT NOT NULL," +
            "MaSach TEXT NOT NULL," +
            "NgayDat TEXT NOT NULL," +
            "TrangThai TEXT," +
            "NgayHetHan TEXT," +
            "FOREIGN KEY(MaDG) REFERENCES docgia(MaDG)," +
            "FOREIGN KEY(MaSach) REFERENCES sach(MaSach))");

        db.execSQL("CREATE TABLE IF NOT EXISTS phieutra (" +
            "MaPT TEXT PRIMARY KEY," +
            "MaMT TEXT NOT NULL," +
            "NgayTra TEXT," +
            "TinhTrang TEXT," +
            "FOREIGN KEY(MaMT) REFERENCES muontra(MaMT))");

        db.execSQL("CREATE TABLE IF NOT EXISTS thethuvien (" +
            "MaThe TEXT PRIMARY KEY," +
            "MaDG TEXT NOT NULL," +
            "NgayCap TEXT NOT NULL," +
            "NgayHetHan TEXT NOT NULL," +
            "TrangThai TEXT NOT NULL," +
            "FOREIGN KEY(MaDG) REFERENCES docgia(MaDG))");
    }

    private void insertSampleData(SQLiteDatabase db) {
        db.execSQL("INSERT OR IGNORE INTO khoa (MaKhoa, TenKhoa) VALUES " +
            "('KH001', 'Công nghệ thông tin')," +
            "('KH002', 'Cơ khí động lực')," +
            "('KH003', 'Ngôn ngữ Anh')," +
            "('KH004', 'Công trình')," +
            "('KH005', 'Điện tử viễn thông')," +
            "('KH006', 'Kinh tế vận tải')," +
            "('KH007', 'Luật')," +
            "('KH008', 'Logistics & Chuỗi cung ứng')," +
            "('KH009', 'Tự động hóa')," +
            "('KH010', 'Khoa học ứng dụng')," +
            "('KH011', 'Quản trị kinh doanh')," +
            "('KH012', 'Tài chính – Ngân hàng')");

        db.execSQL("INSERT OR IGNORE INTO lop (MaLop, TenLop, MaKhoa) VALUES " +
            "('L001', '74DCHT21', 'KH001')," +
            "('L002', '74DCHT22', 'KH001')," +
            "('L003', '74DCHT23', 'KH001')," +
            "('L004', '73DCHT21', 'KH001')," +
            "('L005', '73DCHT22', 'KH001')," +
            "('L006', '73DCHT23', 'KH001')," +
            "('L007', '74DCHT24', 'KH001')," +
            "('L008', '74DCHT25', 'KH001')," +
            "('L009', '74DCHT26', 'KH001')," +
            "('L010', '73DCTT21', 'KH001')," +
            "('L011', '73DCTT22', 'KH001')," +
            "('L012', '74DCTT24', 'KH001')," +
            "('L014', '73DDTVT21', 'KH005')," +
            "('L015', '73DDTVT22', 'KH005')," +
            "('L016', '74DDTVT24', 'KH005')," +
            "('L017', '74DDTVT25', 'KH005')," +
            "('L018', '73DCCT21', 'KH004')," +
            "('L019', '73DCCT22', 'KH004')," +
            "('L020', '74DCCT24', 'KH004')," +
            "('L021', '74DCCT25', 'KH004')," +
            "('L022', '73DCKDL01', 'KH002')," +
            "('L023', '73DCKDL02', 'KH002')," +
            "('L024', '74DCKDL01', 'KH002')," +
            "('L025', '74DCKDL02', 'KH002')," +
            "('L026', '74DCNN21', 'KH003')," +
            "('L027', '73DCNN21', 'KH003')," +
            "('L028', '73DCNN22', 'KH003')," +
            "('L029', '73DCLG02', 'KH003')," +
            "('L030', '73DCL01', 'KH008')," +
            "('L031', '73DCL02', 'KH008')," +
            "('L032', '74DCL01', 'KH008')," +
            "('L033', '74DCL02', 'KH008')," +
            "('L034', '73DKTVT01', 'KH006')," +
            "('L035', '73DKTVT02', 'KH006')," +
            "('L036', '74DKTVT01', 'KH006')," +
            "('L037', '74DKTVT02', 'KH006')");

        db.execSQL("INSERT OR IGNORE INTO docgia (MaDG, MaKhoa, MaLop, TenDG, NamSinh, GioiTinh, DiaChi, Email, Sdt) VALUES " +
            "('DG001', 'KH001', 'L004', 'Ngọc Bích', '2005', 'Nữ', 'Hà Nội', 'nb@gmail.com', '0393916176')," +
            "('DG002', 'KH001', 'L001', 'Nguyễn Ngọc Bích', '2004', 'Nữ', 'Hà Nội', 'bichnn02@gmail.com', '0393916177')," +
            "('DG003', 'KH001', 'L001', 'Nguyễn Ngọc Linh', '2004', 'Nữ', 'Hà Nội', 'linhnn05@gmail.com', '0367123456')," +
            "('DG004', 'KH001', 'L002', 'Lưu Đức Anh Dũng', '2003', 'Nam', 'Hà Nội', 'dunglda06@gmail.com', '0356123456')," +
            "('DG005', 'KH001', 'L003', 'Đinh Hoàng Đức', '2003', 'Nam', 'Hà Nội', 'ducdh07@gmail.com', '0345123456')," +
            "('DG006', 'KH001', 'L001', 'Nguyễn Nam Khánh', '2003', 'Nam', 'Hà Nội', 'khanhnn08@gmail.com', '0334123456')," +
            "('DG007', 'KH001', 'L005', '123', '2005', 'Nam', '213', '22@gmail.com', '0945088052')");

        db.execSQL("INSERT OR IGNORE INTO kesach (MaViTri, TenKe, MoTa) VALUES " +
            "('KS001', 'Kệ CNTT', 'Sách Công nghệ thông tin')," +
            "('KS002', 'Kệ CKĐL', 'Sách Cơ khí động lực')," +
            "('KS003', 'Kệ CT', 'Sách Công trình')," +
            "('KS004', 'Kệ KTVT', 'Sách Kinh tế vận tải')," +
            "('KS005', 'Kệ KHUD & LCT', 'Luật – Chính trị – Khoa học ứng dụng')," +
            "('KS006', 'Kệ Văn học', 'Sách văn học')," +
            "('KS007', 'Kệ Quốc tế', 'Sách học thuật nước ngoài')");

        db.execSQL("INSERT OR IGNORE INTO ngonngu (MaNN, TenNN) VALUES " +
            "('NN001', 'Tiếng Việt')," +
            "('NN002', 'Tiếng Anh')," +
            "('NN003', 'Tiếng Pháp')," +
            "('NN004', 'Tiếng Trung')," +
            "('NN005', 'Tiếng Nhật')," +
            "('NN006', 'Tiếng Hàn')," +
            "('NN007', 'Tiếng Nga')," +
            "('NN008', 'Tiếng Đức')," +
            "('NN009', 'Tiếng Tây Ban Nha')");

        db.execSQL("INSERT OR IGNORE INTO nhanvien (MaNV, TenNV, QueQuan, GioiTinh, NamSinh, VaiTro, Email, Sdt, User, Pass) VALUES " +
            "('NV001', 'Trần Hải', 'Hà Nội', 'Nam', '1999', 'Quản lý', 'hai.tran@utt.edu.vn', '0987654321', 'nv1', '123')," +
            "('NV002', 'Hà Phương', 'Hải Phòng', 'Nữ', '2000', 'Thủ thư', 'haphuong@gmail.com', '0123456789', 'nv2', '1234')," +
            "('NV003', '1', '1', 'Nam', '2005', 'Quản lý', 'ad@gmail.com', '0945088056', 'khanhs', '1234')," +
            "('NV004', 'aaa', 'aaa', 'Nữ', '2005', 'Thủ thư', '1221@gmail.com', '0123456788', '123', '123456')");

        db.execSQL("INSERT OR IGNORE INTO nhaxuatban (MaNXB, TenNXB, DiaChi, Email, Sdt) VALUES " +
            "('NXB001', 'NXB Giáo Dục Việt Nam', 'Hà Nội', 'giaoduc@nxb.vn', '02438220801')," +
            "('NXB002', 'NXB Trẻ', 'TP. Hồ Chí Minh', 'nxbtre@nxb.vn', '02838291145')," +
            "('NXB003', 'NXB Kim Đồng', 'Hà Nội', 'kimdong@nxb.vn', '02439432856')," +
            "('NXB004', 'NXB Lao Động', 'Hà Nội', 'laodong@nxb.vn', '02437336888')," +
            "('NXB005', 'NXB Văn Học', 'Hà Nội', 'vanhoc@nxb.vn', '02439422123')," +
            "('NXB006', 'NXB Khoa Học & Kỹ Thuật', 'Hà Nội', 'khkt@nxb.vn', '02438692222')," +
            "('NXB007', 'NXB Chính Trị Quốc Gia', 'Hà Nội', 'chinhtri@nxb.vn', '02437423333')," +
            "('NXB008', 'NXB Thống Kê', 'Hà Nội', 'thongke@nxb.vn', '02437324444')," +
            "('NXB009', 'NXB Tổng Hợp TP.HCM', 'TP. Hồ Chí Minh', 'tonghop@nxb.vn', '02838339999')," +
            "('NXB010', 'NXB Phụ Nữ', 'Hà Nội', 'phunu@nxb.vn', '02439281111')," +
            "('NXB011', 'NXB Công Thương', 'Hà Nội', 'congthuong@nxb.vn', '02438336666')," +
            "('NXB012', 'NXB Đại Học Quốc Gia Hà Nội', 'Hà Nội', 'dhqghn@nxb.vn', '02437548888')," +
            "('NXB013', 'NXB Đại Học Quốc Gia TP.HCM', 'TP. Hồ Chí Minh', 'dhqgtphcm@nxb.vn', '02837221111')," +
            "('NXB014', 'NXB Xây Dựng', 'Hà Nội', 'xaydung@nxb.vn', '02438775555')," +
            "('NXB015', 'NXB Giao Thông Vận Tải', 'Hà Nội', 'gtvt@nxb.vn', '02439429999')," +
            "('NXB016', 'aaaa', 'aaaa', 'k', '43')");

        db.execSQL("INSERT OR IGNORE INTO tacgia (MaTG, TenTG, NamSinh, GioiTinh, QuocTich) VALUES " +
            "('TG001', 'Nguyễn Nhật Ánh', '1955', 'Nam', 'Việt Nam')," +
            "('TG002', 'Nam Cao', '1915', 'Nam', 'Việt Nam')," +
            "('TG003', 'Tô Hoài', '1920', 'Nam', 'Việt Nam')," +
            "('TG004', 'Xuân Diệu', '1916', 'Nam', 'Việt Nam')," +
            "('TG005', 'Nguyễn Du', '1765', 'Nam', 'Việt Nam')," +
            "('TG006', 'J.K. Rowling', '1965', 'Nữ', 'Anh')," +
            "('TG007', 'Haruki Murakami', '1949', 'Nam', 'Nhật Bản')," +
            "('TG008', 'Paulo Coelho', '1947', 'Nam', 'Brazil')," +
            "('TG009', 'Dan Brown', '1964', 'Nam', 'Mỹ')," +
            "('TG010', 'Agatha Christie', '1890', 'Nữ', 'Anh')," +
            "('TG011', 'Arthur Conan Doyle', '1859', 'Nam', 'Anh')," +
            "('TG012', 'Ernest Hemingway', '1899', 'Nam', 'Mỹ')," +
            "('TG013', 'Victor Hugo', '1802', 'Nam', 'Pháp')," +
            "('TG014', 'Lev Tolstoy', '1828', 'Nam', 'Nga')");

        db.execSQL("INSERT OR IGNORE INTO theloai (MaTL, TenTL) VALUES " +
            "('TL001', 'Văn học Việt Nam')," +
            "('TL002', 'Văn học nước ngoài')," +
            "('TL003', 'Thiếu nhi')," +
            "('TL004', 'Giáo trình')," +
            "('TL005', 'Khoa học')," +
            "('TL006', 'Công nghệ thông tin')," +
            "('TL007', 'Kinh tế')," +
            "('TL008', 'Quản trị kinh doanh')," +
            "('TL009', 'Marketing')," +
            "('TL010', 'Tài chính – Ngân hàng')," +
            "('TL011', 'Luật')," +
            "('TL012', 'Y học')," +
            "('TL013', 'Tâm lý học')," +
            "('TL014', 'Kỹ năng sống')," +
            "('TL015', 'Ngoại ngữ')," +
            "('TL016', 'Lịch sử')," +
            "('TL017', 'Địa lý')," +
            "('TL018', 'Triết học')," +
            "('TL019', 'Văn hóa – Xã hội')");

        db.execSQL("INSERT OR IGNORE INTO sach (MaSach, MaTG, MaNXB, MaTL, TenSach, NamXB, SoLuong, MaNN, MaViTri) VALUES " +
            "('S001', 'TG005', 'NXB001', 'TL006', 'Lập trình Java cơ bản', 2021, 20, 'NN001', 'KS001')," +
            "('S002', 'TG006', 'NXB001', 'TL006', 'Lập trình Java nâng cao', 2022, 15, 'NN001', 'KS001')," +
            "('S003', 'TG009', 'NXB009', 'TL006', 'Cơ sở dữ liệu', 2020, 18, 'NN001', 'KS001')," +
            "('S004', 'TG009', 'NXB009', 'TL006', 'Hệ điều hành', 2019, 12, 'NN001', 'KS001')," +
            "('S005', 'TG002', 'NXB004', 'TL005', 'Nguyên lý động cơ', 2018, 10, 'NN001', 'KS002')," +
            "('S006', 'TG002', 'NXB004', 'TL005', 'Hệ thống truyền động', 2019, 8, 'NN001', 'KS002')," +
            "('S007', 'TG003', 'NXB014', 'TL016', 'Kết cấu bê tông', 2020, 14, 'NN001', 'KS003')," +
            "('S008', 'TG003', 'NXB014', 'TL016', 'Cơ học đất', 2019, 9, 'NN001', 'KS003')," +
            "('S009', 'TG008', 'NXB006', 'TL007', 'Kinh tế vận tải', 2021, 16, 'NN001', 'KS004')," +
            "('S010', 'TG008', 'NXB006', 'TL007', 'Logistics căn bản', 2022, 20, 'NN001', 'KS004')," +
            "('S011', 'TG001', 'NXB005', 'TL001', 'Cho tôi xin một vé đi tuổi thơ', 2015, 25, 'NN001', 'KS006')," +
            "('S012', 'TG002', 'NXB005', 'TL001', 'Chí Phèo', 2014, 30, 'NN001', 'KS006')," +
            "('S013', 'TG006', 'NXB003', 'TL002', 'Harry Potter', 2010, 18, 'NN002', 'KS007')," +
            "('S014', 'TG009', 'NXB003', 'TL002', 'The Da Vinci Code', 2012, 12, 'NN002', 'KS007')");

        db.execSQL("INSERT OR IGNORE INTO muontra (MaMT, MaDG, MaNV, NgayMuon, HanTra, TrangThai) VALUES " +
            "('MT01', 'DG001', 'NV001', '2026-01-05', '2026-01-06', 'Chưa trả')," +
            "('MT02', 'DG002', 'NV001', '2026-01-01', '2026-01-02', 'Chưa trả')," +
            "('MT03', 'DG002', 'NV001', '2026-01-01', '2026-01-22', 'Đã trả')");

        db.execSQL("INSERT OR IGNORE INTO chitietmuontra (MaMT, MaSach, SoLuong) VALUES " +
            "('MT01', 'S001', 1)," +
            "('MT03', 'S001', 3)," +
            "('MT03', 'S005', 3)");

        db.execSQL("INSERT OR IGNORE INTO thethuvien (MaThe, MaDG, NgayCap, NgayHetHan, TrangThai) VALUES " +
            "('TTV001', 'DG001', '2025-06-01', '2025-07-02', 'Hết hiệu lực')," +
            "('TTV002', 'DG002', '2026-01-01', '2027-01-01', 'Còn hiệu lực')," +
            "('TTV003', 'DG003', '2025-11-11', '2026-11-11', 'Còn hiệu lực')");
    }
}
