package kHop;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Node_Info implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int node_id;
	
	private List<Edge_Inf> outEdge;
	
	private List<Edge_Inf> inEdge;
	
	private List<Edge_Inf> khops;
	
	private boolean khopChange;
	
	public Node_Info(int node_id,  List<Edge_Inf> inEdge, List<Edge_Inf> outEdge)
	{
		this.setNode_id(node_id);
		this.setOutEdge(outEdge);
		this.setInEdge(inEdge);

			
		//this.setkhops(inEdge);
	}
	
	public List<Edge_Inf> getkhops(){
		return this.khops;
	}
	
	public void setkhops(List<Edge_Inf> khops) {
		this.khops = khops;
	}
	
	public void printInfor()
	{
		System.out.println("Key: "+ getNode_id());
		
/*		System.out.println("outEdge: ");
		if(outEdge!=null) {
			for(Edge_Inf edge: getOutEdge())
			{
				System.out.println("NodeA: "+ edge.getNodeA().getId() + "NodeB: "+ edge.getNodeB().getId());
			}
		}
		
		
		System.out.println("inEdge: ");
		if(inEdge!=null)
		{
			for(Edge_Inf edge: getInEdge())
			{
				System.out.println("NodeA: "+ edge.getNodeA().getId() + "NodeB: "+ edge.getNodeB().getId());
			}
		}*/
		
		System.out.println("k_hop: ");
		
		if(khops!=null)
		{
			for(Edge_Inf edge: khops)
			{
				System.out.println("NodeA: "+ edge.getNodeA().getId() + "NodeB: "+ edge.getNodeB().getId());
			}
		}
		
	
	}

	
	public String textToPrint() {
		// TODO Auto-generated method stub
		String text;
		text = String.valueOf(getNode_id());
		//List<Edge_Inf> edgeList = khops;
		//Using inedge instead of khops
		List<Edge_Inf> edgeList = khops;
		if(edgeList!=null && edgeList.size()>0)
		{
			for(int i=0; i<edgeList.size();i++)
			{
				text +=",";
				text +=edgeList.get(i).getNodeA().getId() +" "+edgeList.get(i).getNodeB().getId();
				
			}
		}
		
		return text;
	}

	public List<Edge_Inf> getOutEdge() {
		return outEdge;
	}

	public void setOutEdge(List<Edge_Inf> outEdge) {
		this.outEdge = outEdge;
	}

	public List<Edge_Inf> getInEdge() {
		return inEdge;
	}

	public void setInEdge(List<Edge_Inf> inEdge) {
		this.inEdge = inEdge;
	}

	public int getNode_id() {
		return node_id;
	}

	public void setNode_id(int node_id) {
		this.node_id = node_id;
	}

	public boolean isKhopChange() {
		return khopChange;
	}

	public void setKhopChange(boolean khopChange) {
		this.khopChange = khopChange;
	}


	
	

}
