package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteListener extends Remote {
	public void remoteEvent(Object obj) throws RemoteException;

	public void addActiveNode() throws RemoteException;

	public void removeActiveNode() throws RemoteException;
}
