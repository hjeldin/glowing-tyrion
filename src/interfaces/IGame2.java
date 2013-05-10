package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGame2 extends Remote{
	public void addActiveNode(IRemoteListener l) throws RemoteException;
	public void removeActiveNode(IRemoteListener l) throws RemoteException;
}
