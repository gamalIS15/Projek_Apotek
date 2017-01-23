package Apotek;

import java.sql.Date;

public class setObat {
    private Date exGudang;
    private Date exApotek;
    private String namaObat;
    private String golObat;
    private String sat;
    private int sisaApotek;
    private int sisaGudang;
    
    public setObat() {
        
    }

    public setObat(String namaObat, String golObat, String sat, int sisaGudang, Date exGudang, int sisaApotek, Date exApotek) {
        this.namaObat = namaObat;
        this.golObat = golObat;
        this.sat = sat;
        this.sisaGudang = sisaGudang;
        this.exGudang = exGudang;
        this.sisaApotek = sisaApotek;
        this.exApotek = exApotek;
    }
    
    public setObat(Date tanggal, String namaObat, String golObat, String sat, int sisaGudang, int sisaApotek) {
        this.exGudang = tanggal;
        this.namaObat = namaObat;
        this.golObat = golObat;
        this.sat = sat;
        this.sisaGudang = sisaGudang;
        this.sisaApotek = sisaApotek;
    }
    
    public setObat(Date tanggal, String namaObat, String golObat, String sat, int sisaApotek) {
        this.exGudang = tanggal;
        this.namaObat = namaObat;
        this.golObat = golObat;
        this.sat = sat;
        this.sisaApotek = sisaApotek;
    }

    public Date getExGudang() {
        return exGudang;
    }

    public void setExGudang(Date exGudang) {
        this.exGudang = exGudang;
    }

    public Date getExApotek() {
        return exApotek;
    }

    public void setExApotek(Date exApotek) {
        this.exApotek = exApotek;
    }

    public String getNamaObat() {
        return namaObat;
    }

    public void setNamaObat(String namaObat) {
        this.namaObat = namaObat;
    }

    public String getGolObat() {
        return golObat;
    }

    public void setGolObat(String golObat) {
        this.golObat = golObat;
    }

    public String getSat() {
        return sat;
    }

    public void setSat(String sat) {
        this.sat = sat;
    }

    public int getSisaApotek() {
        return sisaApotek;
    }

    public void setSisaApotek(int sisaApotek) {
        this.sisaApotek = sisaApotek;
    }

    public int getSisaGudang() {
        return sisaGudang;
    }

    public void setSisaGudang(int sisaGudang) {
        this.sisaGudang = sisaGudang;
    }

    
}