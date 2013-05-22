package game;
import java.io.Serializable;
import java.util.Vector;
public class Network implements Serializable
{
	public NodeData gateway;
	public Vector<NodeData> nodes;
	private Vector<Node> vis_node;
	
	public Network(){
		nodes = new Vector<NodeData>();
		vis_node = new Vector<Node>();
	}
	
	public void Draw(){
		for(Node n : vis_node){
			n.Draw();
		}
	}

	public void GenerateNodes(int centerX, int centerY){
		double theta = (2*Math.PI)/nodes.size();
		float radius = 200;
		int k =0;
		for(NodeData n : nodes){
			Node tmp = new Node();
			tmp.nd = n;
			if(k%2==0){
				radius = 200;
			} else {
				radius = 500;
			}
			tmp.setX(centerX+(int)(Math.cos(theta*k)*radius));
			tmp.setY(centerY+(int)(Math.sin(theta*k)*radius));
			vis_node.add(tmp);
			k++;
		}
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