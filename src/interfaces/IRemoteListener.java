package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import servers.MobileServer;

public interface IRemoteListener extends Remote {
	public void remoteEvent(Object obj) throws RemoteException;
	public String getIp() throws RemoteException;
	//public void recieveServer(MobileServer ms) throws RemoteException;
}
