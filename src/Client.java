import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

//Exemplo do http://docs.oracle.com/javase/7/docs/technotes/guides/rmi/hello/hello-world.html#startRegistry
public class Client {
	 private Client() {}
	 	
	    public static void main(String[] args) {
	    	// TODO: Acrescentar os comandos
	    	Scanner scanner = new Scanner(System.in);
	    	
	    	System.out.println("Deseja se conectar com qual server?");
	    	String serverName = scanner.nextLine();
	        
	        try {
	        	//Nao mexi aqui, isso é o exemplo da oracle
	        	String host = (args.length < 1) ? null : args[0];
	            Registry registry = LocateRegistry.getRegistry(host);
	            PartRepository stub = (PartRepository) registry.lookup(serverName);
	            
	            String response = stub.getServerName();
	            System.out.println("response: " + response);
	        } catch (Exception e) {
	            System.err.println("Client exception: " + e.toString());
	            e.printStackTrace();
	        }
	    }

}
