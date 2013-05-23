package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ILogin extends Remote {
	public boolean login(String username, String password) throws RemoteException;

	public String register(String username, String password, String publickey) throws RemoteException;
}
