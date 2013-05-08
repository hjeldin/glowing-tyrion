package ssl;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ITest extends Remote {
   String sayHello() throws RemoteException;
   String givemeFive() throws RemoteException;
}