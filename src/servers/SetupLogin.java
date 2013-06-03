package servers;

import interfaces.ILogin;

import java.net.InetAddress;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationDesc;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationGroupDesc;
import java.rmi.activation.ActivationGroupID;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

public class SetupLogin {
	public static void main(String args[]) {
		// INIZIALIZZAZIONI
		String policyGroup = System.getProperty("servers.policy");
		String implCodebase = System.getProperty("servers.impl.codebase");
		String classeserver = System.getProperty("servers.classeserver");
		// LANCIO IL SECURITY MANAGER
		System.setSecurityManager(new RMISecurityManager());
		try {
			Properties prop = new Properties();
			prop.put("java.security.policy", policyGroup);
			prop.put("servers.impl.codebase", implCodebase);
			prop.put("java.class.path", "no_classpath");
			// FASE 1: CREAZIONE DEL GRUPPO DI ATTIVAZIONE
			ActivationGroupDesc groupDesc = new ActivationGroupDesc(prop, null);
			// FASE 2: REGISTRAZIONE DEL GRUPPO DI ATTIVAZIONE
			ActivationGroupID groupID = ActivationGroup.getSystem().registerGroup(groupDesc);
			System.out.println("Il gruppo e' stato creato,  registrato col sistema d'attivazione, ed ha identificativo = "+ groupID);
			// FASE 3: CREAZIONE DELL'ACTIVATION DESCRIPTOR ASSOCIATO AL SERVER
			ActivationDesc actDesc = new ActivationDesc(groupID, classeserver,implCodebase, null);
			// FASE 4: REGISTRAZIONE DEL SERVER ATTIVABILE COL SISTEMA
			// D'ATTIVAZIONE
			ILogin stub_server = (ILogin) Activatable.register(actDesc);
			System.out
					.println("E' stato creato l'activation descriptor del server che e' stato registrato col demone d'attivazione");
			System.out
					.println("Il server attivabile che adesso puo' essere acceduto attraverso lo stub: "
							+ stub_server);
			System.out
					.println("Notate come la RemoteRef dello stub sia a null");
			// FASE 5: BINDING DEL SERVER ATTIVABILE SUL REGISTRO RMI
			System.out
					.println("Faccio il binding dello stub del server attivabile nel registro RMI alla porta 1098 dove gia' si trova registrato il sistema di attivazione ");
			InetAddress ip = InetAddress.getLocalHost();
			String ipp = ip.getHostAddress().toString();
			// Registry registry = LocateRegistry.createRegistry(5552, new
			// SslRMIClientSocketFactory(), new SslRMIServerSocketFactory(null,
			// null, true));
			//String ipp = "157.27.184.217";
			Registry registry = LocateRegistry.getRegistry("localhost", 5551);
			registry.rebind("//" + ipp + ":5551/LoginServer",
					(Remote) stub_server);
			// Thread.sleep(10000);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
