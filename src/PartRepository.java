import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface PartRepository extends Remote {
	public Part getPart(int index) throws RemoteException;
	
	public void AddPart(Part p) throws RemoteException;
	
	public void setServerName (String n) throws RemoteException;
	
	public String getServerName () throws RemoteException;
	
	//TODO: add funcoes
	
	//TODO: Put all RMI paths here to handle the repository functions
	
}
