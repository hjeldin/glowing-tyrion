package game;
import java.io.Serializable;
import java.util.Vector;
public class ISP implements Serializable
{
	public String name="";
	public Vector<Network> networks;
	public ISP(){
		networks = new Vector<Network>();
	}

	public void GenerateNetwork(int centerX, int centerY){
		for(Network n : networks) n.GenerateNodes(centerX,centerY);
	}

	public void Draw(){
		for(Network n : networks) n.Draw();
	}

	public String toString(){
		String tmpStr = "";
		for(Network n : networks) tmpStr+= "\n"+n.toString();
		return tmpStr;
	}
}