package kHop;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import dataset.Node;


public class Self_Inf implements Serializable {
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private Node node;
	private List<Edge_Inf> khop;
	private Map<Integer, List<Edge_Inf>> k_hop;
	
	public Self_Inf(Node node, List<Edge_Inf> khop){
		this.node=node;
		this.khop =khop;
	}
	public Node getNode() {
		return node;
	}
	public void setNode(Node node) {
		this.node = node;
	}
	public List<Edge_Inf> getKhop() {
		return khop;
	}
	public void setKhop(List<Edge_Inf> khop) {
		this.khop = khop;
	}
	public Map<Integer, List<Edge_Inf>> getK_hop() {
		return k_hop;
		
	}
	public void setK_hop(Map<Integer, List<Edge_Inf>> k_hop) {
		this.k_hop = k_hop;
	}
	
}
