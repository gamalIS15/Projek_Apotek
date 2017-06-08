package Apotek;

import java.util.Date;

public class setLaporan {
    Date dari;
    Date sampai;
    String nama;
    String sat;
    String noresep;
    int awal;
    int sedia;
    String Pengdari;
    int Pemasjumlah;
    String Penguntuk;
    int Pengjumlah;
    int akhir;

    public setLaporan(String nama, String sat, int awal, String Pengdari, int Pemasjumlah, String Penguntuk, int Pengjumlah, int akhir) {
        this.nama = nama;
        this.sat = sat;
        this.awal = awal;
        this.Pengdari = Pengdari;
        this.Pemasjumlah = Pemasjumlah;
        this.Penguntuk = Penguntuk;
        this.Pengjumlah = Pengjumlah;
        this.akhir = akhir;
    }
    
    public setLaporan(String noresep, String nama, String sat, int awal, int Pemasjumlah, int akhir) {
        this.noresep = noresep;
        this.nama = nama;
        this.sat = sat;
        this.awal = awal;
        this.Pemasjumlah = Pemasjumlah;
        this.akhir = akhir;
    }
    
    public setLaporan(String nama, String sat, int awal, int Pemasjumlah, int sedia, int Pengjumlah, int akhir) {
        this.nama = nama;
        this.sat = sat;
        this.awal = awal;
        this.Pemasjumlah = Pemasjumlah;
        this.sedia = sedia;
        this.Pengjumlah = Pengjumlah;
        this.akhir = akhir;
    }

    public void setDari(Date dari) {
        this.dari = dari;
    }

    public void setSampai(Date sampai) {
        this.sampai = sampai;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setSat(String sat) {
        this.sat = sat;
    }
    
    public void setNoresep(String noresep) {
        this.noresep = noresep;
    }

    public void setAwal(int awal) {
        this.awal = awal;
    }

    public void setSedia(int sedia) {
        this.sedia = sedia;
    }

    public void setPengdari(String Pengdari) {
        this.Pengdari = Pengdari;
    }

    public void setPemasjumlah(int Pemasjumlah) {
        this.Pemasjumlah = Pemasjumlah;
    }

    public void setPenguntuk(String Penguntuk) {
        this.Penguntuk = Penguntuk;
    }

    public void setPengjumlah(int Pengjumlah) {
        this.Pengjumlah = Pengjumlah;
    }

    public void setAkhir(int akhir) {
        this.akhir = akhir;
    }
    
    public Date getDari() {
        return dari;
    }

    public Date getSampai() {
        return sampai;
    }

    public String getNama() {
        return nama;
    }
    
    public String getNoresep() {
        return noresep;
    }
    
    public String getSat() {
        return sat;
    }

    public int getAwal() {
        return awal;
    }
    
    public int getSedia() {
        return sedia;
    }

    public String getPengdari() {
        return Pengdari;
    }

    public int getPemasjumlah() {
        return Pemasjumlah;
    }

    public String getPenguntuk() {
        return Penguntuk;
    }

    public int getPengjumlah() {
        return Pengjumlah;
    }

    public int getAkhir() {
        return akhir;
    }
    
    
}
