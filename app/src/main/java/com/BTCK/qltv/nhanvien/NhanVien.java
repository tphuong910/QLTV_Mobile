package com.BTCK.qltv.nhanvien;
;

public class NhanVien {
    private String maNV;
    private String tenNV;
    private String queQuan;
    private String gioiTinh;
    private String namSinh;
    private String vaiTro;
    private String email;
    private String sdt;
    private String user;
    private String pass;

    public NhanVien() {
    }

    public NhanVien(String maNV, String tenNV, String queQuan, String gioiTinh, String namSinh, String vaiTro, String email, String sdt, String user, String pass) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.queQuan = queQuan;
        this.gioiTinh = gioiTinh;
        this.namSinh = namSinh;
        this.vaiTro = vaiTro;
        this.email = email;
        this.sdt = sdt;
        this.user = user;
        this.pass = pass;
    }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getTenNV() { return tenNV; }
    public void setTenNV(String tenNV) { this.tenNV = tenNV; }

    public String getQueQuan() { return queQuan; }
    public void setQueQuan(String queQuan) { this.queQuan = queQuan; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getNamSinh() { return namSinh; }
    public void setNamSinh(String namSinh) { this.namSinh = namSinh; }

    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }

    public String getPass() { return pass; }
    public void setPass(String pass) { this.pass = pass; }
}