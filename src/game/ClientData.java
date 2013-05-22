package game;

import interfaces.IGame;
import interfaces.ILogin;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.rmi.Naming;

public class ClientData {
	
	String ip;
	ILogin login_stub;
	public ClientData() throws Exception{
		InetAddress ip = InetAddress.getLocalHost();
		String ipp = ip.getHostAddress().toString();
		this.ip = ipp;
	}
	
	public static void main(String arg[]) throws Exception{
		System.setSecurityManager(new SecurityManager());
		ClientData cd = new ClientData();
		ILogin LoginStub = (ILogin)Naming.lookup("//"+cd.ip+":2222/ProxyServer");
		cd.login_stub = LoginStub;
		LoginFrame lf = new LoginFrame(LoginStub);
		lf.init(cd);
	}

	public void actionPerformed(Boolean logged ){
		System.out.println("Login Done");
		IGame GameStub = (IGame)login_stub;
		DisplayExample l = new DisplayExample(GameStub);
		System.out.println("Created DisplayExample");
		l.start();
		System.out.println("Window Closed");
	}


}
