package com.BTCK.qltv.docgia;

public class DocGia {
    private String maDG, maKhoa, maLop, tenDG, namSinh, gioiTinh, diaChi, email, sdt;

    public DocGia() {}

    public DocGia(String maDG, String maKhoa, String maLop, String tenDG, String namSinh, String gioiTinh, String diaChi, String email, String sdt) {
        this.maDG = maDG;
        this.maKhoa = maKhoa;
        this.maLop = maLop;
        this.tenDG = tenDG;
        this.namSinh = namSinh;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
        this.email = email;
        this.sdt = sdt;
    }

    public String getMaDG() { return maDG; }
    public void setMaDG(String maDG) { this.maDG = maDG; }

    public String getMaKhoa() { return maKhoa; }
    public void setMaKhoa(String maKhoa) { this.maKhoa = maKhoa; }

    public String getMaLop() { return maLop; }
    public void setMaLop(String maLop) { this.maLop = maLop; }

    public String getTenDG() { return tenDG; }
    public void setTenDG(String tenDG) { this.tenDG = tenDG; }

    public String getNamSinh() { return namSinh; }
    public void setNamSinh(String namSinh) { this.namSinh = namSinh; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
}