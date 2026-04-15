package com.BTCK.qltv.nhaxuatban;

public class NhaXuatBan {
    private String maNXB;
    private String tenNXB;
    private String diaChi;
    private String email;
    private String sdt;

    public NhaXuatBan() {
    }

    public NhaXuatBan(String maNXB, String tenNXB, String diaChi, String email, String sdt) {
        this.maNXB = maNXB;
        this.tenNXB = tenNXB;
        this.diaChi = diaChi;
        this.email = email;
        this.sdt = sdt;
    }

    public String getMaNXB() { return maNXB; }
    public void setMaNXB(String maNXB) { this.maNXB = maNXB; }

    public String getTenNXB() { return tenNXB; }
    public void setTenNXB(String tenNXB) { this.tenNXB = tenNXB; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
}