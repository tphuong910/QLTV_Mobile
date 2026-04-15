package com.BTCK.qltv.lop;

public class Lop {
    private String maLop;
    private String tenLop;
    private String maKhoa;
    private String tenKhoa; // Thêm tên khoa để hiển thị cho dễ hiểu

    public Lop(String maLop, String tenLop, String maKhoa) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.maKhoa = maKhoa;
    }

    public Lop(String maLop, String tenLop, String maKhoa, String tenKhoa) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.maKhoa = maKhoa;
        this.tenKhoa = tenKhoa;
    }

    public String getMaLop() { return maLop; }
    public String getTenLop() { return tenLop; }
    public String getMaKhoa() { return maKhoa; }
    public String getTenKhoa() { return tenKhoa; }
}
