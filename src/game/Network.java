package game;
import java.io.Serializable;
import java.util.Vector;
public class Network implements Serializable
{
	public NodeData gateway;
	public Vector<NodeData> nodes;	
	public int centerX, centerY;
	public Network(){
		nodes = new Vector<NodeData>();
	}
	
	public void Draw(){
		/*for(Node n : vis_node){
			n.Draw();
			n.DrawToCenter((float)centerX,(float)centerY);
		}	*/	
	}

	public void GenerateNodes(int centerX, int centerY){
		this.centerX = centerX;
		this.centerY = centerY;
		double theta = (2*Math.PI)/nodes.size();
		float radius = 100;
		int k =0;
		for(NodeData n : nodes){
			n.centerX = centerX+5;
			n.centerY = centerY;
			n.x = (centerX+(int)(Math.cos(theta*k)*radius));
			n.y = (centerY+(int)(Math.sin(theta*k)*radius));
			k++;
		}
		
		gateway.active = true;
		gateway.x = (centerX);
		gateway.y = (centerY-5);
		//vis_node.add(g);
	}

	public void setCenterX(int centerX) {
		gateway.centerX = centerX;
	}

	public void setCenterY(int centerY) {
		gateway.centerY = centerY;
	}
	

	/*public String toString(){
		String tmpStr = "";
		for(Node n : vis_node){
			tmpStr += "\n"+n.toString();
		}
		tmpStr += gateway.toString();
		return tmpStr;
	}*/

}