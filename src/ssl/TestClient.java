package ssl;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;

public class TestClient {
	
	public static void main(String[] args) throws IOException, NotBoundException{
		//java.rmi.server.RMISocketFactory.setSocketFactory(new SecureRMISocketFactory());



	    String name = "rmi://localhost:2378/HelloServer";

	    System.out.println("Looking up " + name + " ...");

	    ITest test = (ITest) Naming.lookup(name);

	    System.out.println("Object found ... invoking remote method");

	    String msg = "Hello World!";

	    System.out.println("toLowerCase(" + msg + ") = " + test.sayHello());
	}

}
