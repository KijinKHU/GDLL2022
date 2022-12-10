package dataset;

import java.io.Serializable;

public class Node implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String label;
	private String info;
	
	public Node(int id, String label, String info)
	{
		this.setId(id);
		this.setLabel(label);
		this.setInfo(info);
	}
	
	public Node(int id)
	{
		this.setId(id);
		this.setLabel(label);
		this.setInfo(info);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}