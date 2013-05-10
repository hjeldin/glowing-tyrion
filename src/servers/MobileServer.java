package servers;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import interfaces.IMobileServer;

public class MobileServer extends UnicastRemoteObject implements IMobileServer, Serializable{

	public  MobileServer() throws RemoteException {
		super();
	}

	@Override
	public void battle(String ip) throws Exception {
		InetAddress myIp = InetAddress.getLocalHost();
		String myIpp = myIp.getHostAddress().toString();
		System.out.println("Chiamato il metodo bbattle di "+myIpp+" da "+ip);
	}
	
}
