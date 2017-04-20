package client;
import java.util.Scanner;

public class Client {

	private Client() {}
	 	
	    public static void main(String[] args) {
	    	Scanner scanner = new Scanner(System.in);
	    	ClientFunctions c = new ClientFunctions(scanner);
	    	boolean repeatS = true;
	    	while(repeatS) {
	    		System.out.println("O que deseja fazer ? (Para visualizar os comandos disponiveis digite 'help')");
	    		String command = scanner.nextLine();
	    		repeatS = c.commandRouter(command);
	    	}
	    }
}
