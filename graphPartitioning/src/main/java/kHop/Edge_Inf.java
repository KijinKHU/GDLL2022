package kHop;


import java.io.Serializable;

import dataset.Node;

public class Edge_Inf implements Serializable   {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** constant that tells that the edge goes from nodeB to nodeA */
	public static final int INCOMING = -1;

	/** constant that tells that the edge has no specific direction */
	public static final int UNDIRECTED = 0;

	/** constant that tells that the edge goes from nodeA to nodeB */
	public static final int OUTGOING = 1;
	
	
	private int edgeID;
	private Node nodeA;
	private Node nodeB;
	private float weight;
	private String inf;
	
	public Edge_Inf(int edgeID, Node nodeA, Node nodeB, float weight, String inf)
	{
		this.setEdgeID(edgeID);
		this.setNodeA(nodeA);
		this.setNodeB(nodeB);
		this.setWeight(weight);
		this.setInf(inf);
	}
	
	public Edge_Inf( Node nodeA, Node nodeB)
	{

		this.setNodeA(nodeA);
		this.setNodeB(nodeB);

	}

	public Node getNodeA() {
		return nodeA;
	}

	public void setNodeA(Node nodeA) {
		this.nodeA = nodeA;
	}

	public Node getNodeB() {
		return nodeB;
	}

	public void setNodeB(Node nodeB) {
		this.nodeB = nodeB;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public String getInf() {
		return inf;
	}

	public void setInf(String inf) {
		this.inf = inf;
	}

	public int getEdgeID() {
		return edgeID;
	}

	public void setEdgeID(int edgeID) {
		this.edgeID = edgeID;
	}
	

	

}
