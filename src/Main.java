import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
//Main do server
//Rodar rmiregistry dentro da pasta /bin do projeto para subir o rmi
public class Main {
	public static void main(String [] args){
		if (args.length == 1) {
			try {
	            Server obj = new Server();
	            PartRepository stub = (PartRepository) UnicastRemoteObject.exportObject(obj, 0);
	           
	            // Bind the remote object's stub in the registry
	            Registry registry = LocateRegistry.getRegistry();
	            // Vincula o nome dado no arg na instancia do server
	            registry.bind(args[0], stub);
	            stub.setServerName(args[0]);
	            System.err.println("Server "+ args[0] +" ready");
	        } catch (Exception e) {
	            System.err.println("Server exception: " + e.toString());
	            e.printStackTrace();
	        }
		} else {
			System.out.println("Acrescente um nome para o servidor ao executar.");
			System.exit(0);
		}
		
	}	
}