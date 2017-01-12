/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Apotek;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class setApoteker {
    Statement stmt;
    ResultSet rs;
    private String id;
    private String pass;
    private String nama;
    private String tmpID = null;
    private String tmpPass = null;
    
    public setApoteker() {
    }
    
    public void LoginApoteker() {
    try {
        setConnection koneksi = new setConnection();
            stmt = koneksi.connection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM LoginApoteker");

            while(rs.next() == true) {
                id = rs.getString("id");
                pass = rs.getString("password");
                nama = rs.getString("nama");
                
                if(Login.txtID.getText().equals(id) && Login.txtPass.getText().equals(pass)) {
                    tmpID = Login.txtID.getText();
                    tmpPass = Login.txtPass.getText();
                    JOptionPane.showMessageDialog(new Login(), "Login Berhasil", 
                        "Selamat", JOptionPane.INFORMATION_MESSAGE);
//                    Form_Withdrawal fw = new Form_Withdrawal(tmpNoRek, tmpPIN);
//                    fw.setVisible(true);
                }
            }
             if(!(Login.txtID.getText().equals(id) && Login.txtPass.getText().equals(pass))) {
                JOptionPane.showMessageDialog(new Login(), "Login Gagal! \n"
                    + "Mohon periksa kembali ID dan Password Anda.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                new Login().setVisible(true);
            }
        } catch (SQLException ex) {
            System.out.println("Ada Kesalahan: " + ex.getMessage()); 
        }
    }
}
