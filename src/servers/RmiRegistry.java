package servers;

import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

public class RmiRegistry {
    public static void main(String[] args) throws Exception {
    	System.setSecurityManager(new RMISecurityManager());
        // Start RMI registry on port 3000
        LocateRegistry.createRegistry(5552,
                                      new SslRMIClientSocketFactory(),
                                      new SslRMIServerSocketFactory(null, null, true));
        System.out.println("RMI registry running on port 5552");
        // Sleep forever
        Thread.sleep(Long.MAX_VALUE);
    }
}