package com.BTCK.qltv.muontra;

// Lớp đối tượng phiếu mượn trả, tương ứng bảng muontra trong CSDL
public class MuonTra {
    private String maMT;
    private String maDG;
    private String maNV;       // Nhân viên xử lý phiếu mượn
    private String ngayMuon;
    private String hanTra;
    private String trangThai;   // "Chưa trả" hoặc "Đã trả"
    private String tenDG;       // Lấy từ bảng docgia để hiển thị, không lưu vào DB

    public MuonTra() {}

    // Constructor dùng khi tạo phiếu mượn mới
    public MuonTra(String maMT, String maDG, String maNV, String ngayMuon, String hanTra, String trangThai) {
        this.maMT = maMT;
        this.maDG = maDG;
        this.maNV = maNV;
        this.ngayMuon = ngayMuon;
        this.hanTra = hanTra;
        this.trangThai = trangThai;
    }

    public String getMaMT() { return maMT; }
    public void setMaMT(String maMT) { this.maMT = maMT; }

    public String getMaDG() { return maDG; }
    public void setMaDG(String maDG) { this.maDG = maDG; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getNgayMuon() { return ngayMuon; }
    public void setNgayMuon(String ngayMuon) { this.ngayMuon = ngayMuon; }

    public String getHanTra() { return hanTra; }
    public void setHanTra(String hanTra) { this.hanTra = hanTra; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getTenDG() { return tenDG; }
    public void setTenDG(String tenDG) { this.tenDG = tenDG; }
}
