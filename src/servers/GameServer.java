package servers;

import game.Internet;
import game.NodeData;
import interfaces.IGame;
import interfaces.IGame2;
import interfaces.IRemoteListener;

import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationID;
import java.rmi.server.Unreferenced;
import java.util.Date;
import java.util.Vector;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

public class GameServer extends Activatable implements IGame, IGame2, Unreferenced{
	private static final long serialVersionUID = 1L;
	private int currentActiveNodes = 0;
	private Vector<String> nodes;
	private Vector<IRemoteListener> listeners;
	private Internet internet;
	
	protected GameServer(ActivationID id, MarshalledObject obj) throws ActivationException, RemoteException {
		super(id, 3788, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory(null, null, true));
		//super(id, 3788);
		nodes = new Vector<String>();
		listeners = new Vector<IRemoteListener>();
	}

	public boolean updateMap(Vector<String> clients) throws RemoteException {	
		nodes.addAll(clients);
		notifyListeners();
		return true;
	}

	private void notifyListeners() {
		try {
			for(IRemoteListener l : listeners){
				l.remoteEvent(nodes);
			}
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	
	public void addActiveNode() throws RemoteException {
		System.out.println("Adding active node");
		//listeners.add(l);
		currentActiveNodes++;
	}

	@Override
	public int getActiveNodes() throws RemoteException {
		return currentActiveNodes;
	}

	
	public void removeActiveNode() throws RemoteException {
		System.out.println("Removing active node");
		//listeners.remove(l);
		currentActiveNodes--;
	}
	
	@Override
	public void unreferenced() {
		try {
			//Naming.unbind("//:5/LoginServer");
			inactive(getID());
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean infect(String nodeIp, String playerIp) throws RemoteException{
		NodeData nd = internet.getNode(nodeIp);
		if(!nd.active){
			if(!nd.infected){
				nd.InfData.Infector =  playerIp;
				nd.InfData.date = new Date();
				nd.infected = true;
				return true;
			}
			return false;
		}
		//TODO:battle
		return false;
	}
	
	/*public MobileServer sendServer(String ip) throws RemoteException{
		MobileServer ms = new MobileServer();
		return ms;
	}*/

	@Override
	public void addActiveNode(IRemoteListener l) throws RemoteException {
		System.out.println("Adding active node");
		listeners.add(l);
		currentActiveNodes++;		
	}

	@Override
	public void removeActiveNode(IRemoteListener l) throws RemoteException {
		System.out.println("Removing active node");
		listeners.remove(l);
		currentActiveNodes--;	
	}
}
