package prototypeapotik;

import java.sql.Date;

public class Pasien {
    private Date tanggal;
    private String nama;
    private int umur;
    private String alamat;
    private String obat;
    private int jumlah;

    public Pasien(Date tanggal, String nama, int umur, String alamat, String obat, int jumlah) {
        this.tanggal = tanggal;
        this.nama = nama;
        this.umur = umur;
        this.alamat = alamat;
        this.obat = obat;
        this.jumlah = jumlah;
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
     * @return the nama
     */
    public String getNama() {
        return nama;
    }

    /**
     * @param nama the nama to set
     */
    public void setNama(String nama) {
        this.nama = nama;
    }

    /**
     * @return the umur
     */
    public int getUmur() {
        return umur;
    }

    /**
     * @param umur the umur to set
     */
    public void setUmur(int umur) {
        this.umur = umur;
    }

    /**
     * @return the alamat
     */
    public String getAlamat() {
        return alamat;
    }

    /**
     * @param alamat the alamat to set
     */
    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    /**
     * @return the obat
     */
    public String getObat() {
        return obat;
    }

    /**
     * @param obat the obat to set
     */
    public void setObat(String obat) {
        this.obat = obat;
    }

    /**
     * @return the jumlah
     */
    public int getJumlah() {
        return jumlah;
    }

    /**
     * @param jumlah the jumlah to set
     */
    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
    
    
}
