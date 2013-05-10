package interfaces;
import java.rmi.Remote;
public interface IMobile extends Remote{
	
	public void battle(String ip) throws Exception;

}
