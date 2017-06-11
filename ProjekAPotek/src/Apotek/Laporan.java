/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Apotek;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author user
 */
public class Laporan extends javax.swing.JFrame {

    Statement stmt, stmt1, stmt2, stmt3;
    ResultSet rsTransNar, rsTransPsi, rsHari, rsMinggu, rsTahun, rsPuskesmas, rsKunjung;
    String[] title = {"Nama Obat", "Satuan", "Saldo Awal", "Pemasukan Dari", "Pemasukan Jumlah", 
            "Penggunaan Untuk", "Penggunaan Jumlah", "Saldo Akhir"};
    String[] title2 = {"Nama Obat", "Satuan", "Stok Awal", "Penerimaan", 
            "Persediaan", "Pemakaian", "Sisa Stok"};
    String[] title1 = {"Nomor Resep", "Nama Obat", "Satuan", "Stok Awal", "Penerimaan", "Persediaan"};
    String[] titleKunjungan ={"Tanggal","Paten","Generik","Puyer","Obat Jadi", "Anti biotik", "Injeksi" ,"Umum","Jamkesmas","Akses","BPJS","Rawat Inap","Rawat Jalan" };
    ArrayList<setLaporan> listNar = new ArrayList<setLaporan>();
    ArrayList<setLaporan> listPsi = new ArrayList<setLaporan>();
    ArrayList<setLaporan> listHarian = new ArrayList<setLaporan>();
    ArrayList<setLaporan> listBulanan = new ArrayList<setLaporan>();
    ArrayList<setLaporan> listTahunan = new ArrayList<setLaporan>();
    ArrayList<setLaporan> listPuskesmas = new ArrayList<setLaporan>();
    ArrayList<setLaporan> listApotek = new ArrayList<setLaporan>();
    ArrayList<setLaporan> listGudang = new ArrayList<setLaporan>();
    ArrayList<setLaporan> listKunjungan = new ArrayList<setLaporan>();
    
    /**
     * Creates new form Resep
     */
    public Laporan() {
        initComponents();
        this.setWaktu();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
//        txtWelcome.setText(MainMenu.txtWelcome.getText());
        updateTableNar();
        updateTablePsi();
        updateTableHarian();
        updateTableBulanan();
        updateTableTahunan();
        updateTablePuskesmas();
        updateTableKunjungan();
    }
    
    private void updateTableNar() {
        Object[][] data = new Object[this.listNar.size()][8];
        int x = 0;
        for(setLaporan o: this.listNar) {
            data[x][0] = o.getNama();
            data[x][1] = o.getSat();
            data[x][2] = o.getAwal();
            data[x][3] = o.getPengdari();
            data[x][4] = o.getPemasjumlah();
            data[x][5] = o.getPenguntuk();
            data[x][6] = o.getPengjumlah();
            data[x][7] = o.getAkhir();
            ++x;
        }
        tblNarkotika.setModel(new DefaultTableModel(data, title));
    }
    
    private void updateTablePsi() {
        Object[][] data = new Object[this.listPsi.size()][8];
        int x = 0;
        for(setLaporan o: this.listPsi) {
            data[x][0] = o.getNama();
            data[x][1] = o.getSat();
            data[x][2] = o.getAwal();
            data[x][3] = o.getPengdari();
            data[x][4] = o.getPemasjumlah();
            data[x][5] = o.getPenguntuk();
            data[x][6] = o.getPengjumlah();
            data[x][7] = o.getAkhir();
            ++x;
        }
        tblPsikotropika.setModel(new DefaultTableModel(data, title));
    }
    
    private void updateTablePuskesmas() {
        Object[][] data = new Object[this.listPuskesmas.size()][7];
        int x = 0;
        for(setLaporan o: this.listPuskesmas) {
            data[x][0] = o.getNama();
            data[x][1] = o.getSat();
            data[x][2] = o.getAwal();
            data[x][3] = o.getPemasjumlah();
            data[x][4] = o.getSedia();
            data[x][5] = o.getPengjumlah();
            data[x][6] = o.getAkhir();
            ++x;
        }
        tblPuskesmas.setModel(new DefaultTableModel(data, title2));
    }
        
    private void updateTableHarian() {
        Object[][] data = new Object[this.listHarian.size()][6];
        int x = 0;
        for(setLaporan o: this.listHarian) {
            data[x][0] = o.getNoresep();
            data[x][1] = o.getNama();
            data[x][2] = o.getSat();
            data[x][3] = o.getAwal();
            data[x][4] = o.getPemasjumlah();
            data[x][5] = o.getAkhir();
            ++x;
        }
        tblHarian.setModel(new DefaultTableModel(data, title1));
    }
    
    private void updateTableBulanan() {
        Object[][] data = new Object[this.listBulanan.size()][6];
        int x = 0;
        for(setLaporan o: this.listBulanan) {
            data[x][0] = o.getNoresep();
            data[x][1] = o.getNama();
            data[x][2] = o.getSat();
            data[x][3] = o.getAwal();
            data[x][4] = o.getPemasjumlah();
            data[x][5] = o.getAkhir();
            ++x;
        }
        tblBulanan.setModel(new DefaultTableModel(data, title1));
    }
    
    private void updateTableTahunan() {
        Object[][] data = new Object[this.listTahunan.size()][6];
        int x = 0;
        for(setLaporan o: this.listTahunan) {
            data[x][0] = o.getNoresep();
            data[x][1] = o.getNama();
            data[x][2] = o.getSat();
            data[x][3] = o.getAwal();
            data[x][4] = o.getPemasjumlah();
            data[x][5] = o.getAkhir();
            ++x;
        }
        tblTahunan.setModel(new DefaultTableModel(data, title1));
    }
    
    private void updateTableKunjungan() {
        Object[][] data = new Object[this.listKunjungan.size()][13];
        int x = 0;
        for(setLaporan o: this.listKunjungan) {
            data[x][0] = o.getTanggal();
            data[x][1] = o.getPaten();
            data[x][2] = o.getGenerik();
            data[x][3] = o.getPuyer();
            data[x][4] = o.getObatjadi();
            data[x][5] = o.getAntibiotik();
            data[x][6] = o.getInjeksi();
            data[x][7] = o.getUmum();
            data[x][8] = o.getJamkesmas();
            data[x][9] = o.getAskes();
            data[x][10] = o.getBpjs();
            data[x][11] = o.getInap();
            data[x][12] = o.getJalan();
            ++x;
        }
        tblKunjung.setModel(new DefaultTableModel(data, titleKunjungan));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new Apotek.SetImage("/image/wall.png");
        clPanelTransparan1 = new PanelTransparan.ClPanelTransparan();
        txtWelcome = new javax.swing.JLabel();
        txtTanggal = new javax.swing.JLabel();
        btnKeluar = new javax.swing.JLabel();
        clPanelTransparan2 = new PanelTransparan.ClPanelTransparan();
        lplpoG = new javax.swing.JTabbedPane();
        LaporanHarian = new javax.swing.JPanel();
        clPanelTransparan11 = new PanelTransparan.ClPanelTransparan();
        clPanelTransparan12 = new PanelTransparan.ClPanelTransparan();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblHarian = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtTanggalHari = new com.toedter.calendar.JDateChooser();
        jLabel19 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        LaporanBulanan = new javax.swing.JPanel();
        clPanelTransparan9 = new PanelTransparan.ClPanelTransparan();
        clPanelTransparan10 = new PanelTransparan.ClPanelTransparan();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblBulanan = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtBulan = new com.toedter.calendar.JMonthChooser();
        jLabel20 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        LaporanTahunan = new javax.swing.JPanel();
        clPanelTransparan7 = new PanelTransparan.ClPanelTransparan();
        clPanelTransparan8 = new PanelTransparan.ClPanelTransparan();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblTahunan = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        spnTahun = new javax.swing.JSpinner();
        jLabel21 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        Narkotika = new javax.swing.JPanel();
        clPanelTransparan5 = new PanelTransparan.ClPanelTransparan();
        clPanelTransparan6 = new PanelTransparan.ClPanelTransparan();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblNarkotika = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtdariNarkotika = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        txtsampaiNarkotika = new com.toedter.calendar.JDateChooser();
        btnTampilNarkotika = new javax.swing.JButton();
        Psikotropika = new javax.swing.JPanel();
        clPanelTransparan3 = new PanelTransparan.ClPanelTransparan();
        clPanelTransparan4 = new PanelTransparan.ClPanelTransparan();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblPsikotropika = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtdariPsikotropika = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        txtsampaiPsikotropika = new com.toedter.calendar.JDateChooser();
        btnTampilPsikotropika = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        clPanelTransparan13 = new PanelTransparan.ClPanelTransparan();
        clPanelTransparan14 = new PanelTransparan.ClPanelTransparan();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblPuskesmas = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        txtDariPuskesmas = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        txtSampaiPuskesmas = new com.toedter.calendar.JDateChooser();
        btnTampilkanPuskesmas = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        clPanelTransparan15 = new PanelTransparan.ClPanelTransparan();
        clPanelTransparan16 = new PanelTransparan.ClPanelTransparan();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblApotek = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        txtDariApotek = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        txtSampaiApotek = new com.toedter.calendar.JDateChooser();
        btnTampilkanApotek = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        clPanelTransparan17 = new PanelTransparan.ClPanelTransparan();
        clPanelTransparan18 = new PanelTransparan.ClPanelTransparan();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblRekapKunjungan = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtDariGudang = new com.toedter.calendar.JDateChooser();
        jLabel18 = new javax.swing.JLabel();
        jDateChooser9 = new com.toedter.calendar.JDateChooser();
        btnTampilkanGudang = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        clPanelTransparan20 = new PanelTransparan.ClPanelTransparan();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblKunjung = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtdariKunjung = new com.toedter.calendar.JDateChooser();
        jLabel17 = new javax.swing.JLabel();
        txtsampaiKunjung = new com.toedter.calendar.JDateChooser();
        btnTampilKunjung = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Resep");

        jPanel2.setPreferredSize(new java.awt.Dimension(1370, 690));

        clPanelTransparan1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        clPanelTransparan1.setMinimumSize(new java.awt.Dimension(1370, 40));
        clPanelTransparan1.setPreferredSize(new java.awt.Dimension(1370, 680));

        txtWelcome.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtWelcome.setText("jLabel1");

        txtTanggal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTanggal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        btnKeluar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Home.png"))); // NOI18N
        btnKeluar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnKeluar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnKeluarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout clPanelTransparan1Layout = new javax.swing.GroupLayout(clPanelTransparan1);
        clPanelTransparan1.setLayout(clPanelTransparan1Layout);
        clPanelTransparan1Layout.setHorizontalGroup(
            clPanelTransparan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan1Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(txtWelcome)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(251, 251, 251)
                .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(296, 296, 296))
        );
        clPanelTransparan1Layout.setVerticalGroup(
            clPanelTransparan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, clPanelTransparan1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(clPanelTransparan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTanggal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtWelcome, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(btnKeluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        clPanelTransparan2.setPreferredSize(new java.awt.Dimension(1429, 690));

        lplpoG.setPreferredSize(new java.awt.Dimension(1419, 690));
        lplpoG.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lplpoGMouseClicked(evt);
            }
        });

        clPanelTransparan11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout clPanelTransparan11Layout = new javax.swing.GroupLayout(clPanelTransparan11);
        clPanelTransparan11.setLayout(clPanelTransparan11Layout);
        clPanelTransparan11Layout.setHorizontalGroup(
            clPanelTransparan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 383, Short.MAX_VALUE)
        );
        clPanelTransparan11Layout.setVerticalGroup(
            clPanelTransparan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        clPanelTransparan12.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblHarian.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Nomor Resep", "Nama Obat", "Satuan", "Stok Awal", "Penerimaan", "Persediaan"
            }
        ));
        jScrollPane5.setViewportView(tblHarian);

        javax.swing.GroupLayout clPanelTransparan12Layout = new javax.swing.GroupLayout(clPanelTransparan12);
        clPanelTransparan12.setLayout(clPanelTransparan12Layout);
        clPanelTransparan12Layout.setHorizontalGroup(
            clPanelTransparan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 965, Short.MAX_VALUE)
                .addContainerGap())
        );
        clPanelTransparan12Layout.setVerticalGroup(
            clPanelTransparan12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel2.setText("Tanggal : ");

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel19.setText("LAPORAN HARIAN");

        jButton1.setText("Tampilkan");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout LaporanHarianLayout = new javax.swing.GroupLayout(LaporanHarian);
        LaporanHarian.setLayout(LaporanHarianLayout);
        LaporanHarianLayout.setHorizontalGroup(
            LaporanHarianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LaporanHarianLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(clPanelTransparan11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(LaporanHarianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clPanelTransparan12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(LaporanHarianLayout.createSequentialGroup()
                        .addGroup(LaporanHarianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(LaporanHarianLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel19))
                            .addGroup(LaporanHarianLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtTanggalHari, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        LaporanHarianLayout.setVerticalGroup(
            LaporanHarianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LaporanHarianLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addGap(18, 18, 18)
                .addGroup(LaporanHarianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(LaporanHarianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtTanggalHari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(LaporanHarianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clPanelTransparan11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clPanelTransparan12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        lplpoG.addTab("Laporan Harian", LaporanHarian);

        clPanelTransparan9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout clPanelTransparan9Layout = new javax.swing.GroupLayout(clPanelTransparan9);
        clPanelTransparan9.setLayout(clPanelTransparan9Layout);
        clPanelTransparan9Layout.setHorizontalGroup(
            clPanelTransparan9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 383, Short.MAX_VALUE)
        );
        clPanelTransparan9Layout.setVerticalGroup(
            clPanelTransparan9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        clPanelTransparan10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblBulanan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "Nama", "Satuan", "Stok Awal", "Penerimaan", "Persediaan"
            }
        ));
        jScrollPane4.setViewportView(tblBulanan);

        javax.swing.GroupLayout clPanelTransparan10Layout = new javax.swing.GroupLayout(clPanelTransparan10);
        clPanelTransparan10.setLayout(clPanelTransparan10Layout);
        clPanelTransparan10Layout.setHorizontalGroup(
            clPanelTransparan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 897, Short.MAX_VALUE)
                .addContainerGap())
        );
        clPanelTransparan10Layout.setVerticalGroup(
            clPanelTransparan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel3.setText("Bulan :");

        txtBulan.setMonth(0);

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel20.setText("LAPORAN BULANAN");

        jButton2.setText("Tampilkan");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout LaporanBulananLayout = new javax.swing.GroupLayout(LaporanBulanan);
        LaporanBulanan.setLayout(LaporanBulananLayout);
        LaporanBulananLayout.setHorizontalGroup(
            LaporanBulananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LaporanBulananLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(clPanelTransparan9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(LaporanBulananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LaporanBulananLayout.createSequentialGroup()
                        .addComponent(clPanelTransparan10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(90, 90, 90))
                    .addGroup(LaporanBulananLayout.createSequentialGroup()
                        .addGroup(LaporanBulananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(LaporanBulananLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtBulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2))
                            .addGroup(LaporanBulananLayout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(jLabel20)))
                        .addContainerGap(773, Short.MAX_VALUE))))
        );
        LaporanBulananLayout.setVerticalGroup(
            LaporanBulananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LaporanBulananLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel20)
                .addGap(18, 18, 18)
                .addGroup(LaporanBulananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(txtBulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(12, 12, 12)
                .addGroup(LaporanBulananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clPanelTransparan9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clPanelTransparan10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        lplpoG.addTab("Laporan Bulanan", LaporanBulanan);

        clPanelTransparan7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout clPanelTransparan7Layout = new javax.swing.GroupLayout(clPanelTransparan7);
        clPanelTransparan7.setLayout(clPanelTransparan7Layout);
        clPanelTransparan7Layout.setHorizontalGroup(
            clPanelTransparan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 383, Short.MAX_VALUE)
        );
        clPanelTransparan7Layout.setVerticalGroup(
            clPanelTransparan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        clPanelTransparan8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblTahunan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "Nama", "Satuan", "Stok Awal", "Penerimaan", "Persediaan"
            }
        ));
        jScrollPane3.setViewportView(tblTahunan);

        javax.swing.GroupLayout clPanelTransparan8Layout = new javax.swing.GroupLayout(clPanelTransparan8);
        clPanelTransparan8.setLayout(clPanelTransparan8Layout);
        clPanelTransparan8Layout.setHorizontalGroup(
            clPanelTransparan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 891, Short.MAX_VALUE)
                .addContainerGap())
        );
        clPanelTransparan8Layout.setVerticalGroup(
            clPanelTransparan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel4.setText("Tahun : ");

        spnTahun.setModel(new javax.swing.SpinnerNumberModel(2017, 2017, 2500, 1));

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel21.setText("LAPORAN TAHUNAN");

        jButton3.setText("Tampilkan");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout LaporanTahunanLayout = new javax.swing.GroupLayout(LaporanTahunan);
        LaporanTahunan.setLayout(LaporanTahunanLayout);
        LaporanTahunanLayout.setHorizontalGroup(
            LaporanTahunanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LaporanTahunanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(clPanelTransparan7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(LaporanTahunanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LaporanTahunanLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spnTahun, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(LaporanTahunanLayout.createSequentialGroup()
                        .addComponent(clPanelTransparan8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(96, 96, 96))))
            .addGroup(LaporanTahunanLayout.createSequentialGroup()
                .addGap(423, 423, 423)
                .addComponent(jLabel21)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        LaporanTahunanLayout.setVerticalGroup(
            LaporanTahunanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LaporanTahunanLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel21)
                .addGap(18, 18, 18)
                .addGroup(LaporanTahunanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(spnTahun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(LaporanTahunanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clPanelTransparan7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clPanelTransparan8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        lplpoG.addTab("Laporan Tahunan", LaporanTahunan);

        clPanelTransparan5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout clPanelTransparan5Layout = new javax.swing.GroupLayout(clPanelTransparan5);
        clPanelTransparan5.setLayout(clPanelTransparan5Layout);
        clPanelTransparan5Layout.setHorizontalGroup(
            clPanelTransparan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 383, Short.MAX_VALUE)
        );
        clPanelTransparan5Layout.setVerticalGroup(
            clPanelTransparan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        clPanelTransparan6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblNarkotika.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Nama Obat", "Saldo Awal", "Pemasukan Dari", "Pemasukan Jumlah", "Penggunaan Untuk", "Penggunaan Jumlah", "Saldo Akhir"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblNarkotika);

        javax.swing.GroupLayout clPanelTransparan6Layout = new javax.swing.GroupLayout(clPanelTransparan6);
        clPanelTransparan6.setLayout(clPanelTransparan6Layout);
        clPanelTransparan6Layout.setHorizontalGroup(
            clPanelTransparan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 965, Short.MAX_VALUE)
                .addContainerGap())
        );
        clPanelTransparan6Layout.setVerticalGroup(
            clPanelTransparan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("LAPORAN NARKOTIKA");

        jLabel5.setText("Dari Tanggal:");

        txtdariNarkotika.setDateFormatString("yyyy-MM-dd");

        jLabel11.setText("Sampai Tanggal:");

        txtsampaiNarkotika.setDateFormatString("yyyy-MM-dd");

        btnTampilNarkotika.setText("Tampilkan");
        btnTampilNarkotika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTampilNarkotikaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout NarkotikaLayout = new javax.swing.GroupLayout(Narkotika);
        Narkotika.setLayout(NarkotikaLayout);
        NarkotikaLayout.setHorizontalGroup(
            NarkotikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NarkotikaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(clPanelTransparan5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(NarkotikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clPanelTransparan6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(NarkotikaLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtdariNarkotika, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtsampaiNarkotika, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnTampilNarkotika)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(NarkotikaLayout.createSequentialGroup()
                .addGap(461, 461, 461)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        NarkotikaLayout.setVerticalGroup(
            NarkotikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NarkotikaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(NarkotikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NarkotikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtdariNarkotika, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtsampaiNarkotika, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(jLabel11))
                    .addComponent(btnTampilNarkotika, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(NarkotikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(clPanelTransparan5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clPanelTransparan6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        lplpoG.addTab("Narkotika", Narkotika);

        Psikotropika.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PsikotropikaMouseClicked(evt);
            }
        });

        clPanelTransparan3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout clPanelTransparan3Layout = new javax.swing.GroupLayout(clPanelTransparan3);
        clPanelTransparan3.setLayout(clPanelTransparan3Layout);
        clPanelTransparan3Layout.setHorizontalGroup(
            clPanelTransparan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 303, Short.MAX_VALUE)
        );
        clPanelTransparan3Layout.setVerticalGroup(
            clPanelTransparan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        clPanelTransparan4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblPsikotropika.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Nama Obat", "Satuan", "Saldo Awal", "Pemasukan Dari", "Pemasukan Jumlah", "Penggunaan Untuk", "Penggunaan Jumlah", "Saldo Akhir"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane10.setViewportView(tblPsikotropika);

        javax.swing.GroupLayout clPanelTransparan4Layout = new javax.swing.GroupLayout(clPanelTransparan4);
        clPanelTransparan4.setLayout(clPanelTransparan4Layout);
        clPanelTransparan4Layout.setHorizontalGroup(
            clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 1004, Short.MAX_VALUE)
                .addContainerGap())
        );
        clPanelTransparan4Layout.setVerticalGroup(
            clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setText("LAPORAN PSIKOTROPIKA");

        jLabel13.setText("Dari Tanggal:");

        txtdariPsikotropika.setDateFormatString("yyyy-MM-dd");

        jLabel14.setText("Sampai Tanggal:");

        txtsampaiPsikotropika.setDateFormatString("yyyy-MM-dd");

        btnTampilPsikotropika.setText("Tampilkan");
        btnTampilPsikotropika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTampilPsikotropikaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PsikotropikaLayout = new javax.swing.GroupLayout(Psikotropika);
        Psikotropika.setLayout(PsikotropikaLayout);
        PsikotropikaLayout.setHorizontalGroup(
            PsikotropikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PsikotropikaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(clPanelTransparan3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(PsikotropikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PsikotropikaLayout.createSequentialGroup()
                        .addGap(98, 98, 98)
                        .addGroup(PsikotropikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PsikotropikaLayout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addComponent(jLabel12))
                            .addGroup(PsikotropikaLayout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtdariPsikotropika, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtsampaiPsikotropika, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnTampilPsikotropika))))
                    .addGroup(PsikotropikaLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clPanelTransparan4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PsikotropikaLayout.setVerticalGroup(
            PsikotropikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PsikotropikaLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addGroup(PsikotropikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PsikotropikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtdariPsikotropika, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtsampaiPsikotropika, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13)
                        .addComponent(jLabel14))
                    .addComponent(btnTampilPsikotropika, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PsikotropikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clPanelTransparan3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clPanelTransparan4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        lplpoG.addTab("Psikotropika", Psikotropika);

        clPanelTransparan13.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout clPanelTransparan13Layout = new javax.swing.GroupLayout(clPanelTransparan13);
        clPanelTransparan13.setLayout(clPanelTransparan13Layout);
        clPanelTransparan13Layout.setHorizontalGroup(
            clPanelTransparan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 383, Short.MAX_VALUE)
        );
        clPanelTransparan13Layout.setVerticalGroup(
            clPanelTransparan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        clPanelTransparan14.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblPuskesmas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Nama Obat", "Satuan", "Stok Awal", "Penerimaan", "Persediaan", "Pemakaian", "Sisa Stok"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(tblPuskesmas);

        javax.swing.GroupLayout clPanelTransparan14Layout = new javax.swing.GroupLayout(clPanelTransparan14);
        clPanelTransparan14.setLayout(clPanelTransparan14Layout);
        clPanelTransparan14Layout.setHorizontalGroup(
            clPanelTransparan14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 965, Short.MAX_VALUE)
                .addContainerGap())
        );
        clPanelTransparan14Layout.setVerticalGroup(
            clPanelTransparan14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel7.setText("Dari Tanggal :");

        jLabel6.setText("Sampai Tanggal :");

        btnTampilkanPuskesmas.setText("Tampilkan");
        btnTampilkanPuskesmas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTampilkanPuskesmasActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel22.setText("LPLPO PUSKESMAS");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(clPanelTransparan13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clPanelTransparan14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDariPuskesmas, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtSampaiPuskesmas, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnTampilkanPuskesmas))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jLabel22)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel22)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDariPuskesmas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSampaiPuskesmas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(btnTampilkanPuskesmas))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clPanelTransparan13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clPanelTransparan14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        lplpoG.addTab("LPLPO Puskesmas", jPanel4);

        clPanelTransparan15.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout clPanelTransparan15Layout = new javax.swing.GroupLayout(clPanelTransparan15);
        clPanelTransparan15.setLayout(clPanelTransparan15Layout);
        clPanelTransparan15Layout.setHorizontalGroup(
            clPanelTransparan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 383, Short.MAX_VALUE)
        );
        clPanelTransparan15Layout.setVerticalGroup(
            clPanelTransparan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        clPanelTransparan16.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblApotek.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "Nama Obat", "Satuan", "Stok Awal", "Penerimaan", "Persediaan"
            }
        ));
        jScrollPane7.setViewportView(tblApotek);

        javax.swing.GroupLayout clPanelTransparan16Layout = new javax.swing.GroupLayout(clPanelTransparan16);
        clPanelTransparan16.setLayout(clPanelTransparan16Layout);
        clPanelTransparan16Layout.setHorizontalGroup(
            clPanelTransparan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 965, Short.MAX_VALUE)
                .addContainerGap())
        );
        clPanelTransparan16Layout.setVerticalGroup(
            clPanelTransparan16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel8.setText("Tanggal :");

        jLabel10.setText("Sampai tanggal :");

        btnTampilkanApotek.setText("Tampilkan");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(clPanelTransparan15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(clPanelTransparan16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(439, 439, 439)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDariApotek, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSampaiApotek, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(96, 96, 96)
                .addComponent(btnTampilkanApotek)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(txtDariApotek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(txtSampaiApotek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnTampilkanApotek)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clPanelTransparan15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clPanelTransparan16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        lplpoG.addTab("LPLPO Apotek", jPanel5);

        clPanelTransparan17.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout clPanelTransparan17Layout = new javax.swing.GroupLayout(clPanelTransparan17);
        clPanelTransparan17.setLayout(clPanelTransparan17Layout);
        clPanelTransparan17Layout.setHorizontalGroup(
            clPanelTransparan17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 383, Short.MAX_VALUE)
        );
        clPanelTransparan17Layout.setVerticalGroup(
            clPanelTransparan17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        clPanelTransparan18.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblRekapKunjungan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "Nama Obat", "Satuan", "Stok Awal", "Penerimaan", "Persediaan"
            }
        ));
        jScrollPane8.setViewportView(tblRekapKunjungan);

        javax.swing.GroupLayout clPanelTransparan18Layout = new javax.swing.GroupLayout(clPanelTransparan18);
        clPanelTransparan18.setLayout(clPanelTransparan18Layout);
        clPanelTransparan18Layout.setHorizontalGroup(
            clPanelTransparan18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 965, Short.MAX_VALUE)
                .addContainerGap())
        );
        clPanelTransparan18Layout.setVerticalGroup(
            clPanelTransparan18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel9.setText("Tanggal :");

        jLabel18.setText("Sampai Tanggal :");

        btnTampilkanGudang.setText("Tampilkan");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(clPanelTransparan17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(clPanelTransparan18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(431, 431, 431)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDariGudang, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser9, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(btnTampilkanGudang)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(txtDariGudang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18)
                            .addComponent(jDateChooser9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnTampilkanGudang)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clPanelTransparan17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clPanelTransparan18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        lplpoG.addTab("LPLPO Gudang", jPanel6);

        clPanelTransparan20.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblKunjung.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Tanggal", "Paten", "Generik", "Puyer", "Obat Jadi", "Antibiotik", "Injeksi", "Umum", "Jamkesmas", "Akses", "BPJS", "Rawat Inap", "Rawat jalan"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKunjung.setPreferredSize(new java.awt.Dimension(195, 80));
        jScrollPane9.setViewportView(tblKunjung);

        javax.swing.GroupLayout clPanelTransparan20Layout = new javax.swing.GroupLayout(clPanelTransparan20);
        clPanelTransparan20.setLayout(clPanelTransparan20Layout);
        clPanelTransparan20Layout.setHorizontalGroup(
            clPanelTransparan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 1370, Short.MAX_VALUE)
                .addContainerGap())
        );
        clPanelTransparan20Layout.setVerticalGroup(
            clPanelTransparan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel15.setText("LAPORAN REKAP KUNJUNGAN");

        jLabel16.setText("Dari Tanggal:");

        txtdariKunjung.setDateFormatString("yyyy-MM-dd");

        jLabel17.setText("Sampai Tanggal:");

        txtsampaiKunjung.setDateFormatString("yyyy-MM-dd");

        btnTampilKunjung.setText("Tampilkan");
        btnTampilKunjung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTampilKunjungActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(clPanelTransparan20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(415, 415, 415)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtdariKunjung, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtsampaiKunjung, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnTampilKunjung))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(446, 446, 446)
                        .addComponent(jLabel15)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel15)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtdariKunjung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtsampaiKunjung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel16)
                        .addComponent(jLabel17))
                    .addComponent(btnTampilKunjung, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(clPanelTransparan20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        lplpoG.addTab("Rekap Kunjungan", jPanel3);

        javax.swing.GroupLayout clPanelTransparan2Layout = new javax.swing.GroupLayout(clPanelTransparan2);
        clPanelTransparan2.setLayout(clPanelTransparan2Layout);
        clPanelTransparan2Layout.setHorizontalGroup(
            clPanelTransparan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lplpoG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        clPanelTransparan2Layout.setVerticalGroup(
            clPanelTransparan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, clPanelTransparan2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lplpoG, javax.swing.GroupLayout.DEFAULT_SIZE, 668, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clPanelTransparan1, javax.swing.GroupLayout.DEFAULT_SIZE, 1613, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(clPanelTransparan2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(174, 174, 174))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(clPanelTransparan1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(clPanelTransparan2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnKeluarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnKeluarMouseClicked
        // TODO add your handling code here:
        new MainMenu(setApoteker.tmpID, setApoteker.tmpPass).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnKeluarMouseClicked

    private void btnTampilNarkotikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTampilNarkotikaActionPerformed
        // TODO add your handling code here:
        String tanggalDari = (txtdariNarkotika.getDate().getYear()+1900) + "-" + 
                (txtdariNarkotika.getDate().getMonth()+1) + "-" + 
                txtdariNarkotika.getDate().getDate();
        String tanggalSampai = (txtsampaiNarkotika.getDate().getYear()+1900) + "-" + 
                (txtsampaiNarkotika.getDate().getMonth()+1) + "-" + 
                txtsampaiNarkotika.getDate().getDate();
        Date tgl = null;
        String obat = null, dari = null, untuk = null, sat=null;
        int guna= 0, awal = 0, masuk = 0, akhir = 0;
        
        try {
            setConnection koneksi = new setConnection();
            stmt = koneksi.connection.createStatement();
            rsTransNar = stmt.executeQuery("SELECT Transaksi.namaObat as nama, DataObat.sat as satuan, dari, ke, tanggal, sum(jumlah) AS masuk, sum(jumlahKeluar) AS guna, "
                    + "(SELECT sum(jumlah) FROM Transaksi WHERE tanggal<'" + tanggalDari + "'AND namaObat=nama GROUP BY namaObat) AS awal "
                    + "FROM Transaksi JOIN DataObat ON Transaksi.namaObat = DataObat.namaObat WHERE tanggal BETWEEN '" + tanggalDari +
                    "' AND '" + tanggalSampai + "' AND golongan='Narkotika' GROUP BY Transaksi.namaObat");
            while(rsTransNar.next() == true) {
                tgl = rsTransNar.getDate("tanggal");
                sat = rsTransNar.getString("satuan");
                obat = rsTransNar.getString("nama");
                dari = rsTransNar.getString("dari");
                untuk = rsTransNar.getString("ke");
                masuk = rsTransNar.getInt("masuk");
                guna = rsTransNar.getInt("guna");
                awal = rsTransNar.getInt("awal");
                akhir = (awal+masuk-guna);
                
            listNar.add(new setLaporan(obat, sat, awal, dari, masuk, untuk, guna, akhir));
            }
            
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        updateTableNar();
    }//GEN-LAST:event_btnTampilNarkotikaActionPerformed

    private void btnTampilPsikotropikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTampilPsikotropikaActionPerformed
        // TODO add your handling code here:
        String tanggalDari = (txtdariPsikotropika.getDate().getYear()+1900) + "-" + 
                (txtdariPsikotropika.getDate().getMonth()+1) + "-" + 
                txtdariPsikotropika.getDate().getDate();
        String tanggalSampai = (txtsampaiPsikotropika.getDate().getYear()+1900) + "-" + 
                (txtsampaiPsikotropika.getDate().getMonth()+1) + "-" + 
                txtsampaiPsikotropika.getDate().getDate();
        Date tgl = null;
        String obat = null, dari = null, untuk = null, sat=null;
        int guna= 0, awal = 0, masuk = 0, akhir = 0;
        
        try {
            setConnection koneksi = new setConnection();
            stmt = koneksi.connection.createStatement();
            rsTransPsi = stmt.executeQuery("SELECT Transaksi.namaObat as nama, DataObat.sat as satuan, dari, ke, tanggal, sum(jumlah) AS masuk, sum(jumlahKeluar) AS guna, "
                    + "(SELECT sum(jumlah) FROM Transaksi WHERE tanggal<'" + tanggalDari + "'AND namaObat=nama GROUP BY namaObat) AS awal "
                    + "FROM Transaksi JOIN DataObat ON Transaksi.namaObat = DataObat.namaObat WHERE tanggal BETWEEN '" + tanggalDari +
                    "' AND '" + tanggalSampai + "' AND golongan='Psikotropika' GROUP BY Transaksi.namaObat");
            while(rsTransPsi.next() == true) {
                tgl = rsTransPsi.getDate("tanggal");
                sat = rsTransPsi.getString("satuan");
                obat = rsTransPsi.getString("nama");
                dari = rsTransPsi.getString("dari");
                untuk = rsTransPsi.getString("ke");
                masuk = rsTransPsi.getInt("masuk");
                guna = rsTransPsi.getInt("guna");
                awal = rsTransPsi.getInt("awal");
                akhir = (awal+masuk-guna);
                
            listPsi.add(new setLaporan(obat, sat, awal, dari, masuk, untuk, guna, akhir));
            }
            
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        updateTablePsi();
    }//GEN-LAST:event_btnTampilPsikotropikaActionPerformed

    private void lplpoGMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lplpoGMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_lplpoGMouseClicked

    private void PsikotropikaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PsikotropikaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_PsikotropikaMouseClicked

    private void btnTampilKunjungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTampilKunjungActionPerformed
        // TODO add your handling code here:
        String tanggalDari = (txtdariKunjung.getDate().getYear()+1900) + "-" + 
                (txtdariKunjung.getDate().getMonth()+1) + "-" + 
                txtdariKunjung.getDate().getDate();
        String tanggalSampai = (txtsampaiKunjung.getDate().getYear()+1900) + "-" + 
                (txtsampaiKunjung.getDate().getMonth()+1) + "-" + 
                txtsampaiKunjung.getDate().getDate();
        Date tgl = null;
        int nonbpjs= 0, bpjs = 0, inap = 0, jalan = 0;
        
        try {
            setConnection koneksi = new setConnection();
            stmt = koneksi.connection.createStatement();
            rsKunjung = stmt.executeQuery("SELECT Tanggal as tgl, "
                    + "(SELECT sum(BpjsNonBpjs) FROM DataResep WHERE Tanggal=tgl AND BpjsNonBpjs='non BPJS' GROUP BY Tanggal) AS nonBPJS, "
                    + "(SELECT sum(BpjsNonBpjs) FROM DataResep WHERE Tanggal=tgl AND BpjsNonBpjs='BPJS' GROUP BY Tanggal) AS BPJS, "
                    + "(SELECT sum(JenisLayanan) FROM DataResep WHERE Tanggal=tgl AND JenisLayanan='Rawat Inap' GROUP BY Tanggal) AS inap, "
                    + "(SELECT sum(JenisLayanan) FROM DataResep WHERE Tanggal=tgl AND JenisLayanan='Rawat Jalan' GROUP BY Tanggal) AS jalan, "
                    + "FROM DataResep WHERE Tanggal BETWEEN '" + tanggalDari + "' AND '" + tanggalSampai + "'");
            while(rsKunjung.next() == true) {
                tgl = rsKunjung.getDate("tgl");
                nonbpjs = rsKunjung.getInt("nonBPJS");
                bpjs = rsKunjung.getInt("BPJS");
                inap = rsKunjung.getInt("inap");
                jalan = rsKunjung.getInt("jalan");
                
            listKunjungan.add(new setLaporan(tgl, 0, 0, 0, 0, 0, 0, nonbpjs, 0, 0, bpjs, inap, jalan));
            }
            
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        updateTableKunjungan();
    }//GEN-LAST:event_btnTampilKunjungActionPerformed

    private void btnTampilkanPuskesmasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTampilkanPuskesmasActionPerformed
        // TODO add your handling code here:
         String tanggalDari = (txtDariPuskesmas.getDate().getYear()+1900) + "-" + 
                (txtDariPuskesmas.getDate().getMonth()+1) + "-" + 
                txtDariPuskesmas.getDate().getDate();
        String tanggalSampai = (txtSampaiPuskesmas.getDate().getYear()+1900) + "-" + 
                (txtSampaiPuskesmas.getDate().getMonth()+1) + "-" + 
                txtSampaiPuskesmas.getDate().getDate();
        Date tgl = null;
        String obat = null, dari = null, untuk = null, sat=null;
        int guna= 0, awal = 0, sedia = 0, akhir = 0;
        
        try {
            setConnection koneksi = new setConnection();
            stmt = koneksi.connection.createStatement();
            rsPuskesmas = stmt.executeQuery("SELECT DataObat.namaObat as nama, DataObat.satJ as satuan, tanggalMasuk, jumlahSedia, jumlahSediaJ "
                    + "FROM DataObat JOIN DataJual ON DataObat.namaObat = DataJual.namaObatJ WHERE tanggalMasuk BETWEEN '" + tanggalDari +
                    "' AND '" + tanggalSampai + "'");
            while(rsPuskesmas.next() == true) {
                tgl = rsPuskesmas.getDate("tanggalMasuk");
                sat = rsPuskesmas.getString("satuan");
                obat = rsPuskesmas.getString("nama");
                sedia = rsPuskesmas.getInt("jumlahSedia");
                guna = rsPuskesmas.getInt("jumlahSediaJ");
                awal = rsPuskesmas.getInt("jumlahSedia");
                akhir = (awal-guna);
                
            listPuskesmas.add(new setLaporan(obat, sat, awal, 0, sedia, guna, akhir));
            }
            
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        updateTablePuskesmas();
    }//GEN-LAST:event_btnTampilkanPuskesmasActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String tanggal = (txtTanggalHari.getDate().getYear()+1900) + "-" + 
                (txtTanggalHari.getDate().getMonth()+1) + "-" + 
                txtTanggalHari.getDate().getDate();
        Date tgl = null;
        String obat = null, dari = null, untuk = null, sat=null, noResep=null;
        int guna= 0, awal = 0, masuk = 0, akhir = 0;
        
        try {
            setConnection koneksi = new setConnection();
            stmt = koneksi.connection.createStatement();
            rsHari = stmt.executeQuery("SELECT Transaksi.namaObat as nama, DataObat.sat as satuan, noResep, tanggal, sum(jumlah) AS masuk, sum(jumlahKeluar) AS guna, "
                    + "(SELECT sum(jumlah) FROM Transaksi WHERE tanggal<'" + tanggal + "'AND namaObat=nama GROUP BY namaObat) AS awal "
                    + "FROM Transaksi JOIN DataObat ON Transaksi.namaObat = DataObat.namaObat WHERE tanggal = '" + tanggal +
                    "' GROUP BY Transaksi.namaObat");
            while(rsHari.next() == true) {
                tgl = rsHari.getDate("tanggal");
                sat = rsHari.getString("satuan");
                obat = rsHari.getString("nama");
                masuk = rsHari.getInt("masuk");
                awal = rsHari.getInt("awal");
                noResep = rsHari.getString("noResep");
                akhir = (awal+masuk);
                
            listHarian.add(new setLaporan(noResep, obat, sat, awal, masuk, akhir));
            }
            
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        updateTableHarian();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        int bulan = txtBulan.getMonth()+1;
        Date tgl = null;
        String obat = null, dari = null, untuk = null, sat=null, noResep=null;
        int guna= 0, awal = 0, masuk = 0, akhir = 0;
        
        try {
            setConnection koneksi = new setConnection();
            stmt = koneksi.connection.createStatement();
            rsMinggu = stmt.executeQuery("SELECT Transaksi.namaObat as nama, DataObat.sat as satuan, noResep, tanggal, sum(jumlah) AS masuk, sum(jumlahKeluar) AS guna, "
                    + "(SELECT sum(jumlah) FROM Transaksi WHERE EXTRACT(MONTH FROM tanggal) <'" + bulan + "' AND namaObat=nama GROUP BY namaObat) AS awal "
                    + "FROM Transaksi JOIN DataObat ON Transaksi.namaObat = DataObat.namaObat WHERE EXTRACT(MONTH FROM tanggal) = '" + bulan +
                    "' GROUP BY Transaksi.namaObat");
            while(rsMinggu.next() == true) {
                tgl = rsMinggu.getDate("tanggal");
                sat = rsMinggu.getString("satuan");
                obat = rsMinggu.getString("nama");
                masuk = rsMinggu.getInt("masuk");
                awal = rsMinggu.getInt("awal");
                noResep = rsMinggu.getString("noResep");
                akhir = (awal+masuk);
                
            listBulanan.add(new setLaporan(noResep, obat, sat, awal, masuk, akhir));
            }
            
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        updateTableBulanan();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        int tahun = (int) spnTahun.getValue();
        Date tgl = null;
        String obat = null, dari = null, untuk = null, sat=null, noResep=null;
        int guna= 0, awal = 0, masuk = 0, akhir = 0;
        
        try {
            setConnection koneksi = new setConnection();
            stmt = koneksi.connection.createStatement();
            rsTahun = stmt.executeQuery("SELECT Transaksi.namaObat as nama, DataObat.sat as satuan, noResep, tanggal, sum(jumlah) AS masuk, sum(jumlahKeluar) AS guna, "
                    + "(SELECT sum(jumlah) FROM Transaksi WHERE EXTRACT(YEAR FROM tanggal) <'" + tahun + "' AND namaObat=nama GROUP BY namaObat) AS awal "
                    + "FROM Transaksi JOIN DataObat ON Transaksi.namaObat = DataObat.namaObat WHERE EXTRACT(YEAR FROM tanggal) = '" + tahun +
                    "' GROUP BY Transaksi.namaObat");
            while(rsTahun.next() == true) {
                tgl = rsTahun.getDate("tanggal");
                sat = rsTahun.getString("satuan");
                obat = rsTahun.getString("nama");
                masuk = rsTahun.getInt("masuk");
                awal = rsTahun.getInt("awal");
                noResep = rsTahun.getString("noResep");
                akhir = (awal+masuk);
                
            listTahunan.add(new setLaporan(noResep, obat, sat, awal, masuk, akhir));
            }
            
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        updateTableTahunan();
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Laporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Laporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Laporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Laporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Laporan().setVisible(true);
            }
        });
    }
    
    public final void setWaktu() {
        ActionListener taskPerformer = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String nol_jam = "", nol_menit = "", nol_detik = "";

            java.util.Date dateTime = new java.util.Date();
            int nilai_jam = dateTime.getHours();
            int nilai_menit = dateTime.getMinutes();
            int nilai_detik = dateTime.getSeconds();

            if(nilai_jam <= 9) nol_jam="0";
            if(nilai_menit <= 9) nol_menit="0";
            if(nilai_detik <= 9) nol_detik="0";

            String waktu = nol_jam + Integer.toString(nilai_jam);
            String menit = nol_menit + Integer.toString(nilai_menit);
            String detik = nol_detik + Integer.toString(nilai_detik);

            txtTanggal.setText(String.valueOf(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) + " " + 
                    waktu + ":" + menit + ":" + detik);
        }
        };
        new Timer(1000, taskPerformer).start();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel LaporanBulanan;
    private javax.swing.JPanel LaporanHarian;
    private javax.swing.JPanel LaporanTahunan;
    private javax.swing.JPanel Narkotika;
    private javax.swing.JPanel Psikotropika;
    private javax.swing.JLabel btnKeluar;
    private javax.swing.JButton btnTampilKunjung;
    private javax.swing.JButton btnTampilNarkotika;
    private javax.swing.JButton btnTampilPsikotropika;
    private javax.swing.JButton btnTampilkanApotek;
    private javax.swing.JButton btnTampilkanGudang;
    private javax.swing.JButton btnTampilkanPuskesmas;
    private PanelTransparan.ClPanelTransparan clPanelTransparan1;
    private PanelTransparan.ClPanelTransparan clPanelTransparan10;
    private PanelTransparan.ClPanelTransparan clPanelTransparan11;
    private PanelTransparan.ClPanelTransparan clPanelTransparan12;
    private PanelTransparan.ClPanelTransparan clPanelTransparan13;
    private PanelTransparan.ClPanelTransparan clPanelTransparan14;
    private PanelTransparan.ClPanelTransparan clPanelTransparan15;
    private PanelTransparan.ClPanelTransparan clPanelTransparan16;
    private PanelTransparan.ClPanelTransparan clPanelTransparan17;
    private PanelTransparan.ClPanelTransparan clPanelTransparan18;
    private PanelTransparan.ClPanelTransparan clPanelTransparan2;
    private PanelTransparan.ClPanelTransparan clPanelTransparan20;
    private PanelTransparan.ClPanelTransparan clPanelTransparan3;
    private PanelTransparan.ClPanelTransparan clPanelTransparan4;
    private PanelTransparan.ClPanelTransparan clPanelTransparan5;
    private PanelTransparan.ClPanelTransparan clPanelTransparan6;
    private PanelTransparan.ClPanelTransparan clPanelTransparan7;
    private PanelTransparan.ClPanelTransparan clPanelTransparan8;
    private PanelTransparan.ClPanelTransparan clPanelTransparan9;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private com.toedter.calendar.JDateChooser jDateChooser9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane lplpoG;
    private javax.swing.JSpinner spnTahun;
    private javax.swing.JTable tblApotek;
    private javax.swing.JTable tblBulanan;
    private javax.swing.JTable tblHarian;
    private javax.swing.JTable tblKunjung;
    private javax.swing.JTable tblNarkotika;
    private javax.swing.JTable tblPsikotropika;
    private javax.swing.JTable tblPuskesmas;
    private javax.swing.JTable tblRekapKunjungan;
    private javax.swing.JTable tblTahunan;
    private com.toedter.calendar.JMonthChooser txtBulan;
    private com.toedter.calendar.JDateChooser txtDariApotek;
    private com.toedter.calendar.JDateChooser txtDariGudang;
    private com.toedter.calendar.JDateChooser txtDariPuskesmas;
    private com.toedter.calendar.JDateChooser txtSampaiApotek;
    private com.toedter.calendar.JDateChooser txtSampaiPuskesmas;
    private javax.swing.JLabel txtTanggal;
    private com.toedter.calendar.JDateChooser txtTanggalHari;
    private javax.swing.JLabel txtWelcome;
    private com.toedter.calendar.JDateChooser txtdariKunjung;
    private com.toedter.calendar.JDateChooser txtdariNarkotika;
    private com.toedter.calendar.JDateChooser txtdariPsikotropika;
    private com.toedter.calendar.JDateChooser txtsampaiKunjung;
    private com.toedter.calendar.JDateChooser txtsampaiNarkotika;
    private com.toedter.calendar.JDateChooser txtsampaiPsikotropika;
    // End of variables declaration//GEN-END:variables
}
