package game;
import org.json.JSONObject;
import org.json.JSONArray;
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

public class TestInternet implements Serializable
{

	public static void main(String args[]) 
	{
		Gson lol = new Gson();
		try{
			FileReader fread = new FileReader("map.dat");
			BufferedReader in = new BufferedReader(fread);
			String jsonFile = "";
			String tmpStr = "";
			while((tmpStr = in.readLine()) != null){
				jsonFile += tmpStr;
			}
			System.out.println("lol");
			Internet alice = new Internet();
			alice = lol.fromJson(jsonFile,Internet.class);
			System.out.println(alice.toString());
		} catch(Exception e){
			System.out.println("MEH");
			Internet inter = new Internet();
			ISP i = new ISP();
			i.name="GARR-X";
			Network n = new Network();
			n.gateway = new NodeData();
			n.gateway.ip = "157.27.241.223";
			NodeData n1 = new NodeData();
			n1.ip = "157.27.241.255";
			NodeData n2 = new NodeData();
			n2.ip = "157.27.241.256";
			n.nodes.add(n1);
			n.nodes.add(n2);
			i.networks.add(n);
			inter.isps.add(i);
			System.out.println(lol.toJson(inter));
			try{
				FileWriter fstream = new FileWriter("map.dat");
				BufferedWriter out = new BufferedWriter(fstream);
				out.write(lol.toJson(inter));
				out.close();
				fstream.close();
			}catch(Exception k){
				System.out.println(k);
			}
		}
	}
}