package com.dovantuan.lab1_ph31763;

public class CarModel {

    private String _id;
    private String ten;

    private int namSx;

    private String hang;

    private double gia;

    public CarModel(String _id, String ten, int namSx, String hang, double gia) {
        this._id = _id;
        this.ten = ten;
        this.namSx = namSx;
        this.hang = hang;
        this.gia = gia;
    }

    public CarModel(String ten, int namSx, String hang, double gia) {
        this.ten = ten;
        this.namSx = namSx;
        this.hang = hang;
        this.gia = gia;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setId(String id) {
        this._id = id;
    }


    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getNamSx() {
        return namSx;
    }

    public void setNamSx(int namSx) {
        this.namSx = namSx;
    }

    public String getHang() {
        return hang;
    }

    public void setHang(String hang) {
        this.hang = hang;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

}
