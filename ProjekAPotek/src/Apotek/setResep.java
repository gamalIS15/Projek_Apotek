/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Apotek;

import java.util.Date;

/**
 *
 * @author user
 */
public class setResep {
    private Date tanggal;
    private String namaPasien;
    private String usia;
    private String alamat;
    private String jenisLayanan;
    private String bpjs_nonBpjs;
    private String namaObat;
    private String jumlahObat;
    private String resep;

    public setResep() {
    }

    public setResep(String resep, Date tanggal, String namaPasien, String usia, String alamat, String jenisLayanan, String bpjs_nonBpjs, String namaObat, String  jumlahObat) {
        this.tanggal = tanggal;
        this.resep = resep;
        this.namaPasien = namaPasien;
        this.usia = usia;
        this.alamat = alamat;
        this.jenisLayanan = jenisLayanan;
        this.bpjs_nonBpjs = bpjs_nonBpjs;
        this.namaObat = namaObat;
        this.jumlahObat = jumlahObat;
    }
    
    

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }
    
    public String getResep() {
        return resep;
    }

    public void setResep(String resep) {
        this.resep = resep;
    }

    public String getNamaPasien() {
        return namaPasien;
    }

    public void setNamaPasien(String namaPasien) {
        this.namaPasien = namaPasien;
    }

    public String getUsia() {
        return usia;
    }

    public void setUsia(String usia) {
        this.usia = usia;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getJenisLayanan() {
        return jenisLayanan;
    }

    public void setJenisLayanan(String jenisLayanan) {
        this.jenisLayanan = jenisLayanan;
    }

    public String getBpjs_nonBpjs() {
        return bpjs_nonBpjs;
    }

    public void setBpjs_nonBpjs(String bpjs_nonBpjs) {
        this.bpjs_nonBpjs = bpjs_nonBpjs;
    }

    public String getNamaObat() {
        return namaObat;
    }

    public void setNamaObat(String namaObat) {
        this.namaObat = namaObat;
    }

    public String getJumlahObat() {
        return jumlahObat;
    }

    public void setJumlahObat(String jumlahObat) {
        this.jumlahObat = jumlahObat;
    }
    
    
}
