package com.BTCK.qltv.kesach;

public class KeSach {
    private String MaViTri;
    private String TenKe;
    private String MoTa;

    public KeSach() {}

    public KeSach(String MaViTri, String TenKe, String MoTa) {
        this.MaViTri = MaViTri;
        this.TenKe = TenKe;
        this.MoTa = MoTa;
    }

    public String getMaViTri() { return MaViTri; }
    public void setMaViTri(String MaViTri) { this.MaViTri = MaViTri; }

    public String getTenKe() { return TenKe; }
    public void setTenKe(String TenKe) { this.TenKe = TenKe; }

    public String getMoTa() { return MoTa; }
    public void setMoTa(String moTa) { this.MoTa = MoTa; }
}
