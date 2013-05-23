package servers;

import game.Internet;
import game.NodeData;
import interfaces.IGame;
import interfaces.IProxy;
import interfaces.IRemoteListener;

import java.net.InetAddress;
import java.rmi.MarshalledObject;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationID;
import java.rmi.server.Unreferenced;
import java.util.Date;
import java.util.Vector;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class GameServer extends Activatable implements IGame, Unreferenced{
	private static final long serialVersionUID = 1L;
	private int currentActiveNodes = 0;
	private Vector<String> nodes;
	private Vector<IRemoteListener> listeners;
	private Internet internet;
	private IProxy proxyStub;
	
	protected GameServer(ActivationID id, MarshalledObject obj) throws ActivationException, RemoteException {
		super(id, 3788, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory(null, null, true));
		//super(id, 3788);
		nodes = new Vector<String>();
		listeners = new Vector<IRemoteListener>();
		try{
			InetAddress ip = InetAddress.getLocalHost();
			String ipp = ip.getHostAddress().toString();
			proxyStub = (IProxy)Naming.lookup("//"+ipp+":2222/ProxyServer");
		}catch(Exception e){
			e.printStackTrace();
		}

		Gson jsonSerializer;
		jsonSerializer = new Gson();
		try{
			FileReader fread = new FileReader("map.dat");
			BufferedReader in = new BufferedReader(fread);
			String jsonFile = "";
			String tmpStr = "";
			while((tmpStr = in.readLine()) != null){
				jsonFile += tmpStr;
			}
			internet = new Internet();
			internet = jsonSerializer.fromJson(jsonFile,Internet.class);
			internet.GenerateISP();
		} catch(Exception e){
			System.out.println("errore"+e.toString());
		}
	}

	public boolean updateMap(Vector<String> clients) throws Exception {	
		nodes.addAll(clients);
		proxyStub.notifyListeners(listeners, nodes);
		return true;
	}

	/*private void notifyListeners() {
		try {
			for(IRemoteListener l : listeners){
				l.remoteEvent(nodes);
			}
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}*/

	
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
		System.out.println(nodeIp+" è un nodo attivo");
		//TODO:Send server to playerIP
		sendServer(playerIp);
		//TODO:communicate with nodeIP
		return false;
	}
	
	private void sendServer(String ip) throws RemoteException{
		MobileServer ms = new MobileServer();
		IRemoteListener l = findListener(ip);
		if(l!=null)
			proxyStub.sendServer(ms, l);
		else
			System.out.println("Listener con ip "+ip+" non trovato");
	}
	
	private IRemoteListener findListener(String ip) throws RemoteException{
		for(IRemoteListener l : listeners){
			if(l.getIp().equals(ip))
				return l;
		}
		return null;
	}
	
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
