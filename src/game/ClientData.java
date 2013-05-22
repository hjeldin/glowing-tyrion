package game;

import interfaces.IGame;
import interfaces.ILogin;

import java.io.Serializable;
import java.net.InetAddress;
import java.rmi.Naming;

public class ClientData implements Runnable, Serializable{
	
	private static final long serialVersionUID = 1L;
	String ip;
	
	public ClientData() throws Exception{
		InetAddress ip = InetAddress.getLocalHost();
		String ipp = ip.getHostAddress().toString();
		this.ip = ipp;
	}
	
	public static void main(String arg[]) throws Exception{
		System.setSecurityManager(new SecurityManager());
		ClientData cd = new ClientData();
		ILogin LoginStub = (ILogin)Naming.lookup("//"+cd.ip+":2222/ProxyServer");
		LoginFrame lf = new LoginFrame(LoginStub);
		lf.init();
		while( !lf.LoginDone ){System.out.println("nope");};
		System.out.println("Login Done");
		IGame GameStub = (IGame)Naming.lookup("//"+cd.ip+":2222/ProxyServer");
		System.out.println("Lookup Done");
		DisplayExample l = new DisplayExample(GameStub);
		System.out.println("Created DisplayExample");
		l.start();
		System.out.println("Window Closed");
	}

	@Override
	public void run() {
		try{
			System.setSecurityManager(new SecurityManager());
			ClientData cd = new ClientData();
			ILogin LoginStub = (ILogin)Naming.lookup("//"+cd.ip+":2222/ProxyServer");
			LoginFrame lf = new LoginFrame(LoginStub);
			lf.init();
			while( !lf.LoginDone )/*{System.out.println(lf.LoginDone);}*/;
			System.out.println("Login Done");
			IGame GameStub = (IGame)Naming.lookup("//"+cd.ip+":2222/ProxyServer");
			System.out.println("Lookup Done");
			DisplayExample l = new DisplayExample(GameStub);
			System.out.println("Created DisplayExample");
			l.start();
			System.out.println("Window Closed");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
