package prototypeapotik;

import java.sql.Date;

public class Pasien {
    private String tanggal;
    private String nama;
    private String umur;
    private String alamat;
    private String obat;
    private String jumlah;

    public Pasien(String tanggal, String nama, String umur, String alamat, String obat, String jumlah) {
        this.tanggal = tanggal;
        this.nama = nama;
        this.umur = umur;
        this.alamat = alamat;
        this.obat = obat;
        this.jumlah = jumlah;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUmur() {
        return umur;
    }

    public void setUmur(String umur) {
        this.umur = umur;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getObat() {
        return obat;
    }

    public void setObat(String obat) {
        this.obat = obat;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }
       
    
}
