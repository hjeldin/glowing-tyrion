package ssl;
    import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

    public class Test extends UnicastRemoteObject implements ITest {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Test() throws RemoteException {
            super();
        }

        public String sayHello() {
        	System.out.println(new java.util.Date() + " invoked by " +
     		       SecureRMISocketFactory.getLocalThreadLastReadPrincipal());
            return  "Hello World:  questa frase proviene dall'invocazione del metodo sayHello del server remoto";
        }

       public String givemeFive() {
            return  "FIVE!!!!";
        }

        public static void main(String args[]) throws IOException {
        	java.rmi.server.RMISocketFactory.setSocketFactory(new SecureRMISocketFactory());
            // Crea ed installa un security manager.
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new RMISecurityManager());
            }
            try {
                // Creo un'istanza del server remoto
                ITest obj = new Test();                          
                // Creo le socket factory
                //Registry registry = LocateRegistry.createRegistry(2378);
                Naming.bind("//:2378/HelloServer", obj);

                System.out.println("L'oggetto remoto HelloServer e' stato registrato nel registro RMI");
               } catch (Exception e) {
                System.out.println("HelloImpl err: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }