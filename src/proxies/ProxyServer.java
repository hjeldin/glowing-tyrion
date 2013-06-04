package proxies;

import interfaces.IGame;
import interfaces.IGameAdmin;
import interfaces.ILogin;
import interfaces.IProxy;
import interfaces.IRemoteListener;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Vector;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.rmi.ssl.SslRMIClientSocketFactory;

import servers.MobileServer;

public class ProxyServer extends PortableRemoteObject implements ILogin, IGame, IGameAdmin, IProxy, Serializable{

	private static final long serialVersionUID = 1L;
	ILogin LoginServerStub;
	IGame GameServerStub;
	IGameAdmin GameServerAdminStub;
	
	public ProxyServer() throws RemoteException{
		System.out.println("Created server proxy");
		try {
			UnicastRemoteObject.exportObject(this, 3333); // esporta su JRMP
			//PortableRemoteObject.exportObject(this); // esporta su IIOP
			//SslRMIClientSocketFactory csf = new SslRMIClientSocketFactory();
			Registry sslRegistry = LocateRegistry.getRegistry("localhost", 5552, new SslRMIClientSocketFactory());
			Registry registry = LocateRegistry.getRegistry("localhost", 5551);
			InetAddress ip = InetAddress.getLocalHost();
			String ipp = ip.getHostAddress().toString();
			//String ipp = "157.27.184.217";
			LoginServerStub = (ILogin)registry.lookup("//"+ipp+":5551/LoginServer");
			GameServerStub = (IGame)sslRegistry.lookup("//"+ipp+":5552/GameServer");
			GameServerAdminStub = (IGameAdmin)sslRegistry.lookup("//"+ipp+":5552/GameServer");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private static String convToHex(byte[] data) {
         StringBuilder buf = new StringBuilder();
         for (int i = 0; i < data.length; i++) {
             int halfbyte = (data[i] >>> 4) & 0x0F;
             int two_halfs = 0;
             do {
                 if ((0 <= halfbyte) && (halfbyte <= 9))
                     buf.append((char) ('0' + halfbyte));
                 else
                 	buf.append((char) ('a' + (halfbyte - 10)));
                 halfbyte = data[i] & 0x0F;
             } while(two_halfs++ < 1);
         }
         return buf.toString();
     }
	
     public static String SHA1(String text) throws NoSuchAlgorithmException,UnsupportedEncodingException  {
	     MessageDigest md = MessageDigest.getInstance("SHA-1");
	     byte[] sha1hash = new byte[40];
	     md.update(text.getBytes("iso-8859-1"), 0, text.length());
	     sha1hash = md.digest();
	     return convToHex(sha1hash);
     }
	@Override
	public boolean login(String username, String password) {
		if(LoginServerStub == null)
			return false;
		try {
			try {
				System.out.println("sending " + username + " " + SHA1(password));
				System.out.println(LoginServerStub.toString());
				return LoginServerStub.login(username, SHA1(password));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public void addActiveNode(IRemoteListener l) throws RemoteException {
		GameServerStub.addActiveNode(l);
	}
	
	@Override
	public void addActiveNode() throws RemoteException {
		GameServerStub.addActiveNode();
	}

	@Override
	public int getActiveNodes() throws RemoteException {
		return GameServerStub.getActiveNodes();
	}

	@Override
	public boolean updateMap(Vector<String> clients) throws Exception {
		return GameServerStub.updateMap(clients);
	}

	@Override
	public void removeActiveNode(IRemoteListener l) throws RemoteException {
		GameServerStub.removeActiveNode(l);		
	}
	
	@Override
	public void removeActiveNode() throws RemoteException {
		GameServerStub.removeActiveNode();		
	}

	@Override
	public String register(String username, String password, String publickey) throws RemoteException {
		return LoginServerStub.register(username, password, publickey);
	}
	
	public void notifyListeners(Vector<IRemoteListener> listeners, Object nodes) throws RemoteException{
		for(IRemoteListener l : listeners){
			try{
				System.out.println(l.toString());
				l.remoteEvent(nodes);
			}catch(java.rmi.NoSuchObjectException e){
			}
		}
	}
	
	public static void main(String args[]) throws Exception {
		System.setSecurityManager(new RMISecurityManager());
		ProxyServer sp = new ProxyServer();
		InetAddress ip = InetAddress.getLocalHost();
		String ipp = ip.getHostAddress().toString();
		//String ipp = "157.27.184.217";
		//RMI Binding
		Properties p_jrmp = new Properties();
		p_jrmp.put("java.naming.factory.initial", "com.sun.jndi.rmi.registry.RegistryContextFactory");
		p_jrmp.put("java.naming.provider.url", "rmi://"+ipp+":2222");
		InitialContext c_jrmp = new InitialContext(p_jrmp);
		c_jrmp.rebind("ProxyServer", sp);
		System.out.println("FUCKING EXPORTING ON " + "rmi://"+ipp+":2222");
		//COSNaming Binding
		Properties p_iiop = new Properties();
		p_iiop.put("java.naming.factory.initial", "com.sun.jndi.cosnaming.CNCtxFactory");
		p_iiop.put("java.naming.provider.url", "iiop://"+ipp+":5555");
		InitialContext c_iiop = new InitialContext(p_iiop);
		c_iiop.rebind("ServerProxy", sp);
	}

	@Override
	public boolean infect(String nodeIp, String playerIp) throws RemoteException {
		return GameServerStub.infect(nodeIp, playerIp);
	}

	@Override
	public void resetMap() throws RemoteException {
		GameServerAdminStub.resetMap();
		try {
			GameServerAdminStub.updateMap(new Vector<String>());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*@Override
	public void sendServer(MobileServer ms, IRemoteListener l) throws RemoteException {
		l.recieveServer(ms);
	}

	/*@Override
	public MobileServer sendServer(String ip) throws RemoteException {
		return GameServerStub.sendServer(ip);
	}*/
}
