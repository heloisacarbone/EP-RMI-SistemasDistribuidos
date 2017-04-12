
import java.util.List;

public class Part {
	
	private int uid;
	private String name;
	private String description;
	private List<Part> subParts;
	
	public Part(int uid, String name, String description) {
		super();
		this.uid = uid;
		this.name = name;
		this.description = description;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Part> getSubParts() {
		return subParts;
	}

	public void setSubParts(List<Part> subParts) {
		this.subParts = subParts;
	}
}