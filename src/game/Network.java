package game;
import java.io.Serializable;
import java.util.Vector;
public class Network implements Serializable
{
	public NodeData gateway;
	public Vector<NodeData> nodes;
	private Vector<Node> vis_node;
	public int centerX, centerY;
	public Network(){
		nodes = new Vector<NodeData>();
		vis_node = new Vector<Node>();
	}
	
	public void Draw(){
		for(Node n : vis_node){
			n.Draw();
			n.DrawToCenter((float)centerX,(float)centerY);
		}		
	}

	public void GenerateNodes(int centerX, int centerY){
		this.centerX = centerX;
		this.centerY = centerY;
		double theta = (2*Math.PI)/nodes.size();
		float radius = 100;
		int k =0;
		for(NodeData n : nodes){
			Node tmp = new Node();
			tmp.nd = n;
			tmp.setX(centerX+(int)(Math.cos(theta*k)*radius));
			tmp.setY(centerY+(int)(Math.sin(theta*k)*radius));
			tmp.width = 10;
			tmp.height = 10;
			tmp.r = 1.0f;
			tmp.g = 0.0f;
			tmp.b = 0.0f;
			tmp.a = 1.0f;
			vis_node.add(tmp);
			k++;
		}

		Node g = new Node();
		g.nd = gateway;
		g.nd.active = true;
		g.setX(centerX);
		g.setY(centerY-5);
		g.width = 10;
		g.height = 10;
		g.r = 1.0f;
		g.g = 0.0f;
		g.b = 0.0f;
		g.a = 1.0f;
		vis_node.add(g);
		System.out.println(k);
	}

	public String toString(){
		String tmpStr = "";
		for(Node n : vis_node){
			tmpStr += "\n"+n.toString();
		}
		tmpStr += gateway.toString();
		return tmpStr;
	}
	
	public Vector<Node> getVis_node() {
		return vis_node;
	}

}