package kHop.Util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.StandardOpenOption;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Map.Entry;


import org.apache.hadoop.fs.Path;

import dataset.Node;
import kHop.Edge_Inf;


 
public class Util_functions_khop {
	
	public Map<Integer, List<Edge_Inf>> vid_inedge;
	public Map<Integer, List<Edge_Inf>> vid_outedge;

	public Map<Integer, Node> nodeList;
	
	public Util_functions_khop(){
		 nodeList= new HashMap();
		 vid_inedge = new HashMap();
		 vid_outedge = new HashMap();
	}

	
	public void readDBForMap_(BufferedReader currentReader) {
		System.out.println("read DB");
		String line;
			
		try {
		line = currentReader.readLine();
		
		String[] temp  = line.split(" ");
		int tid=0;
		int edgeID =0;
		int flag=0;
		
		while (true) {

			tid = Integer.parseInt(temp[2]);
			
			if(temp[0].equals("t"))
			{
				while(true)
				{
					line = currentReader.readLine();
					if(line == null)
					{
						flag=1;
						break;
					}
					
					temp = line.split(" ");
					if(temp[0].equals("t"))
					{

						break;
					}
					if(temp[0].equals("v"))
					{
						int id = Integer.parseInt(temp[1]);
						String label = temp[2];
						String info = "";
						Node node = new Node(id, label, info);
						// For one largeGraph
						nodeList.put(id,node);
						if(vid_inedge.get(id)==null)
						{
							vid_inedge.put(id, new ArrayList());
						}
						if(vid_outedge.get(id)==null)
						{
							vid_outedge.put(id, new ArrayList());
						}
					}
					
					if(temp[0].equals("e"))
					{
						
						int v1id = Integer.parseInt(temp[1]);
						int v2id = Integer.parseInt(temp[2]);
						Node nodeA = nodeList.get(v1id);
						Node nodeB = nodeList.get(v2id);
						float w = Integer.parseInt(temp[3]);
						String inf = "";
						
						/*
						Edge_Inf outedge = new Edge_Inf(nodeA, nodeB,w,inf);
						Edge_Inf inedge = new Edge_Inf(nodeA, nodeB, w, inf);
						
						vid_inedge.get(nodeA.getId()).add(inedge);
						vid_outedge.get(nodeB.getId()).add(outedge);
						*/
						
						Edge_Inf edge = new Edge_Inf(edgeID, nodeA, nodeB,w,inf);
						edgeID++;
						
						vid_inedge.get(nodeB.getId()).add(edge);
						vid_outedge.get(nodeA.getId()).add(edge);
						
						}
					
					if(flag==1)
					{
						break;
					}
				}
				if(flag==1)
				{
					break;
				}
					
				}

			}

		}
		catch (IOException e) {
		}
		

	}
	
	public void readDBForMap(BufferedReader currentReader) {
		System.out.println("read DB");
		String line;
			
		try {

		int edgeID =-1;
		int flag=0;
		
		while (true) {

			line = currentReader.readLine();
			if(line == null)
			{
				flag=1;
				break;
			}
			String[] temp  = line.split(" ");
			edgeID++;
			int nodeAid = Integer.parseInt(temp[0]);
			int nodeBid = Integer.parseInt(temp[1]);
			
			String node_info = ""; // TODO: modify it if dataset contains inf of node.
			float w = 1;
			String edge_info ="";
			Node nodeA= nodeList.get(nodeAid);
			Node nodeB= nodeList.get(nodeBid);
			
			if(nodeA==null)
			{
				nodeA = new Node(nodeAid, String.valueOf(nodeAid), node_info);
				vid_inedge.put(nodeAid, new ArrayList());
				vid_outedge.put(nodeAid, new ArrayList());
				nodeList.put(nodeAid,nodeA);
			}
			if(nodeB==null)
			{
				nodeB = new Node(nodeBid, String.valueOf(nodeBid), node_info);
				vid_inedge.put(nodeBid, new ArrayList());
				vid_outedge.put(nodeBid, new ArrayList());
				nodeList.put(nodeBid,nodeB);
			}

			Edge_Inf edge = new Edge_Inf(edgeID, nodeA, nodeB,w,edge_info);

			vid_inedge.get(nodeB.getId()).add(edge);
			vid_outedge.get(nodeA.getId()).add(edge);

		}

		}
		catch (IOException e) {
		}
		

	}
	
	public void readDBForMap_EdgePartition(BufferedReader currentReader) {
		System.out.println("read DB");
		String line;
			
		try {

		int edgeID =-1;
		int flag=0;
		
		while (true) {

			line = currentReader.readLine();
			if(line == null)
			{
				flag=1;
				break;
			}
			String[] temp  = line.split(",");
			System.out.println("temp[0]: " +temp[0]);
			edgeID++;
			int nodeAid = Integer.parseInt(temp[0]);
			int nodeBid = Integer.parseInt(temp[1]);
			
			String node_info = ""; // TODO: modify it if dataset contains inf of node.
			float w = 1;
			String edge_info ="";
			Node nodeA= nodeList.get(nodeAid);
			Node nodeB= nodeList.get(nodeBid);
			
			if(nodeA==null)
			{
				nodeA = new Node(nodeAid, String.valueOf(nodeAid), node_info);
				vid_inedge.put(nodeAid, new ArrayList());
				//vid_outedge.put(nodeAid, new ArrayList());
				nodeList.put(nodeAid,nodeA);
			}
			if(nodeB==null)
			{
				nodeB = new Node(nodeBid, String.valueOf(nodeBid), node_info);
				vid_inedge.put(nodeBid, new ArrayList());
				//vid_outedge.put(nodeBid, new ArrayList());
				nodeList.put(nodeBid,nodeB);
			}

			Edge_Inf edge = new Edge_Inf(edgeID, nodeA, nodeB,w,edge_info);

			vid_inedge.get(nodeB.getId()).add(edge);
			//vid_outedge.get(nodeA.getId()).add(edge);

		}

		}
		catch (IOException e) {
		}
		

	}
	
	public String getSuffix(Node node) {
		// TODO Auto-generated method stub
		return "";
	}
	
	public static boolean ensureDirExist(String path) throws IOException {
		File dir = new File(path);
		if (!dir.isDirectory())
			dir = dir.getParentFile();
		if (!dir.exists())
			dir.mkdirs();
		return true;
	}
	
	public static File createFile(String path, String fileName) {
		    try {

		      File myfile = new File(path+"\\" +fileName);
		      if (myfile.exists())
		    	  myfile.delete();
		   	  myfile.createNewFile();
		      return myfile;
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		    return null;
		    
		  }


	public void writeToFile(File file, String text) {
		// TODO Auto-generated method stub

		try {
		      //FileWriter myWriter = new FileWriter("filename.txt");
		      FileWriter myWriter = new FileWriter(file);
		      myWriter.write(text);
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		
	}
	
	public void writeToFile_append(File file, String text) throws IOException {
		// TODO Auto-generated method stub

		Writer output;
		output = new BufferedWriter(new FileWriter(file, true));  //clears file every time
		output.append(text);
		output.close();

	}
	
	public String writeAText(List<Edge_Inf> edges)
	{
		String text = null;
		if(edges.size()>0)
			for(Edge_Inf edge: edges)
			{
				text=edge.getNodeA().getId() +"," + edge.getNodeB().getId() +"\n";
			}
		return text;
		
	}
	

}

