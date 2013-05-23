package game;
import java.io.Serializable;
import java.util.Vector;
public class ISP implements Serializable
{
	public String name="";
	public Vector<Network> networks;
	public Node me;
	public ISP(){
		networks = new Vector<Network>();
	}

	public void GenerateNetwork(int centerX, int centerY){
		double theta = (2*Math.PI)/networks.size();
		float radius = 200;
		int k =0;
		for(Network n : networks){
			int cx,cy;
			cx = centerX+(int)(Math.cos(theta*k)*radius);
			cy = centerY+(int)(Math.sin(theta*k)*radius);
			n.GenerateNodes(cx,cy);	
			k++;
		}
		me = new Node();
		me.nd = new NodeData();
		me.nd.active = true;
		me.nd.ip = name;
		me.setX(centerX);
		me.setY(centerY-10);
		me.width = 10;
		me.height = 20;
		me.r = 0.0f;
		me.g = 1.0f;
		me.b = 0.0f;
		me.a = 1.0f;
	}

	public void Draw(){
		for(Network n : networks) {
			n.Draw();
			me.DrawToCenter(n.centerX,n.centerY);
		}
		me.Draw();
	}

	public String toString(){
		String tmpStr = "";
		for(Network n : networks) tmpStr+= "\n"+n.toString();
		return tmpStr;
	}
}