package kernighanLin;

public class Edge {

  public final double weight;
  
  public final Vertex source;
  public final Vertex destination;


  public Edge(Vertex source, Vertex dest, double weight) {
    this.weight = weight;
    this.source = source;
    this.destination = dest;
  }

}
