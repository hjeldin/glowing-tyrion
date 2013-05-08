package servers;

import interfaces.ILogin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationID;
import java.rmi.server.Unreferenced;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class LoginServer extends Activatable implements ILogin, Unreferenced{
	private static final long serialVersionUID = 1L;
	Map<String,String> db = new HashMap<String,String>();
	String serverPublicKey = "";
	//DataManager dm;
	
	@SuppressWarnings("unchecked")
	protected LoginServer(ActivationID id, MarshalledObject obj) throws ActivationException, IOException {
		//super(id, 5798, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory(null, null, true));
		super(id, 5798);
		loadDB();
		loadPublicKey();
	}
	
	private void loadDB() throws IOException{
		FileReader fstream = new FileReader(System.getProperty("user.dir")+"/projects/glowing-tyrion/db.dat");
		BufferedReader in = new BufferedReader(fstream);
		String tmpStr = "";
		while((tmpStr = in.readLine()) != null){
			String[] userPass = tmpStr.split("\\|:\\|");
			if(userPass.length != 1){
				String username = userPass[0];
				String password = userPass[1];
				db.put(username, password);
			}
		}
		in.close();
		fstream.close();
		
	}

	@Override
	public boolean login(String username, String password) {
		System.out.println(new java.util.Date() + " invoked login by " +
  		       /*SecureRMISocketFactory.getLocalThreadLastReadPrincipal()+ */" with username: " + username + " and prehashed pass");
		
		try {
			//return dm.checkUser(username,password);
			return db.get(username).equals(password);
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
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

	public String register(String username, String password, String publickey) throws RemoteException {
		try {
			addToDB(username, password);
			//TODO: writeClientPublicKey(publickey, username);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serverPublicKey;
	}
	
	private void loadPublicKey() throws IOException{
		//String serverpublickey = System.getProperty("ssl.publicKey");
		String serverpublickey = "server.public-key";
		FileReader fstream = new FileReader(System.getProperty("user.dir")+"/projects/glowing-tyrion/"+serverpublickey);
		BufferedReader in = new BufferedReader(fstream);
		String tmpStr = "";
		//Vector<String> toRet = new Vector<String>();
		while((tmpStr = in.readLine()) != null){
			serverPublicKey += tmpStr;
		}
		in.close();
		fstream.close();
	}
	
	@SuppressWarnings("unused")
	private void writeClientPublicKey(String key, String username) throws Exception
	{
		FileWriter fstream = new FileWriter("temp.dat");
		BufferedWriter out = new BufferedWriter(fstream);
		System.out.println(key);
		out.write(key);
		out.close();
		fstream.close();
		
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(new FileInputStream("server.keystore"), "server".toCharArray());
		//TODO: LOAD FUCKING KEY!
		keyStore.store(new FileOutputStream("server.keystore"), "server".toCharArray());
	}

	private void addToDB(String username, String password) throws Exception{
		FileWriter fstream = new FileWriter(System.getProperty("user.dir")+"/projects/glowing-tyrion/db.dat",true);
		BufferedWriter out = new BufferedWriter(fstream);
		out.write(username+"|:|"+SHA1(password));
		out.close();
		fstream.close();
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

}
