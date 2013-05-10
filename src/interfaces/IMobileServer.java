package interfaces;
import java.rmi.Remote;
public interface IMobileServer extends Remote{
	
	public void battle(String ip) throws Exception;

}
