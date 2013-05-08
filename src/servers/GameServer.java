package servers;

import interfaces.IGame;
import interfaces.IRemoteListener;

import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationID;
import java.rmi.server.Unreferenced;
import java.util.Vector;

public class GameServer extends Activatable implements IGame, Unreferenced{
	private static final long serialVersionUID = 1L;
	private int currentActiveNodes = 0;
	private Vector<String> nodes;
	private Vector<IRemoteListener> listeners;
	
	protected GameServer(ActivationID id, MarshalledObject obj) throws ActivationException, RemoteException {
		super(id, 3788);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void addActiveNode(IRemoteListener l) throws RemoteException {
		System.out.println("Adding active node");
		listeners.add(l);
		currentActiveNodes++;
	}

	@Override
	public int getActiveNodes() throws RemoteException {
		return currentActiveNodes;
	}

	@Override
	public void removeActiveNode(IRemoteListener l) throws RemoteException {
		System.out.println("Removing active node");
		listeners.remove(l);
		currentActiveNodes--;
	}
	
	@Override
	public void unreferenced() {
		// TODO Auto-generated method stub
		try {
			//Naming.unbind("//:5/LoginServer");
			inactive(getID());
			System.gc();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
