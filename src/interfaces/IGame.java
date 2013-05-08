package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface IGame extends Remote{
	public boolean updateMap(Vector<String> clients) throws RemoteException;
	public int getActiveNodes() throws RemoteException;
	
	public void addActiveNode(IRemoteListener l) throws RemoteException;
	public void removeActiveNode(IRemoteListener l) throws RemoteException;
}
