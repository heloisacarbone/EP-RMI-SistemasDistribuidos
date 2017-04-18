import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientFunctions {
	
	private PartRepository currentServer;
	private Part currentPart;
	private ArrayList<Part> currentSubParts;
	private Scanner s;
	
	public ClientFunctions(Scanner scanner) {
		this.s = scanner;
		this.currentSubParts = new ArrayList<Part>();
	};
	
	public boolean commandRouter(String cmd) {
		String [] fullCommand = cmd.split(" ");
		String [] args = null;
		
		if(fullCommand.length > 1){
			args = fullCommand;
		}
		
		cmd = fullCommand[0];
		
		switch(cmd.toLowerCase()) {
			case "help":
				return help();
			case "bind":
				return bind(args);
			case "servername":
				return serverName();
			case "listp":
				return listp();
			case "getp":
				return getp(args);
			case "showp":
				return showp();
			case "clearlist":
				return clearlist();
			case "addsubpart":
				return addsubpart();
			case "addp":
				return addp(args);
			case "listsubp":
				return listsubp(args);
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
			"\t bind [nome do server] - Conectar com um server ou mudar de server  \n" +
			"\t serverName - Mostra o nome do servidor que esta conectado  \n" +
			"\t listp - Lista as pecas do repositorio corrente  \n" +
			"\t getp - Busca uma peca por codigo  \n" +
			"\t showp - Mostra atributos da peca corrente  \n" +
			"\t clearlist - Esvazia a lista de subpecas corrente  \n" +
			"\t addsubpart - Adiciona a lista de subpecas corrente n unidades da peca corrente  \n" +
			"\t addp [nome] [descricao] [primitiva (sim/nao)] - Adiciona uma peca ao repositorio corrente. A lista de subpecas correntes e usada como lista de subcomponentes diretos da nova peca \n" +
			"\t quit - Encerra a execucao do cliente"
		);
		return true;
	}

	private boolean bind(String [] args) {
		String serverName = "";
		if(args == null || args.length == 1){
			System.out.println("Deseja se conectar com qual server?");
			serverName = this.s.nextLine();
		} else {
			serverName = args[1];
		}
		
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
	
	private boolean listsubp(String [] args){
		int id = -1;
		
		if(args.length > 1) {
			try{
				id = Integer.parseInt(args[1]);
			} catch(NumberFormatException ex){
				System.err.println("Argumento UID invalido!");
				return false;
			}
		} else {
			System.out.println("Qual peca deseja buscar ? (id)");
			while (!this.s.hasNextInt()) System.out.println("O codigo inserido possui caracteres, por favor so utilize numeros");
	    	id = this.s.nextInt();
		}
		
		try {
			Part part = this.currentServer.getPartByUID(id);
			ArrayList<Part> subParts = part.getSubParts();
			
			if(subParts.isEmpty()){
				System.out.println("A peca " + part.getName() + " nao possui subpecas");
			} else {
				System.out.println("Peca " + part.getName() + ":");
				System.out.println("id      |     nome     |      descricao   ");
				for (Part p : subParts) {
					String info = p.getUid()  +  "    |    " + p.getName() +  "    |    " + p.getDescription();
					System.out.println(info);
				}
			}
			
		} catch (RemoteException e) {
			e.printStackTrace();
			
			return false;
		}
		
		return true;
	}
	
	private boolean addp(String [] args) {
		
		String name = "";
		
		if(args.length > 1){
			name = args[1];
		} else {
			System.out.println("Por favor, digite o nome da nova peca primitiva:");
			name = this.s.nextLine();
		}
		
		String description = "";
		if(args.length > 2) {
			description = args[2];
		} else {
			System.out.println("Digite a descricao da nova peca " + name + ":");
			description = this.s.nextLine();	
		}
		
		String primitive = "";
		if(args.length > 3){
			primitive = args[3];
		} else {
			System.out.println("A peca " + name + " eh primitiva?");
			primitive = this.s.nextLine();
		}

		try {
			Part p = this.currentServer.AddPart(name, description);
			if(primitive.toLowerCase().contains("sim")) {
				System.out.println("Peca primitiva " + name + " adicionada com sucesso! Seu uid eh: " + p.getUid());
			} else {
				if(this.currentSubParts.size() == 0) {
					System.out.println("Voce nao tem pecas a serem adicionadas como subpecas. Peca adicionada no repositorio sem subpecas.");
				} else {
					this.currentServer.AddSubParts(p, this.currentSubParts);
					System.out.println("Peca com subpecas " + name + " adicionada com sucesso! Seu uid eh: " + p.getUid());	
				}
			}
			return true;
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}	
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
		System.out.println("*********Dados da peca corrente*********");
		System.out.println("UID: " + this.currentPart.getUid());
		System.out.println("Nome: " + this.currentPart.getName());
		System.out.println("Descricao: " + this.currentPart.getDescription());
		System.out.println("****************************************");
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

	private boolean getp(String [] args) {
		boolean stillSearch = true;
		while (stillSearch) {
			int id = -1;
			if(args.length > 1){
				try{
					id = Integer.parseInt(args[1]);	
				} catch (NumberFormatException ex){
					System.err.println("Argumento UID invalido!");
					return false;
				}
				
			} else {
				System.out.println("Qual peca deseja buscar ? (id)");
				while (!this.s.hasNextInt()) System.out.println("O codigo inserido possui caracteres, por favor so utilize numeros");
		    	id = this.s.nextInt();	
			}
			
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
	            	this.currentSubParts.add(this.currentPart);
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
