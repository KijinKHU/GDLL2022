//package dataset;
//
//
//import de.parsemis.graph.Graph;
//import de.parsemis.graph.Node;
//
//public interface Edge<NodeType, EdgeType> {
//
//	/** constant that tells that the edge goes from nodeB to nodeA */
//	public static final int INCOMING = -1;
//
//	/** constant that tells that the edge has no specific direction */
//	public static final int UNDIRECTED = 0;
//
//	/** constant that tells that the edge goes from nodeA to nodeB */
//	public static final int OUTGOING = 1;
//
//	/**
//	 * @return the direction that told, if the edge is UNDIRECTED, OUTGOING or
//	 *         INCOMING in respect to the first node
//	 */
//	public int getDirection();
//
//	/**
//	 * @param node
//	 * @return the direction that told, if the edge is UNDIRECTED, OUTGOING or
//	 *         INCOMING in respect to the given node
//	 */
//	public int getDirection(Node<NodeType, EdgeType> node);
//
//	/**
//	 * @return the graph, this edge is part of
//	 */
//	public Graph<NodeType, EdgeType> getGraph();
//
//	/**
//	 * @return the edge index of this edge in the corresponding graph
//	 */
//	public int getIndex();
//
//	/**
//	 * @return the connected label
//	 */
//	public EdgeType getLabel();
//
//	/**
//	 * @return the first node of the edge
//	 */
//	public Node<NodeType, EdgeType> getNodeA();
//
//	/**
//	 * @return the second node of the edge
//	 */
//	public Node<NodeType, EdgeType> getNodeB();
//
//	/**
//	 * @param node
//	 * @return the other node of the edge
//	 */
//	public Node<NodeType, EdgeType> getOtherNode(Node<NodeType, EdgeType> node);
//
//	/**
//	 * set the label of this edge
//	 * 
//	 * @param label
//	 */
//	public void setLabel(EdgeType label);
//
//}
