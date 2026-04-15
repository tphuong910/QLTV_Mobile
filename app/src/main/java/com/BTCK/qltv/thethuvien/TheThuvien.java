package com.BTCK.qltv.thethuvien;

// Lớp đối tượng thẻ thư viện, tương ứng bảng thethuvien trong CSDL
public class TheThuvien {
    private String maThe;
    private String maDG;
    private String ngayCap;
    private String ngayHetHan;
    private String trangThai;  // "Còn hiệu lực" hoặc "Hết hiệu lực"
    private String tenDG;      // Lấy từ bảng docgia để hiển thị, không lưu vào DB

    public TheThuvien() {}

    // Constructor dùng khi thêm thẻ mới
    public TheThuvien(String maThe, String maDG, String ngayCap, String ngayHetHan, String trangThai) {
        this.maThe = maThe;
        this.maDG = maDG;
        this.ngayCap = ngayCap;
        this.ngayHetHan = ngayHetHan;
        this.trangThai = trangThai;
    }

    public String getMaThe() { return maThe; }
    public void setMaThe(String maThe) { this.maThe = maThe; }

    public String getMaDG() { return maDG; }
    public void setMaDG(String maDG) { this.maDG = maDG; }

    public String getNgayCap() { return ngayCap; }
    public void setNgayCap(String ngayCap) { this.ngayCap = ngayCap; }

    public String getNgayHetHan() { return ngayHetHan; }
    public void setNgayHetHan(String ngayHetHan) { this.ngayHetHan = ngayHetHan; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getTenDG() { return tenDG; }
    public void setTenDG(String tenDG) { this.tenDG = tenDG; }
}
