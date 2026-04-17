package com.BTCK.qltv.login;

public class KhachHang {
    private String maDG;
    private String tenDG;
    private String user;
    private String pass;

    public KhachHang(String maDG, String tenDG, String user, String pass) {
        this.maDG = maDG;
        this.tenDG = tenDG;
        this.user = user;
        this.pass = pass;
    }

    public String getMaDG() { return maDG; }
    public String getTenDG() { return tenDG; }
    public String getUser() { return user; }
    public String getPass() { return pass; }
}
