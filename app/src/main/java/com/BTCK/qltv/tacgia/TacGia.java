package com.BTCK.qltv.tacgia;

public class TacGia {
    private String maTG;
    private String tenTG;
    private String namSinh;
    private String gioiTinh;
    private String quocTich;

    public TacGia(String maTG, String tenTG, String namSinh, String gioiTinh, String quocTich) {
        this.maTG = maTG;
        this.tenTG = tenTG;
        this.namSinh = namSinh;
        this.gioiTinh = gioiTinh;
        this.quocTich = quocTich;
    }

    public String getMaTG() { return maTG; }
    public String getTenTG() { return tenTG; }
    public String getNamSinh() { return namSinh; }
    public String getGioiTinh() { return gioiTinh; }
    public String getQuocTich() { return quocTich; }
}