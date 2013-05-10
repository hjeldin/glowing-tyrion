package servers;

import interfaces.IMobile;

import java.io.Serializable;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MobileServer extends UnicastRemoteObject implements IMobile, Serializable{

	private static final long serialVersionUID = 1L;
	
	public  MobileServer() throws RemoteException {
		super();
	}

	@Override
	public void battle(String ip) throws Exception {
		InetAddress myIp = InetAddress.getLocalHost();
		String myIpp = myIp.getHostAddress().toString();
		System.out.println("Chiamato il metodo battle di "+myIpp+" da "+ip);
	}
	
}
