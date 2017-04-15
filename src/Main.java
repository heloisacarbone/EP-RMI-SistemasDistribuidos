import java.rmi.AlreadyBoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
//Main do server
//Rodar rmiregistry dentro da pasta /bin do projeto para subir o rmi
public class Main {
	public static void main(String [] args){
		
		System.out.println("Digite o nome do servidor");
		startServer();
	}

	private static void startServer() {
		Scanner s = new Scanner(System.in);
		String serverName = s.nextLine();
		
		try {
            Server obj = new Server();
            PartRepository stub = (PartRepository) UnicastRemoteObject.exportObject(obj, 0);
           
            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            // Vincula o nome dado no arg na instancia do server
            registry.bind(serverName, stub);
            stub.setServerName(serverName);
            System.err.println("Server " + serverName + " ready");
        }
		catch(AlreadyBoundException ex) {
        	System.out.println("Nome de servidor ja existe. Por favor, utilize um nome de servidor diferente");
        	startServer();
        }
		catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
	}	
}