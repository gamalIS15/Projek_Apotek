package apotek;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;

import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.table.WebTable;

import java.awt.SystemColor;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import PanelTransparan.ClPanelTransparan;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.UIManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;

public class MainMenu extends WebFrame {
	private WebTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu frame = new MainMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainMenu() {
		setTitle("Menu Utama");
		getContentPane().setBackground(SystemColor.windowBorder);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("Resep");
		lblNewLabel_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblNewLabel_2.setIcon(new ImageIcon(MainMenu.class.getResource("/image/Resep2.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblNewLabel_2.setIcon(new ImageIcon(MainMenu.class.getResource("/image/Resep.png")));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				Resep rs = new Resep();
				rs.setVisible(true);
				
			}
		});
		lblNewLabel_2.setForeground(SystemColor.text);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_2.setBounds(170, 357, 66, 77);
		getContentPane().add(lblNewLabel_2);
		lblNewLabel_2.setIcon(new ImageIcon(MainMenu.class.getResource("/image/Resep.png")));
		lblNewLabel_2.setVerticalTextPosition(SwingConstants.BOTTOM);
		lblNewLabel_2.setHorizontalTextPosition(SwingConstants.CENTER);
		
		JLabel lblNewLabel_1 = new JLabel("Data Obat");
		lblNewLabel_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblNewLabel_1.setIcon(new ImageIcon(MainMenu.class.getResource("/image/DataObat2.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblNewLabel_1.setIcon(new ImageIcon(MainMenu.class.getResource("/image/DataObat.png")));
			}
		});
		lblNewLabel_1.setForeground(SystemColor.text);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1.setBounds(262, 357, 73, 77);
		getContentPane().add(lblNewLabel_1);
		lblNewLabel_1.setVerticalTextPosition(SwingConstants.BOTTOM);
		lblNewLabel_1.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel_1.setIcon(new ImageIcon(MainMenu.class.getResource("/image/DataObat.png")));
		
		JLabel lblNewLabel = new JLabel("Gudang");
		lblNewLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblNewLabel.setIcon(new ImageIcon(MainMenu.class.getResource("/image/Gudang2.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblNewLabel.setIcon(new ImageIcon(MainMenu.class.getResource("/image/Gudang.png")));
			}
		});
		lblNewLabel.setForeground(SystemColor.menu);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel.setBounds(358, 357, 59, 77);
		getContentPane().add(lblNewLabel);
		lblNewLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
		lblNewLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(MainMenu.class.getResource("/image/Gudang.png")));
		
		JLabel lblNewLabel_3 = new JLabel("Laporan");
		lblNewLabel_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblNewLabel_3.setIcon(new ImageIcon(MainMenu.class.getResource("/image/Laporan2.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblNewLabel_3.setIcon(new ImageIcon(MainMenu.class.getResource("/image/Laporan.png")));
			}
		});
		lblNewLabel_3.setForeground(SystemColor.text);
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_3.setBounds(446, 357, 59, 77);
		getContentPane().add(lblNewLabel_3);
		lblNewLabel_3.setVerticalTextPosition(SwingConstants.BOTTOM);
		lblNewLabel_3.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel_3.setIcon(new ImageIcon(MainMenu.class.getResource("/image/Laporan.png")));
		
		JLabel lblNewLabel_4 = new JLabel("Bantuan\r\n");
		lblNewLabel_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblNewLabel_4.setIcon(new ImageIcon(MainMenu.class.getResource("/image/Bantuan2.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblNewLabel_4.setIcon(new ImageIcon(MainMenu.class.getResource("/image/Bantuan.png")));
			}
		});
		lblNewLabel_4.setForeground(SystemColor.text);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_4.setBounds(538, 357, 66, 77);
		getContentPane().add(lblNewLabel_4);
		lblNewLabel_4.setVerticalTextPosition(SwingConstants.BOTTOM);
		lblNewLabel_4.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel_4.setIcon(new ImageIcon(MainMenu.class.getResource("/image/Bantuan.png")));
		
		ClPanelTransparan upperPanel = new ClPanelTransparan();
		upperPanel.setBorder(null);
		upperPanel.setBackground(SystemColor.activeCaptionBorder);
		upperPanel.setBounds(0, 0, 772, 21);
		getContentPane().add(upperPanel);
		
		ClPanelTransparan panel = new ClPanelTransparan();
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel.setBounds(124, 386, 508, 59);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		table = new WebTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, "", null, null},
			},
			new String[] {
				"Nama Obat", "Persediaan Awal", "Banyaknya Pengambilan", "Total Persediaan", "Tanggal Pengambilan", "Tanggal Kadarluarsa"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(114);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(132);
		table.getColumnModel().getColumn(3).setPreferredWidth(96);
		table.getColumnModel().getColumn(4).setPreferredWidth(117);
		table.getColumnModel().getColumn(5).setPreferredWidth(119);
		table.setBounds(45, 113, 661, 108);
		getContentPane().add(table);
		
		JLabel background = new JLabel("");
		background.setIcon(new ImageIcon(MainMenu.class.getResource("/image/wall.png")));
		background.setBounds(0, 0, 772, 445);
		getContentPane().add(background);
		setResizable(false);
		setBounds(100, 100, 778, 474);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}
