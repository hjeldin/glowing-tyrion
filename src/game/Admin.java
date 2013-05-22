package game;

import interfaces.IGame;
import interfaces.ILogin;

import java.net.InetAddress;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

public class Admin {
	
	String ip;
	
	public Admin() throws Exception{
		InetAddress ip = InetAddress.getLocalHost();
		String ipp = ip.getHostAddress().toString();
		this.ip = ipp;
	}
	
	public static void main(String arg[]) throws Exception{
		System.setSecurityManager(new SecurityManager());
		Admin a = new Admin();
		Properties p = new Properties();
		p.put("java.naming.factory.initial", "com.sun.jndi.cosnaming.CNCtxFactory");
		p.put("java.naming.provider.url", "iiop://"+a.ip+":5555");
		InitialContext ic = new InitialContext(p);
		Object objRef = ic.lookup("ServerProxy");
		ILogin LoginStub = (ILogin)PortableRemoteObject.narrow(objRef, ILogin.class);
		LoginFrame lf = new LoginFrame(LoginStub);
		lf.init();
		while( !lf.LoginDone );
		System.out.println("Login Done");
		IGame GameStub = (IGame)PortableRemoteObject.narrow(objRef, IGame.class);
		DisplayExample l = new DisplayExample(GameStub);
		System.out.println("Created DisplayExample");
		l.start();
		System.out.println("Window Closed");
	}
}