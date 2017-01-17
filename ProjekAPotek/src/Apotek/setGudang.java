package Apotek;

import java.sql.Date;

public class setGudang {
    private Date tanggal;
    private String namaPasien;
    private String usia;
    private String alamat;
    private String jenisLayanan;
    private String bpjs_nonBpjs;
    private String namaObat;
    private String jumlahObat;

    public setGudang(Date tanggal, String namaObat, String golObat, String sat, int sisaGudang, Date exdate) {
        this.tanggal = tanggal;
        this.namaObat = namaObat;
        this.golObat = golObat;
        this.sat = sat;
        this.sisaGudang = sisaGudang;
        this.exdate = exdate;
    }

    setGudang() {
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
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

    public int getSisaGudang() {
        return sisaGudang;
    }

    public void setSisaGudang(int sisaGudang) {
        this.sisaGudang = sisaGudang;
    }

    public Date getExdate() {
        return exdate;
    }

    public void setExdate(Date exdate) {
        this.exdate = exdate;
    }
    
    

}