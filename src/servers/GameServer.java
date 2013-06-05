package servers;

import game.InfectionData;
import game.Internet;
import game.NodeData;
import interfaces.IGame;
import interfaces.IGameAdmin;
import interfaces.IProxy;
import interfaces.IRemoteListener;

import java.io.BufferedReader;
import java.io.FileReader;
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

public class GameServer extends Activatable implements IGame, Unreferenced, IGameAdmin{
	private static final long serialVersionUID = 1L;
	private int currentActiveNodes = 0;
	private Vector<String> nodes;
	private Vector<IRemoteListener> listeners;
	private int port = 6000;
	private Internet internet;
	private IProxy proxyStub;
	private String jsonFile = "";
	private Vector<float[]> availableColors = new Vector<float[]>();

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
		float[] player1RGB = new float[3];
		player1RGB[0] = 0.5f;
		player1RGB[1] = 0.5f;
		player1RGB[2] = 0.0f;
		availableColors.add(player1RGB);

		float[] player2RGB = new float[3];
		player2RGB[0] = 0.0f;
		player2RGB[1] = 0.5f;
		player2RGB[2] = 0.5f;
		availableColors.add(player2RGB);

		float[] player3RGB = new float[3];
		player3RGB[0] = 0.5f;
		player3RGB[1] = 0.0f;
		player3RGB[2] = 0.5f;
		availableColors.add(player3RGB);

		float[] player4RGB = new float[3];
		player4RGB[0] = 0.9f;
		player4RGB[1] = 0.1f;
		player4RGB[2] = 0.4f;
		availableColors.add(player4RGB);
		resetMap();
		//disp.lulz = alice;jsonSerializer.fromJson(jsonFile,Internet.class);
	}

	public boolean updateMap(Vector<String> clients) throws Exception {	
		proxyStub.notifyListeners(listeners, jsonFile);
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

	@Override
	public int getActiveNodes() throws RemoteException {
		return currentActiveNodes;
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
	
	public String infect(String nodeIp, String playerIp) throws RemoteException{
		String toRet = "";
		NodeData nd = internet.getNode(nodeIp);
		NodeData myPlayer = internet.getNode(playerIp);
		//System.out.println(playerIp + " asd " + myPlayer);
		if(nd != null){
			if(!nd.active){
				if(!nd.infected){
					nd.InfData = new InfectionData();
					nd.InfData.Infector =  playerIp;
					nd.InfData.date = new Date();
					nd.nodeColor = myPlayer.nodeColor;
					nd.infected = true;
					Gson jsonSerializer = new Gson();
					jsonFile = jsonSerializer.toJson(internet); 
					try {
						updateMap(new Vector<String>());
					} catch (Exception e) {
						e.printStackTrace();
					}
					toRet = "Node "+ nodeIp + " infected!";
					return toRet;
				}
				toRet = "Node "+nodeIp+" is already infected by " + nd.InfData.Infector + " on " + nd.InfData.date.toString();
				return toRet;
			}
			toRet = nodeIp+" è un nodo attivo";
			sendServer(playerIp);
			sendServerIP(nodeIp, playerIp);
		}
		else
			toRet = nodeIp+" non è infettabile";
		return toRet;
	}
	
	private void sendServer(String ip) throws RemoteException{
		MobileServer ms = new MobileServer();
		unexportObject(ms, true);
		IRemoteListener l = findListener(ip);
		if(l!=null)
			proxyStub.sendServer(ms, l, port);
		else
			System.out.println("Listener con ip "+ip+" non trovato");
	}
	
	private void sendServerIP(String ip, String serverIp) throws RemoteException{
		IRemoteListener l = findListener(ip);
		if(l!=null)
			proxyStub.sendServerIP(l, serverIp, port);
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
		float[] asd = getColor();
		NodeData nd = new NodeData();
		nd.nodeColor = asd;
		nd.ip = l.getIp();
		nd.active = true;
		nd.infected = false;
		nd.InfData = null;
		int isp = (int)(Math.random() * internet.isps.size());
		int network = (int)(Math.random() * internet.isps.get(isp).networks.size());
		internet.isps.get(isp).networks.get(network).nodes.add(nd);

		Gson jsonSerializer = new Gson();
		jsonFile = jsonSerializer.toJson(internet); 
		try {
			updateMap(new Vector<String>());
		} catch (Exception e) {
			e.printStackTrace();
		}

		currentActiveNodes++;		
	}

	@Override
	public void removeActiveNode(IRemoteListener l) throws RemoteException {
		System.out.println("Removing active node");
		availableColors.add(internet.getNode(l.getIp()).nodeColor);
		internet.removeNode(l.getIp());
		listeners.remove(l);
		currentActiveNodes--;	
	}

	@Override
	public void resetMap() throws RemoteException {
		jsonFile = "";
		try{
			FileReader fread = new FileReader("map.dat");
			BufferedReader in = new BufferedReader(fread);
			String tmpStr = "";
			while((tmpStr = in.readLine()) != null){
				jsonFile += tmpStr;
			}
		} catch(Exception e){
			System.out.println("Error while loading "+e.toString());
		}

		Gson jsonSerializer;
		jsonSerializer = new Gson();
		Internet alice;
		alice = new Internet();
		alice = jsonSerializer.fromJson(jsonFile,Internet.class);
		alice.GenerateISP();
		internet = alice;
	}

	@Override
	public float[] getColor() throws RemoteException{
		System.out.println(availableColors.size());
		if(availableColors.size() > 0)
			return availableColors.remove(availableColors.size()-1);
		else {
			float[] asd = new float[3];
			asd[0] = 1.0f;
			asd[1] = 1.0f;
			asd[2] = 1.0f;
			return asd;
		}
	}
}
