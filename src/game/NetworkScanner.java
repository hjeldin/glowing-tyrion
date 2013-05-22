package game;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Vector;

public class NetworkScanner implements Serializable{
	public static void write(Vector<String> data) throws IOException{
		FileWriter fstream = new FileWriter("network.dat");
		BufferedWriter out = new BufferedWriter(fstream);
		for(String n :data)
			out.write(n+"\n");
		out.close();
		fstream.close();
	}
	
	public static Vector<String> load() throws IOException{
		FileReader fstream = new FileReader("network.dat");
		BufferedReader in = new BufferedReader(fstream);
		String tmpStr = "";
		Vector<String> toRet = new Vector<String>();
		while((tmpStr = in.readLine()) != null){
			toRet.add(tmpStr);
		}
		in.close();
		fstream.close();
		return toRet;
	}
	
	
	public static Vector<String> scan() throws UnknownHostException, IOException {
		Vector<String> toReturn = new Vector<String>();
		List<InterfaceAddress> lol;
		InterfaceAddress ipv4Interface = null;
		InetAddress localHost = Inet4Address.getLocalHost();
		NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);

		lol = networkInterface.getInterfaceAddresses();
		String ipv4Pattern = "[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3}";
		for(InterfaceAddress ab:lol){
			InetAddress ip = ab.getAddress();
			if(ip.getHostAddress().matches(ipv4Pattern)){
				ipv4Interface = ab;
			}
		}
	
		String addr = ipv4Interface.getAddress().getHostAddress()+"/"+ipv4Interface.getNetworkPrefixLength();
	
		String[] parts = addr.split("/");
		String ip = parts[0];
		int prefix;
		if (parts.length < 2) {
		    prefix = 0;
		} else {
		    prefix = Integer.parseInt(parts[1]);
		}
		int mask = 0xffffffff << (32 - prefix);
		int value = mask;
		byte[] bytes = new byte[]{ 
		        (byte)(value >>> 24), (byte)(value >> 16 & 0xff), (byte)(value >> 8 & 0xff), (byte)(value & 0xff) };

		InetAddress netAddr = InetAddress.getByAddress(bytes);
		
		
		String tmpMask = netAddr.getHostAddress();
		String[] subMask = tmpMask.split("\\.");
		String myIp = ipv4Interface.getAddress().getHostAddress();
		String[] spIp = myIp.split("\\.");
	
		int limitA = (255 - Integer.parseInt(subMask[0]) == 0)?Integer.parseInt(spIp[0]):255 - Integer.parseInt(subMask[0]);
		int limitB = (255 - Integer.parseInt(subMask[1]) == 0)?Integer.parseInt(spIp[1]):255 - Integer.parseInt(subMask[1]);
		int limitC = (255 - Integer.parseInt(subMask[2]) == 0)?Integer.parseInt(spIp[2]):255 - Integer.parseInt(subMask[2]);
		int limitD = (255 - Integer.parseInt(subMask[3]) == 0)?Integer.parseInt(spIp[3]):255 - Integer.parseInt(subMask[3]);
	
		int startA = (limitA == Integer.parseInt(spIp[0]))?Integer.parseInt(spIp[0]):0;
		int startB = (limitB == Integer.parseInt(spIp[1]))?Integer.parseInt(spIp[1]):0;
		int startC = (limitC == Integer.parseInt(spIp[2]))?Integer.parseInt(spIp[2]):0;
		int startD = (limitD == Integer.parseInt(spIp[3]))?Integer.parseInt(spIp[3]):0;
	
		for(int a=startA;a<=limitA;a++){
			for(int b=startB;b<=limitB;b++){
				for(int c=startC;c<=limitC;c++){
					for(int d=startD;d<=limitD;d++){
						InetAddress inet = InetAddress.getByName(a+ "." + b + "." + c + "." + d);
						System.out.println("Check if "+a+ "." + b + "." + c + "." + d+" is reachable");
						if(inet.isReachable(100)){
							toReturn.add(a+ "." + b + "." + c + "." + d);
							System.out.println("true!");
						}
						
					}
				}
			}
		}
		return toReturn;
	}
	
	public static String getIp() throws Exception {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}