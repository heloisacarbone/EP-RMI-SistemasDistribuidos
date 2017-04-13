import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Server implements PartRepository{

	private String serverName;
	private List<Part> partsList;
	
	public Server() {
		partsList = new ArrayList<Part>();
		
	}
	
	@Override
	public Part getPart(int index) {
		return partsList.get(index);
	}

	@Override
	public void AddPart(Part p) {
		this.partsList.add(p);
	}

	@Override
	public void setServerName(String n) throws RemoteException {
		this.serverName = n;
	}

	@Override
	public String getServerName() throws RemoteException {
		return this.serverName;
	}

}
