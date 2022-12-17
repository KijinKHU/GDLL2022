package kHop;



import java.io.Serializable;
import java.util.List;
import java.util.Map;


public class Node_information implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private Node self;
	private Self_Inf self;
	//private MR_Pattern pattern;
	private List<Edge_Inf> outEdge;
	
	private Map<Integer,List<Edge_Inf>> k_inEdge;
	
	public Node_information(Self_Inf self,List<Edge_Inf> outEdge)
	{
		this.setSelf(self);
		this.setOutEdge(outEdge);
	}

	public Self_Inf getSelf() {
		return self;
	}

	public void setSelf(Self_Inf self) {
		this.self = self;
	}


	public List<Edge_Inf> getOutEdge() {
		return outEdge;
	}

	public void setOutEdge(List<Edge_Inf> outEdge) {
		this.outEdge = outEdge;
	}
	
	public void printInfor()
	{
		System.out.println("Key: "+ self.getNode().getId());
		/*
		System.out.println("inEdge: ");
		for(Edge_Inf edge: getInEdge())
		{
			System.out.println("NodeA: "+ edge.getNodeA().getId() + "NodeB: "+ edge.getNodeB().getId());
		}
		System.out.println("outEdge: ");
		for(Edge_Inf edge: getOutEdge())
		{
			System.out.println("NodeA: "+ edge.getNodeA().getId() + "NodeB: "+ edge.getNodeB().getId());
		}
		
		System.out.println("khop: ");
		for(Edge_Inf edge: self.getKhop())
		{
			System.out.println("NodeA: "+ edge.getNodeA().getId() + "NodeB: "+ edge.getNodeB().getId());
		}
		*/
		
		System.out.println("outEdge: ");
		for(Edge_Inf edge: getOutEdge())
		{
			System.out.println("NodeA: "+ edge.getNodeA().getId() + "NodeB: "+ edge.getNodeB().getId());
		}
		
		
		System.out.println("k_inEdge: ");
		
		for (Map.Entry<Integer,List<Edge_Inf>>  entry :k_inEdge.entrySet())
		{
            System.out.println("k_inEdge: at k: " + entry.getKey());
            if(entry.getValue()!=null)
				for(Edge_Inf edge: entry.getValue())
				{
					System.out.println("NodeA: "+ edge.getNodeA().getId() + "NodeB: "+ edge.getNodeB().getId());
				}
		}
		
				
		System.out.println("k_khop: ");
		
		for (Map.Entry<Integer,List<Edge_Inf>>  entry :self.getK_hop().entrySet())
		{
            System.out.println("k_hop: at k: " + entry.getKey());
            if(entry.getValue()!=null)
				for(Edge_Inf edge: entry.getValue())
				{
					System.out.println("NodeA: "+ edge.getNodeA().getId() + "NodeB: "+ edge.getNodeB().getId());
				}
		}
		
	
	}

	public Map<Integer,List<Edge_Inf>> getK_inEdge() {
		return k_inEdge;
	}

	public void setK_inEdge(Map<Integer,List<Edge_Inf>> k_inEdge) {
		this.k_inEdge = k_inEdge;
	}


	public String textToPrint(int k) {
		// TODO Auto-generated method stub
		String text;
		text = String.valueOf(self.getNode().getId());
		List<Edge_Inf> edgeList = self.getK_hop().get(k);
		if(edgeList!=null && edgeList.size()>0)
		{
			for(int i=0; i<edgeList.size();i++)
			{
				text +=",";
				text +=edgeList.get(i).getNodeA().getId() + "-" + edgeList.get(i).getEdgeID()+"-"+edgeList.get(i).getNodeB().getId();
				
			}
		}
		
		return text;
	}


	
	

}
