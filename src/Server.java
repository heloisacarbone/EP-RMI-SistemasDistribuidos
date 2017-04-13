import java.rmi.RemoteException;

public class Server implements PartRepository{

	String serverName;
	
	public Server() {}
	
	@Override
	public Part getPart(int index) {
		
		return null;
	}

	@Override
	public void AddPart(Part p) {
		
		
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
