package game;
import java.io.Serializable;
import java.util.Vector;
public class Internet implements Serializable{
	public Vector<ISP> isps;
	float radius = 500;
	int centerX, centerY;
	int precX, precY;
	public Internet(){
		isps = new Vector<ISP>();
		centerX = 400;
		centerY = 300;
	}

	public void GenerateISP(){
		int k =0;
			double theta = (2*Math.PI)/isps.size();
		for(ISP i : isps) 
		{
			int cx,cy;
			cx = centerX+(int)(Math.cos(1.57f+theta*k)*radius);
			cy = centerY+(int)(Math.sin(1.57f+theta*k)*radius);
			i.GenerateNetwork(cx,cy);
			k++;
		}
	}
	/*public void draw(){
		precX = isps.get(isps.size()-1).me.getX();
		precY = isps.get(isps.size()-1).me.getY();
		for(ISP i : isps) {
			i.me.DrawToCenter(precX,precY);
			precX = i.me.getX();
			precY = i.me.getY();
			i.Draw();
		}
	}*/
	public String toString(){
		String tmpStr ="";
		tmpStr += "Internet:";
		for(ISP i : isps) tmpStr += i.toString();
		return tmpStr;
	}
	public NodeData getNode(String ip){
		
		for(ISP i : isps)
			for(Network net : i.networks)
				for(NodeData n : net.nodes)
					if (n.ip.equals(ip))
						return n;
		
		return null;
	}
}
