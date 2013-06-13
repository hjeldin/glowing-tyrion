package servers;

import interfaces.IMobile;

import java.io.Serializable;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MobileServer implements IMobile, Serializable{
	
	public  MobileServer() throws RemoteException {
		super();
	}

	@Override
	public void battle(String ip) throws RemoteException {
		//InetAddress myIp = InetAddress.getLocalHost();
		//String myIpp = myIp.getHostAddress().toString();
		System.out.println("Chiamato il metodo battle da " +ip);
	}
	
}
