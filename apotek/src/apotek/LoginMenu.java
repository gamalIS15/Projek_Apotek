package apotek;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.text.WebTextField;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.SystemColor;
import PanelTransparan.ClPanelTransparan;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class LoginMenu extends WebFrame {

	private JPanel contentPane;	
	private WebTextField wTxtF_1;
	private WebTextField wTxtF;
	private ClPanelTransparan pane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new WebLookAndFeel());
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginMenu frame = new LoginMenu();					
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
	public LoginMenu() {
		setShadeWidth(4);
		setShowWindowButtons(true);
		setRound(5);
		setShowMenuBar(true);
		setShowMinimizeButton(true);
		setShowMaximizeButton(false);
		setShowCloseButton(true);
		setTitle("Halaman Log In\r\n");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 676, 460);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.windowBorder);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		ClPanelTransparan pane = new ClPanelTransparan();		
		pane.setBounds(214, 57, 242, 286);
		contentPane.add(pane);
		pane.setLayout(null);
		
		wTxtF = new WebTextField();
		wTxtF.setBackground(SystemColor.activeCaptionBorder);
		wTxtF.setWebColored(true);
		wTxtF.setShadeWidth(1);
		wTxtF.setRound(1);
		wTxtF.setBounds(34, 122, 177, 31);
		pane.add(wTxtF);
		wTxtF.setColumns(10);
		
		wTxtF_1 = new WebTextField();
		wTxtF_1.setBackground(SystemColor.activeCaptionBorder);
		wTxtF_1.setShadeWidth(1);
		wTxtF_1.setRound(1);
		wTxtF_1.setWebColored(true);
		wTxtF_1.setBounds(34, 184, 177, 31);
		pane.add(wTxtF_1);
		wTxtF_1.setColumns(10);
		
		WebButton btnNewButton = new WebButton("Login");
		btnNewButton.setRound(1);
		btnNewButton.setTopBgColor(SystemColor.inactiveCaption);
		btnNewButton.setShadeColor(SystemColor.activeCaption);
		btnNewButton.setRolloverShine(true);
		btnNewButton.setForeground(SystemColor.text);
		btnNewButton.setBottomSelectedBgColor(SystemColor.activeCaption);
		btnNewButton.setBottomBgColor(SystemColor.textHighlight);
		btnNewButton.setBoldFont(true);
		btnNewButton.setShineColor(SystemColor.textHighlight);
		btnNewButton.setBackground(SystemColor.textHighlight);
		btnNewButton.setBounds(89, 227, 85, 31);
		pane.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("ID Pegawai");
		lblNewLabel.setForeground(SystemColor.text);
		lblNewLabel.setBounds(34, 102, 85, 15);
		pane.add(lblNewLabel);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(SystemColor.text);
		lblPassword.setBounds(34, 169, 53, 15);
		pane.add(lblPassword);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(LoginMenu.class.getResource("/image/login.png")));
		lblNewLabel_1.setBounds(89, 26, 64, 64);
		pane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon(LoginMenu.class.getResource("/image/wall.png")));
		lblNewLabel_2.setBounds(0, 0, 670, 431);
		contentPane.add(lblNewLabel_2);
		
	}
}
