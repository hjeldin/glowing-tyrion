package game;
import interfaces.IMobile;
import interfaces.IRemoteListener;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import servers.MobileServer;

public class ClientRemoteListener implements Serializable, IRemoteListener{
	private String ip;
	public float[] color = new float[3];
	public String gameMap;
	public boolean update;
	
	public ClientRemoteListener(String ip){
		this.ip = ip;
		this.update = false;
	}
	
	public void remoteEvent(Object obj){
		gameMap = (String)obj;
		System.out.println("RemoteEvent to "+this.toString());
		update = true;
	}

	public boolean update(){
		if(update){
			update = false;
			return true;
		}else
			return false;
	}

	public void setColor(float[] c){
		this.color = c;
	}
	
	public String getIp() throws RemoteException{
		return ip;
	}

	public void recieveServer(IMobile ms, int port) throws RemoteException {
		System.out.println("Receved Mobile Server "+ms.toString());
		Registry registry = LocateRegistry.createRegistry(port);
		registry.rebind("//localhost:"+port+"/MobileServer", ms);
		//ms.battle(ip);
	}

	@Override
	public void recieveServerIP(String serverIp, int port) throws RemoteException {
		//Registry registry = LocateRegistry.getRegistry(serverIp, port);
		Registry registry = LocateRegistry.getRegistry("localhost", port);
		try {
			IMobile ms = (IMobile)registry.lookup("/MobileServer");
			ms.battle(ip);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}