import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ServerFunctions implements PartRepository{

	private String serverName;
	private List<Part> partsList;
	
	public ServerFunctions() {
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
	public Part AddPart(String name, String description) {		
		Part newPart = new Part(this.partsList.size()+1, name, description);
		this.partsList.add(newPart);
		return newPart;
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
	public List<Part> getPartsList() throws RemoteException {
		return this.partsList;
	}


	

}
