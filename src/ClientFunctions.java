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
				System.out.println("O comando " + cmd + "não existe.");
				return help();
		}
		
	}
	
	private boolean help() {
		// TODO: Ta escrito que essa lista esta incompleta na descricao
		System.out.printf(
			"Os comandos disponíveis para acesso: \n" +
			"\t bind - Conectar com um server ou mudar de server  \n" +
			"\t serverName - Mostra o nome do servidor que esta conectado  \n" +
			"\t listp - Lista as peças do repositório corrente  \n" +
			"\t getp - Busca uma peça por código  \n" +
			"\t showp - Mostra atributos da peça corrente  \n" +
			"\t clearlist - Esvazia a lista de subpeças corrente  \n" +
			"\t addsubpart - Adiciona à lista de subpeças corrente n unidades da peça corrente  \n" +
			"\t addp - Adiciona uma peça ao repositório corrente. A lista de subpeças correntes é usada como lista de subcomponentes diretos da nova peça \n" +
			"\t quit - Encerra a execução do cliente"
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
	            System.out.println("O nome do servidor em que está conectado é " + name);
			} catch (Exception e) {
	            System.err.println("Client exception: " + e.toString());
	            e.printStackTrace();
	        }
		} else {
			System.out.println("Você não está conectado em nenhum servidor. Utilize o comando 'bind' para se conectar");
		}
		return true;
	}

	private boolean getp() {
		boolean stillSearch = true;
		while (stillSearch) {
			System.out.println("Qual peça deseja buscar ? (id)");
			while (!this.s.hasNextInt()) System.out.println("O código inserido possui caracteres, por favor só utilize números");
	    	int id = this.s.nextInt();
			try {
	            this.currentPart = this.currentServer.getPartByUID(id);
	            if (this.currentPart == null) {
	            	boolean validOption = true;
	            	System.out.println("O id não retornou nenhuma peça. Deseja buscar por outro id ?");
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
		            			System.out.println("A opção escolhida não está na lista.");
		            			validOption = true;
	 	            	}
	            	}
	            } else {
	            	System.out.println("A peça corrente é " + this.currentPart.getName());
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
			System.out.println("id      |     nome     |      descrição   ");
			for (Part p : parts) {
				String info = p.getUid()  +  "    |    " + p.getName() +  "    |    " + p.getDescription();
				System.out.println(info);
			}
			System.out.println("Para ver mais detalhes de alguma das peças utilize o comando 'showp'");
		} catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
		
		return true;
	}

}
