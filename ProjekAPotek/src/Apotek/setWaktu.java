package Apotek;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.Timer;

public class setWaktu {
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
                
                MainMenu.txtTanggal.setText(String.valueOf(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) + " " + 
                        waktu + ":" + menit + ":" + detik);
            }
        };
        new Timer(1000, taskPerformer).start();
    }
}
