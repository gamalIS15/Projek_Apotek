/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Apotek;

import java.util.Date;

/**
 *
 * @author asus
 */
public class setDataObat {
    private String namaObat;
    private String golObat;
    private String satuan;
    private int persediaanGudang;
    private Date kadaluarsaGudang;
    private int persediaanApotek;
    private Date kadaluarsaApotek;

    public setDataObat(String namaObat, String golObat, String satuan, int persediaanGudang, Date kadaluarsaGudang, int persediaanApotek, Date kadaluarsaApotek) {
        this.namaObat = namaObat;
        this.golObat = golObat;
        this.satuan = satuan;
        this.persediaanGudang = persediaanGudang;
        this.kadaluarsaGudang = kadaluarsaGudang;
        this.persediaanApotek = persediaanApotek;
        this.kadaluarsaApotek = kadaluarsaApotek;
    }

    /**
     * @return the namaObat asas
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
     * @return the satuan
     */
    public String getSatuan() {
        return satuan;
    }

    /**
     * @param satuan the satuan to set
     */
    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    /**
     * @return the persediaanGudang
     */
    public int getPersediaanGudang() {
        return persediaanGudang;
    }

    /**
     * @param persediaanGudang the persediaanGudang to set
     */
    public void setPersediaanGudang(int persediaanGudang) {
        this.persediaanGudang = persediaanGudang;
    }

    /**
     * @return the kadaluarsaGudang
     */
    public Date getKadaluarsaGudang() {
        return kadaluarsaGudang;
    }

    /**
     * @param kadaluarsaGudang the kadaluarsaGudang to set
     */
    public void setKadaluarsaGudang(Date kadaluarsaGudang) {
        this.kadaluarsaGudang = kadaluarsaGudang;
    }

    /**
     * @return the persediaanApotek
     */
    public int getPersediaanApotek() {
        return persediaanApotek;
    }

    /**
     * @param persediaanApotek the persediaanApotek to set
     */
    public void setPersediaanApotek(int persediaanApotek) {
        this.persediaanApotek = persediaanApotek;
    }

    /**
     * @return the kadaluarsaApotek
     */
    public Date getKadaluarsaApotek() {
        return kadaluarsaApotek;
    }

    /**
     * @param kadaluarsaApotek the kadaluarsaApotek to set
     */
    public void setKadaluarsaApotek(Date kadaluarsaApotek) {
        this.kadaluarsaApotek = kadaluarsaApotek;
    }
    
    

   
            
    
}
