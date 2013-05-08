package game;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import proxies.ServerProxy;

public class LoginFrame extends JFrame implements ActionListener, KeyListener{
	TextField login,password;
	Button doLogin;
	private static final long serialVersionUID = 1L;
	Label et=new Label();
	ServerProxy proxy;
	public void init () {
		proxy = new ServerProxy();
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		doLogin = new Button("Login");
		doLogin.addActionListener(this);
		login = new TextField(40);
		password = new TextField(40);
		login.addKeyListener(this);
		password.addKeyListener(this);
		et.setText("Login frame");
		setTitle("Login on uplink clone");
		
		setLayout(new FlowLayout());
		
		getContentPane().add(et,BorderLayout.CENTER);
		getContentPane().add(et, BorderLayout.CENTER);
		getContentPane().add(login,BorderLayout.CENTER);
		getContentPane().add(password,BorderLayout.CENTER);
		getContentPane().add(doLogin,BorderLayout.SOUTH);
		// create the status bar panel and shove it down the bottom of the frame
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(getWidth(), 16));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		JLabel statusLabel = new JLabel("status");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
		pack();
		setVisible(true);
	}
	
	public LoginFrame()
	{
		init();
	}
	
	public static void main(String[] args){
		new LoginFrame();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println(arg0.paramString());
		if((arg0.getSource() == "login" || arg0.getSource() == "password") );//&& arg0.getActionCommand()
		 if (arg0.getSource() == doLogin ){
			 System.out.println("Executing login");
			 if(proxy.login(login.getText(), password.getText()))
			 {
				 setVisible(false);
				 DisplayExample l = new DisplayExample();
				 l.start();
				 dispose();
				 dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			 } else {
				 
			 }
		 }		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyChar()=='\n' && (e.getSource()==login || e.getSource()==password)){
			System.out.println("Executing login");
			 if(proxy.login(login.getText(), password.getText()))
			 {
				 setVisible(false);
				 DisplayExample l = new DisplayExample();
				 l.start();
				 dispose();
				 dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			 } else {
				 
			 }
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
