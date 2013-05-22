package bootstrap;

import java.rmi.server.RMIClassLoader;

public class BootstrapClient {
	static String codebase = "";
	static final String clientClass = "game.ClientData";
	
	public static void main(String args []) throws Exception{
		codebase = System.getProperty("bootstrap.codebase");
		System.setSecurityManager(new SecurityManager());
		Class classClient = RMIClassLoader.loadClass(codebase, clientClass);
		Runnable client = (Runnable)classClient.newInstance();
		client.run();
	}
	
}
