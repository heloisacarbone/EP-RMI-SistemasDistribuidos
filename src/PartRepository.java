import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface PartRepository extends Remote {
	public Part getPartByUID(int index) throws RemoteException;
	
	public void AddPart(Part p) throws RemoteException;
	
	// Nomeia o server
	public void setServerName (String n) throws RemoteException;
	
	// Retorna o nome do server
	public String getServerName () throws RemoteException;
	
	// Retorna a lista de peças
	public List<Part> getPartsList () throws RemoteException;
	
	// Retorna o número de peças no server 
	public int getPartsLength () throws RemoteException;
	
}
