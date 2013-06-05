package interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;
public interface IMobile extends Remote{
	
	public void battle(String ip) throws RemoteException;

}
