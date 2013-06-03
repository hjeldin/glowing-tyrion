package game;
import java.io.Serializable;
import java.util.Vector;
public class ISP implements Serializable
{
	public String name="";
	public Vector<Network> networks;
	public NodeData me;
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
			n.setCenterX(centerX);
			n.setCenterY(centerY-5);
			k++;
		}
		me = new NodeData();
		me.active = true;
		me.ip = name;
		me.x = centerX;
		me.y = centerY-10;
	}

	/*public void Draw(){
		for(Network n : networks) {
			n.Draw();
			me.DrawToCenter(n.centerX,n.centerY);
		}
		me.Draw();
	}*/

	public String toString(){
		String tmpStr = "";
		for(Network n : networks) tmpStr+= "\n"+n.toString();
		return tmpStr;
	}
}