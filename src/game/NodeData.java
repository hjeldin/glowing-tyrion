package game;

import java.io.Serializable;

public class NodeData implements Serializable{
	public String ip="";
	public boolean active;
	public boolean infected;
	public InfectionData InfData;
	public int centerX, centerY;
	public int x=0,y=0;
}