package com.BTCK.qltv.sach;

public class Sach {
    private String id;

    private String MaTG;
    private String MaNXB;
    private String MaTL;
    private String TenSach;
    private String MaNN;
    private String MaViTri;
    private int NamXB;
    private int SoLuong;

    public Sach() {
    }

    public Sach(String id, String maTG, String maNXB, String maTL, String tenSach, String maNN, String maViTri, int namXB, int soLuong) {
        this.id = id;
        MaTG = maTG;
        MaNXB = maNXB;
        MaTL = maTL;
        TenSach = tenSach;
        MaNN = maNN;
        MaViTri = maViTri;
        NamXB = namXB;
        SoLuong = soLuong;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getMaTG() {
        return MaTG;
    }
    public void setMaTG(String maTG) {

        MaTG = maTG;
    }

    public String getMaNXB() {

        return MaNXB;
    }
    public void setMaNXB(String maNXB) {
        MaNXB = maNXB;
    }

    public String getMaTL() {
        return MaTL;
    }
    public void setMaTL(String maTL) {
        MaTL = maTL;
    }

    public String getTenSach() {
        return TenSach;
    }
    public void setTenSach(String tenSach) {
        TenSach = tenSach;
    }

    public String getMaNN() {
        return MaNN;
    }
    public void setMaNN(String maNN) {
        MaNN = maNN;
    }

    public String getMaViTri() {
        return MaViTri;
    }
    public void setMaViTri(String maViTri) {
        MaViTri = maViTri;
    }

    public int getNamXB() {
        return NamXB;
    }
    public void setNamXB(int namXB) {
        NamXB = namXB;
    }

    public int getSoLuong() {
        return SoLuong;
    }
    public void setSoLuong(int soLuong) {
        SoLuong = soLuong;
    }
}