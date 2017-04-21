package client;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.rmi.Naming;

import server.Part;
import server.PartRepository;

public class ClientFunctions {
	
	/**
	 * Classe que trata as funcoes executadas pelo usuario. Nela, estao as referencias do servidor, de pecas correntes e subpecas. Eh aqui
	 * tambem que se trata a interface do usuario com o programa cliente.
	 * */
	
	/**
	 * Guarda a referencia do servidor remoto atual. Toda execucao de comando 'bind' este valor eh alterado
	 * */
	private PartRepository currentServer;
	
	/**
	 * Referencia da peca corrente. Toda execucao do comando 'getp' troca o objeto corrente
	 * */
	private Part currentPart;
	
	/**
	 * Lista de subpecas do cliente. Toda execucao do comando 'getp' adiciona um novo item a lista
	 * */
	private ArrayList<Part> currentSubParts;
	
	/**
	 * Scanner utilizado na leitura dos comandos do usuario
	 * */
	private Scanner s;
	
	public ClientFunctions(Scanner scanner) {
		this.s = scanner;
		this.currentSubParts = new ArrayList<Part>();
		this.currentPart = null;
	};
	
	/**
	 * Tratamento do comando digitado no console pelo usuario
	 * @param cmd - Comando completo digitado pelo usuario
	 * */
	public boolean commandRouter(String cmd) {
		// Separo os comandos dos possiveis argumentos por espaco
		String [] splittedCommand = cmd.split(" ");
		cmd = splittedCommand[0];
		
		switch(cmd.toLowerCase()) {
			case "help":
				return help();
			case "bind":
				return bind(splittedCommand);
			case "listservers":
				return listServers();
			case "servername":
				return serverName();
			case "listp":
				return listp();
			case "getp":
				return getp(splittedCommand);
			case "showp":
				return showp();
			case "clearlist":
				return clearlist();
			case "addsubpart":
				return addsubpart(splittedCommand);
			case "addp":
				return addp(splittedCommand);
			case "listsubp":
				return listsubp(splittedCommand);
			case "setup":
				return setup();
			case "quit":
				return false;
			default:
				System.out.println("O comando " + cmd + " nao existe.");
				return help();
		}
		
	}
	
	private boolean setup(){
		try{

			Part p1 = this.currentServer.AddPart("peca exemplo 1 - primitiva", "Peca primitiva adicionada pelo setup");
			Part p2 = this.currentServer.AddPart("peca exemplo 2 - primitiva", "Peca primitiva adicionada pelo setup");
			Part p3 = this.currentServer.AddPart("peca exemplo 3 - primitiva", "Peca primitiva adicionada pelo setup");
			
			this.currentSubParts.add(p1);
			this.currentSubParts.add(p2);
			
	
			
			Part p4 = this.currentServer.AddPart("peca exemplo 4 - nao primitiva", "Peca primitiva adicionada pelo setup");
			this.currentServer.AddSubParts(p4, this.currentSubParts);
			
			this.currentSubParts.clear();
			listp();
		} catch (RemoteException e){
			
			e.printStackTrace();
			return false;
			
		}
		
		
		
		 return true;
	}
	
	private boolean listServers() {
		try {
			String [] servers = Naming.list("localhost");
			System.out.println("Servidores disponiveis:");
			for (String s : servers) {
				System.out.println(s.substring(s.lastIndexOf("/") + 1));
			}
			return true;
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Imprime na tela para o usuario as descricoes dos comandos aceitos pelo programa
	 * */
	private boolean help() {
		System.out.printf(
			"Os comandos disponiveis para acesso: \n" +
			"\t bind [nome do server] - Conectar com um server ou mudar de server  \n" +
			"\t serverName - Mostra o nome do servidor que esta conectado  \n" +
			"\t listp - Lista as pecas do repositorio corrente  \n" +
			"\t listservers - Lista os nomes dos servers \n" +
			"\t getp [ID da peca] - Busca uma peca por codigo  \n" +
			"\t showp - Mostra atributos da peca corrente  \n" +
			"\t clearlist - Esvazia a lista de subpecas corrente  \n" +
			"\t listsubp - Lista as subpecas da peca corrente (sem os atributos mostrados em 'showp')  \n" +
			"\t addsubpart [n copias] - Adiciona a lista de subpecas corrente n unidades da peca corrente  \n" +
			"\t addp [nome] [descricao] [primitiva (sim/nao)] - Adiciona uma peca ao repositorio corrente. A lista de subpecas correntes e usada como lista de subcomponentes diretos da nova peca \n" +
			"\t quit - Encerra a execucao do cliente"
		);
		return true;
	}

	/**
	 * Troca a referencia de currentServer e se conecta em outro servidor
	 * */
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
	
	/**
	 * Lista as subpecas da peca corrente
	 * */
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
	
	/**
	 * Adiciona uma nova peca ao servidor corrente
	 * */
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

	/**
	 * Adiciona n copias de pecas na lista de subpecas corrente
	 * */
	private boolean addsubpart(String [] args) {
		int nParts = 0;
		
		if(args.length > 1){
			try {
				nParts = Integer.parseInt(args[1]);	
			} catch (NumberFormatException ex){
				System.err.println("Argumento n invalido!");
				return false;
			}
		} else {
			System.out.println("Quantas vezes voce deseja adicionar a peca corrente a lista de subpecas?");
			while (!this.s.hasNextInt()) System.out.println("O valor inserido possui caracteres, por favor so utilize numeros");
	    	nParts = this.s.nextInt();
		}
		
		
		if(nParts <= 0){
			System.err.println("Valor invalido! Nenhuma peca sera adicionada a lista de subpecas");
			// Nao ha a necessidade de retornar false nesse caso. Nao encerra o cliente.
			return true;
		}
		
		for(int i = 0; i < nParts; i++){
			this.currentSubParts.add(this.currentPart);
		}
		
		System.out.println("Agora, a lista de subpecas possui " + this.currentSubParts.size());
		
		return true;
	}

	/**
	 * Limpa a lista de subpecas
	 * */
	private boolean clearlist() {
		System.out.println("A lista de subpecas foi limpa.  Ela possuia " + this.currentSubParts.size());
		this.currentSubParts.clear();
		return true;
	}

	/**
	 * Mostra os atributos da peca corrente
	 * */
	private boolean showp() {
		if(this.currentPart == null)
			System.err.println("Nao ha pecas correntes no cliente");
		
		System.out.println("*********Dados da peca corrente*********");
		System.out.println("UID: " + this.currentPart.getUid());
		System.out.println("Nome: " + this.currentPart.getName());
		System.out.println("Descricao: " + this.currentPart.getDescription());
		System.out.println("****************************************");
		return true;
	}

	/**
	 * Retorna o nome do servidor corrente ao usuario
	 * */
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

	/**
	 * Recupera a peca de ID enviado para o usuario e insere como peca corrente do cliente
	 * */
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

	
	/**
	 * Lista todas as pecas do servidor corrente
	 * */
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
