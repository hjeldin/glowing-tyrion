package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

import servers.MobileServer;

public interface IProxy extends Remote{
	public void notifyListeners(Vector<IRemoteListener> listeners, Vector<String> nodes) throws Exception;
	public void sendServer(MobileServer ms, IRemoteListener ip) throws RemoteException;
}
