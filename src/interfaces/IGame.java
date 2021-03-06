package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface IGame extends Remote{
	public boolean updateMap(Vector<String> clients) throws Exception;
	public int getActiveNodes() throws RemoteException;
	//public void addActiveNode() throws RemoteException;
	//public void removeActiveNode() throws RemoteException;
	public String infect(String nodeIp, String playerIp) throws RemoteException;
	public void addActiveNode(IRemoteListener l) throws RemoteException;
	public void removeActiveNode(IRemoteListener l) throws RemoteException;
	public void resetMap() throws RemoteException;
	public float[] getColor() throws RemoteException;
	public void notifyExported(String ipDest, IMobile stub) throws RemoteException;
}