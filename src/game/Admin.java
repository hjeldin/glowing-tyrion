package game;

import interfaces.ILogin;

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
import java.net.InetAddress;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class Admin extends JFrame implements ActionListener, KeyListener{
	
	TextField login,password;
	Button doLogin;
	private static final long serialVersionUID = 1L;
	Label et=new Label();
	ILogin ServerProxyStub;
	public void init () throws Exception {
		System.setSecurityManager(new RMISecurityManager());
		InetAddress ip = InetAddress.getLocalHost();
		String ipp = ip.getHostAddress().toString();
		Properties p = new Properties();
		p.put("java.naming.factory.initial", "com.sun.jndi.cosnaming.CNCtxFactory");
		p.put("java.naming.provider.url", "iiop://"+ipp+":5555");
		InitialContext ic = new InitialContext(p);
		Object objRef = ic.lookup("ServerProxy");
		ServerProxyStub = (ILogin)PortableRemoteObject.narrow(objRef, ILogin.class);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		doLogin = new Button("Login");
		doLogin.addActionListener(this);
		login = new TextField(40);
		password = new TextField(40);
		login.addKeyListener(this);
		password.addKeyListener(this);
		et.setText("Admin Login frame");
		setTitle("Admin Login on uplink clone");
		
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
	
	
	public static void main(String args[]) throws Exception{
		new Admin();
	}
	
	public Admin() throws Exception
	{
		init();
	}
	
	public void actionPerformed(ActionEvent arg0) {
		System.out.println(arg0.paramString() + " " + login.getText() + " " +  password.getText());
		if((arg0.getSource() == "login" || arg0.getSource() == "password") );//&& arg0.getActionCommand()
		 if (arg0.getSource() == doLogin ){
			 System.out.println("Executing login");
			 try {
				if(ServerProxyStub.login(login.getText(), password.getText()))
				 {
					 setVisible(false);
					 DisplayExample l = new DisplayExample();
					 l.start();
					 dispose();
					 dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
				 } else {
					 
				 }
			} catch (RemoteException e) {
				e.printStackTrace();
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
			 try {
				if(ServerProxyStub.login(login.getText(), password.getText()))
				 {
					 setVisible(false);
					 DisplayExample l = new DisplayExample();
					 l.start();
					 dispose();
					 dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
				 } else {
					 
				 }
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
