package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

import servers.MobileServer;

public interface IGame extends Remote{
	public boolean updateMap(Vector<String> clients) throws RemoteException;
	public int getActiveNodes() throws RemoteException;
	//public void addActiveNode() throws RemoteException;
	//public void removeActiveNode() throws RemoteException;
	public boolean infect(String nodeIp, String playerIp) throws RemoteException;
	public MobileServer sendServer(String ip) throws RemoteException;
}
