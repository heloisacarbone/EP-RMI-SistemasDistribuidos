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
		// TODO: Acrescentar as funcoes que sao necessarias para ter todos os checks prontos
		switch(cmd.toLowerCase()) {
			case "help":
				return help();
			case "bind":
				return bind();
			case "serverName":
				return serverName();
			case "listp":
				return listp();
			case "getp":
				return getp();
			case "showp":
				return showp();
			case "clearlist":
				return clearlist();
			case "addsubpart":
				return addsubpart();
			case "addp":
				return addp();
			case "quit":
				return false;
			default:
				System.out.println("O comando " + cmd + "nao existe.");
				return help();
		}
		
	}
	
	private boolean help() {
		// TODO: Ta escrito que essa lista esta incompleta na descricao
		System.out.printf(
			"Os comandos disponiveis para acesso: \n" +
			"\t bind - Conectar com um server ou mudar de server  \n" +
			"\t serverName - Mostra o nome do servidor que esta conectado  \n" +
			"\t listp - Lista as pecas do repositorio corrente  \n" +
			"\t getp - Busca uma peca por codigo  \n" +
			"\t showp - Mostra atributos da peca corrente  \n" +
			"\t clearlist - Esvazia a lista de subpecas corrente  \n" +
			"\t addsubpart - Adiciona a  lista de subpecas corrente n unidades da peca corrente  \n" +
			"\t addp - Adiciona uma peca ao repositorio corrente. A lista de subpecas correntes e usada como lista de subcomponentes diretos da nova peca \n" +
			"\t quit - Encerra a execucao do cliente"
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
	
	private boolean addp() {
		// TODO Auto-generated method stub
		return true;
	}

	private boolean addsubpart() {
		// TODO Auto-generated method stub
		return true;
	}

	private boolean clearlist() {
		// TODO Auto-generated method stub
		return true;
	}

	private boolean showp() {
		// TODO Auto-generated method stub
		return true;
	}

	private boolean serverName() {
		if (this.currentServer != null) {
			try {
				String name = this.currentServer.getServerName();
	            System.out.println("O nome do servidor em que esta conectado e " + name);
			} catch (Exception e) {
	            System.err.println("Client exception: " + e.toString());
	            e.printStackTrace();
	        }
		} else {
			System.out.println("Voce nao esta conectado em nenhum servidor. Utilize o comando 'bind' para se conectar");
		}
		return true;
	}

	private boolean getp() {
		boolean stillSearch = true;
		while (stillSearch) {
			System.out.println("Qual peca deseja buscar ? (id)");
			while (!this.s.hasNextInt()) System.out.println("O codigo inserido possui caracteres, por favor so utilize numeros");
	    	int id = this.s.nextInt();
			try {
	            this.currentPart = this.currentServer.getPartByUID(id);
	            if (this.currentPart == null) {
	            	boolean validOption = true;
	            	System.out.println("O id nao retornou nenhuma peca. Deseja buscar por outro id ?");
	            	while(validOption) {
	            		validOption = false;
	            		System.out.println("sim ou nao");
	            		String option = this.s.nextLine().toLowerCase();
		            	switch(option) {
		            		case "sim":
		            			stillSearch = true;
		            			break;
		            		case "nao":
		            			stillSearch = false;
		            			break;
		            		default:
		            			System.out.println("A opcao escolhida nao esta na lista.");
		            			validOption = true;
	 	            	}
	            	}
	            } else {
	            	System.out.println("A peca corrente e " + this.currentPart.getName());
	            	stillSearch = false;
	            }
	            
	        } catch (Exception e) {
	            System.err.println("Client exception: " + e.toString());
	            e.printStackTrace();
	        }
		}
		
		return true;
	}

	private boolean listp() {
		try {
			List<Part> parts = this.currentServer.getPartsList();
			System.out.println("id      |     nome     |      descricao   ");
			for (Part p : parts) {
				String info = p.getUid()  +  "    |    " + p.getName() +  "    |    " + p.getDescription();
				System.out.println(info);
			}
			System.out.println("Para ver mais detalhes de alguma das pecas utilize o comando 'showp'");
		} catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
		
		return true;
	}

}
