package kernighanLin;
import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import kernighanLin.KernighanLin.VertexGroup;


public class Graph {
  final private Map<Vertex, Map<Vertex, Edge>> vertices;
  final private Map<Edge, Pair<Vertex>> edges;

  
  public Graph() {
    // Maps vertices to a map of vertices to incident edges
    vertices = new HashMap<Vertex, Map<Vertex, Edge>>();
    // Maps edges to edge endpoints
    edges = new HashMap<Edge, Pair<Vertex>>();
  }
  
  public boolean addVertex(Vertex v) {
    if (containsVertex(v)) return false;
    vertices.put(v, new HashMap<Vertex, Edge>());
    return true;
  }
  
  public boolean addEdge(Edge edge, Vertex v1, Vertex v2) {
    
    if (!containsVertex(v1) || !containsVertex(v2)) return false;
    if (findEdge(v1, v2) != null) return false;
    
    Pair<Vertex> pair = new Pair<Vertex>(v1, v2);
    edges.put(edge, pair);
    vertices.get(v1).put(v2, edge);
    vertices.get(v2).put(v1, edge);
    
    return true;
  }
  
  public boolean containsVertex(Vertex v) {
    return vertices.containsKey(v);
  }
  public boolean containsEdge(Edge e) {
    return edges.containsKey(e);
  }
  
  /** Finds an edge if any between v1 and v2 **/
  public Edge findEdge(Vertex v1, Vertex v2) {
    if (!containsVertex(v1) || !containsVertex(v2)) 
      return null;
    return vertices.get(v1).get(v2);
  }
  
  /** Gets the vertices directly connected to v **/
  public Collection<Vertex> getNeighbors(Vertex v) {
    if (!containsVertex(v)) return null;
    return vertices.get(v).keySet();
  }
  
  public Set<Edge> getEdges() {
    return edges.keySet();
  }
  
  public Set<Vertex> getVertices() {
    return vertices.keySet();
  }
  
  /** Returns a pair of vertices that connects by edge e **/
  public Pair<Vertex> getEndpoints(Edge e) {
    return edges.get(e);
  }
  
  public Graph getSubgraphByVertices(VertexGroup vertexGroup )
  {
	Graph g = new Graph();
	for (Vertex x : vertexGroup)
	{
	      Set<Vertex> listVertex = this.getVertices();
	      Vertex v = null;
	      for(Vertex v1: listVertex)
	      {
	    	  if(v1.name==x.name)
	    		  v=v1;
	      }
	      g.addVertex(v);
	      Map<Vertex, Edge> pair = vertices.get(v);
	      Edge edge = null;
	      for(Entry<Vertex,Edge> p: pair.entrySet())
	      {
    		  edge = p.getValue();
    		  if(edge!=null)
    	    	  g.addEdge(edge, edge.source, edge.destination);
	    	  
	      }
	      
	}
	return g;
  }
  
  public String graphToText()
  {
	  String  text = "";
	  for(Edge e: this.getEdges()) {
			text +=e.source +" "+e.destination+ " "+ e.weight;
			text +="\n";
		}
	  return text;
  }
  
}
