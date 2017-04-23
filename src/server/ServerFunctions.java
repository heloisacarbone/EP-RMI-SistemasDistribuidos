package server;
import java.net.MalformedURLException;
import java.rmi.Naming;
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
		Part newPart = new Part(this.generateUid(), name, description);
		this.partsList.add(newPart);
		return newPart;
	}

	@Override
	public Part AddSubParts(Part parent, ArrayList<Part> subParts)
			throws RemoteException {
		getPartByUID(parent.getUid()).setSubParts(subParts);
		System.out.println("FROM SERVER: " + subParts.size());
		return parent;
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
	
	private int generateUid(){
		int sum = this.partsList.size() + 1;
		char[] splittedString = this.serverName.toCharArray();
		
		for(char c : splittedString){
			sum += c;
		}
		return sum;
	}
}
