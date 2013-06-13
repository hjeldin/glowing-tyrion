package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

import servers.MobileServer;

public interface IProxy extends Remote{
	public void notifyListeners(Vector<IRemoteListener> listeners, Object nodes) throws Exception;
	public void sendServer(IMobile ms, IRemoteListener ip, int port) throws RemoteException;
	public void sendServerIP(IRemoteListener l, IMobile stub) throws RemoteException;

}