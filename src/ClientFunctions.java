import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class ClientFunctions {
	
	private PartRepository currentServer;
	private Part currentPart;
	private List<Part> currentSubParts;
	private Scanner s;
	
	public ClientFunctions(Scanner scanner) {
		this.s = scanner;
	};
	
	public boolean commandRouter(String cmd) {
		switch(cmd.toLowerCase()) {
			case "help":
				return help();
			case "bind":
				return bind();
			case "quit":
				return false;
			default:
				System.out.println("O comando " + cmd + "não existe.");
				return true;
		}
		
	}
	
	private boolean help() {
		System.out.printf(
			"Os comandos disponíveis para acesso \n" +
			"bind - Conectar com um server ou mudar de server  \n" +
			"listp - Lista as peças do repositório corrente  \n" +
			"getp - Busca uma peça por código  \n" +
			"showp - Mostra atributos da peça corrente  \n" +
			"clearlist - Esvazia a lista de subpeças corrente  \n" +
			"addsubpart - Adiciona à lista de subpeças corrente n unidades da peça corrente  \n" +
			"addp - Adiciona uma peça ao repositório corrente. A lista de subpeças correntes é usada como lista de subcomponentes diretos da nova peça \n" +
			"quit - Encerra a execução do cliente"
		);
		return true;
	}

	private boolean bind() {
		System.out.println("Deseja se conectar com qual server?");
    	String serverName = this.s.nextLine();
		try {
            Registry registry = LocateRegistry.getRegistry();
            PartRepository stub = (PartRepository) registry.lookup(serverName);
            String response = stub.getServerName();
            System.out.println("bind realizado com repositorio: " + response);
            this.currentServer = stub;
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
		
		return true;
	}

}
