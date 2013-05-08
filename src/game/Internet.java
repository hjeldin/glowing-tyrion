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
}