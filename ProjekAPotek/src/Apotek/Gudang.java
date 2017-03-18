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
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author user
 */
public class Gudang extends javax.swing.JFrame {
    Statement stmt1, stmt2, stmt3;
    ResultSet rsGudang, rsApotek, rsCari, rsGudang2;
    String[] title = {"Tanggal Masuk", "Nama Obat", "Golongan Obat", "Satuan", "Jumlah Obat Masuk", "Tanggal Kadaluarsa"};
    ArrayList<setGudang> list = new ArrayList<setGudang>();
    DefaultTableModel dm;
    /**
     * Creates new form Gudang
     */
    public Gudang() {
        this.setWaktu();
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        try {
            setConnection koneksi = new setConnection();
            stmt1 = koneksi.connection.createStatement();
            rsGudang = stmt1.executeQuery("SELECT * FROM DataGudang ORDER BY namaObatG");
            while(rsGudang.next() == true) {
                list.add(new setGudang(rsGudang.getDate("tglMasukG"), 
                        rsGudang.getString("namaObatG"), 
                        rsGudang.getString("golObatG"), 
                        rsGudang.getString("satG"),
                        rsGudang.getInt("jumlahSediaG"),
                        rsGudang.getDate("exdateG")));    
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        
//        txtWelcome.setText(MainMenu.txtWelcome.getText());
        this.tambah();
        updateTable();
        try {
            SearchSugges();
        } catch (SQLException ex) {
            Logger.getLogger(Gudang.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableFilter();
    }
    
    private void filter(String query){
        dm = (DefaultTableModel) tblEx.getModel();      
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(dm);        
        tblEx.setRowSorter(tr);       
        tr.setRowFilter(RowFilter.regexFilter(query));
       
    }
    
    private void tableFilter(){
        // TableRowSorter<updateTable> sorter;
        tblEx.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        tblEx.setAutoCreateRowSorter(true);
        TableRowSorter<DefaultTableModel> sorter  = new TableRowSorter<DefaultTableModel>((DefaultTableModel) tblEx.getModel());
        tblEx.setRowSorter(sorter);
       
    }
    
    private void updateTable() {
        Object[][] data = new Object[this.list.size()][6];
        int x = 0;
        for(setGudang o: this.list) {
            data[x][0] = o.getTanggal();
            data[x][1] = o.getNamaObat();
            data[x][2] = o.getGolObat();
            data[x][3] = o.getSat();
            data[x][4] = o.getSisaGudang();
            data[x][5] = o.getExdate();
            ++x;
        }
        tblEx.setModel(new DefaultTableModel(data, title));
    }
    
    private void clearAll(){
        txttglMasuk.setCalendar(Calendar.getInstance());
        txtNamaObat.setText("");
        cbGolObat.setSelectedIndex(0);
        cbSatuan.setSelectedIndex(0);
        spJumlah.setValue(1);
        txtEx.setCalendar(Calendar.getInstance());
    }
    
    private void clearAllA(){
        cbNamaObatA.setSelectedIndex(0);
        cbGolObatA.setSelectedIndex(0);
        cbSatA.setSelectedIndex(0);
        spJumlahA.setValue(1);
    
    }
    
    private void removeTable() {
        DefaultTableModel model = (DefaultTableModel)tblEx.getModel();
        while (model.getRowCount() > 0){
            for (int i = 0; i < model.getRowCount(); ++i){
                model.removeRow(i);
            }
        }
    }
    private void SearchSugges() throws SQLException{
        ArrayList<String> li = new ArrayList<String>();
        ArrayList<String> ki = new ArrayList<String>();
        
        rsCari = stmt1.executeQuery("SELECT * FROM DataGudang WHERE jumlahSediaG>0 ORDER BY namaObatG");
        li.add("");
        ki.add("");
        while(rsCari.next()==true){
            li.add(rsCari.getString("namaObatG"));
            ki.add(rsCari.getString("namaObatG")); 
        }
        
        String [] cariObat = new String[li.size()];
        cariObat = li.toArray(cariObat);
        
         String [] namaObat = new String[ki.size()];
        cariObat = ki.toArray(namaObat);
        
        DefaultComboBoxModel<String> cO = new DefaultComboBoxModel<String>(cariObat);
        DefaultComboBoxModel<String> nO = new DefaultComboBoxModel<String>(namaObat);

//        cbCariNama.setModel(cO);
//        AutoCompleteDecorator.decorate(this.cbCariNama);
        
        cbNamaObatA.setModel(nO);
        AutoCompleteDecorator.decorate(this.cbNamaObatA);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new Apotek.SetImage("/image/wall.png");
        clPanelTransparan1 = new PanelTransparan.ClPanelTransparan();
        txtWelcome = new javax.swing.JLabel();
        txtTanggal = new javax.swing.JLabel();
        btnKeluar = new javax.swing.JLabel();
        clPanelTransparan3 = new PanelTransparan.ClPanelTransparan();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEx = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbCariNama = new javax.swing.JTextField();
        clPanelTransparan4 = new PanelTransparan.ClPanelTransparan();
        btnSimpan = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cbGolObat = new javax.swing.JComboBox<>();
        txtNamaObat = new javax.swing.JTextField();
        cbSatuan = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        spJumlah = new javax.swing.JSpinner();
        jLabel19 = new javax.swing.JLabel();
        txttglMasuk = new com.toedter.calendar.JDateChooser();
        txtEx = new com.toedter.calendar.JDateChooser();
        jLabel20 = new javax.swing.JLabel();
        btnTambah = new javax.swing.JButton();
        btnPindahObat = new javax.swing.JButton();
        clPanelTransparan2 = new PanelTransparan.ClPanelTransparan();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        cbDariA = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        cbKeA = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        txtTglMasukA = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        cbGolObatA = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        cbSatA = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        spJumlahA = new javax.swing.JSpinner();
        btnSimpanA = new javax.swing.JButton();
        cbNamaObatA = new javax.swing.JComboBox<>();
        btnSet = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gudang");

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
                .addGap(445, 445, 445)
                .addComponent(txtTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        clPanelTransparan3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblEx.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Tanggal Masuk", "Nama Obat", "Golongan Obat", "Jumlah Persediaan", "Satuan", "Tanggal Kadaluarsa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEx.setEnabled(false);
        jScrollPane1.setViewportView(tblEx);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Obat Gudang");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Nama : ");

        cbCariNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCariNamaActionPerformed(evt);
            }
        });
        cbCariNama.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cbCariNamaKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout clPanelTransparan3Layout = new javax.swing.GroupLayout(clPanelTransparan3);
        clPanelTransparan3.setLayout(clPanelTransparan3Layout);
        clPanelTransparan3Layout.setHorizontalGroup(
            clPanelTransparan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan3Layout.createSequentialGroup()
                .addGroup(clPanelTransparan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(clPanelTransparan3Layout.createSequentialGroup()
                        .addGap(335, 335, 335)
                        .addComponent(jLabel1))
                    .addGroup(clPanelTransparan3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(clPanelTransparan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 850, Short.MAX_VALUE)
                            .addGroup(clPanelTransparan3Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbCariNama, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        clPanelTransparan3Layout.setVerticalGroup(
            clPanelTransparan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, clPanelTransparan3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(29, 29, 29)
                .addGroup(clPanelTransparan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbCariNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
                .addContainerGap())
        );

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

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Tambah Obat Baru dari Dinas");

        jLabel7.setText("Nama Obat");

        jLabel8.setText("Golongan Obat");

        jLabel9.setText("Satuan");

        cbGolObat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NARKOTIKA", "PSIKOTROPIKA", "(1a) ANALG,ANTIP,AN.INFL NON NARKOTIK", "(1b) ANALGETIK NARKOTIK", "(1c) ANTIPIRAI ", "(2) ANASTESI LOKAL", "(3) AN.EPILEPSI, AN.KONV, AN.ASIETAS, SEDATIV, HIPNOTIK, AN.PSIKOTIK", "(4) ANTI PARKINSON ", "(5) ANTI DEPRESI", "(6) ANTI MIGREN", "(7) ANTI ANGINA-ANTI ARITMIA", "(8) ANTI HIPERTENSI-DIURETIKA", "(9) GLUKOSIDA JANTUNG", "(10) OBAT PD SHOK-ANTI ASMA KORTIKOS", "(11) ANTI TUSIF", "(12) EKSPEKTORAN", "(13) ANTI INFLUENZA", "(14) ANTASIDA", "(15) OBAT DIARE-KESEIMBANGAN CAIRAN", "(16) LAKSAN", "(17) ANTI SPASMODIK", "(18) ANTI HISTAMIN", "(19) LARUTAN NUTRISI", "(20) TIROID ANTAGONIS", "(21) ANTI DIABETIK ORAL", "(22) ANTI DIABETIK PARENTERAL", "(23) VITAMIN DAN MINERAL", "(24) ANTI BAKTERI SISTEMIK, ANTISEPTIK", "(25) ANTI VIRUS", "(26) ANTI FUNGSI", "(27) ANTI TUBERKULOSIS", "(28) ANTI SEPTIK, DESINFEKTAN", "(29) ANTELMENTIK", "(30) ANTI AMUBIASIS", "(31) OBAT YG MEMPENGARUHI DARAH, ANTI ANEMIA", "(32) HEMOSTATIK", "(33) PRODUK DAN SUBTITUEN PLASMA", "(34) SERUM", "(35) AKSITOSIK", "(36) RELAKSAN UTERUS", "(37) ANTI INFLAMASI SALEP", "(38) PERANGSANG JARINGAN GRANULASI", "(39) ANTI BAKTERI", "(40) ANTI FUNGSI SALEP", "(41) ANTI SCABIES", "(42) ANTI SEPTIK", "(43) LAIN-LAIN OBAT KULIT", "(44) ANTI SISTEMIK MATA", "(45) ANASTESI LOKAL MATA ", "(46) ANTI INFEKSI MATA", "(47) LAIN-LAIN OBAT MATA", "(48) ANTI INFEKSI THT", "(49) LAIN-LAIN INFEKSI THT ", "(50) ANTI FILARIASIS", "(51) ANTI HEMOROID", "(52) ANTI EMETIK", "(53) ANTI HIPERKOLESTEROLEMIA", "(54) NOOTROPIK", "(55) IMMUNDILATOR", "(56) OBAT GIGI", "(57) OBAT TOPIKAL MULUT ", "(58) ALAT KESEHATAN HABIS PAKAI", "(59) REAGENSIA & LAIN-LAIN" }));

        txtNamaObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaObatActionPerformed(evt);
            }
        });

        cbSatuan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tablet", "Botol", "Injeksi", "Krim", "Gel", "Kapsul", "Spray", "Infus", "Larutan", "Serbuk", "Buah", "Bungkus", "Tabung" }));

        jLabel10.setText("Jumlah");

        spJumlah.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));

        jLabel19.setText("Tanggal");

        txttglMasuk.setDateFormatString("yyyy-MM-dd");

        txtEx.setToolTipText("");
        txtEx.setDateFormatString("yyyy-MM-dd");

        jLabel20.setText("Tanggal Kadaluarsa");

        javax.swing.GroupLayout clPanelTransparan4Layout = new javax.swing.GroupLayout(clPanelTransparan4);
        clPanelTransparan4.setLayout(clPanelTransparan4Layout);
        clPanelTransparan4Layout.setHorizontalGroup(
            clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, clPanelTransparan4Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSimpan))
                    .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                        .addGap(174, 174, 174)
                        .addComponent(jLabel6))
                    .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbSatuan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txttglMasuk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtNamaObat)
                                    .addComponent(cbGolObat, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtEx, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(spJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        clPanelTransparan4Layout.setVerticalGroup(
            clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, clPanelTransparan4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txttglMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNamaObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cbGolObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(spJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtEx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addGap(10, 10, 10)
                .addComponent(btnSimpan)
                .addGap(34, 34, 34))
        );

        btnTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Add.png"))); // NOI18N
        btnTambah.setText("Tambah Obat");
        btnTambah.setFocusable(false);
        btnTambah.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnTambah.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnTambah.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTambahMouseClicked(evt);
            }
        });
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnPindahObat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/PindahObat.png"))); // NOI18N
        btnPindahObat.setText("Pindah Obat");
        btnPindahObat.setFocusable(false);
        btnPindahObat.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnPindahObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPindahObatActionPerformed(evt);
            }
        });

        clPanelTransparan2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Pindah Obat");

        jLabel12.setText("Dari :");

        cbDariA.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Gudang", "Apotek" }));

        jLabel13.setText("Ke :");

        cbKeA.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Apotek", "Gudang", "Pustu Kletek", "Pustu Sadang", "Pustu Wage", "Polindes Bohar", "Polindes Jemundo", "Polindes Kedungturi", "Polindes Sepanjang", "Polindes Tawangsari ", "UGD", "LABORAT", "BKIA", "PERAWATAN", "IMUNISASI", "PROGRAM GIZI", "POLI GIGI" }));

        jLabel14.setText("Tanggal :");

        txtTglMasukA.setDateFormatString("dd-MM-yyyy");

        jLabel15.setText("Nama Obat :");

        jLabel16.setText("Golongan Obat :");

        cbGolObatA.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NARKOTIKA", "PSIKOTROPIKA", "(1a) ANALG,ANTIP,AN.INFL NON NARKOTIK", "(1b) ANALGETIK NARKOTIK", "(1c) ANTIPIRAI ", "(2) ANASTESI LOKAL", "(3) AN.EPILEPSI, AN.KONV, AN.ASIETAS, SEDATIV, HIPNOTIK, AN.PSIKOTIK", "(4) ANTI PARKINSON ", "(5) ANTI DEPRESI", "(6) ANTI MIGREN", "(7) ANTI ANGINA-ANTI ARITMIA", "(8) ANTI HIPERTENSI-DIURETIKA", "(9) GLUKOSIDA JANTUNG", "(10) OBAT PD SHOK-ANTI ASMA KORTIKOS", "(11) ANTI TUSIF", "(12) EKSPEKTORAN", "(13) ANTI INFLUENZA", "(14) ANTASIDA", "(15) OBAT DIARE-KESEIMBANGAN CAIRAN", "(16) LAKSAN", "(17) ANTI SPASMODIK", "(18) ANTI HISTAMIN", "(19) LARUTAN NUTRISI", "(20) TIROID ANTAGONIS", "(21) ANTI DIABETIK ORAL", "(22) ANTI DIABETIK PARENTERAL", "(23) VITAMIN DAN MINERAL", "(24) ANTI BAKTERI SISTEMIK, ANTISEPTIK", "(25) ANTI VIRUS", "(26) ANTI FUNGSI", "(27) ANTI TUBERKULOSIS", "(28) ANTI SEPTIK, DESINFEKTAN", "(29) ANTELMENTIK", "(30) ANTI AMUBIASIS", "(31) OBAT YG MEMPENGARUHI DARAH, ANTI ANEMIA", "(32) HEMOSTATIK", "(33) PRODUK DAN SUBTITUEN PLASMA", "(34) SERUM", "(35) AKSITOSIK", "(36) RELAKSAN UTERUS", "(37) ANTI INFLAMASI SALEP", "(38) PERANGSANG JARINGAN GRANULASI", "(39) ANTI BAKTERI", "(40) ANTI FUNGSI SALEP", "(41) ANTI SCABIES", "(42) ANTI SEPTIK", "(43) LAIN-LAIN OBAT KULIT", "(44) ANTI SISTEMIK MATA", "(45) ANASTESI LOKAL MATA ", "(46) ANTI INFEKSI MATA", "(47) LAIN-LAIN OBAT MATA", "(48) ANTI INFEKSI THT", "(49) LAIN-LAIN INFEKSI THT ", "(50) ANTI FILARIASIS", "(51) ANTI HEMOROID", "(52) ANTI EMETIK", "(53) ANTI HIPERKOLESTEROLEMIA", "(54) NOOTROPIK", "(55) IMMUNDILATOR", "(56) OBAT GIGI", "(57) OBAT TOPIKAL MULUT ", "(58) ALAT KESEHATAN HABIS PAKAI", "(59) REAGENSIA & LAIN-LAIN" }));

        jLabel17.setText("Satuan :");

        cbSatA.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tablet", "Botol", "Injeksi", "Krim", "Gel", "Kapsul", "Spray", "Infus", "Larutan", "Serbuk", "Buah", "Bungkus", "Tabung" }));

        jLabel18.setText("Jumlah :");

        spJumlahA.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));

        btnSimpanA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Save1.png"))); // NOI18N
        btnSimpanA.setText("Simpan");
        btnSimpanA.setFocusable(false);
        btnSimpanA.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSimpanA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanAActionPerformed(evt);
            }
        });

        cbNamaObatA.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnSet.setText("Set Golongan dan Satuan");
        btnSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout clPanelTransparan2Layout = new javax.swing.GroupLayout(clPanelTransparan2);
        clPanelTransparan2.setLayout(clPanelTransparan2Layout);
        clPanelTransparan2Layout.setHorizontalGroup(
            clPanelTransparan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan2Layout.createSequentialGroup()
                .addGroup(clPanelTransparan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(clPanelTransparan2Layout.createSequentialGroup()
                        .addGroup(clPanelTransparan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(clPanelTransparan2Layout.createSequentialGroup()
                                .addGap(201, 201, 201)
                                .addComponent(jLabel11))
                            .addGroup(clPanelTransparan2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbDariA, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbKeA, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(clPanelTransparan2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(clPanelTransparan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel14))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(clPanelTransparan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(cbGolObatA, javax.swing.GroupLayout.Alignment.LEADING, 0, 1, Short.MAX_VALUE)
                                    .addComponent(txtTglMasukA, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbSatA, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, clPanelTransparan2Layout.createSequentialGroup()
                                        .addComponent(spJumlahA, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnSet, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(cbNamaObatA, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(0, 15, Short.MAX_VALUE))
                    .addGroup(clPanelTransparan2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(clPanelTransparan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, clPanelTransparan2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnSimpanA))
                            .addGroup(clPanelTransparan2Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        clPanelTransparan2Layout.setVerticalGroup(
            clPanelTransparan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(clPanelTransparan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(cbDariA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(cbKeA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(clPanelTransparan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTglMasukA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(clPanelTransparan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(cbNamaObatA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(clPanelTransparan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(spJumlahA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSet))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(clPanelTransparan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(cbGolObatA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(clPanelTransparan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(cbSatA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSimpanA)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clPanelTransparan1, javax.swing.GroupLayout.DEFAULT_SIZE, 1409, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(btnTambah)
                        .addGap(116, 116, 116)
                        .addComponent(btnPindahObat))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(clPanelTransparan2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(clPanelTransparan4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(clPanelTransparan3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(clPanelTransparan1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnTambah)
                            .addComponent(btnPindahObat))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clPanelTransparan4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clPanelTransparan2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(clPanelTransparan3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTambahMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_btnTambahMouseClicked

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        this.tambah();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnKeluarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnKeluarMouseClicked
        // TODO add your handling code here:
        new MainMenu(setApoteker.tmpID, setApoteker.tmpPass).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnKeluarMouseClicked

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        String sqlSimpan, sqlDelete, obat = null, g = null, s = null;
        int jumlahSkr = 0;
        String simMas = (txttglMasuk.getDate().getYear()+1900) + "-" + 
                (txttglMasuk.getDate().getMonth()+1) + "-" + 
                txttglMasuk.getDate().getDate();
        String simEx = (txtEx.getDate().getYear()+1900) + "-" + 
                (txtEx.getDate().getMonth()+1) + "-" + txtEx.getDate().getDate();
        Date exp = null, tgl = null;
        
        try {
            setConnection koneksi = new setConnection();
            stmt1 = koneksi.connection.createStatement();
            rsGudang = stmt1.executeQuery("SELECT * FROM DataGudang WHERE namaObatG='" + txtNamaObat.getText() +
                    "' AND exdateG='" + simEx + "' AND satG='" + cbSatuan.getSelectedItem().toString() + 
                    "' AND golObatG='" + cbGolObat.getSelectedItem().toString() + "'");
            while(rsGudang.next() == true) {
                tgl = rsGudang.getDate("tglMasukG");
                obat = rsGudang.getString("namaObatG");
                g = rsGudang.getString("golObatG");
                s = rsGudang.getString("satG");
                jumlahSkr = rsGudang.getInt("jumlahSediaG");
                exp = rsGudang.getDate("exdateG");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        int total = jumlahSkr + (int) spJumlah.getValue();
        
        sqlSimpan = "INSERT INTO DataGudang (tglMasukG,namaObatG,golObatG,satG,jumlahSediaG,exdateG) "
                + "VALUES ('" + simMas + "',"
                + "'" + txtNamaObat.getText() + "',"
                + "'" + cbGolObat.getSelectedItem().toString() + "',"
                + "'" + cbSatuan.getSelectedItem().toString() + "',"
                + "'" + total + "',"
                + "'" + simEx + "');";
                
        try {
           setConnection koneksi = new setConnection();
           stmt2 = koneksi.connection.createStatement();
           int berhasil = stmt2.executeUpdate(sqlSimpan);
        } catch (SQLException errMsg) {
            System.out.println(errMsg);
        }
        setGudang gd = new setGudang();
        gd.setTanggal(Date.valueOf(simMas));
        gd.setNamaObat(txtNamaObat.getText());
        gd.setGolObat(cbGolObat.getSelectedItem().toString());
        gd.setSat(cbSatuan.getSelectedItem().toString());
        gd.setSisaGudang((int) spJumlah.getValue());
        gd.setExdate(Date.valueOf(simEx));
        this.list.add(gd);
        updateTable();
        
        sqlDelete = "DELETE FROM DataGudang "
                + "WHERE namaObatG='" + txtNamaObat.getText() + 
                "' AND exdateG='" + simEx + 
                "' AND jumlahSediaG=" + jumlahSkr + 
                " AND satG='" + cbSatuan.getSelectedItem().toString() + 
                "' AND golObatG='" + cbGolObat.getSelectedItem().toString() + "'";
            
        try {
            setConnection koneksi = new setConnection();
            stmt2 = koneksi.connection.createStatement();
            int berhasil = stmt2.executeUpdate(sqlDelete);
        } catch (SQLException errMsg) {
            System.out.println(errMsg.getMessage());
        }
        this.list.remove(new setGudang(tgl, txtNamaObat.getText(), g, s, jumlahSkr, exp));
        updateTable();
        clearAll();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnSimpanAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanAActionPerformed
        // TODO add your handling code here:
        int transaksi;
        String sqlPindah_new;
        String sqlPindah, sqlUpdate, sqlDelete;
        String obat = null, gol = null, sat = null, Ag = null, As = null, Aobat = null;
        int jumlahSkr = 0, AjumlahSkr = 0;
        int isiJumlah = (int) spJumlahA.getValue();
        String simMasA = (txtTglMasukA.getDate().getYear()+1900) + "-" + 
                (txtTglMasukA.getDate().getMonth()+1) + "-" + 
                txtTglMasukA.getDate().getDate();
        Date exp = null, mGd = null, Amgd = null, Aexp = null;
        
        setConnection koneksi;
        try {
            koneksi = new setConnection();
            stmt1 = koneksi.connection.createStatement();
            stmt2 = koneksi.connection.createStatement();
            stmt3 = koneksi.connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Gudang.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Transaksi -->        
        sqlPindah_new = "INSERT INTO Transaksi (namaObat,golongan,dari,ke,tanggal,jumlah) "
                        + "VALUES ('" + cbNamaObatA.getSelectedItem().toString() + "',"
                        + "'" +  cbGolObatA.getSelectedItem().toString() + "',"
                        + "'" + cbDariA.getSelectedItem().toString() + "',"
                        + "'" + cbKeA.getSelectedItem().toString() + "',"
                        + "'" + simMasA + "',"
                        + "'" + (int) spJumlahA.getValue() + "');";
        try {
            transaksi = stmt3.executeUpdate(sqlPindah_new);
        } catch (SQLException ex) {
            Logger.getLogger(Gudang.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(cbDariA.getSelectedItem().equals("Gudang") && cbKeA.getSelectedItem().equals("Apotek")) {
            try {
                rsGudang = stmt2.executeQuery("SELECT * FROM DataGudang WHERE namaObatG='" + cbNamaObatA.getSelectedItem().toString() +
                     "' AND satG='" + cbSatA.getSelectedItem().toString() + 
                    "' AND golObatG='" + cbGolObatA.getSelectedItem().toString() + "'");
                while(rsGudang.next() == true) {
                    mGd = rsGudang.getDate("tglMasukG");
                    obat = rsGudang.getString("namaObatG");
                    gol = rsGudang.getString("golObatG");
                    sat = rsGudang.getString("satG");
                    jumlahSkr = rsGudang.getInt("jumlahSediaG");
                    exp = rsGudang.getDate("exdateG");
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }

            if ( cbNamaObatA.getSelectedItem().toString().equalsIgnoreCase(obat) && cbGolObatA.getSelectedItem().toString().equals(gol) &&
                    cbSatA.getSelectedItem().toString().equals(sat)) {
                try {
                    rsApotek = stmt1.executeQuery("SELECT * FROM DataObat WHERE namaObat='" +  cbNamaObatA.getSelectedItem().toString() +
                            "' AND exdate='" + exp + "' AND sat='" + cbSatA.getSelectedItem().toString() + 
                            "' AND golObat='" + cbGolObatA.getSelectedItem().toString() + "'");
                    while(rsApotek.next() == true) {
                        Amgd = rsApotek.getDate("tglMasuk");
                        Aobat = rsApotek.getString("namaObat");
                        Ag = rsApotek.getString("golObat");
                        As = rsApotek.getString("sat");
                        AjumlahSkr = rsApotek.getInt("jumlahSedia");
                        Aexp = rsApotek.getDate("exdate");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                
                int totalA = isiJumlah + AjumlahSkr;
                
                if (isiJumlah <= jumlahSkr) {
                    sqlPindah = "INSERT INTO DataObat (tglMasuk,namaObat,golObat,sat,jumlahSedia,exdate) "
                        + "VALUES ('" + simMasA + "',"
                        + "'" +  cbNamaObatA.getSelectedItem().toString() + "',"
                        + "'" + cbGolObatA.getSelectedItem().toString() + "',"
                        + "'" + cbSatA.getSelectedItem().toString() + "',"
                        + "'" + totalA + "',"
                        + "'" + exp + "');";
                    
                    try {
                       int berhasil = stmt2.executeUpdate(sqlPindah);
                    } catch (SQLException errMsg) {
                        System.out.println(errMsg);
                    }
                    
                    sqlDelete = "DELETE FROM DataObat "
                            + "WHERE namaObat='" +  cbNamaObatA.getSelectedItem().toString() + 
                            "' AND exdate='" + Aexp + 
                            "' AND jumlahSedia=" + AjumlahSkr + 
                            " AND sat='" + cbSatA.getSelectedItem().toString() + 
                            "' AND golObat='" + cbGolObatA.getSelectedItem().toString() + "'";

                    try {
                        int berhasil = stmt2.executeUpdate(sqlDelete);
                    } catch (SQLException errMsg) {
                        System.out.println(errMsg.getMessage());
                    }
                    
                    int sisaGudang = jumlahSkr - isiJumlah;
                    
                    sqlUpdate = "UPDATE DataGudang SET "
                        + "jumlahSediaG='" + sisaGudang + "' "
                        + "WHERE namaObatG='" + obat + "' AND golObatG='" + gol + "' AND satG='" + sat + "'";
                    
                    try {
                       int berhasil = stmt2.executeUpdate(sqlUpdate);
                    } catch (SQLException errMsg) {
                        System.out.println(errMsg);
                    }                   
                    setGudang gd = new setGudang();
                    gd.setTanggal(mGd);
                    gd.setNamaObat(obat);
                    gd.setGolObat(gol);
                    gd.setSat(sat);
                    gd.setSisaGudang(sisaGudang);
                    gd.setExdate(exp);
                    this.list.add(gd);
                    updateTable();
                    clearAllA(); //tess
                }
            }
        } else if (cbDariA.getSelectedItem().equals("Apotek") && cbKeA.getSelectedItem().equals("Gudang")) {
            try {
                rsApotek = stmt1.executeQuery("SELECT * FROM DataObat WHERE namaObat='" +  cbNamaObatA.getSelectedItem().toString() +
                     "' AND sat='" + cbSatA.getSelectedItem().toString() + 
                    "' AND golObat='" + cbGolObatA.getSelectedItem().toString() + "'");
                while(rsApotek.next() == true) {
                    mGd = rsApotek.getDate("tglMasuk");
                    obat = rsApotek.getString("namaObat");
                    gol = rsApotek.getString("golObat");
                    sat = rsApotek.getString("sat");
                    jumlahSkr = rsApotek.getInt("jumlahSedia");
                    exp = rsApotek.getDate("exdate");
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }

            if ( cbNamaObatA.getSelectedItem().toString().equalsIgnoreCase(obat) && cbGolObatA.getSelectedItem().toString().equals(gol) &&
                    cbSatA.getSelectedItem().toString().equals(sat)) {
                try {
                    rsApotek = stmt1.executeQuery("SELECT * FROM DataGudang WHERE namaObatG='" +  cbNamaObatA.getSelectedItem().toString() +
                            "' AND exdateG='" + exp + "' AND satG='" + cbSatA.getSelectedItem().toString() + 
                            "' AND golObatG='" + cbGolObatA.getSelectedItem().toString() + "'");
                    while(rsApotek.next() == true) {
                        Amgd = rsApotek.getDate("tglMasukG");
                        Aobat = rsApotek.getString("namaObatG");
                        Ag = rsApotek.getString("golObatG");
                        As = rsApotek.getString("satG");
                        AjumlahSkr = rsApotek.getInt("jumlahSediaG");
                        Aexp = rsApotek.getDate("exdateG");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }

                int totalA = isiJumlah + AjumlahSkr;

                if (isiJumlah <= jumlahSkr) {
                    sqlPindah = "INSERT INTO DataGudang (tglMasukG,namaObatG,golObatG,satG,jumlahSediaG,exdateG) "
                        + "VALUES ('" + simMasA + "',"
                        + "'" +  cbNamaObatA.getSelectedItem().toString() + "',"
                        + "'" + cbGolObatA.getSelectedItem().toString() + "',"
                        + "'" + cbSatA.getSelectedItem().toString() + "',"
                        + "'" + totalA + "',"
                        + "'" + exp + "');";

                    try {
                       int berhasil = stmt2.executeUpdate(sqlPindah);
                    } catch (SQLException errMsg) {
                        System.out.println(errMsg);
                    }

                    sqlDelete = "DELETE FROM DataGudang "
                            + "WHERE namaObatG='" +  cbNamaObatA.getSelectedItem().toString() + 
                            "' AND exdateG='" + Aexp + 
                            "' AND jumlahSediaG=" + AjumlahSkr + 
                            " AND satG='" + cbSatA.getSelectedItem().toString() + 
                            "' AND golObatG='" + cbGolObatA.getSelectedItem().toString() + "'";

                    try {
                        int berhasil = stmt2.executeUpdate(sqlDelete);
                    } catch (SQLException errMsg) {
                        System.out.println(errMsg.getMessage());
                    }
                    
                    setGudang gd = new setGudang();
                    gd.setTanggal(Date.valueOf(simMasA));
                    gd.setNamaObat(obat);
                    gd.setGolObat(gol);
                    gd.setSat(sat);
                    gd.setSisaGudang(totalA);
                    gd.setExdate(exp);
                    this.list.add(gd);
                    updateTable();

                    int sisaGudang = jumlahSkr - isiJumlah;

                    sqlUpdate = "UPDATE DataObat SET "
                        + "jumlahSedia ='" + sisaGudang + "' "
                        + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                    try {
                       int berhasil = stmt2.executeUpdate(sqlUpdate);
                    } catch (SQLException errMsg) {
                        System.out.println(errMsg);
                    }                   
                }
            }
        }
    }//GEN-LAST:event_btnSimpanAActionPerformed

    private void btnPindahObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPindahObatActionPerformed
        // TODO add your handling code here:
        this.pindah();
    }//GEN-LAST:event_btnPindahObatActionPerformed

    private void txtNamaObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaObatActionPerformed
        // TODO add your handling code here:
//         TextAutoCompleter complete=new TextAutoCompleter(txtNamaObat);
//          Statement stmt;
//          stmt.connection();
//          stmt.retrieve();
//    while(conn.rs.next()){
//
//        complete.addItem(conn.rs.getString("number"));
//    }
    }//GEN-LAST:event_txtNamaObatActionPerformed

    private void cbCariNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCariNamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbCariNamaActionPerformed

    private void cbCariNamaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbCariNamaKeyReleased
        // TODO add your handling code here:
        String query = cbCariNama.getText();
        filter(query);
    }//GEN-LAST:event_cbCariNamaKeyReleased

    private void btnSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetActionPerformed
        // TODO add your handling code here:
        String golongan = null, satuan = null;
        try {
            setConnection koneksi = new setConnection();
            stmt1 = koneksi.connection.createStatement();
            rsGudang = stmt1.executeQuery("SELECT * FROM DataGudang WHERE namaObatG= '" + cbNamaObatA.getSelectedItem()+ "'");
            while(rsGudang.next() == true) {
                    golongan = rsGudang.getString("golObatG");
                    satuan = rsGudang.getString("satG");  
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        cbGolObatA.setSelectedItem(golongan);
        cbSatA.setSelectedItem(satuan);
    }//GEN-LAST:event_btnSetActionPerformed

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
            java.util.logging.Logger.getLogger(Gudang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Gudang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Gudang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Gudang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                new Gudang().setVisible(true);
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
        
        public final void tambah() {
            txtEx.setEnabled(true);
            txtNamaObat.setEnabled(true);
            txttglMasuk.setEnabled(true);
            spJumlah.setEnabled(true);
            cbGolObat.setEnabled(true);
            cbSatuan.setEnabled(true);
            btnSimpan.setEnabled(true);
            cbNamaObatA.setEnabled(false);
            txtTglMasukA.setEnabled(false);
            spJumlahA.setEnabled(false);
            cbGolObatA.setEnabled(false);
            cbSatA.setEnabled(false);
            btnSimpanA.setEnabled(false);
            cbDariA.setEnabled(false);
            cbKeA.setEnabled(false);            
            btnSet.setEnabled(false);
            
        }
        
        public final void pindah() {
            txtEx.setEnabled(false);
            txtNamaObat.setEnabled(false);
            txttglMasuk.setEnabled(false);
            spJumlah.setEnabled(false);
            cbGolObat.setEnabled(false);
            cbSatuan.setEnabled(false);
            btnSimpan.setEnabled(false);
            cbNamaObatA.setEnabled(true);
            txtTglMasukA.setEnabled(true);
            spJumlahA.setEnabled(true);
//            cbGolObatA.setEnabled(true);
//            cbSatA.setEnabled(true);
            btnSimpanA.setEnabled(true);
            cbDariA.setEnabled(true);
            cbKeA.setEnabled(true);
            btnSet.setEnabled(true);
        }
       
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnKeluar;
    private javax.swing.JButton btnPindahObat;
    private javax.swing.JButton btnSet;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnSimpanA;
    private javax.swing.JButton btnTambah;
    private javax.swing.JTextField cbCariNama;
    private javax.swing.JComboBox<String> cbDariA;
    private javax.swing.JComboBox<String> cbGolObat;
    private javax.swing.JComboBox<String> cbGolObatA;
    private javax.swing.JComboBox<String> cbKeA;
    private javax.swing.JComboBox<String> cbNamaObatA;
    private javax.swing.JComboBox<String> cbSatA;
    private javax.swing.JComboBox<String> cbSatuan;
    private PanelTransparan.ClPanelTransparan clPanelTransparan1;
    private PanelTransparan.ClPanelTransparan clPanelTransparan2;
    private PanelTransparan.ClPanelTransparan clPanelTransparan3;
    private PanelTransparan.ClPanelTransparan clPanelTransparan4;
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
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner spJumlah;
    private javax.swing.JSpinner spJumlahA;
    private javax.swing.JTable tblEx;
    private com.toedter.calendar.JDateChooser txtEx;
    private javax.swing.JTextField txtNamaObat;
    private javax.swing.JLabel txtTanggal;
    private com.toedter.calendar.JDateChooser txtTglMasukA;
    private javax.swing.JLabel txtWelcome;
    private com.toedter.calendar.JDateChooser txttglMasuk;
    // End of variables declaration//GEN-END:variables
}
