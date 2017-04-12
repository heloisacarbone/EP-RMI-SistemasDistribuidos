import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface PartRepository extends Remote {

	List<Part> partsList = new ArrayList<Part>();
	
	public Part getPart(int index);
	
	public void AddPart(Part p);
	
	//TODO: add funcoes
	
	//TODO: Put all RMI paths here to handle the repository functions
	
}
