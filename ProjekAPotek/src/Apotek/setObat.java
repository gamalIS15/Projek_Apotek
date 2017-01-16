package Apotek;

import java.sql.Date;

public class setObat {
    private Date tanggal;
    private String namaObat;
    private String golObat;
    private String sat;
    private int sisaApotek;
    private int sisaGudang;

    public setObat(Date tanggal, String namaObat, String golObat, String sat, int sisaApotek) {
        this.tanggal = tanggal;
        this.namaObat = namaObat;
        this.golObat = golObat;
        this.sat = sat;
        //this.sisaGudang = sisaGudang;
        this.sisaApotek = sisaApotek;
    }

    /**
     * @return the tanggal
     */
    public Date getTanggal() {
        return tanggal;
    }

    /**
     * @param tanggal the tanggal to set
     */
    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    /**
     * @return the namaObat
     */
    public String getNamaObat() {
        return namaObat;
    }

    /**
     * @param namaObat the namaObat to set
     */
    public void setNamaObat(String namaObat) {
        this.namaObat = namaObat;
    }

    /**
     * @return the golObat
     */
    public String getGolObat() {
        return golObat;
    }

    /**
     * @param golObat the golObat to set
     */
    public void setGolObat(String golObat) {
        this.golObat = golObat;
    }

    /**
     * @return the sat
     */
    public String getSat() {
        return sat;
    }

    /**
     * @param sat the sat to set
     */
    public void setSat(String sat) {
        this.sat = sat;
    }

    /**
     * @return the sisaApotek
     */
    public int getSisaApotek() {
        return sisaApotek;
    }

    /**
     * @param sisaApotek the sisaApotek to set
     */
    public void setSisaApotek(int sisaApotek) {
        this.sisaApotek = sisaApotek;
    }

    /**
     * @return the sisaGudang
     */
    public int getSisaGudang() {
        return sisaGudang;
    }

    /**
     * @param sisaGudang the sisaGudang to set
     */
    public void setSisaGudang(int sisaGudang) {
        this.sisaGudang = sisaGudang;
    }
    
    
}
