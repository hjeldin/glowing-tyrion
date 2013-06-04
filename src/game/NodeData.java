package game;

import java.io.Serializable;

public class NodeData implements Serializable{
	public NodeData(){
		nodeColor[0] = 1.0f;
		nodeColor[1] = 1.0f;
		nodeColor[2] = 1.0f;
	}
	public String ip="";
	public boolean active;
	public boolean infected;
	public InfectionData InfData;
	public int centerX, centerY;
	public int x=0,y=0;
	public float[] nodeColor = new float[3];
}