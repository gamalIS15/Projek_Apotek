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
    private Date tanggal;
    private int umum;
    private int paten;
    private int generik;
    private int puyer;
    private int obatjadi;
    private int antibiotik;
    private int injeksi;
    private int jamkesmas;
    private int askes;
    private int bpjs;
    private int jalan;
    private int inap;

    public setLaporan(Date tanggal, int paten, int generik, int puyer, int obatjadi, int antibiotik, int injeksi, int umum, int jamkesmas, int askes, int bpjs, int inap, int jalan) {
        this.tanggal = tanggal;
        this.umum = umum;
        this.paten = paten;
        this.generik = generik;
        this.puyer = puyer;
        this.obatjadi = obatjadi;
        this.antibiotik = antibiotik;
        this.injeksi = injeksi;
        this.jamkesmas = jamkesmas;
        this.askes = askes;
        this.bpjs = bpjs;
        this.jalan = jalan;
        this.inap = inap;
    }
    
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
     * @return the umum
     */
    public int getUmum() {
        return umum;
    }

    /**
     * @param umum the umum to set
     */
    public void setUmum(int umum) {
        this.umum = umum;
    }

    /**
     * @return the bpjs
     */
    public int getBpjs() {
        return bpjs;
    }

    /**
     * @param bpjs the bpjs to set
     */
    public void setBpjs(int bpjs) {
        this.bpjs = bpjs;
    }

    /**
     * @return the jalan
     */
    public int getJalan() {
        return jalan;
    }

    /**
     * @param jalan the jalan to set
     */
    public void setJalan(int jalan) {
        this.jalan = jalan;
    }

    /**
     * @return the inap
     */
    public int getInap() {
        return inap;
    }

    /**
     * @param inap the inap to set
     */
    public void setInap(int inap) {
        this.inap = inap;
    }

    /**
     * @return the paten
     */
    public int getPaten() {
        return paten;
    }

    /**
     * @param paten the paten to set
     */
    public void setPaten(int paten) {
        this.paten = paten;
    }

    /**
     * @return the generik
     */
    public int getGenerik() {
        return generik;
    }

    /**
     * @param generik the generik to set
     */
    public void setGenerik(int generik) {
        this.generik = generik;
    }

    /**
     * @return the puyer
     */
    public int getPuyer() {
        return puyer;
    }

    /**
     * @param puyer the puyer to set
     */
    public void setPuyer(int puyer) {
        this.puyer = puyer;
    }

    /**
     * @return the obatjadi
     */
    public int getObatjadi() {
        return obatjadi;
    }

    /**
     * @param obatjadi the obatjadi to set
     */
    public void setObatjadi(int obatjadi) {
        this.obatjadi = obatjadi;
    }

    /**
     * @return the antibiotik
     */
    public int getAntibiotik() {
        return antibiotik;
    }

    /**
     * @param antibiotik the antibiotik to set
     */
    public void setAntibiotik(int antibiotik) {
        this.antibiotik = antibiotik;
    }

    /**
     * @return the injeksi
     */
    public int getInjeksi() {
        return injeksi;
    }

    /**
     * @param injeksi the injeksi to set
     */
    public void setInjeksi(int injeksi) {
        this.injeksi = injeksi;
    }

    /**
     * @return the jamkesmas
     */
    public int getJamkesmas() {
        return jamkesmas;
    }

    /**
     * @param jamkesmas the jamkesmas to set
     */
    public void setJamkesmas(int jamkesmas) {
        this.jamkesmas = jamkesmas;
    }

    /**
     * @return the askes
     */
    public int getAskes() {
        return askes;
    }

    /**
     * @param askes the askes to set
     */
    public void setAskes(int askes) {
        this.askes = askes;
    }
    
    
}
