package kernighanLin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.MatchResult;

import org.apache.spark.api.java.JavaPairRDD;

import kHop.Node_Info;
import scala.Tuple2;
import util.FolderManager;

public class KernighanLinProgram {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String inputPath = null;
		String outputPath = null;
		int numPart= 2;
		if(args.length==0)
		{
/*			inputPath = "E:\\scala_codes\\graphPartitioning\\src\\main\\java\\kernighanLin\\weightedEdges.txt";
			outputPath = "E:\\scala_codes\\graphPartitioning\\src\\main\\java\\kernighanLin\\output\\";
			numPart = 2;*/
			System.out.println("usage: inputPath outputPath numPart");
		}
		else
		{
			inputPath = args[0];
			outputPath = args[1];
			numPart = Integer.parseInt(args[2]);
		}
	    try {
	        new KernighanLinProgram(inputPath, outputPath, numPart);
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	}
	
	
	
	 public KernighanLinProgram(String inputPath) throws IOException {

		  	System.out.println("input path " + inputPath);
		  	Graph graph = graphFromFile(inputPath);
		    KernighanLin k = KernighanLin.process(graph);

		    System.out.print("Group A: ");
		    for (Vertex x : k.getGroupA())
		      System.out.print(x+" ");
		    System.out.print("\nGroup B: ");
		    for (Vertex x : k.getGroupB())
		      System.out.print(x+" ");
		    System.out.println("");
		    System.out.println("Cut cost: "+k.getCutCost());
		  }
	 
	 public KernighanLinProgram(String inputPath, String outputPath, int numPart) throws IOException {

		  	System.out.println("input path " + inputPath);
		  	Graph graph = graphFromFile(inputPath);
		  	int i=0;
		  	if(numPart ==4)
		  	{
			  	KernighanLin k = KernighanLin.process(graph);
				Graph subgraph1 = graph.getSubgraphByVertices(k.getGroupA());
				i=biPartioning(outputPath, subgraph1, i);
				
				Graph subgraph2 = graph.getSubgraphByVertices(k.getGroupB());
				i=biPartioning(outputPath, subgraph2, i);
		  	}
		  	if(numPart==2)
		  	{
		  		biPartioning(outputPath, graph, i);
		  	}

		    
		    
/*		    //Create graph A
		    for (Vertex x : k.getGroupA())
		      System.out.print(x+" ");
		    System.out.print("\nGroup B: ");
		    for (Vertex x : k.getGroupB())
		      System.out.print(x+" ");
		    System.out.println("");
		    System.out.println("Cut cost: "+k.getCutCost());*/
		  }
	 
	 public KernighanLinProgram(String inputPath, String outputPath, int numPart, String backup) throws IOException {

		  	System.out.println("input path " + inputPath);
		  	Graph graph = graphFromFile(inputPath);
		    
		  	int num_subset = numPart/2;
		  	List<Graph> graphs = new ArrayList<Graph>();
		    int i=0;
		    int j=0;
		    while(i<numPart)
		    {
		    	for(j=0;j<num_subset;j++)
		    	{
		    		KernighanLin k = KernighanLin.process(graph);
		    		Graph subgraph = graph.getSubgraphByVertices(k.getGroupA());
		    		partitioningPrint(subgraph, outputPath, i);
		    		i=i+1;
		    		Graph subgraph2 = graph.getSubgraphByVertices(k.getGroupB());
		    		partitioningPrint(subgraph2, outputPath, i);
		    		i=i+1;
		    	}
		    }
		    
/*		    //Create graph A
		    for (Vertex x : k.getGroupA())
		      System.out.print(x+" ");
		    System.out.print("\nGroup B: ");
		    for (Vertex x : k.getGroupB())
		      System.out.print(x+" ");
		    System.out.println("");
		    System.out.println("Cut cost: "+k.getCutCost());*/
		  }



	private int biPartioning(String outputPath, Graph graph, int i) throws IOException {
		KernighanLin k = KernighanLin.process(graph);
		Graph subgraph = graph.getSubgraphByVertices(k.getGroupA());
		partitioningPrint(subgraph, outputPath, i);
		i=i+1;
		Graph subgraph2 = graph.getSubgraphByVertices(k.getGroupB());
		partitioningPrint(subgraph2, outputPath, i);
		i=i+1;
		return i;
	}
	 
	 private static void partitioningPrint(Graph graph, String outpath, int part)
				throws IOException {

		 	String fileName = "part" + String.valueOf(part)+".txt";
			
			Writer output;
			
			String filePath = outpath +File.separatorChar+ fileName;

			FolderManager.ensureDirExist(filePath);
			
			output = new BufferedWriter(new FileWriter(filePath, false));  //clears file every time			
			
			System.out.println("Print result part to: " + filePath);
			
			output.append(graph.graphToText());
					
			output.close();
		}
		  
		  public static Graph graphFromFile(String filename) throws IOException {
		    FileReader fileReader = new FileReader(filename);
		    BufferedReader bufferedReader = new BufferedReader(fileReader);
		    
		    //Graph g = fromReadable(bufferedReader);
		    Graph g = weightEdge_fromReadable(bufferedReader);
		    bufferedReader.close();
		    
		    return g;
		  }
		  
		  public static Graph fromReadable(Readable readable) {
		    Graph graph = new Graph();
		    HashMap<String, Vertex> names = new HashMap<String, Vertex>();
		    
		    Scanner s = new Scanner(readable);

		    while(s.hasNext("\r|\n")) s.next("\r|\n");
		    
		    s.skip("vertices:");
		    while (s.findInLine("([A-Z])") != null) {
		      MatchResult match = s.match();
		      
		      String name = match.group(1);
		      Vertex v = new Vertex(name);
		      graph.addVertex(v);
		      names.put(name, v);
		    }

		    s.skip("\nedges:");
		    while (s.findInLine("([A-Z])([A-Z])\\(([0-9]+(?:\\.[0-9]+)?)\\)") != null) {
		      MatchResult match = s.match();
		      
		      Vertex first = names.get(match.group(1));
		      Vertex second = names.get(match.group(2));
		      Double weight = Double.parseDouble(match.group(3));
		      graph.addEdge(new Edge(first, second, weight), first, second);
		    }
		    return graph;
		  }
		  
		  public static Graph weightEdge_fromReadable(Readable readable) {
			    Graph graph = new Graph();
			    HashMap<String, Vertex> names = new HashMap<String, Vertex>();
			    
			    @SuppressWarnings("resource")
				Scanner s = new Scanner(readable);
			    
			    String line;
			    
			    while(s.hasNextLine()) {
			    	line = s.nextLine();
			    	String[] temp  = line.split(" ");
					String nodeA = temp[0];
					String nodeB = temp[1];
					Double weight=1.0;
					if(temp.length>2)
					{
						weight = Double.parseDouble(temp[2]);
					}
					Vertex v1 = names.get(nodeA);
					if(v1 ==null)
					{
						v1 = new Vertex(nodeA);
						graph.addVertex(v1);
						names.put(nodeA, v1);
					}
											
					Vertex v2 = names.get(nodeB);
					if(v2== null)
					{
						v2 = new Vertex(nodeB);
						graph.addVertex(v2);
						names.put(nodeB, v2);
					}
				    graph.addEdge(new Edge(v1, v2, weight), v1, v2);
			    }
			    return graph;
			  }
		  
		  /** Adds a random vertex on an edge if the number of 
		   *  vertices in the given graph isn't even */
		  public static void makeVerticesEven(Graph g) {
		    if (g.getVertices().size() % 2 == 0) return;
		    
		    ArrayList<Vertex> vlist = new ArrayList<Vertex>();
		    for (Vertex v : g.getVertices()) vlist.add(v);
		    Random r = new Random();
		    Vertex randomV = vlist.get(r.nextInt(vlist.size()));
		    Vertex newV = new Vertex("?");
		    Edge newE = new Edge(newV, randomV, 0);
		    
		    g.addVertex(newV);
		    g.addEdge(newE, newV, randomV);
		  }
		  
		  public static void printGraph(Graph g) {
		    for (Vertex v : g.getVertices())
		      System.out.print(v+" ");
		    System.out.println();
		    
		    for (Edge e : g.getEdges()) {
		      Pair<Vertex> endpoints = g.getEndpoints(e);
		      System.out.print(endpoints.first+""+endpoints.second+"("+e.weight+") ");
		    }
		    System.out.println();
		    
		  }
	

}


