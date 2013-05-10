package game;
import java.util.Vector;
public class Internet{
	public Vector<ISP> isps;
	public Internet(){
		isps = new Vector<ISP>();
	}

	public void GenerateISP(){
		for(ISP i : isps) i.GenerateNetwork(200,200);
	}
	public void draw(){
		for(ISP i : isps) i.Draw();	
	}
	public String toString(){
		String tmpStr ="";
		tmpStr += "Internet:";
		for(ISP i : isps) tmpStr += i.toString();
		return tmpStr;
	}
	public NodeData getNode(String ip){
		
		for(ISP i : isps)
			for(Network net : i.networks)
				for(Node n : net.getVis_node())
					if (n.nd.ip.equals(ip))
						return n.nd;
		
		return null;
	}
}