package kHop.Util;
import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Map.Entry;
 
public class Util_functions {
 
	public Map<Integer, Integer> vid_to_lbl;
	public Map<String,Map<Integer,List<List<Map<Integer,Integer>>>>> l1vat;
	public Set<MR_Pattern> freq_pats_hadoop;
    public List<MR_Pattern> freq_pats;
	public List<Map<Integer,List<Map<Integer,List<Integer>>>>> l1map;
	public Map<Integer,List<Map<Integer,List<Integer>>>> l1map_v2;
	public int minsup;
	public Map<String, MR_Pattern> code_to_pat;
     Map<String,Integer> check_unique;
	public Util_functions(){
		freq_pats_hadoop = new HashSet();
        freq_pats = new ArrayList();
        check_unique = new HashMap();
	}
	public boolean isContains(Map<Integer,Integer> m, List<Map<Integer,Integer>> adj_list)
	{
		Iterator it = m.keySet().iterator();
		Object o = new Object();
		o = it.next();
		int key = ((Integer)(o)).intValue();
		int val = ((Integer)(m.get(o))).intValue();
		int key1=0;
		int value1=0;
		it = adj_list.iterator();
		while(it.hasNext())
		{
			o = it.next();
			Iterator it1 = (((Map)o).keySet()).iterator();	
			Object o1 = new Object();
			o1 = it1.next();			
			key1 = 	((Integer)(o1)).intValue();
			value1 = ((Integer)(((Map)o).get(o1))).intValue();
			if((key == key1)  && (val == value1))
				return true;	

		}
		return false;

	}
	public String test()
	{
		String s = "mine";
		return s;
	}
	public void get_neighbors(List<Map<Integer,Integer>> nb, int vlabel, List<Map<Integer,Integer>> adj_list)
	{
		
		Iterator it = (l1map_v2.keySet()).iterator();
		Object obj1 = new Object();
		Object obj2 = new Object();
		Object obj3 = new Object();
		
		Map<Integer,Integer> m1 = new HashMap();
		
		while(it.hasNext())
		{
				obj1 = it.next();
				
				if(vlabel != ((Integer)obj1).intValue())
				{
					continue;
				}
				Iterator it1 = ((List)(l1map_v2.get(obj1))).iterator();
				while(it1.hasNext())
				{
					obj2 = it1.next();
					Iterator it2 = ((Map)obj2).keySet().iterator();
					obj3 = it2.next();
					int nbid = (Integer)obj3;
					it2 = ((List)(((Map)obj2).get(obj3))).iterator();
					while(it2.hasNext())
					{
						Map<Integer,Integer> m = new HashMap();
						Object o = it2.next();
						m.put(nbid,((Integer)o));
						nb.add(m);
					}
				}
		}
	}
//	public void generate_candidate(MR_Pattern P)
//	{
//        backward_extension(P);
//		forward_extension(P);
//		return;
//	}
	
	public void generate_candidate(MR_Pattern P, MR_Serialize serial)
	{
        backward_extension(P, serial);
		forward_extension(P, serial);
		return;
	}

	public void backward_extension_old(MR_Pattern P)
	{
       
		if(P.right_most_path.size()<3)
			return;
		List<Integer> rv = new ArrayList();
		for(int i=0;i<P.right_most_path.size();i++)
			rv.add((Integer)P.right_most_path.get(i));

		List<Map<Integer,Integer>> adj_list = new ArrayList();
		List<Map<Integer,Integer>> nbrs = new ArrayList();

		
		Object obj1 = new Object();
		Object obj2 = new Object();

		int right_most_vertex = (Integer)((rv.get(rv.size()-1)));
		
		rv.remove(rv.size()-1);
		
		rv.remove(rv.size()-1);
		

		adj_list = P.get_adjlist(right_most_vertex);
		int vlabel = (Integer)(P.idtolabels.get(right_most_vertex));
		get_neighbors(nbrs,vlabel,adj_list);
		
		for(int i=0;i<rv.size();i++)
		{ 
			int flag=0;
			Iterator it = nbrs.iterator();
			int v2label_back = (Integer)P.idtolabels.get(rv.get(i));
			if(!(P.noEdgeexist(right_most_vertex,(Integer)(rv.get(i)))))
			{
				continue;
			}
		
				for(int j=0; j<nbrs.size();j++)
				{
					
					if((Integer)(((Map)(nbrs.get(j))).keySet().toArray()[0])==v2label_back)
					{
						int elabel = (Integer)(((Map)(nbrs.get(j))).get(v2label_back));
						MR_CannonicalCode cc  = null;
						if(v2label_back <= vlabel)
						{
							cc = new MR_CannonicalCode(1,2,v2label_back,elabel,vlabel);//check
						}
						else
						{
					 		cc = new MR_CannonicalCode(1,2,vlabel,elabel,v2label_back);//check
						}
						
						String cc_code = cc.getCan_code_();

						
						MR_Pattern p = (MR_Pattern)P.clone();
						p.addflesh_backextension(P,vlabel,elabel,v2label_back,right_most_vertex,(Integer)(rv.get(i)));
						
						if(!p.check_isomorphism())
						{
							continue;
							
						}

						
						p.vat_intersection(P,l1vat.get(cc_code));
						
						if(p.support > 0 && !check_unique.containsKey(p.getCan_code()))
						{
							freq_pats_hadoop.add(p);
                            check_unique.put(p.getCan_code(),1);
							
						}
					}
				}

		}
	}
	
	public void backward_extension(MR_Pattern P, MR_Serialize serial)
	{
       
		if(P.right_most_path.size()<3)
			return;
		//System.out.println("backward_extension -1");
		List<Integer> rv = new ArrayList();
		for(int i=0;i<P.right_most_path.size();i++)
			rv.add((Integer)P.right_most_path.get(i));

		List<Map<Integer,Integer>> adj_list = new ArrayList();
		List<Map<Integer,Integer>> nbrs = new ArrayList();

		
		Object obj1 = new Object();
		Object obj2 = new Object();

		int right_most_vertex = (Integer)((rv.get(rv.size()-1)));
		
		rv.remove(rv.size()-1);
		
		rv.remove(rv.size()-1);
		

		adj_list = P.get_adjlist(right_most_vertex);
		int vlabel = (Integer)(P.idtolabels.get(right_most_vertex));
		get_neighbors(nbrs,vlabel,adj_list);
		//System.out.println("backward_extension");
		for(int i=0;i<rv.size();i++)
		{ 
			int flag=0;
			Iterator it = nbrs.iterator();
			int v2label_back = (Integer)P.idtolabels.get(rv.get(i));
			if(!(P.noEdgeexist(right_most_vertex,(Integer)(rv.get(i)))))
			{
				continue;
			}
			//System.out.println("backward_extension_0");
			for(int j=0; j<nbrs.size();j++){				
				if((Integer)(((Map)(nbrs.get(j))).keySet().toArray()[0])==v2label_back){
					//System.out.println("backward_extension_1");
					int elabel = (Integer)(((Map)(nbrs.get(j))).get(v2label_back));
					MR_CannonicalCode cc  = null;
					if(v2label_back <= vlabel)
					{
						cc = new MR_CannonicalCode(1,2,v2label_back,elabel,vlabel);//check
					}
					else
					{
				 		cc = new MR_CannonicalCode(1,2,vlabel,elabel,v2label_back);//check
					}
					
					String cc_code = cc.getCan_code_();
					
					List<Integer> supportList = getMaxSupportList(serial, cc_code);
					//System.out.println("maxFreq:" + supportList.size());
					if(supportList.size()<minsup)
						break;
					
					MR_Pattern p = (MR_Pattern)P.clone();
					
					p.addflesh_backextension(P,vlabel,elabel,v2label_back,right_most_vertex,(Integer)(rv.get(i)));
					
					if(!p.check_isomorphism())
					{
						continue;
						
					}

					
					p.vat_intersection(P,l1vat.get(cc_code));
					
					if(p.support > 0 && !check_unique.containsKey(p.getCan_code()))
					{
						freq_pats_hadoop.add(p);
                        check_unique.put(p.getCan_code(),1);
						
					}
				}
			}
		}
		//System.out.println("backward_extension exit");
	}

	public void forward_extension_old(MR_Pattern P)
	{
       
		List<Integer> rv = new ArrayList();
	
		for(int i=0;i<P.right_most_path.size();i++)
			rv.add((Integer)P.right_most_path.toArray()[i]);
		
		Collections.sort(rv, Collections.reverseOrder());
		
		Iterator it = rv.iterator();
		Object extensionpoint = new Object();
		Object nbrspoint = new Object();
		Object obj3 = new Object();
		List<Map<Integer,Integer>> nbrs = new ArrayList();
		List<Map<Integer,Integer>> adj_list = new ArrayList();
		int rmpsize = rv.size();
		int el=0;;
		int prevvlabel = 0;
		
		while(it.hasNext())
		{
			extensionpoint = it.next();
			
			adj_list = P.get_adjlist((Integer)extensionpoint);
			nbrs.clear();
			int vlabel = ((Integer)(P.idtolabels.get(extensionpoint))).intValue();
			get_neighbors(nbrs,vlabel,adj_list);
			Iterator it1 = nbrs.iterator();
	
			while(it1.hasNext())
			{
				obj3 = it1.next();
				Iterator it2 = ((Map)obj3).keySet().iterator();
				nbrspoint = it2.next();
				int added_vlabel = (Integer)nbrspoint;
				int elabel = ((Integer)(((Map)obj3).get(nbrspoint))).intValue();
				int lastvid = P.get_lastvid();
				MR_CannonicalCode cc  = null;
				if(vlabel <= added_vlabel)
				{
					cc = new MR_CannonicalCode(1,2,vlabel,elabel,added_vlabel);//check
				}
				else
				{
					 cc = new MR_CannonicalCode(1,2,added_vlabel,elabel,vlabel);//check
				}
				String cc_code = cc.getCan_code_();
				MR_Pattern p = (MR_Pattern)P.clone();
				
				
				// cho ni
				
				/*
			        * IntersecedSupportList inteSupport = new emptyList
			        * foreach edge e in P
			        * 	supportList_e = getSupportList(e);
			        * 	interSupport = intersect(interSuport, supportList_e)
			        * endfor
			        * 
			        * return interSupport.size();
			        */
					//pat.vat
				
				
				////

				p.addflesh(P,vlabel,elabel,added_vlabel,(Integer)extensionpoint,lastvid+1,minsup);
				
				
				if(!p.check_isomorphism())
				{
					continue;
				}

				p.vat_intersection(P,l1vat.get(cc_code));

				if(p.support > 0 && !check_unique.containsKey(p.getCan_code()))
				{
					freq_pats_hadoop.add(p);
                    check_unique.put(p.getCan_code(),1);
				}	
				
			}

		}
	}
	
	public void forward_extension(MR_Pattern P, MR_Serialize serial)
	{
       
		List<Integer> rv = new ArrayList();
	
		for(int i=0;i<P.right_most_path.size();i++)
			rv.add((Integer)P.right_most_path.toArray()[i]);
		
		Collections.sort(rv, Collections.reverseOrder());
		
		Iterator it = rv.iterator();
		Object extensionpoint = new Object();
		Object nbrspoint = new Object();
		Object obj3 = new Object();
		List<Map<Integer,Integer>> nbrs = new ArrayList();
		List<Map<Integer,Integer>> adj_list = new ArrayList();
		int rmpsize = rv.size();
		int el=0;;
		int prevvlabel = 0;
		while(it.hasNext())
		{
			extensionpoint = it.next();
			
			adj_list = P.get_adjlist((Integer)extensionpoint);
			nbrs.clear();
			int vlabel = ((Integer)(P.idtolabels.get(extensionpoint))).intValue();
			get_neighbors(nbrs,vlabel,adj_list);
			Iterator it1 = nbrs.iterator();
			
			while(it1.hasNext())
			{
				obj3 = it1.next();
				Iterator it2 = ((Map)obj3).keySet().iterator();
				nbrspoint = it2.next();
				int added_vlabel = (Integer)nbrspoint;
				int elabel = ((Integer)(((Map)obj3).get(nbrspoint))).intValue();
				int lastvid = P.get_lastvid();
				MR_CannonicalCode cc  = null;
				if(vlabel <= added_vlabel)
				{
					cc = new MR_CannonicalCode(1,2,vlabel,elabel,added_vlabel);//check
				}
				else
				{
					 cc = new MR_CannonicalCode(1,2,added_vlabel,elabel,vlabel);//check
				}
				String cc_code = cc.getCan_code_();
				MR_Pattern p = (MR_Pattern)P.clone();
				
				List<Integer> supportList = getMaxSupportList(serial, cc_code);
				//System.out.println("maxFreq:" + supportList.size());
				if(supportList.size()<minsup)
					break;

				p.addflesh(P,vlabel,elabel,added_vlabel,(Integer)extensionpoint,lastvid+1,minsup);
				
				
				if(!p.check_isomorphism())
				{
					continue;
				}

				p.vat_intersection(P,l1vat.get(cc_code));

				if(p.support > 0 && !check_unique.containsKey(p.getCan_code()))
				{
					freq_pats_hadoop.add(p);
                    check_unique.put(p.getCan_code(),1);
				}	
				
			}

		}
	}
	private List<Integer> getMaxSupportList(MR_Serialize serial, String cc_code) {
		List<Integer> supportList = new ArrayList<>();					
		if (serial.getGlobalL1VAT()!=null){
			Map<Integer, List<List<Map<Integer, Integer>>>> graphMap = serial.getGlobalL1VAT().get(cc_code);
			if (graphMap!=null){						
				//intersect two list
				for(Integer graphId:serial.llGlobalSupport.keySet()){
					if (graphMap.containsKey(graphId))
						if (! supportList.contains(graphId))
							supportList.add(graphId);
				}			
			}
		}
		return supportList;
	}
	
	public void getMaxFrequent(MR_Pattern pat,MR_Serialize serial)
	{
       /*
        * IntersecedSupportList inteSupport = new emptyList
        * foreach edge e in P
        * 	supportList_e = getSupportList(e);
        * 	interSupport = intersect(interSuport, supportList_e)
        * endfor
        * 
        * return interSupport.size();
        */
		//pat.vat
	}
	
	public void readDB_VAT_L1map(BufferedReader currentReader) {
 
				vid_to_lbl = new HashMap();
				l1vat = new HashMap();
				freq_pats = new ArrayList();
				l1map = new ArrayList();
				
				l1map_v2 = new HashMap();
				code_to_pat = new HashMap();
				
				String line;
 				
				try {
				line = currentReader.readLine();
				
				String[] temp  = line.split(" ");
				int tid=0;
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
								vid_to_lbl.put(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
							}

							if(temp[0].equals("e"))
							{
								MR_Pattern g1 = new MR_Pattern();
								int v1id = Integer.parseInt(temp[1]);
								int v2id = Integer.parseInt(temp[2]);
								int v1label = vid_to_lbl.get(v1id);
								int v2label = vid_to_lbl.get(v2id);
								if(v1label <= v2label)
								{
									g1 = new MR_Pattern(v1label,v2label,Integer.parseInt(temp[3]));

								}
								else
								{
									g1 = new MR_Pattern(v2label,v1label,Integer.parseInt(temp[3]));
									int temp2 = v1id;
									v1id = v2id;
									v2id = temp2;
								}	

								if(!l1vat.containsKey(g1.getCan_code()))
								{
									Map<Integer,Integer> m1 = new HashMap();
									m1.put(v1id,v2id);
									MR_Pattern g = new MR_Pattern();
									List<Map<Integer,Integer>> l = new ArrayList();
									List<List<Map<Integer,Integer>>> l2 = new ArrayList();
									l.add(m1);
									l2.add(l);
									List<Map<Integer,List<List<Map<Integer,Integer>>>>> l1 = new ArrayList();
									Map<Integer,List<List<Map<Integer,Integer>>>> m2 = new HashMap();
									m2.put(tid,l2);
					
									
									l1vat.put(g1.getCan_code(), m2);
									
									freq_pats.add(g1);
									code_to_pat.put(g1.getCan_code(),g1);
									g1.get_to_insert(tid,v1id,v2id);
							
								}
								else if(l1vat.containsKey(g1.getCan_code()))
								{
									
									int flag2=0;
									if(((Map)(l1vat.get(g1.getCan_code()))).containsKey(tid))
									{
										flag2 = 1;
									}
								
									if(flag2==1)
									{
										Map<Integer,Integer> m1 = new HashMap();
										m1.put(v1id,v2id);
	
										Iterator it1 = ((List)(((Map)(l1vat.get(g1.getCan_code()))).get(tid))).iterator();
										((List)(it1.next())).add(m1);
									
										((MR_Pattern)(code_to_pat.get(g1.getCan_code()))).insert_vid_hs(tid, v1id, v2id);
			
									}
									if(flag2==0)
									{
										Map<Integer,Integer> m1 = new HashMap();
										m1.put(v1id,v2id);
										List<Map<Integer,Integer>> l = new ArrayList();
										List<List<Map<Integer,Integer>>> l1 = new ArrayList();
										l.add(m1);
										l1.add(l);
										Map<Integer,List<List<Map<Integer,Integer>>>> m2 = new HashMap();
										m2.put(tid,l1);
										
										l1vat.get(g1.getCan_code()).put(tid,l1);
										
									
										((MR_Pattern)(code_to_pat.get(g1.getCan_code()))).get_to_insert(tid, v1id, v2id);

									}
									
								}
							}
							
							if(flag==1)
								break;
							
						}

					}
					
					if(flag==1)
					{
						break;
					}
				}
 
				}
				catch (IOException e) {
    			}
				Iterator itr = freq_pats.iterator();
				
				
				while(itr.hasNext())
				{
					MR_Pattern t = ((MR_Pattern)itr.next());
					t.support = t.vat.size();
					
				}
			
				itr = freq_pats.iterator();
				
				while(itr.hasNext())
				{
					MR_Pattern t = ((MR_Pattern)itr.next());
					int srcl = t.idtolabels.get(1);
					int destl = t.idtolabels.get(2);
					int el = ((MR_CannonicalCode)(t.can_cod.toArray()[0])).get_elabel();
					int iter=1;
					int temp=0;
					if(srcl != destl){iter=2;}
					while(iter>0)
					{

						
						if(l1map_v2.size()==0)
						{
							List<Integer> l1 = new ArrayList();
							l1.add(el);
							Map<Integer,List<Integer>> m1 = new HashMap();
							m1.put(destl,l1);
							List<Map<Integer,List<Integer>>> l2 = new ArrayList();
							l2.add(m1);
							Map<Integer,List<Map<Integer,List<Integer>>>> m3 = new HashMap();
							
							l1map_v2.put(srcl,l2);
							if(srcl != destl)
							{
								List<Integer> l11 = new ArrayList();
								l11.add(el);
								Map<Integer,List<Integer>> m11 = new HashMap();
								m11.put(srcl,l11);
								List<Map<Integer,List<Integer>>> l21 = new ArrayList();
								l21.add(m11);
								Map<Integer,List<Map<Integer,List<Integer>>>> m31 = new HashMap();
								
								l1map_v2.put(destl,l21);
							}
							iter--;
							continue;
							
						}
						int flag1=0;
						int flag11=0;
						Object Obj1 = new Object();
						Object Obj2 = new Object();
							
						if(l1map_v2.containsKey(srcl))
						{
							flag1=1;
						}

					
						if(flag1==1)
						{
							List<Map<Integer,List<Integer>>> l2 = new ArrayList();
							
							l2 = (List)l1map_v2.get(srcl);
							Iterator itr2 = l2.iterator();
							int flag2=0;
							while(itr2.hasNext())
							{
								Obj2 = itr2.next();
								if(((Map)Obj2).containsKey(destl))
								{
									flag2=1;
									break;
								}
							}
							if(flag2==1)
							{
								((List)((Map)Obj2).get(destl)).add(el);

							}
							if(flag2==0)
							{
								Map<Integer,List<Integer>> m2 = new HashMap();
								List<Integer> l1 = new ArrayList();
								l1.add(el);
								m2.put(destl,l1);
							
								((List)(l1map_v2.get(srcl))).add(m2);

							}

						}
						if(flag1==0)
						{
							List<Integer> l1 = new ArrayList();
							l1.add(el);
							Map<Integer,List<Integer>> m1 = new HashMap();
							m1.put(destl,l1);
							List<Map<Integer,List<Integer>>> l2 = new ArrayList();
							l2.add(m1);
							Map<Integer,List<Map<Integer,List<Integer>>>> m3 = new HashMap();
						
							l1map_v2.put(srcl,l2);

						}	
						iter--;
						if(iter==1)
						{
							temp = srcl;
							srcl= destl;
							destl = temp;
						}
					}
					
				}
	}
	public void update_l1vat()
	{
		Iterator itr = freq_pats.iterator();
		Object obj = new Object();
		int v1label = 0;
		int v2label = 0;
		int elabel = 0;
		Map<Integer,List<Map<Integer,List<Integer>>>> l1map_temp = new HashMap();
		while(itr.hasNext())
		{
			
			obj = itr.next();
			
			if(((MR_Pattern)obj).vat.size() < minsup)
			{
				l1vat.remove(((MR_Pattern)obj).getCan_code());
				v1label = ((MR_CannonicalCode)(((MR_Pattern)obj).can_cod.toArray()[0])).v1label;
				v2label = ((MR_CannonicalCode)(((MR_Pattern)obj).can_cod.toArray()[0])).v2label;
				elabel = ((MR_CannonicalCode)(((MR_Pattern)obj).can_cod.toArray()[0])).elabel;
				
				Iterator it = ((List)(l1map_v2.get(v1label))).iterator();
				while(it.hasNext())
				{
					Object obj1 = it.next();
					int v2 = (Integer)(((Map)obj1).keySet().toArray()[0]);
					List<Integer> l1 = (List)(((Map)obj1).get(v2));
					if(v2 == v2label && l1.size()==1)
					{
						((List)(l1map_v2.get(v1label))).remove(obj1);
						break;
					}
					if(v2 == v2label && l1.size()>1)
					{
						for(int i=0; i< l1.size();i++)
						{
							if((Integer)(l1.toArray()[i]) == elabel)
							{
								l1.remove(i);
								break;
							}
						}
						
						break;
					}
				}
				if(v1label != v2label)
				{
					
					
					it = ((List)(l1map_v2.get(v2label))).iterator();
					while(it.hasNext())
					{
						Object obj1 = it.next();
						int v1 = (Integer)(((Map)obj1).keySet().toArray()[0]);
						List<Integer> l1 = (List)(((Map)obj1).get(v1));
						if(v1 == v1label && l1.size()==1)
						{
							
							((List)(l1map_v2.get(v2label))).remove(obj1);
							break;
						}
						if(v1 == v1label && l1.size()>1)
						{
							for(int i=0; i< l1.size();i++)
							{
								if((Integer)(l1.toArray()[i]) == elabel)
								{
									l1.remove(i);
									break;
								}
							}
							break;
						}	
					}

				}
				itr.remove();
		
			}
		}
	
	}

	public void printl1map()
	{
		Iterator itr = l1map_v2.keySet().iterator();
		Object Obj1 = new Object();
		Object Obj2 = new Object();
		while(itr.hasNext())
		{
			Obj1 = itr.next();
			int key = ((Integer)Obj1);
			System.out.print("src = "+ key);
			Iterator itr4 = ((List)l1map_v2.get(Obj1)).iterator();
			while(itr4.hasNext())
			{
				Obj1 = itr4.next();
				Iterator itr2 = ((Map)Obj1).keySet().iterator();
				Obj2 = itr2.next();
				key = ((Integer)Obj2);
				System.out.print(" dest = "+ key);
				Iterator itr3 = ((List)((Map)Obj1).get(Obj2)).iterator();
				System.out.print("label = (");
				while(itr3.hasNext())
				{
					System.out.print(itr3.next()+",");
				}
				
				System.out.print(")--");
			}
			System.out.print("\n");
		}
	}
/*	public String get_l1map_stringtize()
	{
		//itr = l1map.iterator();
			String str = new String();
			Iterator itr = l1map_v2.keySet().iterator();
			
			Object Obj1 = new Object();
			Object Obj2 = new Object();
			while(itr.hasNext())
			{
				Obj1 = itr.next();
				//Iterator itr1 = ((Map)Obj1).keySet().iterator();
				//Obj2 = itr1.next();
				int key = ((Integer)Obj1);
//				//System.out.print("src = "+ key);
				str = str + Integer.toString(key)+"-";
				Iterator itr4 = ((List)l1map_v2.get(Obj1)).iterator();
				while(itr4.hasNext())
				{
					Obj1 = itr4.next();
					Iterator itr2 = ((Map)Obj1).keySet().iterator();
					Obj2 = itr2.next();
					key = ((Integer)Obj2);
//					//System.out.print(" dest = "+ key);
					Iterator itr3 = ((List)((Map)Obj1).get(Obj2)).iterator();
//					//System.out.print("label = (");
					str = str + Integer.toString(key) + ",";
					while(itr3.hasNext())
					{
						str = str + Integer.toString((Integer)itr3.next()) + ",";
						////System.out.print(itr3.next()+",");
					}
					str = str + ":";
					////System.out.print(")--");
				}
				str = str + "]";
				////System.out.print("\n");
			}		
			return str;
	}*/
/*	String serialized_vat_of_pattern1(String pattern)
	{
		Iterator itr = ((Map)(l1vat.get(pattern))).keySet().iterator();
		String str= new String();
		Object Obj1 = new Object();
		Object Obj2 = new Object();
		Object Obj3 = new Object();
		////System.out.print(pattern);
			////System.out.print("-->");
		while(itr.hasNext())
		{
			Obj1 = itr.next();
			
			int tid = (Integer)Obj1;
			////System.out.print(tid);
			str = str + Integer.toString(tid)+",";
			Iterator it = ((List)(((Map)(l1vat.get(pattern))).get(Obj1))).iterator();
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
					str = str + Integer.toString(key) +","+ (Integer)(((Map)Obj3).get(Obj1)) + ",";
					////System.out.print("(" + key + "," +(Integer)(((Map)Obj3).get(Obj1))+")");
				}	
				str = str + ":";
					////System.out.print(":");
				
			}
			str = str + "]";
				////System.out.print("  ");
		}
		return str;
	}*/
/*	public String get_value_for_reducer1(String pattern1)
	{
		String str= new String();
		Iterator itr1 = l1vat.keySet().iterator();
		int support = ((Map)(l1vat.get(pattern1))).keySet().size();
		while(itr1.hasNext())
		{
			Object obj = itr1.next();
			String pattern = (String)obj;
			str = str + pattern +"-";	
			Iterator itr = ((Map)(l1vat.get(pattern))).keySet().iterator();
			
			Object Obj1 = new Object();
			Object Obj2 = new Object();
			Object Obj3 = new Object();
			////System.out.print(pattern);
			////System.out.print("-->");
			while(itr.hasNext())
			{
				Obj1 = itr.next();
				
				int tid = (Integer)Obj1;
				////System.out.print(tid);
				str = str + Integer.toString(tid)+",";
				Iterator it = ((List)(((Map)(l1vat.get(pattern))).get(Obj1))).iterator();
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
						str = str + Integer.toString(key) +","+ (Integer)(((Map)Obj3).get(Obj1)) + ",";
						////System.out.print("(" + key + "," +(Integer)(((Map)Obj3).get(Obj1))+")");
					}	
					str = str + ":";
					////System.out.print(":");
				
				}
				
				////System.out.print("  ");
			}
			str = str + "]";		
		}
		str = str + "__" + Integer.toString(support); //index 1
		//System.out.println(str);
		str = str +"__" +get_l1map_stringtize(); //index 2
		//add rmp of the current pattern which is an edge.
		str = str + "__" + "1,2"; //index 3
		str = str + "__" +serialized_vat_of_pattern1(pattern1); //index 4
		str = str + "__" +code_to_pat.get(pattern1).serialized_vset(); //index 5
		return str;
	}*/
    

/*	public String get_value_for_reducer1(MR_Pattern P)
	{
		String str= new String();
		Iterator itr1 = l1vat.keySet().iterator();
		//int support = ((Map)(l1vat.get(pattern1))).keySet().size();
		while(itr1.hasNext())
		{
			System.out.println("SE");
			Object obj = itr1.next();
			String pattern = (String)obj;
			str = str + pattern +"-";	
			Iterator itr = ((Map)(l1vat.get(pattern))).keySet().iterator();
			
			Object Obj1 = new Object();
			Object Obj2 = new Object();
			Object Obj3 = new Object();
			////System.out.print(pattern);
			////System.out.print("-->");
			while(itr.hasNext())
			{
				Obj1 = itr.next();
				
				int tid = (Integer)Obj1;
				////System.out.print(tid);
				str = str + Integer.toString(tid)+",";
				Iterator it = ((List)(((Map)(l1vat.get(pattern))).get(Obj1))).iterator();
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
						str = str + Integer.toString(key) +","+ (Integer)(((Map)Obj3).get(Obj1)) + ",";
						////System.out.print("(" + key + "," +(Integer)(((Map)Obj3).get(Obj1))+")");
					}	
					str = str + ":";
					////System.out.print(":");
				
				}
				
				////System.out.print("  ");
			}
			str = str + "]";		
		}
		str = str + "__" + Integer.toString(P.support);
		//System.out.println(str);
		str = str +"__" +get_l1map_stringtize();
		//add rmp of the current pattern which is an edge.
		str = str + "__";
		int i=0;
		for(i=0; i < P.right_most_path.size()-1; i++)
		{
			str = str + Integer.toString(P.right_most_path.get(i))+",";
		}
		str = str + Integer.toString(P.right_most_path.get(i));
		str = str + "__" +P.serialized_vat_of_P();
		str = str + "__" +P.serialized_vset();
		return str;
	}*/
	//Map<Integer,List<Map<Integer,List<Integer>>>> l1map_v2;;
/*	public void deserialize_l1map(String l1map_serialize)	
	{
		l1map_v2 = new HashMap();
		String [] map = l1map_serialize.split(":]");
		for(int i=0; i < map.length; i++)
		{
			////System.out.println(map[i]);
			String [] temp = map[i].split("-");
			int vlabel = Integer.parseInt(temp[0]);
			String [] temp2 = temp[1].split(":");
			List<Map<Integer,List<Integer>>> l = new ArrayList();
			for(int j=0; j< temp2.length ; j++)
			{
				
				Map<Integer,List<Integer>> m = new HashMap();
				List<Integer> l1 = new ArrayList();
				String[] temp3 = temp2[j].split(",");
				for(int k=1; k < temp3.length; k++)
				{
					l1.add(Integer.parseInt(temp3[k]));
				}
				m.put(Integer.parseInt(temp3[0]),l1);
				l.add(m);				
			}			
			l1map_v2.put(vlabel,l);
		}
//		printl1map();
	}*/
	
	void printl1vat()
	{
		Iterator itr1 = l1vat.keySet().iterator();

		while(itr1.hasNext())
		{
			Object obj = itr1.next();
			String pattern = (String)obj;
			Iterator itr = ((Map)(l1vat.get(pattern))).keySet().iterator();
			
			Object Obj1 = new Object();
			Object Obj2 = new Object();
			Object Obj3 = new Object();
			System.out.print(pattern);
			System.out.print("-->");
			while(itr.hasNext())
			{
				Obj1 = itr.next();
				
				int tid = (Integer)Obj1;
				System.out.print(tid);
				System.out.print("-");
				Iterator it = ((List)(((Map)(l1vat.get(pattern))).get(Obj1))).iterator();
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
						System.out.print("(" + key + "," +(Integer)(((Map)Obj3).get(Obj1))+")");
					}	
					
					System.out.print(":");
				
				}
				
				System.out.print("  ");
			}
			System.out.print("\n");
		}
		
	}
	
/*	public void deserialize_l1vat(String l1vat_serialize)
	{
		l1vat = new HashMap();
		String[] vat = l1vat_serialize.split(":]");
		for(int i=0; i < vat.length; i++)
		{
			String [] temp = vat[i].split("-");
			String [] temp2 = temp[1].split(":");
			Map<Integer,List<List<Map<Integer,Integer>>>> m = new HashMap();
			for(int j=0; j < temp2.length; j++)
			{
				List<List<Map<Integer,Integer>>> l = new ArrayList();
				List<Map<Integer,Integer>> l1 = new ArrayList();
				String[] temp3 = temp2[j].split(",");
				for(int k=1 ; k< temp3.length; k=k+2)
				{
					Map<Integer,Integer> m1 = new HashMap();
					m1.put(Integer.parseInt(temp3[k]),Integer.parseInt(temp3[k+1]));
					l1.add(m1);
				}
				l.add(l1);
				m.put(Integer.parseInt(temp3[0]), l);
		
			}
			l1vat.put(temp[0],m);

		}
//		printl1vat();
	}*/
}

