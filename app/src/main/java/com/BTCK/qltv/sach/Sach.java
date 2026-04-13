package com.BTCK.qltv.sach;

public class Sach {
    private String maSach;
    private String maTG;
    private String maNXB;
    private String maTL;
    private String tenSach;
    private String maNN;
    private String maViTri;
    private int namXB;
    private int soLuong;

    public Sach() {
    }

    public Sach(String maSach, String maTG, String maNXB, String maTL, String tenSach, String maNN, String maViTri, int namXB, int soLuong) {
        this.maSach = maSach;
        this.maTG = maTG;
        this.maNXB = maNXB;
        this.maTL = maTL;
        this.tenSach = tenSach;
        this.maNN = maNN;
        this.maViTri = maViTri;
        this.namXB = namXB;
        this.soLuong = soLuong;
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getMaTG() {
        return maTG;
    }

    public void setMaTG(String maTG) {
        this.maTG = maTG;
    }

    public String getMaNXB() {
        return maNXB;
    }

    public void setMaNXB(String maNXB) {
        this.maNXB = maNXB;
    }

    public String getMaTL() {
        return maTL;
    }

    public void setMaTL(String maTL) {
        this.maTL = maTL;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getMaNN() {
        return maNN;
    }

    public void setMaNN(String maNN) {
        this.maNN = maNN;
    }

    public String getMaViTri() {
        return maViTri;
    }

    public void setMaViTri(String maViTri) {
        this.maViTri = maViTri;
    }

    public int getNamXB() {
        return namXB;
    }

    public void setNamXB(int namXB) {
        this.namXB = namXB;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}