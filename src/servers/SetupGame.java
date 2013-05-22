package servers;

import interfaces.IGame;

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

import javax.rmi.ssl.SslRMIClientSocketFactory;

public class SetupGame {
	public static void main(String args[]) {
		// INIZIALIZZAZIONI
		String policyGroup = System.getProperty("servers.policy");
		String implCodebase = System.getProperty("servers.impl.codebase");
		String classeserver = System.getProperty("servers.classeserver");
		String trustStore = System.getProperty("javax.net.ssl.trustStore");
		String trustStorePassword = System.getProperty("javax.net.ssl.trustStorePassword");
		String keyStore = System.getProperty("javax.net.ssl.keyStore");
		String keyStorePassword = System.getProperty("javax.net.ssl.keyStorePassword");
		// LANCIO IL SECURITY MANAGER
		System.setSecurityManager(new RMISecurityManager());
		try {
			Properties prop = new Properties();
			prop.put("java.security.policy", policyGroup);
			prop.put("servers.impl.codebase", implCodebase);
			prop.put("java.class.path", "no_classpath");
			prop.put("javax.net.ssl.trustStore", trustStore);
			prop.put("javax.net.ssl.trustStorePassword", trustStorePassword);
			prop.put("javax.net.ssl.keyStore", keyStore);
			prop.put("javax.net.ssl.keyStorePassword", keyStorePassword);
			// FASE 1: CREAZIONE DEL GRUPPO DI ATTIVAZIONE
			ActivationGroupDesc groupDesc = new ActivationGroupDesc(prop, null);
			// FASE 2: REGISTRAZIONE DEL GRUPPO DI ATTIVAZIONE
			ActivationGroupID groupID = ActivationGroup.getSystem()
					.registerGroup(groupDesc);
			System.out
					.println("Il gruppo e' stato creato,  registrato col sistema d'attivazione, ed ha identificativo = "
							+ groupID);
			// FASE 3: CREAZIONE DELL'ACTIVATION DESCRIPTOR ASSOCIATO AL SERVER
			ActivationDesc actDesc = new ActivationDesc(groupID, classeserver,
					implCodebase, null);
			// FASE 4: REGISTRAZIONE DEL SERVER ATTIVABILE COL SISTEMA
			// D'ATTIVAZIONE
			IGame stub_server = (IGame) Activatable.register(actDesc);
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
			//Registry registry = LocateRegistry.createRegistry(5552, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory(null, null, true));
			Registry registry = LocateRegistry.getRegistry("localhost", 5552, new SslRMIClientSocketFactory());
			registry.bind("//" + ipp + ":5552/GameServer", (Remote) stub_server);
			// Thread.sleep(10000);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
