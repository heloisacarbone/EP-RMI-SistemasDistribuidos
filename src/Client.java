import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

//Exemplo do http://docs.oracle.com/javase/7/docs/technotes/guides/rmi/hello/hello-world.html#startRegistry
public class Client {
	 private Client() {}
	 	
	    public static void main(String[] args) {
	    	//Nao mexi aqui, isso é o exemplo da oracle
	        String host = (args.length < 1) ? null : args[0];
	        try {
	            Registry registry = LocateRegistry.getRegistry(host);
	            PartRepository stub = (PartRepository) registry.lookup("Hello");
	            Part response = stub.getPart(2);
	            System.out.println("response: " + response);
	        } catch (Exception e) {
	            System.err.println("Client exception: " + e.toString());
	            e.printStackTrace();
	        }
	    }

}
