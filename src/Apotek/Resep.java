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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.*;

/**
 *
 * @author user
 */
public class Resep extends javax.swing.JFrame {

    /**
     * Creates new form Resep
     */
    Statement stmt, stmt1, stmt2;
    ResultSet rsResep,rsObat,rsJual,rsCariResep,rscariObat;
    int index = 0;
    String title [] = {"No. Resep", "Tanggal", "Nama Pasien", "Usia", "Alamat", "Jenis Layanan", 
        "BPJS/Non BPJS", "Nama Obat", "Jumlah Pengambilan"};
    String [] judul= {"Tanggal", "Nama Obat", "Golongan", "Satuan", "Jumlah Obat (dalam satuan)"};
    ArrayList<setResep> list = new ArrayList<setResep>();
    ArrayList<setObat> list1 = new ArrayList<setObat>();
    private final ArrayList<String> ls = new ArrayList<>();
    
    
    public Resep() {
        try {
            this.setWaktu();
            initComponents();
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            txtWelcome.setText(MainMenu.txtWelcome.getText());
            
            setConnection koneksi = new setConnection();
            stmt = koneksi.connection.createStatement();
            rsResep = stmt.executeQuery("SELECT * FROM DataResep ORDER BY Tanggal");
            
            while(rsResep.next() == true){
                list.add(new setResep(rsResep.getString("NoResep"),
                        rsResep.getDate("Tanggal"),
                        rsResep.getString("NamaPasien"),
                        rsResep.getString("Usia"),
                        rsResep.getString("Alamat"),
                        rsResep.getString("JenisLayanan"),
                        rsResep.getString("BpjsNonBpjs"),
                        rsResep.getString("namaObat"),
                        rsResep.getString("jmlObat")));
            }
            
            stmt1 = koneksi.connection.createStatement();
            rsJual = stmt1.executeQuery("SELECT * FROM DataJual ORDER BY namaObatJ");
            while(rsJual.next() == true) {
                list1.add(new setObat(rsJual.getDate("tglMasukJ"), 
                        rsJual.getString("namaObatJ"), 
                        rsJual.getString("golObatJ"), 
                        rsJual.getString("satJ"), 
                        rsJual.getInt("jumlahSediaJ"))); 
            }
            
        } catch (SQLException ex) {
            System.out.println(ex);;
        }
        
        try {
            
            Suggestion();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        updateTable();
        updateTableJual();
        try {
            SearchSugges();
        } catch (SQLException ex) {
            Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void updateTable(){
        Object[][] data = new Object[this.list.size()][9];
        int x = 0;
        for (setResep sr : this.list){
            data[x][0] = sr.getNoResep();
            data[x][1] = sr.getTanggal();
            data[x][2] = sr.getNamaPasien();
            data[x][3] = sr.getUsia();            
            data[x][4] = sr.getAlamat();
            data[x][5] = sr.getJenisLayanan();
            data[x][6] = sr.getBpjs_nonBpjs();
            data[x][7] = sr.getNamaObat();
            data[x][8] = sr.getJumlahObat();
            ++x;
        }
        tblEx.setModel(new DefaultTableModel(data,title));
    }
    
    private void updateTableJual() {
        Object[][] data1 = new Object[this.list1.size()][5];
        int x = 0;
        for(setObat o: this.list1) {
            data1[x][0] = o.getExGudang();
            data1[x][1] = o.getNamaObat();
            data1[x][2] = o.getGolObat();
            data1[x][3] = o.getSat();
            data1[x][4] = o.getSisaApotek();
            ++x;
        }
        tblJual.setModel(new DefaultTableModel(data1, judul));
    }
    
    private void removeTable() {
        DefaultTableModel model = (DefaultTableModel)tblEx.getModel();
        while (model.getRowCount() > 0){
            for (int i = 0; i < model.getRowCount(); ++i){
                model.removeRow(i);
            }
        }
    }
    
        private void removeTableJual() {
        DefaultTableModel model = (DefaultTableModel)tblJual.getModel();
        while (model.getRowCount() > 0){
            for (int i = 0; i < model.getRowCount(); ++i){
                model.removeRow(i);
            }
        }
    }
    private void SearchSugges() throws SQLException{
        ArrayList <String> li = new ArrayList<String>();
        ArrayList <String> ki = new ArrayList<String>();
        rsCariResep = stmt.executeQuery("SELECT * FROM DataResep ORDER BY NamaPasien");
        rscariObat = stmt1.executeQuery("SELECT * FROM DataJual ORDER BY namaObatJ");
        
        li.add("");
        ki.add("");
        while(rsCariResep.next()==true){
            li.add(rsCariResep.getString("NamaPasien"));
        }
         while(rscariObat.next()==true){
            ki.add(rscariObat.getString("namaObatJ"));
        }
         
        String [] cariResep = new String[li.size()];
        cariResep = li.toArray(cariResep);
        
        String [] cariObat = new String[ki.size()];
        cariObat = ki.toArray(cariObat);
        
        DefaultComboBoxModel<String> cR = new DefaultComboBoxModel<String>(cariResep);
        DefaultComboBoxModel<String> cO = new DefaultComboBoxModel<String>(cariObat);
        
        cbCariNama.setModel(cR);
        AutoCompleteDecorator.decorate(this.cbCariNama);
        
        cbCariObat.setModel(cO);
        AutoCompleteDecorator.decorate(this.cbCariObat);
    }
    
    private void Suggestion() throws SQLException{        
        rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE jumlahSedia>0 ORDER BY namaObat");
        ls.add("<Pilih Obat>");
        while(rsObat.next()==true){           
            ls.add(rsObat.getString("namaObat"));
        }
        //Autocomplete
        
        String [] namaObat = new String[ls.size()];
        namaObat = ls.toArray(namaObat);
        
        DefaultComboBoxModel<String> dcm = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm1 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm2 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm3 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm4 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm5 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm6 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm7 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm8 = new DefaultComboBoxModel<String>(namaObat);
            cbNamaObat1.setModel( dcm );
            AutoCompleteDecorator.decorate(this.cbNamaObat1);
            cbNamaObat2.setModel( cm );
            AutoCompleteDecorator.decorate(this.cbNamaObat2);
            cbNamaObat3.setModel( cm1 );
            AutoCompleteDecorator.decorate(this.cbNamaObat3);
            cbNamaObat4.setModel( cm2 );
            AutoCompleteDecorator.decorate(this.cbNamaObat4);
            cbNamaObat5.setModel( cm3 );
            AutoCompleteDecorator.decorate(this.cbNamaObat5);
            cbNamaObat6.setModel( cm4 );
            AutoCompleteDecorator.decorate(this.cbNamaObat6);
            cbNamaObat7.setModel( cm5 );
            AutoCompleteDecorator.decorate(this.cbNamaObat7);
            cbNamaObat8.setModel( cm6 );
            AutoCompleteDecorator.decorate(this.cbNamaObat8);
            cbNamaObat9.setModel( cm7 );
            AutoCompleteDecorator.decorate(this.cbNamaObat9);
            cbNamaObat10.setModel( cm8 );
            AutoCompleteDecorator.decorate(this.cbNamaObat10);
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgbpjs = new javax.swing.ButtonGroup();
        jPanel1 = new Apotek.SetImage("/image/wall.png");
        clPanelTransparan1 = new PanelTransparan.ClPanelTransparan();
        txtWelcome = new javax.swing.JLabel();
        txtTanggal = new javax.swing.JLabel();
        btnKeluar = new javax.swing.JLabel();
        clPanelTransparan3 = new PanelTransparan.ClPanelTransparan();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cbCariGol = new javax.swing.JComboBox<String>();
        btnCari = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cbCariSort = new javax.swing.JComboBox<String>();
        cbCariNama = new javax.swing.JComboBox<String>();
        clPanelTransparan4 = new PanelTransparan.ClPanelTransparan();
        btnSimpan = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtTanggalResep = new com.toedter.calendar.JDateChooser();
        txtNamaPasien = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAlamat = new javax.swing.JTextArea();
        cbJenisLayanan = new javax.swing.JComboBox<String>();
        spnThn = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        rbBPJS = new javax.swing.JRadioButton();
        rbNBPJS = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        spnJumlah1 = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        spnJumlah2 = new javax.swing.JSpinner();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        spnJumlah3 = new javax.swing.JSpinner();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        spnJumlah4 = new javax.swing.JSpinner();
        jLabel18 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        spnJumlah5 = new javax.swing.JSpinner();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        spnJumlah6 = new javax.swing.JSpinner();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        spnJumlah7 = new javax.swing.JSpinner();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        spnJumlah8 = new javax.swing.JSpinner();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        spnJumlah9 = new javax.swing.JSpinner();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        spnJumlah10 = new javax.swing.JSpinner();
        cbNamaObat2 = new javax.swing.JComboBox<String>();
        cbNamaObat3 = new javax.swing.JComboBox<String>();
        cbNamaObat4 = new javax.swing.JComboBox<String>();
        cbNamaObat5 = new javax.swing.JComboBox<String>();
        cbNamaObat6 = new javax.swing.JComboBox<String>();
        cbNamaObat7 = new javax.swing.JComboBox<String>();
        cbNamaObat8 = new javax.swing.JComboBox<String>();
        cbNamaObat9 = new javax.swing.JComboBox<String>();
        cbNamaObat10 = new javax.swing.JComboBox<String>();
        cbNamaObat1 = new javax.swing.JComboBox<String>();
        clPanelTransparan5 = new PanelTransparan.ClPanelTransparan();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblJual = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        cbCariGol1 = new javax.swing.JComboBox<String>();
        btnCari1 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        cbCariSort1 = new javax.swing.JComboBox<String>();
        cbCariObat = new javax.swing.JComboBox<String>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEx = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gudang");

        clPanelTransparan1.setBackground(new java.awt.Color(255, 255, 255));
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
                .addGap(499, 499, 499)
                .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
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

        clPanelTransparan3.setBackground(new java.awt.Color(255, 255, 255));
        clPanelTransparan3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Log Pasien");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Nama : ");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Golongan :");

        cbCariGol.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--Tampilkan Semua--", "Rawat Jalan", "Rawat Inap", "BPJS", "Non BPJS" }));

        btnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/cari.png"))); // NOI18N
        btnCari.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCari.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCariMouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("Urutkan Berdasar :");

        cbCariSort.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tanggal", "Nama Pasien", "Usia Pasien", "Jenis Layanan", "BPJS / Non BPJS" }));

        cbCariNama.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout clPanelTransparan3Layout = new javax.swing.GroupLayout(clPanelTransparan3);
        clPanelTransparan3.setLayout(clPanelTransparan3Layout);
        clPanelTransparan3Layout.setHorizontalGroup(
            clPanelTransparan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbCariNama, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbCariGol, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbCariSort, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCari)
                .addContainerGap(79, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, clPanelTransparan3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(361, 361, 361))
        );
        clPanelTransparan3Layout.setVerticalGroup(
            clPanelTransparan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, clPanelTransparan3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(clPanelTransparan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(clPanelTransparan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel3)
                        .addComponent(cbCariGol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(cbCariSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbCariNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCari))
                .addContainerGap(242, Short.MAX_VALUE))
        );

        clPanelTransparan4.setBackground(new java.awt.Color(255, 255, 255));
        clPanelTransparan4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Save1.png"))); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.setFocusable(false);
        btnSimpan.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Delete.png"))); // NOI18N
        btnHapus.setText("Hapus");
        btnHapus.setFocusable(false);
        btnHapus.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Tambah Resep Pasien");

        jLabel24.setText("Tanggal");

        jLabel25.setText("Nama Pasien");

        jLabel26.setText("Alamat");
        jLabel26.setToolTipText("");

        jLabel27.setText("Usia");

        jLabel7.setText("Jenis Layanan");

        txtTanggalResep.setDateFormatString("yyyy-MM-dd");

        txtAlamat.setColumns(20);
        txtAlamat.setRows(5);
        jScrollPane3.setViewportView(txtAlamat);

        cbJenisLayanan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Rawat Jalan", "Rawat Inap" }));

        spnThn.setModel(new javax.swing.SpinnerNumberModel());

        jLabel8.setText("th");

        bgbpjs.add(rbBPJS);
        rbBPJS.setText("BPJS");

        bgbpjs.add(rbNBPJS);
        rbNBPJS.setSelected(true);
        rbNBPJS.setText("non BPJS");
        rbNBPJS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbNBPJSActionPerformed(evt);
            }
        });

        jLabel10.setText("Nama Obat 1");

        spnJumlah1.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jLabel11.setText("Jumlah");

        jLabel12.setText("Nama Obat 2");

        jLabel13.setText("Jumlah");

        spnJumlah2.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jLabel14.setText("Nama Obat 3");

        jLabel15.setText("Jumlah");

        spnJumlah3.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jLabel16.setText("Nama Obat 4");

        jLabel17.setText("Jumlah");

        spnJumlah4.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jLabel18.setText("Nama Obat 5");

        jLabel28.setText("Jumlah");

        spnJumlah5.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jLabel29.setText("Nama Obat 6");

        jLabel30.setText("Jumlah");

        spnJumlah6.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jLabel31.setText("Nama Obat 7");

        jLabel32.setText("Jumlah");

        spnJumlah7.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jLabel33.setText("Nama Obat 8");

        jLabel34.setText("Jumlah");

        spnJumlah8.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jLabel35.setText("Nama Obat 9");

        jLabel36.setText("Jumlah");

        spnJumlah9.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jLabel37.setText("Nama Obat 10");

        jLabel38.setText("Jumlah");

        spnJumlah10.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        cbNamaObat2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat9.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat10.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout clPanelTransparan4Layout = new javax.swing.GroupLayout(clPanelTransparan4);
        clPanelTransparan4.setLayout(clPanelTransparan4Layout);
        clPanelTransparan4Layout.setHorizontalGroup(
            clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                                        .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel25)
                                            .addComponent(jLabel24)
                                            .addComponent(jLabel26))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                                            .addComponent(txtTanggalResep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtNamaPasien)
                                            .addComponent(cbJenisLayanan, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel14))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                                        .addGap(16, 16, 16)
                                        .addComponent(jLabel17)
                                        .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(spnJumlah10, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                                                    .addComponent(spnJumlah9)
                                                    .addComponent(spnJumlah8)
                                                    .addComponent(spnJumlah7)
                                                    .addComponent(spnJumlah6)
                                                    .addComponent(spnJumlah5)))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, clPanelTransparan4Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(spnJumlah3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(spnJumlah4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                    .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                                        .addComponent(rbBPJS)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(rbNBPJS)
                                            .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(spnJumlah1, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(spnJumlah2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE))))
                                    .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addComponent(jLabel27)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(spnThn, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel8))))
                            .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(18, 18, 18)
                                .addComponent(cbNamaObat4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(18, 18, 18)
                                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                                        .addComponent(cbNamaObat2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel13))
                                    .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                                        .addComponent(cbNamaObat3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel15))
                                    .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                                        .addComponent(cbNamaObat1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel11))))))
                    .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                        .addGap(174, 174, 174)
                        .addComponent(jLabel6))
                    .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                                .addComponent(jLabel35)
                                .addGap(18, 18, 18)
                                .addComponent(cbNamaObat9, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                                .addComponent(jLabel37)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbNamaObat10, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel36)
                            .addComponent(jLabel38)))
                    .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                                .addComponent(jLabel33)
                                .addGap(18, 18, 18)
                                .addComponent(cbNamaObat8, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                                    .addComponent(jLabel31)
                                    .addGap(18, 18, 18)
                                    .addComponent(cbNamaObat7, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                                        .addComponent(jLabel29)
                                        .addGap(18, 18, 18)
                                        .addComponent(cbNamaObat6, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, clPanelTransparan4Layout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addGap(18, 18, 18)
                                        .addComponent(cbNamaObat5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(18, 18, 18)
                        .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28)
                            .addComponent(jLabel30)
                            .addComponent(jLabel32)
                            .addComponent(jLabel34)))
                    .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(btnSimpan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnHapus)))
                .addGap(0, 10, Short.MAX_VALUE))
        );
        clPanelTransparan4Layout.setVerticalGroup(
            clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, clPanelTransparan4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel24))
                    .addComponent(txtTanggalResep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel27)
                    .addComponent(txtNamaPasien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnThn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addGap(11, 11, 11)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cbJenisLayanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbBPJS)
                    .addComponent(rbNBPJS))
                .addGap(18, 18, 18)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(spnJumlah1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(cbNamaObat1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(spnJumlah2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(cbNamaObat2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spnJumlah3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel14)
                    .addComponent(cbNamaObat3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(cbNamaObat4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel17))
                    .addComponent(spnJumlah4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18)
                        .addComponent(cbNamaObat5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel28))
                    .addComponent(spnJumlah5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel29)
                        .addComponent(cbNamaObat6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel30))
                    .addComponent(spnJumlah6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel31)
                        .addComponent(cbNamaObat7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel32))
                    .addComponent(spnJumlah7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33)
                    .addComponent(spnJumlah8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbNamaObat8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel34)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel35)
                        .addComponent(cbNamaObat9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel36))
                    .addComponent(spnJumlah9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel37)
                        .addComponent(cbNamaObat10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel38))
                    .addComponent(spnJumlah10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan)
                    .addComponent(btnHapus))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        clPanelTransparan5.setBackground(new java.awt.Color(255, 255, 255));
        clPanelTransparan5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblJual.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Tanggal Masuk", "Nama Obat", "Golongan Obat", "Satuan", "Jumlah Obat"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblJual.setEnabled(false);
        jScrollPane2.setViewportView(tblJual);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel19.setText("Obat Keluar");

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setText("Nama : ");

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel21.setText("Golongan :");

        cbCariGol1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--Tampilkan Semua--", "NARKOTIKA", "PSIKOTROPIKA", "(1a) ANALG,ANTIP,AN.INFL NON NARKOTIK", "(1b) ANALGETIK NARKOTIK", "(1c) ANTIPIRAI ", "(2) ANASTESI LOKAL", "(3) AN.EPILEPSI, AN.KONV, AN.ASIETAS, SEDATIV, HIPNOTIK, AN.PSIKOTIK", "(4) ANTI PARKINSON ", "(5) ANTI DEPRESI", "(6) ANTI MIGREN", "(7) ANTI ANGINA-ANTI ARITMIA", "(8) ANTI HIPERTENSI-DIURETIKA", "(9) GLUKOSIDA JANTUNG", "(10) OBAT PD SHOK-ANTI ASMA KORTIKOS", "(11) ANTI TUSIF", "(12) EKSPEKTORAN", "(13) ANTI INFLUENZA", "(14) ANTASIDA", "(15) OBAT DIARE-KESEIMBANGAN CAIRAN", "(16) LAKSAN", "(17) ANTI SPASMODIK", "(18) ANTI HISTAMIN", "(19) LARUTAN NUTRISI", "(20) TIROID ANTAGONIS", "(21) ANTI DIABETIK ORAL", "(22) ANTI DIABETIK PARENTERAL", "(23) VITAMIN DAN MINERAL", "(24) ANTI BAKTERI SISTEMIK, ANTISEPTIK", "(25) ANTI VIRUS", "(26) ANTI FUNGSI", "(27) ANTI TUBERKULOSIS", "(28) ANTI SEPTIK, DESINFEKTAN", "(29) ANTELMENTIK", "(30) ANTI AMUBIASIS", "(31) OBAT YG MEMPENGARUHI DARAH, ANTI ANEMIA", "(32) HEMOSTATIK", "(33) PRODUK DAN SUBTITUEN PLASMA", "(34) SERUM", "(35) AKSITOSIK", "(36) RELAKSAN UTERUS", "(37) ANTI INFLAMASI SALEP", "(38) PERANGSANG JARINGAN GRANULASI", "(39) ANTI BAKTERI", "(40) ANTI FUNGSI SALEP", "(41) ANTI SCABIES", "(42) ANTI SEPTIK", "(43) LAIN-LAIN OBAT KULIT", "(44) ANTI SISTEMIK MATA", "(45) ANASTESI LOKAL MATA ", "(46) ANTI INFEKSI MATA", "(47) LAIN-LAIN OBAT MATA", "(48) ANTI INFEKSI THT", "(49) LAIN-LAIN INFEKSI THT ", "(50) ANTI FILARIASIS", "(51) ANTI HEMOROID", "(52) ANTI EMETIK", "(53) ANTI HIPERKOLESTEROLEMIA", "(54) NOOTROPIK", "(55) IMMUNDILATOR", "(56) OBAT GIGI", "(57) OBAT TOPIKAL MULUT ", "(58) ALAT KESEHATAN HABIS PAKAI", "(59) REAGENSIA & LAIN-LAIN" }));

        btnCari1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/cari.png"))); // NOI18N
        btnCari1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCari1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCari1MouseClicked(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel23.setText("Urutkan Berdasar :");

        cbCariSort1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tanggal Masuk", "Nama Obat", "Golongan Obat", "Satuan", "Jumlah Obat" }));

        cbCariObat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbCariObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCariObatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout clPanelTransparan5Layout = new javax.swing.GroupLayout(clPanelTransparan5);
        clPanelTransparan5.setLayout(clPanelTransparan5Layout);
        clPanelTransparan5Layout.setHorizontalGroup(
            clPanelTransparan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(clPanelTransparan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(clPanelTransparan5Layout.createSequentialGroup()
                        .addGap(342, 342, 342)
                        .addComponent(jLabel19)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(clPanelTransparan5Layout.createSequentialGroup()
                        .addGroup(clPanelTransparan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(clPanelTransparan5Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(clPanelTransparan5Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(clPanelTransparan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbCariObat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbCariGol1, 0, 374, Short.MAX_VALUE))
                                .addGap(28, 28, 28)))
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbCariSort1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCari1)
                        .addGap(85, 85, 85))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)))
        );
        clPanelTransparan5Layout.setVerticalGroup(
            clPanelTransparan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, clPanelTransparan5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(clPanelTransparan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(clPanelTransparan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel20)
                        .addComponent(jLabel23)
                        .addComponent(cbCariSort1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbCariObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCari1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(clPanelTransparan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(cbCariGol1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblEx.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No. Resep", "Tanggal", "Nama Pasien", "Usia", "Alamat", "Jenis Layanan", "BPJS / Non BPJS", "Nama Obat", "Jumlah Pengambilan"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEx.setEnabled(false);
        jScrollPane1.setViewportView(tblEx);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clPanelTransparan1, javax.swing.GroupLayout.DEFAULT_SIZE, 1375, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(clPanelTransparan4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(clPanelTransparan3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(24, 24, 24))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(471, Short.MAX_VALUE)
                    .addComponent(clPanelTransparan5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(26, 26, 26)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(clPanelTransparan1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clPanelTransparan4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(clPanelTransparan3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46)))
                .addGap(37, 37, 37))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(50, 50, 50)
                    .addComponent(clPanelTransparan5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(353, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1366, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnKeluarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnKeluarMouseClicked
        // TODO add your handling code here:
        new MainMenu(setApoteker.tmpID, setApoteker.tmpPass).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnKeluarMouseClicked

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        String cekBpjs = rbBPJS.isSelected() ? "BPJS" : "non BPJS";
        String sql;        
        String sqlInsert, sqlUpdate, sqlDelete;
        String obat = null, gol = null, sat = null, Ag = null, As = null, Aobat = null;
        int jumlahSkr = 0, AjumlahSkr=0;
        Date exp = null, masuk = null, Amgd = null, Aexp = null;
        String tanggal = (txtTanggalResep.getDate().getYear()+1900) + "-" + 
                (txtTanggalResep.getDate().getMonth()+1) + "-" + 
                txtTanggalResep.getDate().getDate();
        setConnection koneksi;
        
        setResep rs = new setResep();
        rs.setTanggal(Date.valueOf(tanggal));
        rs.setNamaPasien(txtNamaPasien.getText());
        rs.setUsia(spnThn.getValue().toString());
        rs.setAlamat(txtAlamat.getText());
        rs.setJenisLayanan(cbJenisLayanan.getSelectedItem().toString());
        rs.setBpjs_nonBpjs(cekBpjs);  
        ArrayList<String> ar = new ArrayList<String>();
       if(!(cbNamaObat1.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
           ar.add(cbNamaObat1.getSelectedItem().toString());
       if(!(cbNamaObat2.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
           ar.add(cbNamaObat2.getSelectedItem().toString());
       if(!(cbNamaObat3.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
           ar.add(cbNamaObat3.getSelectedItem().toString());
       if(!(cbNamaObat4.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
           ar.add(cbNamaObat4.getSelectedItem().toString());
       if(!(cbNamaObat5.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
           ar.add(cbNamaObat5.getSelectedItem().toString());
       if(!(cbNamaObat6.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
           ar.add(cbNamaObat6.getSelectedItem().toString());
       if(!(cbNamaObat7.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
           ar.add(cbNamaObat7.getSelectedItem().toString());
       if(!(cbNamaObat8.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
           ar.add(cbNamaObat8.getSelectedItem().toString());
       if(!(cbNamaObat9.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
           ar.add(cbNamaObat9.getSelectedItem().toString());
       if(!(cbNamaObat10.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
           ar.add(cbNamaObat10.getSelectedItem().toString());
       }}}}}}}}}}
       String [] namaObat = new String[ar.size()];
       for (int i = 0; i < ar.size(); i++) {
             namaObat[i] = ar.get(i);
         }
       rs.setNamaObat(Arrays.toString(namaObat));
       
       ArrayList<String> an = new ArrayList<String>();
       if(!(spnJumlah1.getValue().toString().equals("0"))) {
           an.add(spnJumlah1.getValue().toString());
       if(!(spnJumlah2.getValue().toString().equals("0"))) {
           an.add(spnJumlah2.getValue().toString());
       if(!(spnJumlah3.getValue().toString().equals("0"))) {
           an.add(spnJumlah3.getValue().toString());
       if(!(spnJumlah4.getValue().toString().equals("0"))) {
           an.add(spnJumlah4.getValue().toString());
       if(!(spnJumlah5.getValue().toString().equals("0"))) {
           an.add(spnJumlah5.getValue().toString());
       if(!(spnJumlah6.getValue().toString().equals("0"))) {
           an.add(spnJumlah6.getValue().toString());
       if(!(spnJumlah7.getValue().toString().equals("0"))) {
           an.add(spnJumlah7.getValue().toString());
       if(!(spnJumlah8.getValue().toString().equals("0"))) {
           an.add(spnJumlah8.getValue().toString());
       if(!(spnJumlah9.getValue().toString().equals("0"))) {
           an.add(spnJumlah9.getValue().toString());
       if(!(spnJumlah10.getValue().toString().equals("0"))) {
           an.add(spnJumlah10.getValue().toString());
       }}}}}}}}}}
       
       String [] jmlObat = new String[an.size()];
       for (int i = 0; i < an.size(); i++) {
             jmlObat[i] = an.get(i);
         }
       rs.setJumlahObat(Arrays.toString(jmlObat));
        
        try {
            koneksi = new setConnection();
            stmt = koneksi.connection.createStatement();
            stmt1 = koneksi.connection.createStatement();
            stmt2 = koneksi.connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Gudang.class.getName()).log(Level.SEVERE, null, ex);
        }
        
// INPUT OBAT KE 1        
        if(cbNamaObat1.getSelectedItem().toString() != null || !(cbNamaObat1.getSelectedItem().toString().equals("")) || 
                !(cbNamaObat1.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat1.getSelectedItem().toString() + "'");
                while(rsObat.next() == true) {
                    masuk = rsObat.getDate("tglMasuk");
                    obat = rsObat.getString("namaObat");
                    gol = rsObat.getString("golObat");
                    sat = rsObat.getString("sat");
                    jumlahSkr = rsObat.getInt("jumlahSedia");
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            
            if(jumlahSkr >= (int) spnJumlah1.getValue() && cbNamaObat1.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat1.getSelectedItem().toString() +
                            "' AND satJ='" + sat + "' AND golObatJ='" + gol + "'");
                    while(rsJual.next() == true) {;
                        Aobat = rsJual.getString("namaObatJ");
                        Ag = rsJual.getString("golObatJ");
                        As = rsJual.getString("satJ");
                        AjumlahSkr = rsJual.getInt("jumlahSediaJ");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                
                int totalA = AjumlahSkr + (int) spnJumlah1.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat1.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat1.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                   
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah1.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }              
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah1.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
            
// INPUT OBAT KE 2                
        if(!(cbNamaObat2.getSelectedItem().toString().equals(null) && cbNamaObat2.getSelectedItem().toString().equals("") 
                && cbNamaObat2.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat2.getSelectedItem().toString() + "'");
                while(rsObat.next() == true) {
                    masuk = rsObat.getDate("tglMasuk");
                    obat = rsObat.getString("namaObat");
                    gol = rsObat.getString("golObat");
                    sat = rsObat.getString("sat");
                    jumlahSkr = rsObat.getInt("jumlahSedia");
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            
            if(jumlahSkr >= (int) spnJumlah2.getValue() && cbNamaObat2.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat2.getSelectedItem().toString() +
                            "' AND satJ='" + sat + "' AND golObatJ='" + gol + "'");
                    while(rsJual.next() == true) {;
                        Aobat = rsJual.getString("namaObatJ");
                        Ag = rsJual.getString("golObatJ");
                        As = rsJual.getString("satJ");
                        AjumlahSkr = rsJual.getInt("jumlahSediaJ");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                
                int totalA = AjumlahSkr + (int) spnJumlah2.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat2.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat2.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah2.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah2.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }

// INPUT OBAT KE 3
        if(!(cbNamaObat3.getSelectedItem().toString().equals(null) && cbNamaObat3.getSelectedItem().toString().equals("") 
                && cbNamaObat3.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat3.getSelectedItem().toString() + "'");
                while(rsObat.next() == true) {
                    masuk = rsObat.getDate("tglMasuk");
                    obat = rsObat.getString("namaObat");
                    gol = rsObat.getString("golObat");
                    sat = rsObat.getString("sat");
                    jumlahSkr = rsObat.getInt("jumlahSedia");
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            
            if(jumlahSkr >= (int) spnJumlah3.getValue() && cbNamaObat3.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat3.getSelectedItem().toString() +
                            "' AND satJ='" + sat + "' AND golObatJ='" + gol + "'");
                    while(rsJual.next() == true) {;
                        Aobat = rsJual.getString("namaObatJ");
                        Ag = rsJual.getString("golObatJ");
                        As = rsJual.getString("satJ");
                        AjumlahSkr = rsJual.getInt("jumlahSediaJ");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                
                int totalA = AjumlahSkr + (int) spnJumlah3.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat3.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat3.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah3.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah3.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
            
// INPUT OBAT KE 4                
        if(!(cbNamaObat4.getSelectedItem().toString().equals(null) && cbNamaObat4.getSelectedItem().toString().equals("") 
                && cbNamaObat4.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat4.getSelectedItem().toString() + "'");
                while(rsObat.next() == true) {
                    masuk = rsObat.getDate("tglMasuk");
                    obat = rsObat.getString("namaObat");
                    gol = rsObat.getString("golObat");
                    sat = rsObat.getString("sat");
                    jumlahSkr = rsObat.getInt("jumlahSedia");
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            
            if(jumlahSkr >= (int) spnJumlah4.getValue() && cbNamaObat4.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat4.getSelectedItem().toString() +
                            "' AND satJ='" + sat + "' AND golObatJ='" + gol + "'");
                    while(rsJual.next() == true) {;
                        Aobat = rsJual.getString("namaObatJ");
                        Ag = rsJual.getString("golObatJ");
                        As = rsJual.getString("satJ");
                        AjumlahSkr = rsJual.getInt("jumlahSediaJ");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                
                int totalA = AjumlahSkr + (int) spnJumlah4.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat4.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat4.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah4.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah4.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
            
// INPUT OBAT KE 5                
        if(!(cbNamaObat5.getSelectedItem().toString().equals(null) && cbNamaObat5.getSelectedItem().toString().equals("") 
                && cbNamaObat5.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat5.getSelectedItem().toString() + "'");
                while(rsObat.next() == true) {
                    masuk = rsObat.getDate("tglMasuk");
                    obat = rsObat.getString("namaObat");
                    gol = rsObat.getString("golObat");
                    sat = rsObat.getString("sat");
                    jumlahSkr = rsObat.getInt("jumlahSedia");
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            
            if(jumlahSkr >= (int) spnJumlah5.getValue() && cbNamaObat5.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat5.getSelectedItem().toString() +
                            "' AND satJ='" + sat + "' AND golObatJ='" + gol + "'");
                    while(rsJual.next() == true) {;
                        Aobat = rsJual.getString("namaObatJ");
                        Ag = rsJual.getString("golObatJ");
                        As = rsJual.getString("satJ");
                        AjumlahSkr = rsJual.getInt("jumlahSediaJ");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                
                int totalA = AjumlahSkr + (int) spnJumlah5.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat5.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat5.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah5.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah5.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }

// INPUT OBAT KE 6                
        if(!(cbNamaObat6.getSelectedItem().toString().equals(null) && cbNamaObat6.getSelectedItem().toString().equals("") 
                && cbNamaObat6.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat6.getSelectedItem().toString() + "'");
                while(rsObat.next() == true) {
                    masuk = rsObat.getDate("tglMasuk");
                    obat = rsObat.getString("namaObat");
                    gol = rsObat.getString("golObat");
                    sat = rsObat.getString("sat");
                    jumlahSkr = rsObat.getInt("jumlahSedia");
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            
            if(jumlahSkr >= (int) spnJumlah6.getValue() && cbNamaObat6.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat6.getSelectedItem().toString() +
                            "' AND satJ='" + sat + "' AND golObatJ='" + gol + "'");
                    while(rsJual.next() == true) {;
                        Aobat = rsJual.getString("namaObatJ");
                        Ag = rsJual.getString("golObatJ");
                        As = rsJual.getString("satJ");
                        AjumlahSkr = rsJual.getInt("jumlahSediaJ");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                
                int totalA = AjumlahSkr + (int) spnJumlah6.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat6.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat6.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah6.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah6.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
            
// INPUT OBAT KE 7                
        if(!(cbNamaObat7.getSelectedItem().toString().equals(null) && cbNamaObat7.getSelectedItem().toString().equals("") 
                && cbNamaObat7.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat7.getSelectedItem().toString() + "'");
                while(rsObat.next() == true) {
                    masuk = rsObat.getDate("tglMasuk");
                    obat = rsObat.getString("namaObat");
                    gol = rsObat.getString("golObat");
                    sat = rsObat.getString("sat");
                    jumlahSkr = rsObat.getInt("jumlahSedia");
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            
            if(jumlahSkr >= (int) spnJumlah7.getValue() && cbNamaObat7.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat7.getSelectedItem().toString() +
                            "' AND satJ='" + sat + "' AND golObatJ='" + gol + "'");
                    while(rsJual.next() == true) {;
                        Aobat = rsJual.getString("namaObatJ");
                        Ag = rsJual.getString("golObatJ");
                        As = rsJual.getString("satJ");
                        AjumlahSkr = rsJual.getInt("jumlahSediaJ");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                
                int totalA = AjumlahSkr + (int) spnJumlah7.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat7.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat7.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah7.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah7.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
            
// INPUT OBAT KE 8                
        if(!(cbNamaObat8.getSelectedItem().toString().equals(null) && cbNamaObat8.getSelectedItem().toString().equals("") 
                && cbNamaObat8.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat8.getSelectedItem().toString() + "'");
                while(rsObat.next() == true) {
                    masuk = rsObat.getDate("tglMasuk");
                    obat = rsObat.getString("namaObat");
                    gol = rsObat.getString("golObat");
                    sat = rsObat.getString("sat");
                    jumlahSkr = rsObat.getInt("jumlahSedia");
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            
            if(jumlahSkr >= (int) spnJumlah8.getValue() && cbNamaObat8.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat8.getSelectedItem().toString() +
                            "' AND satJ='" + sat + "' AND golObatJ='" + gol + "'");
                    while(rsJual.next() == true) {;
                        Aobat = rsJual.getString("namaObatJ");
                        Ag = rsJual.getString("golObatJ");
                        As = rsJual.getString("satJ");
                        AjumlahSkr = rsJual.getInt("jumlahSediaJ");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                
                int totalA = AjumlahSkr + (int) spnJumlah8.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat8.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat8.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah8.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah8.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
            
// INPUT OBAT KE 9
        if(!(cbNamaObat9.getSelectedItem().toString().equals(null) && cbNamaObat9.getSelectedItem().toString().equals("") 
                && cbNamaObat9.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat9.getSelectedItem().toString() + "'");
                while(rsObat.next() == true) {
                    masuk = rsObat.getDate("tglMasuk");
                    obat = rsObat.getString("namaObat");
                    gol = rsObat.getString("golObat");
                    sat = rsObat.getString("sat");
                    jumlahSkr = rsObat.getInt("jumlahSedia");
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            
            if(jumlahSkr >= (int) spnJumlah9.getValue() && cbNamaObat9.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat9.getSelectedItem().toString() +
                            "' AND satJ='" + sat + "' AND golObatJ='" + gol + "'");
                    while(rsJual.next() == true) {;
                        Aobat = rsJual.getString("namaObatJ");
                        Ag = rsJual.getString("golObatJ");
                        As = rsJual.getString("satJ");
                        AjumlahSkr = rsJual.getInt("jumlahSediaJ");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                
                int totalA = AjumlahSkr + (int) spnJumlah9.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat9.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat9.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah9.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah9.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
            
// INPUT OBAT KE 10                
        if(!(cbNamaObat10.getSelectedItem().toString().equals(null) && cbNamaObat10.getSelectedItem().toString().equals("") 
                && cbNamaObat10.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat10.getSelectedItem().toString() + "'");
                while(rsObat.next() == true) {
                    masuk = rsObat.getDate("tglMasuk");
                    obat = rsObat.getString("namaObat");
                    gol = rsObat.getString("golObat");
                    sat = rsObat.getString("sat");
                    jumlahSkr = rsObat.getInt("jumlahSedia");
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            
            if(jumlahSkr >= (int) spnJumlah10.getValue() && cbNamaObat10.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat10.getSelectedItem().toString() +
                            "' AND satJ='" + sat + "' AND golObatJ='" + gol + "'");
                    while(rsJual.next() == true) {;
                        Aobat = rsJual.getString("namaObatJ");
                        Ag = rsJual.getString("golObatJ");
                        As = rsJual.getString("satJ");
                        AjumlahSkr = rsJual.getInt("jumlahSediaJ");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                
                int totalA = AjumlahSkr + (int) spnJumlah10.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat10.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat10.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah10.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah10.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
        }}}}}}}}}}
       
       sql = "INSERT INTO DataResep (Tanggal,NamaPasien,Usia,Alamat,JenisLayanan,BpjsNonBpjs,namaObat,jmlObat) "
                + "VALUES ('"+Date.valueOf(tanggal) +  "',"
                + "'" + txtNamaPasien.getText() + "',"
                + "'" + spnThn.getValue() + "',"
                + "'" + txtAlamat.getText() + "',"
                + "'" + cbJenisLayanan.getSelectedItem() + "',"
                + "'" + cekBpjs + "',"
                + "'" + Arrays.toString(namaObat) + "',"
                + "'" + Arrays.toString(jmlObat) + "'"                 
                + ");";
        try{
            koneksi = new setConnection();
            stmt = koneksi.connection.createStatement();
            int berhasil = stmt.executeUpdate(sql);
            this.list.add(rs);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        updateTable();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void rbNBPJSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbNBPJSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbNBPJSActionPerformed

    private void btnCariMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCariMouseClicked
        // TODO add your handling code here:
        removeTable();
        String urut = null;
        
        switch(cbCariSort.getSelectedIndex()) {
            case 0: urut = "Tanggal"; break;
            case 1: urut = "NamaPasien"; break;
            case 2: urut = "Usia"; break;
            case 3: urut = "JenisLayanan"; break;
            case 4: urut = "BpjsNonBpjs"; break;
        }
        
        try {
            setConnection koneksi = new setConnection();
            stmt1 = koneksi.connection.createStatement();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        if(cbCariNama.getSelectedItem().toString().equals("") || cbCariNama.getSelectedItem().toString()==null) {
            if(cbCariGol.getSelectedItem().equals("--Tampilkan Semua--")) {
                try {
                    rsResep = stmt1.executeQuery("SELECT * FROM DataResep ORDER BY " + urut + "");
                    while(rsResep.next() == true) {
                        list.add(new setResep(rsResep.getString("NoResep"),
                                rsResep.getDate("Tanggal"),
                                rsResep.getString("NamaPasien"),
                                rsResep.getString("Usia"),
                                rsResep.getString("Alamat"),
                                rsResep.getString("JenisLayanan"),
                                rsResep.getString("BpjsNonBpjs"),
                                rsResep.getString("namaObat"),
                                rsResep.getString("jmlObat")));   
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            } else {
                try {
                    rsResep = stmt1.executeQuery("SELECT * FROM DataResep "
                            + "WHERE BpjsNonBpjs='" + cbCariGol.getSelectedItem().toString()
                            + "' OR JenisLayanan='" + cbCariGol.getSelectedItem().toString()
                            + "' ORDER BY " + urut + "");
                    while(rsResep.next() == true) {
                        list.add(new setResep(rsResep.getString("NoResep"),
                                rsResep.getDate("Tanggal"),
                                rsResep.getString("NamaPasien"),
                                rsResep.getString("Usia"),
                                rsResep.getString("Alamat"),
                                rsResep.getString("JenisLayanan"),
                                rsResep.getString("BpjsNonBpjs"),
                                rsResep.getString("namaObat"),
                                rsResep.getString("jmlObat")));  
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
        } else {
            if(cbCariGol.getSelectedItem().equals("--Tampilkan Semua--")) {
                try {
                    rsResep = stmt1.executeQuery("SELECT * FROM DataResep "
                            + "WHERE NamaPasien='" + cbCariNama.getSelectedItem().toString() 
                            + "' ORDER BY " + urut + "");
                    while(rsResep.next() == true) {
                        list.add(new setResep(rsResep.getString("NoResep"),
                                rsResep.getDate("Tanggal"),
                                rsResep.getString("NamaPasien"),
                                rsResep.getString("Usia"),
                                rsResep.getString("Alamat"),
                                rsResep.getString("JenisLayanan"),
                                rsResep.getString("BpjsNonBpjs"),
                                rsResep.getString("namaObat"),
                                rsResep.getString("jmlObat")));    
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            } else {
                try {
                    rsResep = stmt1.executeQuery("SELECT * FROM DataResep "
                            + "WHERE NamaPasien='" + cbCariNama.getSelectedItem().toString() 
                            + "' AND (BpjsNonBpjs='" + cbCariGol.getSelectedItem().toString() 
                            + "' OR JenisLayanan ='" + cbCariGol.getSelectedItem().toString()
                            + "') ORDER BY " + urut + "");
                    while(rsResep.next() == true) {
                        list.add(new setResep(rsResep.getString("NoResep"),
                                rsResep.getDate("Tanggal"),
                                rsResep.getString("NamaPasien"),
                                rsResep.getString("Usia"),
                                rsResep.getString("Alamat"),
                                rsResep.getString("JenisLayanan"),
                                rsResep.getString("BpjsNonBpjs"),
                                rsResep.getString("namaObat"),
                                rsResep.getString("jmlObat")));  
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
        }
        removeTable();
        updateTable();
    }//GEN-LAST:event_btnCariMouseClicked

    private void btnCari1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCari1MouseClicked
        // TODO add your handling code here:
        removeTableJual();
        String urut = null;
        
        switch(cbCariSort1.getSelectedIndex()) {
            case 0: urut = "tglMasukJ"; break;
            case 1: urut = "namaObatJ"; break;
            case 2: urut = "golObatJ"; break;
            case 3: urut = "satJ"; break;
            case 4: urut = "jumlahSediaJ"; break;
        }
        
        try {
            setConnection koneksi = new setConnection();
            stmt1 = koneksi.connection.createStatement();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        if(cbCariObat.getSelectedItem().toString().equals("") || cbCariObat.getSelectedItem().toString()==null) {
            if(cbCariGol1.getSelectedItem().equals("--Tampilkan Semua--")) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual ORDER BY " + urut + "");
                    while(rsJual.next() == true) {
                        list1.add(new setObat(rsJual.getDate("tglMasukJ"), 
                                rsJual.getString("namaObatJ"), 
                                rsJual.getString("golObatJ"), 
                                rsJual.getString("satJ"), 
                                rsJual.getInt("jumlahSediaJ")));    
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            } else {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual "
                            + "WHERE golObatJ='" + cbCariGol1.getSelectedItem().toString() 
                            + "' ORDER BY " + urut + "");
                    while(rsJual.next() == true) {
                        list1.add(new setObat(rsJual.getDate("tglMasukJ"), 
                                rsJual.getString("namaObatJ"), 
                                rsJual.getString("golObatJ"), 
                                rsJual.getString("satJ"), 
                                rsJual.getInt("jumlahSediaJ")));    
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
        } else {
            if(cbCariGol1.getSelectedItem().equals("--Tampilkan Semua--")) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual "
                            + "WHERE namaObatJ='" + cbCariObat.getSelectedItem().toString()
                            + "' ORDER BY " + urut + "");
                    while(rsJual.next() == true) {
                        list1.add(new setObat(rsJual.getDate("tglMasukJ"), 
                                rsJual.getString("namaObatJ"), 
                                rsJual.getString("golObatJ"), 
                                rsJual.getString("satJ"), 
                                rsJual.getInt("jumlahSediaJ")));     
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            } else {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual "
                            + "WHERE namaObatJ='" + cbCariObat.getSelectedItem().toString()
                            + "' AND golObatJ='" + cbCariGol1.getSelectedItem().toString() 
                            + "' ORDER BY " + urut + "");
                    while(rsJual.next() == true) {
                        list1.add(new setObat(rsJual.getDate("tglMasukJ"), 
                                rsJual.getString("namaObatJ"), 
                                rsJual.getString("golObatJ"), 
                                rsJual.getString("satJ"), 
                                rsJual.getInt("jumlahSediaJ")));    
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
        }
        removeTableJual();
        updateTableJual();
    }//GEN-LAST:event_btnCari1MouseClicked

    private void cbCariObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCariObatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbCariObatActionPerformed

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
            java.util.logging.Logger.getLogger(Resep.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Resep.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Resep.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Resep.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Resep().setVisible(true);
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
    private javax.swing.ButtonGroup bgbpjs;
    private javax.swing.JLabel btnCari;
    private javax.swing.JLabel btnCari1;
    private javax.swing.JButton btnHapus;
    private javax.swing.JLabel btnKeluar;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cbCariGol;
    private javax.swing.JComboBox<String> cbCariGol1;
    private javax.swing.JComboBox<String> cbCariNama;
    private javax.swing.JComboBox<String> cbCariObat;
    private javax.swing.JComboBox<String> cbCariSort;
    private javax.swing.JComboBox<String> cbCariSort1;
    private javax.swing.JComboBox<String> cbJenisLayanan;
    private javax.swing.JComboBox<String> cbNamaObat1;
    private javax.swing.JComboBox<String> cbNamaObat10;
    private javax.swing.JComboBox<String> cbNamaObat2;
    private javax.swing.JComboBox<String> cbNamaObat3;
    private javax.swing.JComboBox<String> cbNamaObat4;
    private javax.swing.JComboBox<String> cbNamaObat5;
    private javax.swing.JComboBox<String> cbNamaObat6;
    private javax.swing.JComboBox<String> cbNamaObat7;
    private javax.swing.JComboBox<String> cbNamaObat8;
    private javax.swing.JComboBox<String> cbNamaObat9;
    private PanelTransparan.ClPanelTransparan clPanelTransparan1;
    private PanelTransparan.ClPanelTransparan clPanelTransparan3;
    private PanelTransparan.ClPanelTransparan clPanelTransparan4;
    private PanelTransparan.ClPanelTransparan clPanelTransparan5;
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
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JRadioButton rbBPJS;
    private javax.swing.JRadioButton rbNBPJS;
    private javax.swing.JSpinner spnJumlah1;
    private javax.swing.JSpinner spnJumlah10;
    private javax.swing.JSpinner spnJumlah2;
    private javax.swing.JSpinner spnJumlah3;
    private javax.swing.JSpinner spnJumlah4;
    private javax.swing.JSpinner spnJumlah5;
    private javax.swing.JSpinner spnJumlah6;
    private javax.swing.JSpinner spnJumlah7;
    private javax.swing.JSpinner spnJumlah8;
    private javax.swing.JSpinner spnJumlah9;
    private javax.swing.JSpinner spnThn;
    private javax.swing.JTable tblEx;
    private javax.swing.JTable tblJual;
    private javax.swing.JTextArea txtAlamat;
    private javax.swing.JTextField txtNamaPasien;
    private javax.swing.JLabel txtTanggal;
    private com.toedter.calendar.JDateChooser txtTanggalResep;
    private javax.swing.JLabel txtWelcome;
    // End of variables declaration//GEN-END:variables
}
