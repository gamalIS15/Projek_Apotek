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
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
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
    ResultSet rsResep,rsObat,rsJual,rsCariResep,rscariObat,rsResep1,rsResep2;
    int index = 0;
    String title [] = {"No Resep","Tanggal", "Nama Pasien", "Usia", "Alamat", "Jenis Layanan", 
        "BPJS/Non BPJS", "Nama Obat", "Jumlah Pengambilan"};
    String [] judul= {"Tanggal", "Nama Obat", "Golongan", "Satuan", "Jumlah Obat (dalam satuan)"};
    ArrayList<setResep> list = new ArrayList<setResep>();
    ArrayList<setObat> list1 = new ArrayList<setObat>();
    private final ArrayList<String> ls = new ArrayList<>();
    DefaultTableModel dm, dm1;
    
    
    public Resep() {
        try {
            this.setWaktu();
            initComponents();
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
//            txtWelcome.setText(MainMenu.txtWelcome.getText());
            
            setConnection koneksi = new setConnection();
            stmt = koneksi.connection.createStatement();
            rsResep = stmt.executeQuery("SELECT * FROM DataResep WHERE datediff(Tanggal, current_date())=0 ORDER BY Tanggal");
            
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
            rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE datediff(tglMasukJ, current_date())=0 ORDER BY namaObatJ");
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
        tableFilter();
    }
    
    private void filter(String query){
        dm = (DefaultTableModel) tblEx.getModel();
        dm1 = (DefaultTableModel) tblJual.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(dm);
         TableRowSorter<DefaultTableModel> td = new TableRowSorter<DefaultTableModel>(dm1);
        tblEx.setRowSorter(tr);
        tblJual.setRowSorter(td);
        tr.setRowFilter(RowFilter.regexFilter(query));
        td.setRowFilter(RowFilter.regexFilter(query));
    }
    private void tableFilter(){
        // TableRowSorter<updateTable> sorter;
        tblEx.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        tblJual.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        
        tblEx.setAutoCreateRowSorter(true);
        tblJual.setAutoCreateRowSorter(true);
        
        TableRowSorter<DefaultTableModel> sorter  = new TableRowSorter<DefaultTableModel>((DefaultTableModel) tblEx.getModel());
        TableRowSorter<DefaultTableModel> sorter1  = new TableRowSorter<DefaultTableModel>((DefaultTableModel) tblJual.getModel());
        tblJual.setRowSorter(sorter1);
        tblEx.setRowSorter(sorter);
       
    }
    private void updateTable(){
        Object[][] data = new Object[this.list.size()][9];
        int x = 0;
        for (setResep sr : this.list){
            data[x][0] = sr.getResep();
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
        
//        cbCariNama.setModel(cR);
//        AutoCompleteDecorator.decorate(this.cbCariNama);
        
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
        DefaultComboBoxModel<String> cm9 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm10 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm11 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm12 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm13 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm14 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm15 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm16 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm17 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm18 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm19 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm20 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm21 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm22 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm23 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm24 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm25 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm26 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm27 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm28 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm29 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm30 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm31 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm32 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm33 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm34 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm35 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm36 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm37 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm38 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm39 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm40 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm41 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm42 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm43 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm44 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm45 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm46 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm47 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm48 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm49 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm50 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm51 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm52 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm53 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm54 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm55 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm56 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm57 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm58 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm59 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm60 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm61 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm62 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm63 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm64 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm65 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm66 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm67 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm68 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm69 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm70 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm71 = new DefaultComboBoxModel<String>(namaObat);
        DefaultComboBoxModel<String> cm72 = new DefaultComboBoxModel<String>(namaObat);
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
            cbNamaObat11.setModel( cm9 );
            AutoCompleteDecorator.decorate(this.cbNamaObat11);
            cbNamaObat12.setModel( cm10 );
            AutoCompleteDecorator.decorate(this.cbNamaObat12);
            cbNamaObat13.setModel( cm11 );
            AutoCompleteDecorator.decorate(this.cbNamaObat13);
            cbNamaObat14.setModel( cm12 );
            AutoCompleteDecorator.decorate(this.cbNamaObat14);
            cbNamaObat15.setModel( cm13 );
            AutoCompleteDecorator.decorate(this.cbNamaObat15);
            cbNamaObat16.setModel( cm14 );
            AutoCompleteDecorator.decorate(this.cbNamaObat16);
            cbNamaObat17.setModel( cm15 );
            AutoCompleteDecorator.decorate(this.cbNamaObat17);
            cbNamaObat18.setModel( cm16 );
            AutoCompleteDecorator.decorate(this.cbNamaObat18);
            cbNamaObat19.setModel( cm17 );
            AutoCompleteDecorator.decorate(this.cbNamaObat19);
            cbNamaObat20.setModel( cm18 );
            AutoCompleteDecorator.decorate(this.cbNamaObat20);
            cbNamaObat21.setModel( cm19 );
            AutoCompleteDecorator.decorate(this.cbNamaObat21);
            cbNamaObat22.setModel( cm20 );
            AutoCompleteDecorator.decorate(this.cbNamaObat22);
            cbNamaObat23.setModel( cm21 );
            AutoCompleteDecorator.decorate(this.cbNamaObat23);
            cbNamaObat24.setModel( cm22 );
            AutoCompleteDecorator.decorate(this.cbNamaObat24);
            cbNamaObat25.setModel( cm23 );
            AutoCompleteDecorator.decorate(this.cbNamaObat25);
            cbNamaObat26.setModel( cm24 );
            AutoCompleteDecorator.decorate(this.cbNamaObat26);
            cbNamaObat27.setModel( cm25 );
            AutoCompleteDecorator.decorate(this.cbNamaObat27);
            cbNamaObat28.setModel( cm26 );
            AutoCompleteDecorator.decorate(this.cbNamaObat28);
            cbNamaObat29.setModel( cm27 );
            AutoCompleteDecorator.decorate(this.cbNamaObat29);
            cbNamaObat30.setModel( cm28 );
            AutoCompleteDecorator.decorate(this.cbNamaObat30);
            cbNamaObat31.setModel( cm29 );
            AutoCompleteDecorator.decorate(this.cbNamaObat31);
            cbNamaObat32.setModel( cm30 );
            AutoCompleteDecorator.decorate(this.cbNamaObat32);
            cbNamaObat33.setModel( cm31 );
            AutoCompleteDecorator.decorate(this.cbNamaObat33);
            cbNamaObat34.setModel( cm32 );
            AutoCompleteDecorator.decorate(this.cbNamaObat34);
            cbNamaObat35.setModel( cm33 );
            AutoCompleteDecorator.decorate(this.cbNamaObat35);
            cbNamaObat36.setModel( cm34 );
            AutoCompleteDecorator.decorate(this.cbNamaObat36);
            cbNamaObat37.setModel( cm35 );
            AutoCompleteDecorator.decorate(this.cbNamaObat37);      
            cbNamaObat38.setModel( cm36 );
            AutoCompleteDecorator.decorate(this.cbNamaObat38);
            cbNamaObat39.setModel( cm37 );
            AutoCompleteDecorator.decorate(this.cbNamaObat39);
            cbNamaObat40.setModel( cm38 );
            AutoCompleteDecorator.decorate(this.cbNamaObat40);            
            cbNamaObat41.setModel( cm39 );
            AutoCompleteDecorator.decorate(this.cbNamaObat41);
            cbNamaObat42.setModel( cm40 );
            AutoCompleteDecorator.decorate(this.cbNamaObat42);
            cbNamaObat43.setModel( cm41 );
            AutoCompleteDecorator.decorate(this.cbNamaObat43);
            cbNamaObat44.setModel( cm42 );
            AutoCompleteDecorator.decorate(this.cbNamaObat44);
            cbNamaObat45.setModel( cm43 );
            AutoCompleteDecorator.decorate(this.cbNamaObat45);
            cbNamaObat46.setModel( cm44 );
            AutoCompleteDecorator.decorate(this.cbNamaObat46);
            cbNamaObat47.setModel( cm45 );
            AutoCompleteDecorator.decorate(this.cbNamaObat47);
            cbNamaObat48.setModel( cm46 );
            AutoCompleteDecorator.decorate(this.cbNamaObat48);
            cbNamaObat49.setModel( cm47 );
            AutoCompleteDecorator.decorate(this.cbNamaObat49);
            cbNamaObat50.setModel( cm48 );
            AutoCompleteDecorator.decorate(this.cbNamaObat50);
            cbNamaObat51.setModel( cm49 );
            AutoCompleteDecorator.decorate(this.cbNamaObat51);
            cbNamaObat52.setModel( cm50 );
            AutoCompleteDecorator.decorate(this.cbNamaObat52);
            cbNamaObat53.setModel( cm51 );
            AutoCompleteDecorator.decorate(this.cbNamaObat53);
            cbNamaObat54.setModel( cm52 );
            AutoCompleteDecorator.decorate(this.cbNamaObat54);
            cbNamaObat55.setModel( cm53 );
            AutoCompleteDecorator.decorate(this.cbNamaObat55);
            cbNamaObat56.setModel( cm54 );
            AutoCompleteDecorator.decorate(this.cbNamaObat56);
            cbNamaObat57.setModel( cm55 );
            AutoCompleteDecorator.decorate(this.cbNamaObat57);      
            cbNamaObat58.setModel( cm56 );
            AutoCompleteDecorator.decorate(this.cbNamaObat58);
            cbNamaObat59.setModel( cm57 );
            AutoCompleteDecorator.decorate(this.cbNamaObat59);
            cbNamaObat60.setModel( cm58 );
            AutoCompleteDecorator.decorate(this.cbNamaObat60);                  
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEx = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbCariNama = new javax.swing.JTextField();
        clPanelTransparan4 = new PanelTransparan.ClPanelTransparan();
        btnSimpan = new javax.swing.JButton();
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
        cbJenisLayanan = new javax.swing.JComboBox<>();
        spnThn = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        rbBPJS = new javax.swing.JRadioButton();
        rbNBPJS = new javax.swing.JRadioButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
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
        cbNamaObat2 = new javax.swing.JComboBox<>();
        cbNamaObat3 = new javax.swing.JComboBox<>();
        cbNamaObat4 = new javax.swing.JComboBox<>();
        cbNamaObat5 = new javax.swing.JComboBox<>();
        cbNamaObat6 = new javax.swing.JComboBox<>();
        cbNamaObat7 = new javax.swing.JComboBox<>();
        cbNamaObat8 = new javax.swing.JComboBox<>();
        cbNamaObat1 = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        cbNamaObat9 = new javax.swing.JComboBox<>();
        jLabel36 = new javax.swing.JLabel();
        spnJumlah10 = new javax.swing.JSpinner();
        jLabel38 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        cbNamaObat10 = new javax.swing.JComboBox<>();
        spnJumlah9 = new javax.swing.JSpinner();
        jLabel39 = new javax.swing.JLabel();
        cbNamaObat11 = new javax.swing.JComboBox<>();
        jLabel40 = new javax.swing.JLabel();
        spnJumlah11 = new javax.swing.JSpinner();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        cbNamaObat12 = new javax.swing.JComboBox<>();
        spnJumlah12 = new javax.swing.JSpinner();
        jLabel43 = new javax.swing.JLabel();
        spnJumlah13 = new javax.swing.JSpinner();
        jLabel44 = new javax.swing.JLabel();
        cbNamaObat13 = new javax.swing.JComboBox<>();
        cbNamaObat14 = new javax.swing.JComboBox<>();
        jLabel45 = new javax.swing.JLabel();
        spnJumlah14 = new javax.swing.JSpinner();
        jLabel46 = new javax.swing.JLabel();
        spnJumlah15 = new javax.swing.JSpinner();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        cbNamaObat15 = new javax.swing.JComboBox<>();
        cbNamaObat16 = new javax.swing.JComboBox<>();
        jLabel49 = new javax.swing.JLabel();
        spnJumlah16 = new javax.swing.JSpinner();
        jLabel50 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        cbNamaObat17 = new javax.swing.JComboBox<>();
        jLabel52 = new javax.swing.JLabel();
        spnJumlah17 = new javax.swing.JSpinner();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        cbNamaObat18 = new javax.swing.JComboBox<>();
        spnJumlah18 = new javax.swing.JSpinner();
        jLabel55 = new javax.swing.JLabel();
        cbNamaObat19 = new javax.swing.JComboBox<>();
        jLabel56 = new javax.swing.JLabel();
        spnJumlah19 = new javax.swing.JSpinner();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        cbNamaObat20 = new javax.swing.JComboBox<>();
        spnJumlah20 = new javax.swing.JSpinner();
        jLabel59 = new javax.swing.JLabel();
        spnJumlah21 = new javax.swing.JSpinner();
        jLabel60 = new javax.swing.JLabel();
        cbNamaObat21 = new javax.swing.JComboBox<>();
        cbNamaObat22 = new javax.swing.JComboBox<>();
        jLabel61 = new javax.swing.JLabel();
        spnJumlah22 = new javax.swing.JSpinner();
        jLabel62 = new javax.swing.JLabel();
        spnJumlah23 = new javax.swing.JSpinner();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        cbNamaObat23 = new javax.swing.JComboBox<>();
        cbNamaObat24 = new javax.swing.JComboBox<>();
        jLabel65 = new javax.swing.JLabel();
        spnJumlah24 = new javax.swing.JSpinner();
        jLabel66 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel67 = new javax.swing.JLabel();
        cbNamaObat25 = new javax.swing.JComboBox<>();
        jLabel68 = new javax.swing.JLabel();
        spnJumlah25 = new javax.swing.JSpinner();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        cbNamaObat26 = new javax.swing.JComboBox<>();
        spnJumlah26 = new javax.swing.JSpinner();
        jLabel71 = new javax.swing.JLabel();
        cbNamaObat27 = new javax.swing.JComboBox<>();
        jLabel72 = new javax.swing.JLabel();
        spnJumlah27 = new javax.swing.JSpinner();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        cbNamaObat28 = new javax.swing.JComboBox<>();
        spnJumlah28 = new javax.swing.JSpinner();
        jLabel75 = new javax.swing.JLabel();
        spnJumlah29 = new javax.swing.JSpinner();
        jLabel76 = new javax.swing.JLabel();
        cbNamaObat29 = new javax.swing.JComboBox<>();
        cbNamaObat30 = new javax.swing.JComboBox<>();
        jLabel77 = new javax.swing.JLabel();
        spnJumlah30 = new javax.swing.JSpinner();
        jLabel78 = new javax.swing.JLabel();
        spnJumlah31 = new javax.swing.JSpinner();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        cbNamaObat31 = new javax.swing.JComboBox<>();
        cbNamaObat32 = new javax.swing.JComboBox<>();
        jLabel81 = new javax.swing.JLabel();
        spnJumlah32 = new javax.swing.JSpinner();
        jLabel82 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel83 = new javax.swing.JLabel();
        cbNamaObat33 = new javax.swing.JComboBox<>();
        jLabel84 = new javax.swing.JLabel();
        spnJumlah33 = new javax.swing.JSpinner();
        jLabel85 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        cbNamaObat34 = new javax.swing.JComboBox<>();
        spnJumlah34 = new javax.swing.JSpinner();
        jLabel87 = new javax.swing.JLabel();
        cbNamaObat35 = new javax.swing.JComboBox<>();
        jLabel88 = new javax.swing.JLabel();
        spnJumlah35 = new javax.swing.JSpinner();
        jLabel89 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        cbNamaObat36 = new javax.swing.JComboBox<>();
        spnJumlah36 = new javax.swing.JSpinner();
        jLabel91 = new javax.swing.JLabel();
        spnJumlah37 = new javax.swing.JSpinner();
        jLabel92 = new javax.swing.JLabel();
        cbNamaObat37 = new javax.swing.JComboBox<>();
        cbNamaObat38 = new javax.swing.JComboBox<>();
        jLabel93 = new javax.swing.JLabel();
        spnJumlah38 = new javax.swing.JSpinner();
        jLabel94 = new javax.swing.JLabel();
        spnJumlah39 = new javax.swing.JSpinner();
        jLabel95 = new javax.swing.JLabel();
        jLabel96 = new javax.swing.JLabel();
        cbNamaObat39 = new javax.swing.JComboBox<>();
        cbNamaObat40 = new javax.swing.JComboBox<>();
        jLabel97 = new javax.swing.JLabel();
        spnJumlah40 = new javax.swing.JSpinner();
        jLabel98 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel99 = new javax.swing.JLabel();
        cbNamaObat41 = new javax.swing.JComboBox<>();
        jLabel100 = new javax.swing.JLabel();
        spnJumlah41 = new javax.swing.JSpinner();
        jLabel101 = new javax.swing.JLabel();
        jLabel102 = new javax.swing.JLabel();
        cbNamaObat42 = new javax.swing.JComboBox<>();
        spnJumlah42 = new javax.swing.JSpinner();
        jLabel103 = new javax.swing.JLabel();
        cbNamaObat43 = new javax.swing.JComboBox<>();
        jLabel104 = new javax.swing.JLabel();
        spnJumlah43 = new javax.swing.JSpinner();
        jLabel105 = new javax.swing.JLabel();
        jLabel106 = new javax.swing.JLabel();
        cbNamaObat44 = new javax.swing.JComboBox<>();
        spnJumlah44 = new javax.swing.JSpinner();
        jLabel107 = new javax.swing.JLabel();
        spnJumlah45 = new javax.swing.JSpinner();
        jLabel108 = new javax.swing.JLabel();
        cbNamaObat45 = new javax.swing.JComboBox<>();
        cbNamaObat46 = new javax.swing.JComboBox<>();
        jLabel109 = new javax.swing.JLabel();
        spnJumlah46 = new javax.swing.JSpinner();
        jLabel110 = new javax.swing.JLabel();
        spnJumlah47 = new javax.swing.JSpinner();
        jLabel111 = new javax.swing.JLabel();
        jLabel112 = new javax.swing.JLabel();
        cbNamaObat47 = new javax.swing.JComboBox<>();
        cbNamaObat48 = new javax.swing.JComboBox<>();
        jLabel113 = new javax.swing.JLabel();
        spnJumlah48 = new javax.swing.JSpinner();
        jLabel114 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel115 = new javax.swing.JLabel();
        cbNamaObat49 = new javax.swing.JComboBox<>();
        jLabel116 = new javax.swing.JLabel();
        spnJumlah49 = new javax.swing.JSpinner();
        jLabel117 = new javax.swing.JLabel();
        jLabel118 = new javax.swing.JLabel();
        cbNamaObat50 = new javax.swing.JComboBox<>();
        spnJumlah50 = new javax.swing.JSpinner();
        jLabel119 = new javax.swing.JLabel();
        cbNamaObat51 = new javax.swing.JComboBox<>();
        jLabel120 = new javax.swing.JLabel();
        spnJumlah51 = new javax.swing.JSpinner();
        jLabel121 = new javax.swing.JLabel();
        jLabel122 = new javax.swing.JLabel();
        cbNamaObat52 = new javax.swing.JComboBox<>();
        spnJumlah52 = new javax.swing.JSpinner();
        jLabel123 = new javax.swing.JLabel();
        spnJumlah53 = new javax.swing.JSpinner();
        jLabel124 = new javax.swing.JLabel();
        cbNamaObat53 = new javax.swing.JComboBox<>();
        cbNamaObat54 = new javax.swing.JComboBox<>();
        jLabel125 = new javax.swing.JLabel();
        spnJumlah54 = new javax.swing.JSpinner();
        jLabel126 = new javax.swing.JLabel();
        spnJumlah55 = new javax.swing.JSpinner();
        jLabel127 = new javax.swing.JLabel();
        jLabel128 = new javax.swing.JLabel();
        cbNamaObat55 = new javax.swing.JComboBox<>();
        cbNamaObat56 = new javax.swing.JComboBox<>();
        jLabel129 = new javax.swing.JLabel();
        spnJumlah56 = new javax.swing.JSpinner();
        jLabel130 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel131 = new javax.swing.JLabel();
        cbNamaObat57 = new javax.swing.JComboBox<>();
        jLabel132 = new javax.swing.JLabel();
        spnJumlah57 = new javax.swing.JSpinner();
        jLabel133 = new javax.swing.JLabel();
        jLabel134 = new javax.swing.JLabel();
        cbNamaObat58 = new javax.swing.JComboBox<>();
        spnJumlah58 = new javax.swing.JSpinner();
        jLabel135 = new javax.swing.JLabel();
        cbNamaObat59 = new javax.swing.JComboBox<>();
        jLabel136 = new javax.swing.JLabel();
        spnJumlah59 = new javax.swing.JSpinner();
        jLabel137 = new javax.swing.JLabel();
        jLabel138 = new javax.swing.JLabel();
        cbNamaObat60 = new javax.swing.JComboBox<>();
        spnJumlah60 = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        txtResep = new javax.swing.JTextField();
        btnUpdate = new javax.swing.JButton();
        clPanelTransparan5 = new PanelTransparan.ClPanelTransparan();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblJual = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        cbCariObat = new javax.swing.JComboBox<>();
        btnEdit = new javax.swing.JButton();

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

        clPanelTransparan3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblEx.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No Resep", "Tanggal", "Nama Pasien", "Usia", "Alamat", "Jenis Layanan", "BPJS / Non BPJS", "Nama Obat", "Jumlah Pengambilan"
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

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Log Pasien");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Nama : ");

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
                .addContainerGap()
                .addGroup(clPanelTransparan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(clPanelTransparan3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbCariNama)))
                .addContainerGap())
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
                .addGap(15, 15, 15)
                .addGroup(clPanelTransparan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbCariNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        cbJenisLayanan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Rawat Jalan", "Rawat Inap", "Pustu Kletek", "Pustu Sadang", "Pustu Wage", "Polindes Bohar", "Polindes Jemundo", "Polindes Kedungturi", "Polindes Sepanjang", "Polindes Tawangsari ", "UGD", "LABORAT", "BKIA", "PERAWATAN", "IMUNISASI", "PROGRAM GIZI", "POLI GIGI" }));

        spnThn.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

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

        spnJumlah1.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel11.setText("Jumlah");

        jLabel12.setText("Nama Obat 2");

        jLabel13.setText("Jumlah");

        spnJumlah2.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel14.setText("Nama Obat 3");

        jLabel15.setText("Jumlah");

        spnJumlah3.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel16.setText("Nama Obat 4");

        jLabel17.setText("Jumlah");

        spnJumlah4.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel18.setText("Nama Obat 5");

        jLabel28.setText("Jumlah");

        spnJumlah5.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel29.setText("Nama Obat 6");

        jLabel30.setText("Jumlah");

        spnJumlah6.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel31.setText("Nama Obat 7");

        jLabel32.setText("Jumlah");

        spnJumlah7.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel33.setText("Nama Obat 8");

        jLabel34.setText("Jumlah");

        spnJumlah8.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        cbNamaObat2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel12)
                        .addComponent(jLabel14)
                        .addComponent(jLabel16)
                        .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jLabel29)
                    .addComponent(jLabel31)
                    .addComponent(jLabel33)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cbNamaObat2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel13))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cbNamaObat3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel15))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cbNamaObat4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel17))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cbNamaObat5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel28))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cbNamaObat6, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel30))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cbNamaObat7, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel32))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cbNamaObat8, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel34)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                    .addGap(7, 7, 7)
                                                    .addComponent(spnJumlah2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(spnJumlah3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(spnJumlah4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(spnJumlah5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(spnJumlah6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(spnJumlah7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(spnJumlah8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cbNamaObat1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(spnJumlah1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spnJumlah1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbNamaObat1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(spnJumlah2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(spnJumlah3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(spnJumlah4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34)
                    .addComponent(jLabel33))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("1-8", jPanel2);

        jLabel37.setText("Nama Obat 10");

        cbNamaObat9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel36.setText("Jumlah");

        spnJumlah10.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel38.setText("Jumlah");

        jLabel35.setText("Nama Obat 9");

        cbNamaObat10.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        spnJumlah9.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel39.setText("Nama Obat 11");

        cbNamaObat11.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel40.setText("Jumlah");

        spnJumlah11.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel41.setText("Jumlah");

        jLabel42.setText("Nama Obat 12");

        cbNamaObat12.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        spnJumlah12.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel43.setText("Jumlah");

        spnJumlah13.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel44.setText("Nama Obat 13");

        cbNamaObat13.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat14.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel45.setText("Jumlah");

        spnJumlah14.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel46.setText("Nama Obat 14");

        spnJumlah15.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel47.setText("Nama Obat 15");

        jLabel48.setText("Jumlah");

        cbNamaObat15.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat16.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel49.setText("Jumlah");

        spnJumlah16.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel50.setText("Nama Obat 16");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel35)
                    .addComponent(jLabel37)
                    .addComponent(jLabel39)
                    .addComponent(jLabel42)
                    .addComponent(jLabel44)
                    .addComponent(jLabel46)
                    .addComponent(jLabel47)
                    .addComponent(jLabel50))
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbNamaObat9, 0, 200, Short.MAX_VALUE)
                    .addComponent(cbNamaObat10, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat11, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat12, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat13, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat14, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat15, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat16, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36)
                    .addComponent(jLabel38)
                    .addComponent(jLabel40)
                    .addComponent(jLabel41)
                    .addComponent(jLabel43)
                    .addComponent(jLabel45)
                    .addComponent(jLabel48)
                    .addComponent(jLabel49))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spnJumlah9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36)
                    .addComponent(jLabel35))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38)
                    .addComponent(jLabel37))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40)
                    .addComponent(jLabel39))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41)
                    .addComponent(jLabel42))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43)
                    .addComponent(jLabel44))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45)
                    .addComponent(jLabel46))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel48)
                    .addComponent(jLabel47))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49)
                    .addComponent(jLabel50))
                .addContainerGap(80, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("9-16", jPanel3);

        jLabel51.setText("Nama Obat 18");

        cbNamaObat17.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel52.setText("Jumlah");

        spnJumlah17.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel53.setText("Jumlah");

        jLabel54.setText("Nama Obat 17");

        cbNamaObat18.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        spnJumlah18.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel55.setText("Nama Obat 19");

        cbNamaObat19.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel56.setText("Jumlah");

        spnJumlah19.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel57.setText("Jumlah");

        jLabel58.setText("Nama Obat 20");

        cbNamaObat20.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        spnJumlah20.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel59.setText("Jumlah");

        spnJumlah21.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel60.setText("Nama Obat 21");

        cbNamaObat21.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat22.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel61.setText("Jumlah");

        spnJumlah22.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel62.setText("Nama Obat 22");

        spnJumlah23.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel63.setText("Nama Obat 23");

        jLabel64.setText("Jumlah");

        cbNamaObat23.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat24.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel65.setText("Jumlah");

        spnJumlah24.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel66.setText("Nama Obat 24");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel54)
                    .addComponent(jLabel51)
                    .addComponent(jLabel55)
                    .addComponent(jLabel58)
                    .addComponent(jLabel60)
                    .addComponent(jLabel62)
                    .addComponent(jLabel63)
                    .addComponent(jLabel66))
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbNamaObat17, 0, 200, Short.MAX_VALUE)
                    .addComponent(cbNamaObat18, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat19, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat20, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat21, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat22, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat23, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat24, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel52)
                    .addComponent(jLabel53)
                    .addComponent(jLabel56)
                    .addComponent(jLabel57)
                    .addComponent(jLabel59)
                    .addComponent(jLabel61)
                    .addComponent(jLabel64)
                    .addComponent(jLabel65))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spnJumlah18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52)
                    .addComponent(jLabel54))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53)
                    .addComponent(jLabel51))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel56)
                    .addComponent(jLabel55))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel57)
                    .addComponent(jLabel58))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel59)
                    .addComponent(jLabel60))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel61)
                    .addComponent(jLabel62))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel64)
                    .addComponent(jLabel63))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel65)
                    .addComponent(jLabel66))
                .addContainerGap(80, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("17-24", jPanel4);

        jLabel67.setText("Nama Obat 26");

        cbNamaObat25.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel68.setText("Jumlah");

        spnJumlah25.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel69.setText("Jumlah");

        jLabel70.setText("Nama Obat 25");

        cbNamaObat26.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        spnJumlah26.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel71.setText("Nama Obat 27");

        cbNamaObat27.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel72.setText("Jumlah");

        spnJumlah27.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel73.setText("Jumlah");

        jLabel74.setText("Nama Obat 28");

        cbNamaObat28.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        spnJumlah28.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel75.setText("Jumlah");

        spnJumlah29.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel76.setText("Nama Obat 29");

        cbNamaObat29.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat30.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel77.setText("Jumlah");

        spnJumlah30.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel78.setText("Nama Obat 30");

        spnJumlah31.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel79.setText("Nama Obat 31");

        jLabel80.setText("Jumlah");

        cbNamaObat31.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat32.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel81.setText("Jumlah");

        spnJumlah32.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel82.setText("Nama Obat 32");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel70)
                    .addComponent(jLabel67)
                    .addComponent(jLabel71)
                    .addComponent(jLabel74)
                    .addComponent(jLabel76)
                    .addComponent(jLabel78)
                    .addComponent(jLabel79)
                    .addComponent(jLabel82))
                .addGap(12, 12, 12)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbNamaObat25, 0, 200, Short.MAX_VALUE)
                    .addComponent(cbNamaObat26, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat27, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat28, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat29, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat30, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat31, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat32, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel68)
                    .addComponent(jLabel69)
                    .addComponent(jLabel72)
                    .addComponent(jLabel73)
                    .addComponent(jLabel75)
                    .addComponent(jLabel77)
                    .addComponent(jLabel80)
                    .addComponent(jLabel81))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spnJumlah26, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah28, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah32, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel68)
                    .addComponent(jLabel70))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel69)
                    .addComponent(jLabel67))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel72)
                    .addComponent(jLabel71))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel73)
                    .addComponent(jLabel74))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel75)
                    .addComponent(jLabel76))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel77)
                    .addComponent(jLabel78))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel80)
                    .addComponent(jLabel79))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel81)
                    .addComponent(jLabel82))
                .addContainerGap(80, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("25-32", jPanel5);

        jLabel83.setText("Nama Obat 34");

        cbNamaObat33.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel84.setText("Jumlah");

        spnJumlah33.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel85.setText("Jumlah");

        jLabel86.setText("Nama Obat 33");

        cbNamaObat34.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        spnJumlah34.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel87.setText("Nama Obat 35");

        cbNamaObat35.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel88.setText("Jumlah");

        spnJumlah35.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel89.setText("Jumlah");

        jLabel90.setText("Nama Obat 36");

        cbNamaObat36.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        spnJumlah36.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel91.setText("Jumlah");

        spnJumlah37.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel92.setText("Nama Obat 37");

        cbNamaObat37.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat38.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel93.setText("Jumlah");

        spnJumlah38.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel94.setText("Nama Obat 38");

        spnJumlah39.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel95.setText("Nama Obat 39");

        jLabel96.setText("Jumlah");

        cbNamaObat39.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat40.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel97.setText("Jumlah");

        spnJumlah40.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel98.setText("Nama Obat 40");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel86)
                    .addComponent(jLabel83)
                    .addComponent(jLabel87)
                    .addComponent(jLabel90)
                    .addComponent(jLabel92)
                    .addComponent(jLabel94)
                    .addComponent(jLabel95)
                    .addComponent(jLabel98))
                .addGap(12, 12, 12)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbNamaObat33, 0, 200, Short.MAX_VALUE)
                    .addComponent(cbNamaObat34, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat35, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat36, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat37, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat38, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat39, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat40, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel84)
                    .addComponent(jLabel85)
                    .addComponent(jLabel88)
                    .addComponent(jLabel89)
                    .addComponent(jLabel91)
                    .addComponent(jLabel93)
                    .addComponent(jLabel96)
                    .addComponent(jLabel97))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spnJumlah34, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah35, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah36, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah37, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah39, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah40, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel84)
                    .addComponent(jLabel86))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel85)
                    .addComponent(jLabel83))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel88)
                    .addComponent(jLabel87))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel89)
                    .addComponent(jLabel90))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel91)
                    .addComponent(jLabel92))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel93)
                    .addComponent(jLabel94))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel96)
                    .addComponent(jLabel95))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel97)
                    .addComponent(jLabel98))
                .addContainerGap(80, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("33-40", jPanel6);

        jLabel99.setText("Nama Obat 42");

        cbNamaObat41.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel100.setText("Jumlah");

        spnJumlah41.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel101.setText("Jumlah");

        jLabel102.setText("Nama Obat 41");

        cbNamaObat42.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        spnJumlah42.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel103.setText("Nama Obat 43");

        cbNamaObat43.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel104.setText("Jumlah");

        spnJumlah43.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel105.setText("Jumlah");

        jLabel106.setText("Nama Obat 44");

        cbNamaObat44.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        spnJumlah44.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel107.setText("Jumlah");

        spnJumlah45.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel108.setText("Nama Obat 45");

        cbNamaObat45.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat46.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel109.setText("Jumlah");

        spnJumlah46.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel110.setText("Nama Obat 46");

        spnJumlah47.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel111.setText("Nama Obat 47");

        jLabel112.setText("Jumlah");

        cbNamaObat47.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat48.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel113.setText("Jumlah");

        spnJumlah48.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel114.setText("Nama Obat 48");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel102)
                    .addComponent(jLabel99)
                    .addComponent(jLabel103)
                    .addComponent(jLabel106)
                    .addComponent(jLabel108)
                    .addComponent(jLabel110)
                    .addComponent(jLabel111)
                    .addComponent(jLabel114))
                .addGap(12, 12, 12)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbNamaObat41, 0, 200, Short.MAX_VALUE)
                    .addComponent(cbNamaObat42, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat43, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat44, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat45, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat46, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat47, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat48, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel100)
                    .addComponent(jLabel101)
                    .addComponent(jLabel104)
                    .addComponent(jLabel105)
                    .addComponent(jLabel107)
                    .addComponent(jLabel109)
                    .addComponent(jLabel112)
                    .addComponent(jLabel113))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spnJumlah42, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah41, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah43, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah44, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah45, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah46, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah47, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah48, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel100)
                    .addComponent(jLabel102))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel101)
                    .addComponent(jLabel99))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel104)
                    .addComponent(jLabel103))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel105)
                    .addComponent(jLabel106))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel107)
                    .addComponent(jLabel108))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel109)
                    .addComponent(jLabel110))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel112)
                    .addComponent(jLabel111))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat48, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah48, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel113)
                    .addComponent(jLabel114))
                .addContainerGap(80, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("41-48", jPanel7);

        jLabel115.setText("Nama Obat 50");

        cbNamaObat49.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel116.setText("Jumlah");

        spnJumlah49.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel117.setText("Jumlah");

        jLabel118.setText("Nama Obat 49");

        cbNamaObat50.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        spnJumlah50.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel119.setText("Nama Obat 51");

        cbNamaObat51.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel120.setText("Jumlah");

        spnJumlah51.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel121.setText("Jumlah");

        jLabel122.setText("Nama Obat 52");

        cbNamaObat52.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        spnJumlah52.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel123.setText("Jumlah");

        spnJumlah53.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel124.setText("Nama Obat 53");

        cbNamaObat53.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat54.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel125.setText("Jumlah");

        spnJumlah54.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel126.setText("Nama Obat 54");

        spnJumlah55.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel127.setText("Nama Obat 55");

        jLabel128.setText("Jumlah");

        cbNamaObat55.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNamaObat56.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel129.setText("Jumlah");

        spnJumlah56.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel130.setText("Nama Obat 56");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel118)
                    .addComponent(jLabel115)
                    .addComponent(jLabel119)
                    .addComponent(jLabel122)
                    .addComponent(jLabel124)
                    .addComponent(jLabel126)
                    .addComponent(jLabel127)
                    .addComponent(jLabel130))
                .addGap(12, 12, 12)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbNamaObat49, 0, 200, Short.MAX_VALUE)
                    .addComponent(cbNamaObat50, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat51, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat52, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat53, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat54, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat55, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat56, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel116)
                    .addComponent(jLabel117)
                    .addComponent(jLabel120)
                    .addComponent(jLabel121)
                    .addComponent(jLabel123)
                    .addComponent(jLabel125)
                    .addComponent(jLabel128)
                    .addComponent(jLabel129))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spnJumlah50, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah49, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah51, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah52, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah53, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah54, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah55, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah56, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel116)
                    .addComponent(jLabel118))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel117)
                    .addComponent(jLabel115))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat51, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah51, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel120)
                    .addComponent(jLabel119))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat52, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah52, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel121)
                    .addComponent(jLabel122))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat53, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah53, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel123)
                    .addComponent(jLabel124))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat54, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah54, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel125)
                    .addComponent(jLabel126))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat55, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah55, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel128)
                    .addComponent(jLabel127))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat56, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah56, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel129)
                    .addComponent(jLabel130))
                .addContainerGap(80, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("49-56", jPanel8);

        jLabel131.setText("Nama Obat 58");

        cbNamaObat57.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel132.setText("Jumlah");

        spnJumlah57.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel133.setText("Jumlah");

        jLabel134.setText("Nama Obat 57");

        cbNamaObat58.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        spnJumlah58.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel135.setText("Nama Obat 59");

        cbNamaObat59.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel136.setText("Jumlah");

        spnJumlah59.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel137.setText("Jumlah");

        jLabel138.setText("Nama Obat 60");

        cbNamaObat60.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        spnJumlah60.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel134)
                    .addComponent(jLabel131)
                    .addComponent(jLabel135)
                    .addComponent(jLabel138))
                .addGap(12, 12, 12)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbNamaObat57, 0, 200, Short.MAX_VALUE)
                    .addComponent(cbNamaObat58, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat59, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbNamaObat60, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel132)
                    .addComponent(jLabel133)
                    .addComponent(jLabel136)
                    .addComponent(jLabel137))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spnJumlah58, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah57, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah59, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah60, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat57, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah58, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel132)
                    .addComponent(jLabel134))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat58, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah57, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel133)
                    .addComponent(jLabel131))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat59, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah59, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel136)
                    .addComponent(jLabel135))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat60, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnJumlah60, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel137)
                    .addComponent(jLabel138))
                .addContainerGap(204, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("57-60", jPanel9);

        jLabel3.setText("No Resep");

        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Save1.png"))); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.setFocusable(false);
        btnUpdate.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout clPanelTransparan4Layout = new javax.swing.GroupLayout(clPanelTransparan4);
        clPanelTransparan4.setLayout(clPanelTransparan4Layout);
        clPanelTransparan4Layout.setHorizontalGroup(
            clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnSimpan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnUpdate))
                    .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                        .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                                .addGap(174, 174, 174)
                                .addComponent(jLabel6))
                            .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, clPanelTransparan4Layout.createSequentialGroup()
                        .addComponent(jTabbedPane1)
                        .addContainerGap())
                    .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                        .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel25)
                            .addComponent(jLabel26)
                            .addComponent(jLabel24))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTanggalResep, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                            .addComponent(cbJenisLayanan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtResep)
                            .addComponent(txtNamaPasien))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                                .addComponent(rbBPJS)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rbNBPJS))
                            .addGroup(clPanelTransparan4Layout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(spnThn, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel8)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        clPanelTransparan4Layout.setVerticalGroup(
            clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, clPanelTransparan4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(14, 14, 14)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addComponent(txtTanggalResep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtResep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(clPanelTransparan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan)
                    .addComponent(btnUpdate))
                .addGap(48, 48, 48))
        );

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

        cbCariObat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbCariObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCariObatActionPerformed(evt);
            }
        });
        cbCariObat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cbCariObatKeyReleased(evt);
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
                                .addGap(0, 819, Short.MAX_VALUE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 861, Short.MAX_VALUE)
                            .addGroup(clPanelTransparan5Layout.createSequentialGroup()
                                .addGap(69, 69, 69)
                                .addComponent(cbCariObat, 0, 782, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        clPanelTransparan5Layout.setVerticalGroup(
            clPanelTransparan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, clPanelTransparan5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addGap(15, 15, 15)
                .addGroup(clPanelTransparan5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(cbCariObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clPanelTransparan1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(clPanelTransparan4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(clPanelTransparan5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clPanelTransparan3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEdit))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(clPanelTransparan1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(clPanelTransparan5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(clPanelTransparan3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(clPanelTransparan4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
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
        rs.setResep(txtResep.getText());
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
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat1.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah1.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
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
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat2.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah2.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
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
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat3.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah3.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
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
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat4.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah4.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
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
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat5.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah5.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
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
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat6.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah6.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
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
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat7.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah7.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
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
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat8.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah8.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
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
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat9.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah9.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
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
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat10.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah10.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
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
// INPUT OBAT KE 11                
        if(!(cbNamaObat11.getSelectedItem().toString().equals(null) && cbNamaObat11.getSelectedItem().toString().equals("") 
                && cbNamaObat11.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat11.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah11.getValue() && cbNamaObat11.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat11.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah11.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat11.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat11.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah11.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat11.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah11.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah11.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
            // INPUT OBAT KE 12                
        if(!(cbNamaObat12.getSelectedItem().toString().equals(null) && cbNamaObat12.getSelectedItem().toString().equals("") 
                && cbNamaObat12.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat12.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah12.getValue() && cbNamaObat12.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat12.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah12.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat12.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat12.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah12.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat12.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah12.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah12.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 13                
        if(!(cbNamaObat13.getSelectedItem().toString().equals(null) && cbNamaObat13.getSelectedItem().toString().equals("") 
                && cbNamaObat13.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat13.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah13.getValue() && cbNamaObat13.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat13.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah13.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat13.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat13.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah13.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat13.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah13.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah13.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 14                
        if(!(cbNamaObat14.getSelectedItem().toString().equals(null) && cbNamaObat14.getSelectedItem().toString().equals("") 
                && cbNamaObat14.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat14.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah14.getValue() && cbNamaObat14.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat14.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah14.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat14.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat14.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah14.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat14.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah14.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah14.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 15                
        if(!(cbNamaObat15.getSelectedItem().toString().equals(null) && cbNamaObat15.getSelectedItem().toString().equals("") 
                && cbNamaObat15.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat15.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah15.getValue() && cbNamaObat15.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat15.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah15.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat15.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat15.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah15.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat15.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah15.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah15.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 16                
        if(!(cbNamaObat16.getSelectedItem().toString().equals(null) && cbNamaObat16.getSelectedItem().toString().equals("") 
                && cbNamaObat16.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat16.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah16.getValue() && cbNamaObat16.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat16.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah16.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat16.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat16.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah16.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat16.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah16.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah16.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 17                
        if(!(cbNamaObat17.getSelectedItem().toString().equals(null) && cbNamaObat17.getSelectedItem().toString().equals("") 
                && cbNamaObat17.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat17.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah17.getValue() && cbNamaObat17.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat17.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah17.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat17.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat17.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah17.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat17.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah17.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah17.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 18                
        if(!(cbNamaObat18.getSelectedItem().toString().equals(null) && cbNamaObat18.getSelectedItem().toString().equals("") 
                && cbNamaObat18.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat18.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah18.getValue() && cbNamaObat18.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat18.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah18.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat18.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat18.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah18.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat18.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah18.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah18.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 19                
        if(!(cbNamaObat19.getSelectedItem().toString().equals(null) && cbNamaObat19.getSelectedItem().toString().equals("") 
                && cbNamaObat19.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat19.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah19.getValue() && cbNamaObat19.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat19.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah19.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat19.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat19.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah19.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat19.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah19.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah19.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 20                
        if(!(cbNamaObat20.getSelectedItem().toString().equals(null) && cbNamaObat20.getSelectedItem().toString().equals("") 
                && cbNamaObat20.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat20.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah20.getValue() && cbNamaObat20.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat20.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah20.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat20.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat20.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah20.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat20.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah20.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah20.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 21                
        if(!(cbNamaObat21.getSelectedItem().toString().equals(null) && cbNamaObat21.getSelectedItem().toString().equals("") 
                && cbNamaObat21.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat21.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah21.getValue() && cbNamaObat21.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat21.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah21.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat21.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat21.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah21.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat21.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah21.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah21.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 22                
        if(!(cbNamaObat22.getSelectedItem().toString().equals(null) && cbNamaObat22.getSelectedItem().toString().equals("") 
                && cbNamaObat22.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat22.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah22.getValue() && cbNamaObat22.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat22.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah22.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat22.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat22.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah22.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat22.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah22.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah22.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 23                
        if(!(cbNamaObat23.getSelectedItem().toString().equals(null) && cbNamaObat23.getSelectedItem().toString().equals("") 
                && cbNamaObat23.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat23.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah23.getValue() && cbNamaObat23.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat23.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah23.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat23.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat23.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah23.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat23.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah23.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah23.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 24                
        if(!(cbNamaObat24.getSelectedItem().toString().equals(null) && cbNamaObat24.getSelectedItem().toString().equals("") 
                && cbNamaObat24.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat24.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah24.getValue() && cbNamaObat24.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat24.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah24.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat24.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat24.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah24.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat24.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah24.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah24.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 25                
        if(!(cbNamaObat25.getSelectedItem().toString().equals(null) && cbNamaObat25.getSelectedItem().toString().equals("") 
                && cbNamaObat25.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat25.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah25.getValue() && cbNamaObat25.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat25.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah25.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat25.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat25.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah25.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat25.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah25.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah25.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 26                
        if(!(cbNamaObat26.getSelectedItem().toString().equals(null) && cbNamaObat26.getSelectedItem().toString().equals("") 
                && cbNamaObat26.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat26.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah26.getValue() && cbNamaObat26.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat26.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah26.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat26.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat26.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah26.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat26.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah26.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah26.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 27                
        if(!(cbNamaObat27.getSelectedItem().toString().equals(null) && cbNamaObat27.getSelectedItem().toString().equals("") 
                && cbNamaObat27.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat27.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah27.getValue() && cbNamaObat27.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat27.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah27.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat27.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat27.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah27.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat27.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah27.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah27.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 28                
        if(!(cbNamaObat28.getSelectedItem().toString().equals(null) && cbNamaObat28.getSelectedItem().toString().equals("") 
                && cbNamaObat28.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat28.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah28.getValue() && cbNamaObat28.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat28.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah28.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat28.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat28.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah28.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat28.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah28.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah28.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 29                
        if(!(cbNamaObat29.getSelectedItem().toString().equals(null) && cbNamaObat29.getSelectedItem().toString().equals("") 
                && cbNamaObat29.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat29.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah29.getValue() && cbNamaObat29.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat29.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah29.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat29.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat29.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah29.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat29.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah29.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah29.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 30                
        if(!(cbNamaObat30.getSelectedItem().toString().equals(null) && cbNamaObat30.getSelectedItem().toString().equals("") 
                && cbNamaObat30.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat30.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah30.getValue() && cbNamaObat30.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat30.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah30.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat30.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat30.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah30.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat30.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah30.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah30.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 31                
        if(!(cbNamaObat31.getSelectedItem().toString().equals(null) && cbNamaObat31.getSelectedItem().toString().equals("") 
                && cbNamaObat31.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat31.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah31.getValue() && cbNamaObat31.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat31.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah31.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat31.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat31.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah31.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat31.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah31.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah31.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 32                
        if(!(cbNamaObat32.getSelectedItem().toString().equals(null) && cbNamaObat32.getSelectedItem().toString().equals("") 
                && cbNamaObat32.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat32.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah32.getValue() && cbNamaObat32.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat32.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah32.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat32.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat32.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah32.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat32.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah32.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah32.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 33                
        if(!(cbNamaObat33.getSelectedItem().toString().equals(null) && cbNamaObat33.getSelectedItem().toString().equals("") 
                && cbNamaObat33.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat33.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah33.getValue() && cbNamaObat33.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat33.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah33.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat33.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat33.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah33.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat33.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah33.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah33.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 34                
        if(!(cbNamaObat34.getSelectedItem().toString().equals(null) && cbNamaObat34.getSelectedItem().toString().equals("") 
                && cbNamaObat34.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat34.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah34.getValue() && cbNamaObat34.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat34.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah34.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat34.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat34.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah34.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat34.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah34.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah34.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 35                
        if(!(cbNamaObat35.getSelectedItem().toString().equals(null) && cbNamaObat35.getSelectedItem().toString().equals("") 
                && cbNamaObat35.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat35.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah35.getValue() && cbNamaObat35.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat35.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah35.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat35.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat35.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah35.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat35.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah35.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah35.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 36                
        if(!(cbNamaObat36.getSelectedItem().toString().equals(null) && cbNamaObat36.getSelectedItem().toString().equals("") 
                && cbNamaObat36.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat36.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah36.getValue() && cbNamaObat36.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat36.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah36.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat36.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat36.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah36.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat36.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah36.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah36.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 37                
        if(!(cbNamaObat37.getSelectedItem().toString().equals(null) && cbNamaObat37.getSelectedItem().toString().equals("") 
                && cbNamaObat37.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat37.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah37.getValue() && cbNamaObat37.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat37.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah37.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat37.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat37.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah37.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat37.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah37.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah37.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 38                
        if(!(cbNamaObat38.getSelectedItem().toString().equals(null) && cbNamaObat38.getSelectedItem().toString().equals("") 
                && cbNamaObat38.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat38.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah38.getValue() && cbNamaObat38.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat38.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah38.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat38.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat38.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah38.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat38.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah38.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah38.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 39                
        if(!(cbNamaObat39.getSelectedItem().toString().equals(null) && cbNamaObat39.getSelectedItem().toString().equals("") 
                && cbNamaObat39.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat39.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah39.getValue() && cbNamaObat39.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat39.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah39.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat39.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat39.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah39.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat39.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah39.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah39.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 40                
        if(!(cbNamaObat40.getSelectedItem().toString().equals(null) && cbNamaObat40.getSelectedItem().toString().equals("") 
                && cbNamaObat40.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat40.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah40.getValue() && cbNamaObat40.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat40.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah40.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat40.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat40.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah40.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat40.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah40.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah40.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 41                
        if(!(cbNamaObat41.getSelectedItem().toString().equals(null) && cbNamaObat41.getSelectedItem().toString().equals("") 
                && cbNamaObat41.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat41.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah41.getValue() && cbNamaObat41.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat41.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah41.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat41.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat41.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah41.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat41.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah41.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah41.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 42                
        if(!(cbNamaObat42.getSelectedItem().toString().equals(null) && cbNamaObat42.getSelectedItem().toString().equals("") 
                && cbNamaObat42.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat42.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah42.getValue() && cbNamaObat42.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat42.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah42.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat42.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat42.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah42.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat42.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah42.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah42.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 43                
        if(!(cbNamaObat43.getSelectedItem().toString().equals(null) && cbNamaObat43.getSelectedItem().toString().equals("") 
                && cbNamaObat43.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat43.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah43.getValue() && cbNamaObat43.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat43.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah43.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat43.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat43.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah43.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat43.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah43.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah43.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 44                
        if(!(cbNamaObat44.getSelectedItem().toString().equals(null) && cbNamaObat44.getSelectedItem().toString().equals("") 
                && cbNamaObat44.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat44.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah44.getValue() && cbNamaObat44.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat44.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah44.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat44.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat44.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah44.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat44.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah44.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah44.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 45                
        if(!(cbNamaObat45.getSelectedItem().toString().equals(null) && cbNamaObat45.getSelectedItem().toString().equals("") 
                && cbNamaObat45.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat45.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah45.getValue() && cbNamaObat45.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat45.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah45.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat45.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat45.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah45.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat45.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah45.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah45.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 46                
        if(!(cbNamaObat46.getSelectedItem().toString().equals(null) && cbNamaObat46.getSelectedItem().toString().equals("") 
                && cbNamaObat46.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat46.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah46.getValue() && cbNamaObat46.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat46.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah46.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat46.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat46.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah46.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat46.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah46.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah46.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 47                
        if(!(cbNamaObat47.getSelectedItem().toString().equals(null) && cbNamaObat47.getSelectedItem().toString().equals("") 
                && cbNamaObat47.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat47.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah47.getValue() && cbNamaObat47.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat47.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah47.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat47.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat47.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah47.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat47.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah47.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah47.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 48                
        if(!(cbNamaObat48.getSelectedItem().toString().equals(null) && cbNamaObat48.getSelectedItem().toString().equals("") 
                && cbNamaObat48.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat48.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah48.getValue() && cbNamaObat48.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat48.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah48.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat48.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat48.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah48.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat48.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah48.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah48.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 49                
        if(!(cbNamaObat49.getSelectedItem().toString().equals(null) && cbNamaObat49.getSelectedItem().toString().equals("") 
                && cbNamaObat49.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat49.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah49.getValue() && cbNamaObat49.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat49.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah49.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat49.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat49.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah49.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat49.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah49.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah49.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 50                
        if(!(cbNamaObat50.getSelectedItem().toString().equals(null) && cbNamaObat50.getSelectedItem().toString().equals("") 
                && cbNamaObat50.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat50.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah50.getValue() && cbNamaObat50.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat50.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah50.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat50.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat50.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah50.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat50.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah50.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah50.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 51                
        if(!(cbNamaObat51.getSelectedItem().toString().equals(null) && cbNamaObat51.getSelectedItem().toString().equals("") 
                && cbNamaObat51.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat51.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah51.getValue() && cbNamaObat51.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat51.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah51.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat51.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat51.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah51.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat51.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah51.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah51.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 52                
        if(!(cbNamaObat52.getSelectedItem().toString().equals(null) && cbNamaObat52.getSelectedItem().toString().equals("") 
                && cbNamaObat52.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat52.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah52.getValue() && cbNamaObat52.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat52.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah52.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat52.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat52.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah52.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat52.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah52.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah52.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 53                
        if(!(cbNamaObat53.getSelectedItem().toString().equals(null) && cbNamaObat53.getSelectedItem().toString().equals("") 
                && cbNamaObat53.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat53.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah53.getValue() && cbNamaObat53.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat53.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah53.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat53.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat53.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah53.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat53.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah53.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah53.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 54                
        if(!(cbNamaObat54.getSelectedItem().toString().equals(null) && cbNamaObat54.getSelectedItem().toString().equals("") 
                && cbNamaObat54.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat54.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah54.getValue() && cbNamaObat54.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat54.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah54.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat54.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat54.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah54.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat54.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah54.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah54.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 55                
        if(!(cbNamaObat55.getSelectedItem().toString().equals(null) && cbNamaObat55.getSelectedItem().toString().equals("") 
                && cbNamaObat55.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat55.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah55.getValue() && cbNamaObat55.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat55.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah55.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat55.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat55.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah55.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat55.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah55.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah55.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 56                
        if(!(cbNamaObat56.getSelectedItem().toString().equals(null) && cbNamaObat56.getSelectedItem().toString().equals("") 
                && cbNamaObat56.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat56.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah56.getValue() && cbNamaObat56.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat56.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah56.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat56.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat56.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah56.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat57.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah57.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah56.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 57                
        if(!(cbNamaObat57.getSelectedItem().toString().equals(null) && cbNamaObat57.getSelectedItem().toString().equals("") 
                && cbNamaObat57.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat57.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah57.getValue() && cbNamaObat57.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat57.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah57.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat57.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat57.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah57.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat57.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah57.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah57.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 58                
        if(!(cbNamaObat58.getSelectedItem().toString().equals(null) && cbNamaObat58.getSelectedItem().toString().equals("") 
                && cbNamaObat58.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat58.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah58.getValue() && cbNamaObat58.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat58.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah58.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat58.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat58.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah58.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat58.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah58.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah58.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 59                
        if(!(cbNamaObat59.getSelectedItem().toString().equals(null) && cbNamaObat59.getSelectedItem().toString().equals("") 
                && cbNamaObat59.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat59.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah59.getValue() && cbNamaObat59.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat59.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah59.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat59.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat59.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah59.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat59.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah59.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah59.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }
			// INPUT OBAT KE 60                
        if(!(cbNamaObat60.getSelectedItem().toString().equals(null) && cbNamaObat60.getSelectedItem().toString().equals("") 
                && cbNamaObat60.getSelectedItem().toString().equalsIgnoreCase("<Pilih Obat>"))) {
            try {
                rsObat = stmt.executeQuery("SELECT * FROM DataObat WHERE namaObat='" 
                        + cbNamaObat60.getSelectedItem().toString() + "'");
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
            
            if(jumlahSkr >= (int) spnJumlah60.getValue() && cbNamaObat60.getSelectedItem().toString().equalsIgnoreCase(obat)) {
                try {
                    rsJual = stmt1.executeQuery("SELECT * FROM DataJual WHERE namaObatJ='" + cbNamaObat60.getSelectedItem().toString() +
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
                
                int totalA = AjumlahSkr + (int) spnJumlah60.getValue();
                
                 sqlInsert = "INSERT INTO DataJual (tglMasukJ,namaObatJ,golObatJ,satJ,jumlahSediaJ) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat60.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'" + sat + "',"
                        + "'" + totalA + "');";
                 
                 sqlDelete = "DELETE FROM DataJual "
                            + "WHERE namaObatJ='" + cbNamaObat60.getSelectedItem().toString() + 
                            "' AND jumlahSediaJ=" + AjumlahSkr + 
                            " AND satJ='" + sat + 
                            "' AND golObatJ='" + gol + "'";
                    
                try {
                   int berhasil = stmt1.executeUpdate(sqlInsert);
                   int berhasil1 = stmt2.executeUpdate(sqlDelete);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }

                int sisaObat = jumlahSkr - (int) spnJumlah60.getValue();

                sqlUpdate = "UPDATE DataObat SET "
                    + "jumlahSedia='" + sisaObat + "' "
                    + "WHERE namaObat='" + obat + "' AND golObat='" + gol + "' AND sat='" + sat + "'";

                try {
                   int berhasil = stmt2.executeUpdate(sqlUpdate);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                String sqlInsertTrans = "INSERT INTO Transaksi (tanggal,namaObat,golongan,penggunaanUntuk,noResep,jumlahKeluar) "
                        + "VALUES ('" + tanggal + "',"
                        + "'" + cbNamaObat60.getSelectedItem().toString() + "',"
                        + "'" + gol + "',"
                        + "'Resep',"
                        + "'" + txtResep.getText() + "',"
                        + "'" + (int) spnJumlah60.getValue() + "');";
                
                try {
                   int trans = stmt2.executeUpdate(sqlInsertTrans);
                } catch (SQLException errMsg) {
                    System.out.println(errMsg);
                }
                
                setObat so = new setObat();
                    so.setExGudang(Date.valueOf(tanggal));
                    so.setNamaObat(obat);
                    so.setGolObat(gol);
                    so.setSat(sat);
                    so.setSisaApotek((int)spnJumlah60.getValue());
                    this.list1.add(so);
                    updateTableJual();
            }	
        }}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}
       
       sql = "INSERT INTO DataResep (NoResep,Tanggal,NamaPasien,Usia,Alamat,JenisLayanan,BpjsNonBpjs,namaObat,jmlObat) "
                + "VALUES ('"+ txtResep.getText() +  "',"
                + "'" + Date.valueOf(tanggal) + "',"
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

    private void cbCariObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCariObatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbCariObatActionPerformed

    private void cbCariNamaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbCariNamaKeyReleased
        // TODO add your handling code here:
        String query = cbCariNama.getText();
        filter(query);
    }//GEN-LAST:event_cbCariNamaKeyReleased

    private void cbCariObatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbCariObatKeyReleased
        // TODO add your handling code here:
        String query = cbCariObat.getSelectedItem().toString();
        filter(query);
    }//GEN-LAST:event_cbCariObatKeyReleased

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        String pilihan = null;
        try {
            setResep rs = new setResep();
            ArrayList<String> list2 = new ArrayList<String>();
            rsResep1 = stmt.executeQuery("SELECT * FROM DataResep");
          
            while(rsResep1.next()==true){
                list2.add(rsResep1.getString("NoResep"));
            }
             String [] noResep = new String[list2.size()];
             noResep = list2.toArray(noResep);
            System.out.println(noResep);
            JFrame frame = new JFrame("Input Dialog Example 3");
            pilihan = (String) JOptionPane.showInputDialog(frame,
                    "Pilih Nomor Resep",
                    "Edit",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    noResep,
                    noResep[0]);
            System.out.println(pilihan);
        } catch (SQLException ex) {
            Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        String no = null, nama = null, umur = null, alamat = null, jnsLay = null, bpjs = null;
        Date tgl = null;
        try {
            rsResep2 = stmt1.executeQuery("SELECT * FROM DataResep WHERE NoResep='" + pilihan + "'");
            
            while(rsResep2.next()==true){
                no = rsResep2.getString("NoResep");
                tgl = rsResep2.getDate("Tanggal");
                nama = rsResep2.getString("NamaPasien");
                umur = rsResep2.getString("Usia");
                alamat = rsResep2.getString("Alamat");
                jnsLay = rsResep2.getString("JenisLayanan");
                bpjs = rsResep2.getString("BpjsNonBpjs");
            }
            
            txtTanggalResep.setDate(tgl);
            txtNamaPasien.setText(nama);
            txtResep.setText(no);
            txtAlamat.setText(alamat);
            spnThn.setValue(Integer.parseInt(umur));
            cbJenisLayanan.setSelectedItem(jnsLay);
            if(bpjs.equalsIgnoreCase("BPJS")) {
                rbBPJS.setSelected(true);
            } else if(bpjs.equalsIgnoreCase("non BPJS")) {
                rbNBPJS.setSelected(true);
            }
            btnSimpan.setEnabled(false);
            clearAll();
        } catch (SQLException ex) {
            Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        String tanggal = (txtTanggalResep.getDate().getYear()+1900) + "-" + 
                (txtTanggalResep.getDate().getMonth()+1) + "-" + 
                txtTanggalResep.getDate().getDate();
        String cekBpjs = rbBPJS.isSelected() ? "BPJS" : "non BPJS";
        String sql;
        sql="UPDATE DataResep SET "
                + "NamaPasien='" + txtNamaPasien.getText() + "', "
                + "Alamat='" + txtAlamat.getText() + "', "
                + "Usia='" + spnThn.getValue() + "', "
                + "Tanggal='" + tanggal + "', "
                + "BpjsNonBpjs='" + cekBpjs + "', "
                + "JenisLayanan='" + cbJenisLayanan.getSelectedItem() + "' "
                + "WHERE NoResep='" + txtResep.getText() + "';";
        
        try {
            setConnection koneksi = new setConnection();
            stmt2 = koneksi.connection.createStatement();
            int update = stmt2.executeUpdate(sql);
        } catch (SQLException errMsg) {
            System.out.println("Ada Kesalahan: " + errMsg.getMessage());
        }
        
        setResep resep = new setResep();
        resep.setResep(txtResep.getText());
        resep.setNamaPasien(txtNamaPasien.getText());
        resep.setAlamat(txtAlamat.getText());
        resep.setBpjs_nonBpjs(cekBpjs);
        resep.setJenisLayanan(String.valueOf(cbJenisLayanan.getSelectedItem()));
        resep.setTanggal(Date.valueOf(tanggal));
        resep.setUsia(String.valueOf(spnThn.getValue()));
        this.list.set(this.index, resep);
        updateTable();
        
        btnSimpan.setEnabled(true);
        clearAll();
    }//GEN-LAST:event_btnUpdateActionPerformed
    private void editTable(String s) throws Exception{
        rsResep1 = stmt.executeQuery("SELECT * FROM DataResep WHERE NoResep = " + s);
        
    }
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
    
    private void clearAll(){
        txtTanggalResep.setCalendar(Calendar.getInstance());
        txtNamaPasien.setText("");
        txtAlamat.setText("");
        txtResep.setText("");
        spnThn.setValue(0);
        cbJenisLayanan.setSelectedIndex(0);
        cbNamaObat1.setSelectedIndex(0);
        cbNamaObat2.setSelectedIndex(0);
        cbNamaObat3.setSelectedIndex(0);
        cbNamaObat4.setSelectedIndex(0);
        cbNamaObat5.setSelectedIndex(0);
        cbNamaObat6.setSelectedIndex(0);
        cbNamaObat7.setSelectedIndex(0);
        cbNamaObat8.setSelectedIndex(0);
        cbNamaObat9.setSelectedIndex(0);
        cbNamaObat10.setSelectedIndex(0);
        cbNamaObat11.setSelectedIndex(0);
        cbNamaObat12.setSelectedIndex(0);
        cbNamaObat13.setSelectedIndex(0);
        cbNamaObat14.setSelectedIndex(0);
        cbNamaObat15.setSelectedIndex(0);
        cbNamaObat16.setSelectedIndex(0);
        cbNamaObat17.setSelectedIndex(0);
        cbNamaObat18.setSelectedIndex(0);
        cbNamaObat19.setSelectedIndex(0);
        cbNamaObat20.setSelectedIndex(0);
        cbNamaObat21.setSelectedIndex(0);
        cbNamaObat22.setSelectedIndex(0);
        cbNamaObat23.setSelectedIndex(0);
        cbNamaObat24.setSelectedIndex(0);
        cbNamaObat25.setSelectedIndex(0);
        cbNamaObat26.setSelectedIndex(0);
        cbNamaObat27.setSelectedIndex(0);
        cbNamaObat28.setSelectedIndex(0);
        cbNamaObat29.setSelectedIndex(0);
        cbNamaObat30.setSelectedIndex(0);
        cbNamaObat31.setSelectedIndex(0);
        cbNamaObat32.setSelectedIndex(0);
        cbNamaObat33.setSelectedIndex(0);
        cbNamaObat34.setSelectedIndex(0);
        cbNamaObat35.setSelectedIndex(0);
        cbNamaObat36.setSelectedIndex(0);
        cbNamaObat37.setSelectedIndex(0);
        cbNamaObat38.setSelectedIndex(0);
        cbNamaObat39.setSelectedIndex(0);
        cbNamaObat40.setSelectedIndex(0);
        cbNamaObat41.setSelectedIndex(0);
        cbNamaObat42.setSelectedIndex(0);
        cbNamaObat43.setSelectedIndex(0);
        cbNamaObat44.setSelectedIndex(0);
        cbNamaObat45.setSelectedIndex(0);
        cbNamaObat46.setSelectedIndex(0);
        cbNamaObat47.setSelectedIndex(0);
        cbNamaObat48.setSelectedIndex(0);
        cbNamaObat49.setSelectedIndex(0);
        cbNamaObat50.setSelectedIndex(0);
        cbNamaObat51.setSelectedIndex(0);
        cbNamaObat52.setSelectedIndex(0);
        cbNamaObat53.setSelectedIndex(0);
        cbNamaObat54.setSelectedIndex(0);
        cbNamaObat55.setSelectedIndex(0);
        cbNamaObat56.setSelectedIndex(0);
        cbNamaObat57.setSelectedIndex(0);
        cbNamaObat58.setSelectedIndex(0);
        cbNamaObat59.setSelectedIndex(0);
        cbNamaObat60.setSelectedIndex(0);        
       
       
        
        spnJumlah1.setValue(0);
        spnJumlah2.setValue(0);
        spnJumlah3.setValue(0);
        spnJumlah4.setValue(0);
        spnJumlah5.setValue(0);
        spnJumlah6.setValue(0);
        spnJumlah7.setValue(0);
        spnJumlah8.setValue(0);
        spnJumlah9.setValue(0);
        spnJumlah10.setValue(0);
        spnJumlah11.setValue(0);
        spnJumlah12.setValue(0);
        spnJumlah13.setValue(0);
        spnJumlah14.setValue(0);
        spnJumlah15.setValue(0);
        spnJumlah16.setValue(0);
        spnJumlah17.setValue(0);
        spnJumlah18.setValue(0);
        spnJumlah19.setValue(0);
        spnJumlah20.setValue(0);
        spnJumlah21.setValue(0);
        spnJumlah22.setValue(0);
        spnJumlah23.setValue(0);
        spnJumlah24.setValue(0);
        spnJumlah25.setValue(0);
        spnJumlah26.setValue(0);
        spnJumlah27.setValue(0);
        spnJumlah28.setValue(0);
        spnJumlah29.setValue(0);
        spnJumlah30.setValue(0);
        spnJumlah31.setValue(0);
        spnJumlah32.setValue(0);
        spnJumlah33.setValue(0);
        spnJumlah34.setValue(0);
        spnJumlah35.setValue(0);
        spnJumlah36.setValue(0);
        spnJumlah37.setValue(0);
        spnJumlah38.setValue(0);
        spnJumlah39.setValue(0);
        spnJumlah40.setValue(0);
        spnJumlah41.setValue(0);
        spnJumlah42.setValue(0);
        spnJumlah43.setValue(0);
        spnJumlah44.setValue(0);
        spnJumlah45.setValue(0);
        spnJumlah46.setValue(0);
        spnJumlah47.setValue(0);
        spnJumlah48.setValue(0);
        spnJumlah49.setValue(0);
        spnJumlah50.setValue(0);
        spnJumlah51.setValue(0);
        spnJumlah52.setValue(0);
        spnJumlah53.setValue(0);
        spnJumlah54.setValue(0);
        spnJumlah55.setValue(0);
        spnJumlah56.setValue(0);
        spnJumlah57.setValue(0);
        spnJumlah58.setValue(0);
        spnJumlah59.setValue(0);
        spnJumlah60.setValue(0);           
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
    private javax.swing.JButton btnEdit;
    private javax.swing.JLabel btnKeluar;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JTextField cbCariNama;
    private javax.swing.JComboBox<String> cbCariObat;
    private javax.swing.JComboBox<String> cbJenisLayanan;
    private javax.swing.JComboBox<String> cbNamaObat1;
    private javax.swing.JComboBox<String> cbNamaObat10;
    private javax.swing.JComboBox<String> cbNamaObat11;
    private javax.swing.JComboBox<String> cbNamaObat12;
    private javax.swing.JComboBox<String> cbNamaObat13;
    private javax.swing.JComboBox<String> cbNamaObat14;
    private javax.swing.JComboBox<String> cbNamaObat15;
    private javax.swing.JComboBox<String> cbNamaObat16;
    private javax.swing.JComboBox<String> cbNamaObat17;
    private javax.swing.JComboBox<String> cbNamaObat18;
    private javax.swing.JComboBox<String> cbNamaObat19;
    private javax.swing.JComboBox<String> cbNamaObat2;
    private javax.swing.JComboBox<String> cbNamaObat20;
    private javax.swing.JComboBox<String> cbNamaObat21;
    private javax.swing.JComboBox<String> cbNamaObat22;
    private javax.swing.JComboBox<String> cbNamaObat23;
    private javax.swing.JComboBox<String> cbNamaObat24;
    private javax.swing.JComboBox<String> cbNamaObat25;
    private javax.swing.JComboBox<String> cbNamaObat26;
    private javax.swing.JComboBox<String> cbNamaObat27;
    private javax.swing.JComboBox<String> cbNamaObat28;
    private javax.swing.JComboBox<String> cbNamaObat29;
    private javax.swing.JComboBox<String> cbNamaObat3;
    private javax.swing.JComboBox<String> cbNamaObat30;
    private javax.swing.JComboBox<String> cbNamaObat31;
    private javax.swing.JComboBox<String> cbNamaObat32;
    private javax.swing.JComboBox<String> cbNamaObat33;
    private javax.swing.JComboBox<String> cbNamaObat34;
    private javax.swing.JComboBox<String> cbNamaObat35;
    private javax.swing.JComboBox<String> cbNamaObat36;
    private javax.swing.JComboBox<String> cbNamaObat37;
    private javax.swing.JComboBox<String> cbNamaObat38;
    private javax.swing.JComboBox<String> cbNamaObat39;
    private javax.swing.JComboBox<String> cbNamaObat4;
    private javax.swing.JComboBox<String> cbNamaObat40;
    private javax.swing.JComboBox<String> cbNamaObat41;
    private javax.swing.JComboBox<String> cbNamaObat42;
    private javax.swing.JComboBox<String> cbNamaObat43;
    private javax.swing.JComboBox<String> cbNamaObat44;
    private javax.swing.JComboBox<String> cbNamaObat45;
    private javax.swing.JComboBox<String> cbNamaObat46;
    private javax.swing.JComboBox<String> cbNamaObat47;
    private javax.swing.JComboBox<String> cbNamaObat48;
    private javax.swing.JComboBox<String> cbNamaObat49;
    private javax.swing.JComboBox<String> cbNamaObat5;
    private javax.swing.JComboBox<String> cbNamaObat50;
    private javax.swing.JComboBox<String> cbNamaObat51;
    private javax.swing.JComboBox<String> cbNamaObat52;
    private javax.swing.JComboBox<String> cbNamaObat53;
    private javax.swing.JComboBox<String> cbNamaObat54;
    private javax.swing.JComboBox<String> cbNamaObat55;
    private javax.swing.JComboBox<String> cbNamaObat56;
    private javax.swing.JComboBox<String> cbNamaObat57;
    private javax.swing.JComboBox<String> cbNamaObat58;
    private javax.swing.JComboBox<String> cbNamaObat59;
    private javax.swing.JComboBox<String> cbNamaObat6;
    private javax.swing.JComboBox<String> cbNamaObat60;
    private javax.swing.JComboBox<String> cbNamaObat7;
    private javax.swing.JComboBox<String> cbNamaObat8;
    private javax.swing.JComboBox<String> cbNamaObat9;
    private PanelTransparan.ClPanelTransparan clPanelTransparan1;
    private PanelTransparan.ClPanelTransparan clPanelTransparan3;
    private PanelTransparan.ClPanelTransparan clPanelTransparan4;
    private PanelTransparan.ClPanelTransparan clPanelTransparan5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
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
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JRadioButton rbBPJS;
    private javax.swing.JRadioButton rbNBPJS;
    private javax.swing.JSpinner spnJumlah1;
    private javax.swing.JSpinner spnJumlah10;
    private javax.swing.JSpinner spnJumlah11;
    private javax.swing.JSpinner spnJumlah12;
    private javax.swing.JSpinner spnJumlah13;
    private javax.swing.JSpinner spnJumlah14;
    private javax.swing.JSpinner spnJumlah15;
    private javax.swing.JSpinner spnJumlah16;
    private javax.swing.JSpinner spnJumlah17;
    private javax.swing.JSpinner spnJumlah18;
    private javax.swing.JSpinner spnJumlah19;
    private javax.swing.JSpinner spnJumlah2;
    private javax.swing.JSpinner spnJumlah20;
    private javax.swing.JSpinner spnJumlah21;
    private javax.swing.JSpinner spnJumlah22;
    private javax.swing.JSpinner spnJumlah23;
    private javax.swing.JSpinner spnJumlah24;
    private javax.swing.JSpinner spnJumlah25;
    private javax.swing.JSpinner spnJumlah26;
    private javax.swing.JSpinner spnJumlah27;
    private javax.swing.JSpinner spnJumlah28;
    private javax.swing.JSpinner spnJumlah29;
    private javax.swing.JSpinner spnJumlah3;
    private javax.swing.JSpinner spnJumlah30;
    private javax.swing.JSpinner spnJumlah31;
    private javax.swing.JSpinner spnJumlah32;
    private javax.swing.JSpinner spnJumlah33;
    private javax.swing.JSpinner spnJumlah34;
    private javax.swing.JSpinner spnJumlah35;
    private javax.swing.JSpinner spnJumlah36;
    private javax.swing.JSpinner spnJumlah37;
    private javax.swing.JSpinner spnJumlah38;
    private javax.swing.JSpinner spnJumlah39;
    private javax.swing.JSpinner spnJumlah4;
    private javax.swing.JSpinner spnJumlah40;
    private javax.swing.JSpinner spnJumlah41;
    private javax.swing.JSpinner spnJumlah42;
    private javax.swing.JSpinner spnJumlah43;
    private javax.swing.JSpinner spnJumlah44;
    private javax.swing.JSpinner spnJumlah45;
    private javax.swing.JSpinner spnJumlah46;
    private javax.swing.JSpinner spnJumlah47;
    private javax.swing.JSpinner spnJumlah48;
    private javax.swing.JSpinner spnJumlah49;
    private javax.swing.JSpinner spnJumlah5;
    private javax.swing.JSpinner spnJumlah50;
    private javax.swing.JSpinner spnJumlah51;
    private javax.swing.JSpinner spnJumlah52;
    private javax.swing.JSpinner spnJumlah53;
    private javax.swing.JSpinner spnJumlah54;
    private javax.swing.JSpinner spnJumlah55;
    private javax.swing.JSpinner spnJumlah56;
    private javax.swing.JSpinner spnJumlah57;
    private javax.swing.JSpinner spnJumlah58;
    private javax.swing.JSpinner spnJumlah59;
    private javax.swing.JSpinner spnJumlah6;
    private javax.swing.JSpinner spnJumlah60;
    private javax.swing.JSpinner spnJumlah7;
    private javax.swing.JSpinner spnJumlah8;
    private javax.swing.JSpinner spnJumlah9;
    private javax.swing.JSpinner spnThn;
    private javax.swing.JTable tblEx;
    private javax.swing.JTable tblJual;
    private javax.swing.JTextArea txtAlamat;
    private javax.swing.JTextField txtNamaPasien;
    private javax.swing.JTextField txtResep;
    private javax.swing.JLabel txtTanggal;
    private com.toedter.calendar.JDateChooser txtTanggalResep;
    private javax.swing.JLabel txtWelcome;
    // End of variables declaration//GEN-END:variables
}
