package game;
import interfaces.IRemoteListener;
import java.io.Serializable;
import java.rmi.RemoteException;

import servers.MobileServer;

public class ClientRemoteListener implements Serializable, IRemoteListener{
	private String ip;
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
	
	public String getIp() throws RemoteException{
		return ip;
	}

	public void recieveServer(MobileServer ms) throws RemoteException {
		
	}
}