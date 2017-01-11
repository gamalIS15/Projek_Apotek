package Apotek;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Color;

public class SetImage extends JPanel {
    private Image image=null;
    private Color warna=null;
    
    public SetImage (String file) {
        try{
            image = new ImageIcon(getClass().getResource(file)).getImage();
        }catch(Exception e){
            System.out.println(e);; 
        }
    }
    
    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        Graphics2D graphic = (Graphics2D) grphcs.create();
        graphic.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        graphic.dispose();
    }
}