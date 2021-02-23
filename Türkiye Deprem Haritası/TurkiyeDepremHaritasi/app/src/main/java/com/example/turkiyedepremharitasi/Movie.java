package com.example.turkiyedepremharitasi;

import com.google.gson.annotations.SerializedName;
public class Movie {
    @SerializedName("tarih")
    private String  tarih;
    @SerializedName("saat")
    private String saat;
    @SerializedName("enlem")
    private String enlem;
    @SerializedName("boylam")
    private String boylam;
    @SerializedName("derinlik")
    private String derinlik;
    @SerializedName("siddet")
    private String siddet;
    @SerializedName("sehir")
    private String sehir;
    @SerializedName("yer")
    private String yer;

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getSaat() {
        return saat;
    }

    public void setSaat(String saat) {
        this.saat = saat;
    }

    public String getEnlem() {
        return enlem;
    }

    public void setEnlem(String enlem) {
        this.enlem = enlem;
    }

    public String getBoylam() {
        return boylam;
    }

    public void setBoylam(String boylam) {
        this.boylam = boylam;
    }

    public String getDerinlik() {
        return derinlik;
    }

    public void setDerinlik(String derinlik) {
        this.derinlik = derinlik;
    }

    public String getSiddet() {
        return siddet;
    }

    public void setSiddet(String buyukluk) {
        this.siddet = siddet;
    }

    public String getYer() {
        return yer;
    }
    public void setYer(String yer) {
        this.yer = yer;
    }
}