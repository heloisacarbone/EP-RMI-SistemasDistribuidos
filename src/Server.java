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
	public Part getPartByUID(int uid) throws RemoteException {
		Part r = null;
		for (Part p :  this.partsList) {
			if (p.getUid() == uid) {
				r = p;
			}
		}
		
		return r;
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

	@Override
	public int getPartsLength() throws RemoteException {
		return this.partsList.size();
	}

	@Override
	public List<Part> getPartsList(String n) throws RemoteException {
		return this.partsList;
	}


	

}
