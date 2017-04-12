import java.util.ArrayList;
import java.util.List;

public class PartRepository {

	private List<Part> partsList;
	
	public PartRepository() {
		this.partsList = new ArrayList<Part>();
	}
	
	public Part getPart(int index){
		return this.partsList.get(index);
	}
	
	public void AddPart(Part p){
		this.partsList.add(p);
	}
	
	//TODO: Put all RMI paths here to handle the repository functions
	
}
