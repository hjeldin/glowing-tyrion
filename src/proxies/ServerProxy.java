package proxies;

import interfaces.IGame;
import interfaces.ILogin;
import interfaces.IRemoteListener;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;
public class ServerProxy implements Remote, ILogin, IGame, IRemoteListener, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ILogin LoginServerStub;
	IGame GameServerStub;
	
	public ServerProxy(){
		System.setSecurityManager(new RMISecurityManager());
		System.out.println("Created server proxy");
		try {
			//SslRMIClientSocketFactory csf = new SslRMIClientSocketFactory();
			Registry registry = LocateRegistry.getRegistry("localhost", 5551);
			InetAddress ip = InetAddress.getLocalHost();
			String ipp = ip.getHostAddress().toString();
			LoginServerStub = (ILogin)registry.lookup("//"+ipp+":5551/LoginServer");
			GameServerStub = (IGame)registry.lookup("//"+ipp+":5551/GameServer");
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
	public void addActiveNode() throws RemoteException {
		GameServerStub.addActiveNode((IRemoteListener)this);
	}

	@Override
	public int getActiveNodes() throws RemoteException {
		return GameServerStub.getActiveNodes();
	}

	@Override
	public boolean updateMap(Vector<String> clients) throws RemoteException {
		return GameServerStub.updateMap(clients);
	}

	@Override
	public void removeActiveNode() throws RemoteException {
		GameServerStub.removeActiveNode(this);
		
	}

	@Override
	public void remoteEvent(Object obj) throws RemoteException {
		System.out.println("HELLOOOO!");
	}

	@Override
	public void addActiveNode(IRemoteListener l) throws RemoteException {
		
	}

	@Override
	public void removeActiveNode(IRemoteListener l) throws RemoteException {
		
	}

	public String register(String username, String password, String publickey) throws RemoteException {
		return LoginServerStub.register(username, password, publickey);
		//return "";
	}

}
