package game;
import interfaces.IRemoteListener;
import java.io.Serializable;
import java.rmi.RemoteException;

import servers.MobileServer;

public class ClientRemoteListener implements Serializable, IRemoteListener{
	private String ip;
	
	public ClientRemoteListener(String ip){
		this.ip = ip;
	}
	
	public void remoteEvent(Object obj){
		System.out.println("RemoteEvent to "+this.toString()+", " + obj.toString());
	}

	public void update(){
		//System.out.println("called update");
	}
	
	public String getIp() throws RemoteException{
		return ip;
	}

	public void recieveServer(MobileServer ms) throws RemoteException {
		
	}
}