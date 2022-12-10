package kHop.Util;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

class MR_Serialize implements java.io.Serializable
{
   public MR_Pattern Pattern;
   public Map<Integer,List<Map<Integer,List<Integer>>>> l1map_v2;
   public Map<String,Map<Integer,List<List<Map<Integer,Integer>>>>> l1vat;
   public Map<String,Map<Integer,List<List<Map<Integer,Integer>>>>> globalL1vat;
   public Map<Integer,List<List<Map<Integer,Integer>>>> llGlobalSupport;
   
   MR_Serialize()
   {
       l1vat = new HashMap();
       l1map_v2 = new HashMap();
   }
   
   public Map<String, Map<Integer, List<List<Map<Integer, Integer>>>>> getGlobalL1VAT() {
	   if (globalL1vat==null)
		   return l1vat;
		return globalL1vat;
	}
   
   public Map<Integer, List<List<Map<Integer, Integer>>>> getGlobalSupportList(){
	  return llGlobalSupport;
   }
   String getGlobalSupportString(String prefix){
	   Map<Integer, List<List<Map<Integer, Integer>>>> llGlobalSupport = getGlobalSupportList();
	   if (llGlobalSupport==null)
		   return "null";
	   Iterator itr = llGlobalSupport.keySet().iterator();
		Object Obj1 = new Object();
		Object Obj2 = new Object();
		Object Obj3 = new Object();
		StringBuffer sb = new StringBuffer();
		while(itr.hasNext())
		{
			Obj1 = itr.next();
				
			int tid = (Integer)Obj1;
			sb.append(prefix+"tid = "+tid);
			Iterator it = ((List)llGlobalSupport.get(Obj1)).iterator();
			while(it.hasNext())
			{
				Obj2 = it.next();
				Iterator it3 = ((List)Obj2).iterator();
				while(it3.hasNext())
				{
					Obj3 = it3.next();
					Iterator it1 = ((Map)Obj3).keySet().iterator();
					Obj1 = it1.next();
					int key = (Integer)Obj1;
					sb.append("(" + key + "," +(Integer)(((Map)Obj3).get(Obj1))+")");
				}	
				sb.append(":");
				
			}
			
			sb.append("  ");
		}
		return sb.toString(); 
   }
   
   public void print() {
	   print("");
   }
   public void print(String prefix) {
	System.out.println(prefix+"--- Pattern:");
	Pattern.print();

	System.out.println(prefix+"llGlobalSupport: "+getGlobalSupportString(prefix));
	
	//System.out.println("--- l1vat:\n"+getL1vatString(prefix));
	//System.out.println(prefix+"--- mapv2:\n"+getMapv2String(prefix));
}

   public void serialize(Appendable buff, long id) throws IOException{
	   //TODO:
	   try{
	   System.out.println("start printing");
	   buff.append("t #"+ id);
	   buff.append("\n");
	   MR_Pattern pattern  = this.Pattern;
	   
	   System.out.println("this pattern:");
	   this.Pattern.print();
	   
	   int nodeCount = pattern.v_ids.size();
	   Map<Integer,Integer> map_vid = new HashMap<Integer,Integer>(); // [0]:id moi, label cu
	   for(int i=0;i< nodeCount;i++)
	   {
		   int oldId = pattern.v_ids.get(i);
		   int label = pattern.getLabelById(oldId);
		   //buff.append("v "+i+" "+ label +" "+oldId);
		   buff.append("v "+i+" "+ label);
		   buff.append("\n");
		   map_vid.put(oldId,i);
	   }
	   for( int vidOld : pattern.v_ids){
		   int fromIdNew = map_vid.get(vidOld);
		   for(Map<Integer, Integer> map_adjList:pattern.get_adjlist_ids(vidOld)){
			   for ( Entry<Integer, Integer> en:map_adjList.entrySet()){
				   int toIdOld = en.getKey();
				   int toIdNew = map_vid.get(toIdOld);
				   //int toLabel = en.getValue();
				   int edgeLabel = pattern.getelabel(vidOld, toIdOld);
				   buff.append("e "+fromIdNew +" "+toIdNew+ " "+ edgeLabel);
				   buff.append("\n");
			   }
			   
		   }
	   }
		   
//	   for( int vidOld : pattern.v_ids)
//	   {
//		   int fromLabelOld = pattern.getLabelById(vidOld); // lay label cua first node o pattern cu.
//		   int fromId = id_label.get(fromLabelOld); // lay Id cua first node pattern moi
//		   System.out.println("adjList size: "+ pattern.get_adjlist_ids(vidOld).size());
//		   for(Map<Integer,Integer> adj : pattern.get_adjlist_ids(vidOld))
//		   {
//			   System.out.println("adjlist_ids:");
//			   for ( Entry<Integer, Integer> en:adj.entrySet()){
//				   int toIdOld = en.getKey();
//				   int label = en.getValue();
//				   
//				   System.out.println("from id, to Id" + fromId + "," +toIdOld);
//				   int toLabelOld = pattern.getLabelById(toIdOld);
//			       int toId = id_label.get(toLabelOld);
//				   int edgeLabel = pattern.getelabel(vidOld, toIdOld);
//				   buff.append("e "+fromId +" "+toId+ " "+ edgeLabel);
//				   buff.append("\n");
//			   }			   
//		   }
//		   
//	   }
	   }catch(Exception ex){
		   System.out.println("error: "+ex.getMessage());   
		   ex.printStackTrace(System.out);
	   }
	   System.out.println("end serialize");
   }
   
   
   public void serialize_EdgeFormat(Appendable buff) {
	 //TODO:
	   try{
	   System.out.println("start printing EdgeFormat");
	   
	   MR_Pattern pattern  = this.Pattern;
	   int nodeCount = pattern.v_ids.size();
	   for( int fromId : pattern.v_ids){
		   int fromLabel = pattern.getLabelById(fromId);
		   for(Map<Integer, Integer> map_adjList:pattern.get_adjlist_ids(fromId)){
			   for ( Entry<Integer, Integer> en:map_adjList.entrySet()){
				   int toId = en.getKey();
				   int toLabel = pattern.getLabelById(toId);
				   int edgeLabel = pattern.getelabel(fromId, toId);
				   buff.append(fromId +"_"+fromLabel+ " "+toId +"_"+toLabel+ " "+ edgeLabel);
				   buff.append("\n");
			   }
			   
		   }

	   }
	   }catch(Exception ex){
		   System.out.println("error: "+ex.getMessage());   
		   ex.printStackTrace(System.out);
	   }
	   System.out.println("end serialize");
	   }

   
   private int getNewIdFromLabel_serialize(Map<Integer, Integer> id_label, int label)
   {
	   return id_label.get(label);
   }
   
   private String getMapv2String(String prefix) {
	   StringBuffer sb = new StringBuffer();
		for(Entry<Integer, List<Map<Integer, List<Integer>>>> et:l1map_v2.entrySet()){
			sb.append(prefix+"(key: "+et.getKey()+"");
			for(Map<Integer, List<Integer>> map:et.getValue()){
				for(Entry<Integer, List<Integer>> et2:map.entrySet()){
					sb.append(prefix+"\n\t(key= "+et2.getKey()+"; [");
					for(Integer it:et2.getValue()){
						sb.append(it+" ");
					}
					sb.append("]");
				}
			}
			sb.append(")\n");
		}
		return sb.toString();
   }

   public String getL1vatString(){
	   return getL1vatString("", this.l1vat);
	}
   public String getGlobalL1VatString(){
	   if (getGlobalL1VAT()==null)
		   return "null";
	   return getL1vatString("", getGlobalL1VAT());
	}
   
	public String getL1vatString(String prefix,Map<String,Map<Integer,List<List<Map<Integer,Integer>>>>> l1vat) {		
		   StringBuffer sb = new StringBuffer();
			for(Entry<String, Map<Integer, List<List<Map<Integer, Integer>>>>> et:l1vat.entrySet()){
				sb.append(prefix+"(key: "+et.getKey()+"");
				for(Entry<Integer, List<List<Map<Integer, Integer>>>> et2:et.getValue().entrySet()){
					sb.append(prefix+"\n\t(key: "+et2.getKey());
					int i=0;
					for(List<Map<Integer, Integer>> item:et2.getValue()){				
						sb.append(prefix+"\n\t\tlist["+(i++)+"]: " );
						for(int j=0;j<item.size();j++){
							Map<Integer, Integer> it2 = item.get(j);
							for(Entry<Integer, Integer> et3:it2.entrySet()){
								sb.append("("+et3.getKey()+","+et3.getValue()+")" );
							}
						}
					}
					sb.append(prefix+"\n\t)");
				}
				sb.append(")\n");
			}
			return sb.toString();
	}

	//add localList to globalList without duplicate
	public static List<Integer> addUnion(List<Integer> globalList, List<Integer> localList) {
		for(int item:localList){
			if (!globalList.contains(item))
				globalList.add(item);			
		}
		return globalList;
	}
	
	public static Map<Integer, List<List<Map<Integer, Integer>>>> addUnion(
			Map<Integer, List<List<Map<Integer, Integer>>>> globalList,
			Map<Integer, List<List<Map<Integer, Integer>>>> localList) {
		for(Entry<Integer, List<List<Map<Integer, Integer>>>> lcEntry:localList.entrySet()){
			//int locGraphId = lcEntry.getKey();
			if (!globalList.containsKey(lcEntry.getKey())){
				//Add new entry
				globalList.put(lcEntry.getKey(), lcEntry.getValue());
			}else{
				//graphId is already in the globalList
				//List<List<Map<Integer, Integer>>> glEdges = globalList.get(locGraphId);
				//List<List<Map<Integer, Integer>>> locEdges = lcEntry.getValue();			
				System.out.println("graphId is already in the globalList");
			}
		}
		return globalList;
	}

	public static Map<String, Map<Integer, List<List<Map<Integer, Integer>>>>> unionL1VAT(
			Map<String, Map<Integer, List<List<Map<Integer, Integer>>>>> globalList,
			Map<String, Map<Integer, List<List<Map<Integer, Integer>>>>> localList) {
		boolean isNew = globalList.size()==0;
		
		for( Entry<String, Map<Integer, List<List<Map<Integer, Integer>>>>> lcEntry:localList.entrySet()){
			Map<Integer, List<List<Map<Integer, Integer>>>> glbGraphMap = globalList.get(lcEntry.getKey());
			if (glbGraphMap==null){
				if (!isNew){
					System.out.println("Warning: edgeId does not found");
				}
				glbGraphMap = new HashMap<>();
				globalList.put(lcEntry.getKey(), glbGraphMap);				
			}
			
			//add graphs from local to global
			Map<Integer, List<List<Map<Integer, Integer>>>> lcGraphMap = localList.get(lcEntry.getKey());
			
			for( Entry<Integer, List<List<Map<Integer, Integer>>>> lcGraphEntry:lcGraphMap.entrySet()){
				int graphId = lcGraphEntry.getKey();
				List<List<Map<Integer, Integer>>> glbEdges = glbGraphMap.get(graphId);
				if (glbEdges==null){
					List<List<Map<Integer, Integer>>> lcEdges = lcGraphEntry.getValue();
					//new graph
					glbGraphMap.put(graphId, lcEdges);
				}else{
					//graph existed => do nothing 						
					if (lcGraphEntry.getValue().size()!=glbEdges.size())
						System.out.println("Warning: two edge list have different size!");
				}
			}
		}
		return globalList;
	}
}

	

