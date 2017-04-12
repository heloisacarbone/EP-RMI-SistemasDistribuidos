import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
//Main do server
//Rodar rmiregistry dentro da pasta /bin do projeto para subir o rmi
public class Main {
	public static void main(String [] args){
		try {
            Server obj = new Server();
            PartRepository stub = (PartRepository) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Hello", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
		
	}	
}