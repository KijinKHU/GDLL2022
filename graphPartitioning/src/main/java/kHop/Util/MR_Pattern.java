package kHop.Util;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.*;

public class MR_Pattern implements Cloneable, java.io.Serializable{
   
    public Map<Integer, List<Map<Integer,Integer>>> adj_list;
	public List<MR_CannonicalCode> can_cod;
	public List<Integer> v_ids;
	public Map<Integer,Integer> idtolabels;
	
	public Map<Integer,List<List<Map<Integer,Integer>>>> vat;
	public Map<Integer,List<Set<Integer>>> Vsets;
	public List<Integer> right_most_path;
	public int support;
	public boolean iscannonical;
	MR_CannonicalCode c;
	public MR_Pattern(){
	 	v_ids = new ArrayList<Integer>();
		can_cod = new ArrayList();
        adj_list = new HashMap();
		right_most_path = new ArrayList<Integer>();
		support=0;
		vat = new HashMap<Integer,List<List<Map<Integer,Integer>>>>();
		Vsets = new HashMap();
		idtolabels = new HashMap<Integer, Integer>();
		c = new MR_CannonicalCode();
	}
	public String test()
	{
		String s = "mine";
		return s;
	}
	
	public int getLabelById(int vid)
	{
		return idtolabels.get(vid);
	}

	public String getCan_code()
	{
		String cc = ((MR_CannonicalCode)(can_cod.toArray()[0])).getCan_code_();
		for(int i=1; i<can_cod.size(); i++)
		{
			cc = cc + ":"+((MR_CannonicalCode)(can_cod.toArray()[i])).getCan_code_();
		}
		return cc;
	
	}
	public void print_rmp()
	{
		for(int i=0; i<this.right_most_path.size();i++)
		{
			System.out.print("("+  (Integer)this.right_most_path.toArray()[i] +")");	
		}
		System.out.print("\n");
	}
	public int get_lastvid()
	{
		return (Integer)(v_ids.toArray()[v_ids.size()-1]);
	}
	
/*	public String serialized_vat_of_P()
	{
		Iterator itr = this.vat.keySet().iterator();
		String str= new String();
		Object Obj1 = new Object();
		Object Obj2 = new Object();
		Object Obj3 = new Object();
		
		while(itr.hasNext())
		{
			Obj1 = itr.next();
			
			int tid = (Integer)Obj1;
			//System.out.print(tid);
			str = str + Integer.toString(tid)+",";
			Iterator it = ((List)((this.vat).get(Obj1))).iterator();
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
				}	
				str = str + ":";
				
			}
			str = str + "]";
				
		}
		return str;
	}*/
	/*public String serialized_vset()
	{
		Set<Integer> s = (Set)(this.Vsets.keySet());
		String str = new String();
	
		for(int i=0; i< s.size();i++)
		{
			int tid = (Integer)(s.toArray()[i]);
			List<Set<Integer>> l = (List)(this.Vsets.get(tid));
		
			str = str + Integer.toString(tid) + ",";
			for(int j=0; j< l.size();j++)
			{
				Set<Integer> s1 = (Set)(l.toArray()[j]);
		
				for(int k=0; k<s1.size();k++)
				{
					str = str + Integer.toString((Integer)(s1.toArray()[k])) + ",";
				}
				str = str + ":";

			}
			str = str + "]";
		}
		return str;
	}*/
/*	public int deserialize_patten_object(String cancode, String rmp, String str_vat, String str_vset)
	{
		
		String[] codes = cancode.split(";");
		int v1,v2,l1,e,l2;
		for(int i=0; i < codes.length ; i++)
		{
	
			String[] more_split = codes[i].split("_");
			v1 = Integer.parseInt(more_split[0]);
			v2 = Integer.parseInt(more_split[1]);
			l1 = Integer.parseInt(more_split[2]);
			e = Integer.parseInt(more_split[3]);
			l2 = Integer.parseInt(more_split[4]);
			MR_CannonicalCode c = new MR_CannonicalCode(v1,v2,l1,e,l2);
			this.can_cod.add(c);
			if(!this.idtolabels.containsKey(v1))
			{
				this.idtolabels.put(v1,l1);
			}
			if(!this.idtolabels.containsKey(v2))
			{
				this.idtolabels.put(v2,l2);
			}
			if(!this.adj_list.containsKey(v1))
			{
				List<Map<Integer,Integer>> l = new ArrayList();
				Map<Integer,Integer> m = new HashMap();
				m.put(v2,e);
				l.add(m);
				this.adj_list.put(v1,l);
			}
			else
			{
				Map<Integer,Integer> m = new HashMap();
				m.put(v2,e);
				((List)(this.adj_list.get(v1))).add(m);
			}
			if(!this.adj_list.containsKey(v2))
			{
				List<Map<Integer,Integer>> l = new ArrayList();
				Map<Integer,Integer> m = new HashMap();
				m.put(v1,e);
				l.add(m);
				this.adj_list.put(v2,l);
			}
			else
			{
				Map<Integer,Integer> m = new HashMap();
				m.put(v1,e);
				((List)(this.adj_list.get(v2))).add(m);		
			}
		}
		for(int i=0 ; i < (this.idtolabels.keySet()).size(); i++ )
		{
			this.v_ids.add((Integer)(this.idtolabels.keySet().toArray()[i]));
		}
		codes = rmp.split(","); //use codes string to get rmp vertices
		for(int i = 0; i < codes.length ; i++)
		{
			right_most_path.add(Integer.parseInt(codes[i]));
		}
		
		codes = str_vat.split("]"); //use codes string to get vat
		int gid=0;
		int index=0;
		for(int i = 0; i < codes.length ; i++)
		{
			String [] temp = codes[i].split(":");
			List<List<Map<Integer,Integer>>> l = new ArrayList();
			
			for(int j=0; j < temp.length ; j++)
			{
				index=0;
				String []temp2 = temp[j].split(",");
				if(j==0)
				{
					gid = Integer.parseInt(temp2[0]);
					index++;
				}
				List<Map<Integer,Integer>> L1 = new ArrayList();
		//		int i1=0;
				for(int k = index; k < temp2.length; k=k+2)
				{
					Map<Integer,Integer> m1 = new HashMap();
					m1.put(Integer.parseInt(temp2[k]), Integer.parseInt(temp2[k+1]));
					L1.add(m1);
				}
				l.add(L1);
				//lset.add(set1);
			}
			
			vat.put(gid,l);
		//	Vsets.put(gid,lset);
			
		}
		
		codes = str_vset.split("]"); //use codes string to get vset

		for(int i = 0; i < codes.length ; i++)
		{
			String [] temp = codes[i].split(":");
			List<Set<Integer>> lset = new ArrayList();
			for(int j=0; j < temp.length ; j++)
			{
				index=0;
				String []temp2 = temp[j].split(",");
				if(j==0)
				{
					gid = Integer.parseInt(temp2[0]);
					index++;
				}
				Set<Integer> s1 = new HashSet();
				for(int k = index; k < temp2.length; k=k+1)
				{
					s1.add(Integer.parseInt(temp2[k]));
				}
				lset.add(s1);
			}
			Vsets.put(gid,lset);
		}		
		
		return 1;
	}*/
	/*public String getthis()
	{
		String s = "GET THIS DOEN";	
		return s;
	
	}*/
	public void insert_vid_tid(int tid ,int v1id, int v2id)
	{
		Set<Integer> s = new HashSet();
		s.add(v1id);
		s.add(v2id);
		List<Set<Integer>> l = new ArrayList();
		l.add(s);
		Vsets.put(tid,l);
	}

	public void get_to_insert(int tid, int v1id, int v2id)
	{
		this.insert_vid_tid(tid,v1id,v2id);
	}
	public void insert_vid_hs(int tid, int v1id, int v2id)
	{
		Set<Integer> s = new HashSet();
		s.add(new Integer(v1id));
		s.add(new Integer(v2id));
		((List)(this.Vsets.get(tid))).add(s);		

	}
	public void print_vset()
	{
		Set<Integer> s = (Set)(this.Vsets.keySet());
		System.out.println("print VSET "+ s.size());
		for(int i=0; i< s.size();i++)
		{
			int tid = (Integer)(s.toArray()[i]);
			List<Set<Integer>> l = (List)(this.Vsets.get(tid));
			System.out.print("tid = "+tid);
			for(int j=0; j< l.size();j++)
			{
				Set<Integer> s1 = (Set)(l.toArray()[j]);
				System.out.println("j= "+j);
				for(int k=0; k<s1.size();k++)
				{
					System.out.print("--"+(Integer)(s1.toArray()[k])+",");
				}

			}
		}

	}
	public Object clone() {
            
            MR_Pattern obj = new MR_Pattern();
			for(int i=0; i<this.v_ids.size();i++)
			{
				((List)(obj.v_ids)).add(this.v_ids.toArray()[i]);
			}
			Iterator it = (this.idtolabels.keySet()).iterator();
			Iterator it1 = (this.idtolabels.values()).iterator();
			while(it.hasNext())
			{
				Object o1 = it.next();
				Object o2 = it1.next();
				(obj.idtolabels).put((Integer)(o1), (Integer)(o2));

			}
			
			Iterator it3 = (this.adj_list.keySet()).iterator();
			
			while(it3.hasNext())
			{
				Object o1 = new Object();
				o1 = it3.next();
				
				it =((List)this.adj_list.get((Integer)(o1))).iterator();
				Object o = new Object();
				List<Map<Integer, Integer>> l = new ArrayList();
				while(it.hasNext())
				{
					o = it.next();
					it1  = (((Map)(o)).keySet()).iterator();
					Object o2 = it1.next();
					Map<Integer,Integer> m = new HashMap();
					int elabel =  (Integer)(((Map)(o)).get(o2));
					int vlabel = ((Integer)o2);
					
					m.put(vlabel, elabel);
					l.add(m);
				}
				obj.adj_list.put((Integer)(o1),l);
				
			}

			obj.vat = new HashMap();
			obj.iscannonical = true;
			for(int i=0; i<this.can_cod.size();i++)
			{
				((List)(obj.can_cod)).add(this.can_cod.toArray()[i]);
			}
			
			obj.support = this.support;
            return obj;
        }
    public MR_Pattern(int v1label, int v2label, int elabel) {
        //your node labels are consecutive integers starting with one. 
        //to make the indexing easier we will allocate an array of adjacency one element larger than necessary
		
		support=0;

		v_ids = new ArrayList<Integer>();
		can_cod = new ArrayList();
        adj_list = new HashMap();
		right_most_path = new ArrayList<Integer>();
		
		vat = new HashMap<Integer,List<List<Map<Integer,Integer>>>>();
		Vsets = new HashMap();
		idtolabels = new HashMap<Integer, Integer>();
		idtolabels.put(1,v1label);
		idtolabels.put(2,v2label);
		v_ids.add(1);
		v_ids.add(2);
		Map<Integer,Integer> m1 = new HashMap();
		m1.put(2,elabel);
		Map<Integer,Integer> m2 = new HashMap();
		m2.put(1,elabel);
		List<Map<Integer,Integer>> l1= new ArrayList();
		l1.add(m1);
		List<Map<Integer,Integer>> l2= new ArrayList();
		l2.add(m2);
		adj_list.put(1, l1);
		adj_list.put(2, l2);
		MR_CannonicalCode cc = new MR_CannonicalCode(1,2,v1label,elabel,v2label);
		can_cod.add(cc);
		right_most_path.add(1);
		right_most_path.add(2);
		iscannonical=true;
		support=1;
        
    }
	public boolean noEdgeexist(int v1id,int v2id)
	{
		List<Map<Integer, Integer>> l = new ArrayList();
		Iterator it =((List)this.adj_list.get(v1id)).iterator();
		Object o = new Object();
		while(it.hasNext())
		{
			o = it.next();
			Iterator it1  = (((Map)(o)).keySet()).iterator();
			Object o1 = it1.next();
			
			if(((Integer)o1).intValue() == v2id)
				return false;
			
		}

		return true;

	}
	public List<Map<Integer,Integer>> get_adjlist(int vid)
	{
		List<Map<Integer, Integer>> l = new ArrayList();
		Iterator it =((List)this.adj_list.get(vid)).iterator();
		Object o = new Object();
		while(it.hasNext())
		{
			o = it.next();
			Iterator it1  = (((Map)(o)).keySet()).iterator();
			Object o1 = it1.next();
			Map<Integer,Integer> m = new HashMap();
			Integer elabel =  (Integer)(((Map)(o)).get(o1));
			
		    m.put(this.idtolabels.get((Integer)o1), elabel);
			l.add(m);
		}

		return l;
	}

	public List<Map<Integer,Integer>> get_adjlist_ids(int vid)
	{
		List<Map<Integer, Integer>> l = new ArrayList();
		Iterator it =((List)this.adj_list.get(vid)).iterator();
		Object o = new Object();

		while(it.hasNext())
		{
			o = it.next();
			Iterator it1  = (((Map)(o)).keySet()).iterator();
			Object o1 = it1.next();
			Map<Integer,Integer> m = new HashMap();
			Integer elabel =  (Integer)(((Map)(o)).get(o1));
		    m.put((Integer)o1, elabel);
			l.add(m);
		}

		return l;
	}
	public void print_adjlist()
	{
		Iterator it3 = (this.adj_list.keySet()).iterator();
		List<Map<Integer, Integer>> l = new ArrayList();
		while(it3.hasNext())
		{
			Object o1 = new Object();
			o1 = it3.next();
			System.out.print((Integer)(o1)+"--->");
			Iterator it =((List)this.adj_list.get((Integer)(o1))).iterator();
			Object o = new Object();
			while(it.hasNext())
			{
				o = it.next();
				Iterator it1  = (((Map)(o)).keySet()).iterator();
				Object o2 = it1.next();
				
				int elabel =  ((Integer)(((Map)(o)).get(o2))).intValue();
				
				System.out.print("("+(Integer)o2 + "  "+elabel+")");
				
			}
			System.out.print("\n");
		}

	}
	public int getelabel(int v1id, int v2id)
	{
		
		List<Map<Integer,Integer>> n = new ArrayList();
		n =(List)this.adj_list.get(v1id);
		Iterator it = n.iterator();
		int elabel=0;
		while(it.hasNext())
		{
			Object o = it.next();
			Iterator it1  = (((Map)(o)).keySet()).iterator();
			Object o1 = it1.next();
			if(((Integer)o1).intValue() == v2id)
			{
				elabel =  ((Integer)(((Map)(o)).get(o1))).intValue();
				break;
			}

		}
		
		return elabel;

	}
	public void print_vat()
	{
		
		Iterator itr = this.vat.keySet().iterator();
		Object Obj1 = new Object();
		Object Obj2 = new Object();
		Object Obj3 = new Object();
		System.out.print(this.getCan_code());
		System.out.print("-->");
		while(itr.hasNext())
		{
			Obj1 = itr.next();
				
			int tid = (Integer)Obj1;
			System.out.print("tid = "+tid);
			Iterator it = ((List)this.vat.get(Obj1)).iterator();
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
		System.out.println("END print vat");
	} 


	public void print_vat_from_map(Map<Integer,List<List<Map<Integer,Integer>>>> vat)
	{
		
		Iterator itr = vat.keySet().iterator();
		Object Obj1 = new Object();
		Object Obj2 = new Object();
		Object Obj3 = new Object();
		
		while(itr.hasNext())
		{
			Obj1 = itr.next();
				
			int tid = (Integer)Obj1;
			System.out.print("tid = "+tid);
			Iterator it = ((List)vat.get(Obj1)).iterator();
			while(it.hasNext())
			{
				Obj3 = it.next();
				Iterator it3 = ((List)Obj3).iterator();
				while(it3.hasNext())
				{
					Obj2 = it3.next();
					Iterator it1 = ((Map)Obj2).keySet().iterator();
					Obj1 = it1.next();
					int key = (Integer)Obj1;
					System.out.print("(" + key + "," +(Integer)(((Map)Obj2).get(Obj1))+")");
				}
				System.out.print(":");
				
			}
			
			System.out.print("  ");
		}
		
	} 

	public void creatvat(Map<Integer,List<List<Map<Integer,Integer>>>> m1)
	{
		
		this.vat = m1;
	}
	public void update_vat_entry(int tid, Map<Integer,Integer> m1)
	{
		if(this.vat.containsKey(tid))
		{
			Iterator it1 =((List)(this.vat.get(tid))).iterator();
			((List)(it1.next())).add(m1);
			
		}
	}
	

	public void extend_vat(int tid, List<List<Map<Integer,Integer>>> l1)
	{
		this.vat.put(tid,l1);
	}
	public void get_neighbors_adj(int vid)
	{
		List<Map<Integer,Integer>> n = new ArrayList();
		this.adj_list.get(vid);		

	}

	public void update_right_most_path(int firstv)
	{ 
			Object obj1 = new Object();
			Iterator it = ((List)(this.adj_list.get(firstv))).iterator();
			List<Integer> temp = new ArrayList();
			

			while(it.hasNext())
			{
				obj1 = it.next();
				
				Iterator it1 = ((Map)obj1).keySet().iterator();
				obj1 = it1.next();
				
				if(((Integer)obj1).intValue() < firstv)
				{
					continue;
				}
				else
				{
					temp.add((Integer)obj1);
				}
			}
			
			if(temp.size()>=1)
			{
				
				Collections.sort(temp);
				
				this.right_most_path.add((Integer)((temp.toArray())[temp.size()-1]));
				this.update_right_most_path((Integer)((temp.toArray())[temp.size()-1]));
				
			}
			else
			{
				return;
			}


	}
	public void test_clone(MR_Pattern P)
	{
		this.v_ids.add((P.v_ids.size())+1);
		

	}

	public void addflesh_backextension(MR_Pattern P, int vlabel, int elabel, int added_vlabel,int v1id, int v2id)
	{
		MR_CannonicalCode cc = new MR_CannonicalCode(v1id, v2id, vlabel, elabel, added_vlabel);
		this.can_cod.add(cc);
		
		Map<Integer,Integer> m = new HashMap();
		
		m.put(v2id,elabel);
		this.adj_list.get(v1id).add(m);

		Map<Integer,Integer> m1 = new HashMap();
		m1.put(v1id,elabel);
		this.adj_list.get(v2id).add(m1);
		
		for(int i=0; i<P.right_most_path.size();i++)
		{
			this.right_most_path.add((Integer)(P.right_most_path.toArray()[i]));
		}

	}

	public void update_right_most_path_new(MR_Pattern P, int extensionpoint, int idtobeadded)
	{
		this.right_most_path.clear();
		List<Integer> temp = new ArrayList();
		int index=0;
		int i=0;
		for(i=0; i<P.right_most_path.size(); i++)
		{
			this.right_most_path.add(P.right_most_path.get(i));
			if(((Integer)(P.right_most_path.toArray()[i])).intValue() == extensionpoint)
				break;
		}
		
		this.right_most_path.add(idtobeadded);
		

	}
	public void addflesh(MR_Pattern P, int vlabel, int elabel, int added_vlabel,int v1id, int v2id,int minsup)
	{
		
		MR_CannonicalCode cc = new MR_CannonicalCode(v1id, v2id, vlabel, elabel, added_vlabel);

		this.can_cod.add(cc);
		
		Map<Integer,Integer> m = new HashMap();
		
		m.put(v2id,elabel);
		this.adj_list.get(v1id).add(m);

		Map<Integer,Integer> m1 = new HashMap();
		m1.put(v1id,elabel);
		List<Map<Integer,Integer>> l1= new ArrayList();
		l1.add(m1);
		this.adj_list.put(v2id,l1);

		this.v_ids.add((P.v_ids.size())+1);

		this.idtolabels.put(this.v_ids.size(),added_vlabel);

		Iterator it = v_ids.iterator();
		Object obj1 = new Object();	
		obj1 = it.next();
	
		this.update_right_most_path_new(P, v1id, v2id );

	}
	public boolean check_isomorphism()
	{
		List<Map<Integer,Integer>> ids = new ArrayList();
		List<Integer> sde = new ArrayList();
		List<Mindfs> dfscode = new ArrayList();
		
		Integer srcl1 = new Integer(this.idtolabels.get(1));
		Integer destl1 = new Integer(this.idtolabels.get(2));
		int e = (this.getelabel(1,2));

		int new_e = iso_startup(srcl1.intValue(),destl1.intValue(),e,ids,sde);
		int srcl = ((Integer)(sde.toArray()[0])).intValue();
		int destl = ((Integer)(sde.toArray()[1])).intValue();

		Iterator it = ids.iterator();
		while(it.hasNext())
		{
			Object obj = new Object();
			obj = it.next();
			Iterator it1 = (((Map)obj).keySet()).iterator();
			Object obj1 = it1.next();
			int gi = ((Integer)obj1).intValue();
			int gj = ((Integer)(((Map)obj).get(obj1))).intValue();
		
			Mindfs dfs = new Mindfs(1,2,srcl,new_e,destl,gi,gj);
			dfscode.add(dfs);
		}

		c = (MR_CannonicalCode)(((Mindfs)(dfscode.toArray()[0])).codes.toArray()[0]);
		if(c.lessthan((MR_CannonicalCode)(this.can_cod.toArray()[0])))
			return false;

		for(int i=0;i<dfscode.size();i++)
		{
			
			boolean r = minimal((Mindfs)(dfscode.toArray()[i]));

			if(!r) {
      			this.iscannonical=false;
      			return false;
    		}

		}
		this.iscannonical=true;
		return true;
	}

	boolean minimal(Mindfs new_code)
	{

		if( this.can_cod.size()==new_code.codes.size())
    	{
			return true;
		}
		MR_CannonicalCode lastadded = (MR_CannonicalCode)(new_code.codes.toArray()[new_code.codes.size()-1]);
		boolean is_last_fwd=(lastadded.v1id<lastadded.v2id); // denotes if last edge in new_code was a fwd edge
		int last_vid=(is_last_fwd)? lastadded.v2id: lastadded.v1id; // vid to which edge shall be added  	
		int last_vid_g=new_code.gid(last_vid);
		int e=0;
		List<Map<Integer,Integer>> adj_list = this.get_adjlist_ids(last_vid_g);
		int code_index=new_code.codes.size();

		List<Integer> rmp = new_code.right_most_path;

		///// first try to add BACK EDGE /////////
  		int back_vid, last_back_vid;
  		back_vid=last_vid;

		if(is_last_fwd)
    	// relatively straight forward - add the farthest back edge you find
    		last_back_vid=-1;
  		else
    	// add a back edge only if it's after the last back edge present
    		last_back_vid=lastadded.v2id;

		Iterator it = adj_list.iterator();
		while(it.hasNext())
		{
			int rmvaddindex = rmp.size()-3;
			Object obj1 = it.next();
			Iterator it1 = ((Map)obj1).keySet().iterator();
			Object obj2 = it1.next();
			int neibor = ((Integer)obj2).intValue();
			while(rmvaddindex >= 0)
			{
				if(((Integer)(rmp.toArray()[rmvaddindex])).intValue() == new_code.cid(neibor))
        			break;

      			if(((Integer)(rmp.toArray()[rmvaddindex])).intValue() < new_code.cid(neibor))
        			rmvaddindex=0;
  
      			rmvaddindex--;

			}				
			if(rmvaddindex < 0) 
			{
      			continue;
    		}

			if(new_code.cid(neibor)<back_vid // this vertex is farthest one seen so far
       			&& new_code.cid(neibor)>last_back_vid) { // a valid back edge must end  
                                                              // at a vertex after last_back_vid
      			back_vid=new_code.cid(neibor);
       			e=((Integer)(((Map)obj1).get(obj2))).intValue();
    		}
		}

		if(back_vid!=last_vid) {
    		// valid back edge found
    		int lblv1=((Integer)(this.idtolabels.get(new_code.gid(last_vid)))).intValue();
    		int lblv2=((Integer)(this.idtolabels.get(new_code.gid(back_vid)))).intValue();
    		MR_CannonicalCode c =  new MR_CannonicalCode(last_vid, back_vid, lblv1, e, lblv2);
    		if(c.lessthan((MR_CannonicalCode)(this.can_cod.toArray()[code_index]))) {

//      			System.out.println("decision at back-edge: new tuple is more minimal");
      			return false;
    		}
			if(((MR_CannonicalCode)(this.can_cod.toArray()[code_index])).lessthan(c)) {

//			  	System.out.println("decision at back-edge: current tuple is more minimal");
			  	return true;
    		}
    		else {
      			new_code.codes.add(c);
      // no changes to new_code's rmp, nor to the cid-gid mappings since
      // back edge implies both vertices were already present in it
				//System.out.println("size of new_code inside back = "+new_code.codes.size());
      			return minimal(new_code);
				//System.out.println("size of new_code inside back 2 = "+new_code.codes.size());
    		}
		}

  	///// try to add a FWD EDGE /////
  	// how to: find the deepest outgoing edge, and among all such edges find the 
  	// minimal one, i.e. least edge-label + least dest-label

	boolean fwd_found=false;

  	last_vid=(Integer)(rmp.toArray()[rmp.size()-1]);
  	List<Integer> new_vids = new ArrayList(); // equivalent minimal vertices whose minimality has to be checked recursively
  	e=99999;
  	int dest_v=99999;
	int extensionpoint=0;
	for(int i=rmp.size()-1; i>=0; i--)
	{
		extensionpoint = ((Integer)(rmp.toArray()[i])).intValue();	
		adj_list.clear();
		adj_list = this.get_adjlist_ids((Integer)(new_code.gid(extensionpoint)));
		it = adj_list.iterator();
		while(it.hasNext())
		{
			Object obj1 = it.next();
			Iterator it1 = ((Map)obj1).keySet().iterator();
			Object obj2 = it1.next();
			int possibleextension = ((Integer)obj2).intValue();
			if(new_code.cid(possibleextension)!=-1)
			{
				continue;
			}
			int possibleextensionElabel = ((Integer)(((Map)obj1).get(obj2))).intValue();
			if(possibleextensionElabel<=e) {
		    // minimal edge
//				System.out.println("possibleextension = "+ possibleextension);
		    	int curr_lbl=((Integer)(this.idtolabels.get(possibleextension))).intValue();
		    	if(possibleextensionElabel<e) {
		      // new minimal edge found
		      		new_vids.clear();
		      		e=possibleextensionElabel;
		      		dest_v=curr_lbl;
		    	}

		    	if(curr_lbl<=dest_v) {
		      // minimal dest label
		      		if(curr_lbl<dest_v) {
		        		new_vids.clear();
		        		dest_v=curr_lbl;
		      		}
		      	new_vids.add(possibleextension);
		    	}
      		}	

		}

		if(!new_vids.isEmpty()) {
      	// fwd extension found at this level
      		fwd_found=true;
      		break;
    	}

	}

	if(!fwd_found) {
    	// no fwd edge extension could be found i.e. all fwd edges have been added
    	// so this code is minimal
    	//cout<<"Isomorphism decision at fwd-edge: all edges exhausted"<<endl;
    	return true;
  	}

	//System.out.println("extensionpoint = "+extensionpoint + "added with ="+ last_vid+1);
	MR_CannonicalCode c = new MR_CannonicalCode(extensionpoint, last_vid+1, (Integer)(this.idtolabels.get(new_code.gid(extensionpoint))), e, (Integer)(this.idtolabels.get(new_vids.toArray()[0])));
  	if(c.lessthan((MR_CannonicalCode)(this.can_cod.toArray()[code_index]))) {
    // new edge is more minimal


    	return false;
  	}
	if(((MR_CannonicalCode)(this.can_cod.toArray()[code_index])).lessthan(c)) {
    // current tuple is more minimal

    	return true;
  	}

	// check minimality against each new code

  	for(int i=0; i<new_vids.size(); i++) {
   // CAN_CODE next_code=new_code;
  		Mindfs next_code = (Mindfs)new_code.clone(extensionpoint);
		//mindfs next_code = new_code;
		int gi = next_code.gid(extensionpoint);
		int gj = ((Integer)(new_vids.toArray()[i])).intValue();

    	next_code.append(c, gi, gj);
    	next_code.append_rmp(extensionpoint, last_vid+1);
		
    	if(!minimal(next_code))
      		return false;	
  	}
	
		return true;		
	
}
	int iso_startup(Integer srcl, Integer destl, int e,List<Map<Integer,Integer>> ids,List<Integer> sde)
	{
		List<Map<Integer,Integer>> adj_list = new ArrayList();
		sde.add(0,srcl);
		sde.add(1,destl);
		sde.add(2,e);
		//System.out.println("v_ids size = "+ this.v_ids.size());
		int new_e=e;

		for(int i=0; i< this.v_ids.size();i++)
		{
			if(((Integer)(this.idtolabels.get(((this.v_ids.toArray()[i]))))).intValue() > srcl)
				continue;
			
			adj_list = this.get_adjlist_ids((Integer)(this.v_ids.toArray()[i]));
			Iterator it = adj_list.iterator();
			if(((Integer)(this.idtolabels.get(((this.v_ids.toArray()[i]))))).intValue() < srcl) 
			{
      			// new minimal src label found  
      			// find edge with least label      
      			ids.clear();
				
				Object obj = it.next();
				Iterator it2 = ((Map)obj).keySet().iterator();
				Object obj2 = it2.next();
				srcl = ((Integer)(this.idtolabels.get(((this.v_ids.toArray()[i]))))).intValue();
				destl = ((Integer)(this.idtolabels.get(((Integer)obj2)))).intValue();
				new_e = ((Integer)(((Map)obj).get(obj2))).intValue();
				sde.add(0,srcl);
				sde.add(1,destl);
				sde.add(2,new_e);
				Map<Integer,Integer> m = new HashMap();
				m.put((Integer)(this.v_ids.toArray()[i]),(Integer)obj2);
      			ids.add(m);
    
   			 }
			while(it.hasNext())
			{
				Object obj = it.next();
				Iterator it2 = ((Map)obj).keySet().iterator();
				Object obj2 = it2.next();
				
				if(((Integer)(((Map)obj).get(obj2))).intValue()<=e)
				{
					
					if(((Integer)(((Map)obj).get(obj2))).intValue()<e)
					{
						ids.clear();
						destl = ((Integer)(this.idtolabels.get(((Integer)obj2)))).intValue();
						new_e = ((Integer)(((Map)obj).get(obj2))).intValue();
						
						sde.add(1,destl);
						sde.add(2,new_e);
						Map<Integer,Integer> m = new HashMap();
						m.put((Integer)(this.v_ids.toArray()[i]),(Integer)obj2);
      					ids.add(m);

						continue;
					}

					if((Integer)(this.idtolabels.get(((Integer)obj2))) <= destl)
					{
						if((Integer)(this.idtolabels.get(((Integer)obj2))) < destl)
						{
							ids.clear();	
							destl = ((Integer)(this.idtolabels.get(((Integer)obj2)))).intValue();
							sde.add(1,destl);

						}
							Map<Integer,Integer> m = new HashMap();
							m.put((Integer)(this.v_ids.toArray()[i]),(Integer)obj2);

							ids.add(m);
						
					}
				}

			}
		}
		return new_e;

	}
	public void copy_vats_entry(MR_Pattern P, int rmp_index, int gid, boolean l2_swap,int index,int offset_v1)
	{

		List<List<Map<Integer,Integer>>> l= new ArrayList();
		Map<Integer,Integer> m = new HashMap();
		Map<Integer,List<List<Map<Integer,Integer>>>> m2= new HashMap();
		if(this.vat.isEmpty())
		{
			int k=0;
			
			for(int i=0; i< rmp_index; i++)
			{
				
				m = (Map)((((List)((List)(P.vat.get(gid))).toArray()[i])).toArray()[index]);
				int key = ((Integer)(m.keySet().toArray()[0])).intValue();
				int value = ((Integer)(m.get(key))).intValue();
				if(l2_swap)
				{
					int t = key;
					key = value;
					value = t;
				}
				Map<Integer,Integer> m1 = new HashMap();
				m1.put(key,value); 
				List<Map<Integer,Integer>> l1 = new ArrayList();
				l1.add(m1);
				l.add(l1);
			}
			
			this.vat.put(gid,l);
			this.copy_vids_tid(P,gid,offset_v1);
		}
		else
		{
			int k=0;
			
			if(!this.vat.containsKey(gid))
			{
				int k1=0;
				
				for(int i=0; i< rmp_index; i++)
				{
					
					m = (Map)((((List)((List)(P.vat.get(gid))).toArray()[i])).toArray()[index]);
					int key = ((Integer)(m.keySet().toArray()[0])).intValue();
					int value = ((Integer)(m.get(key))).intValue();
					if(l2_swap)
					{
						int t = key;
						key = value;
						value = t;
					}
					Map<Integer,Integer> m1 = new HashMap();
					m1.put(key,value); 
					List<Map<Integer,Integer>> l1 = new ArrayList();
					l1.add(m1);
					l.add(l1);
				}
				
				this.vat.put(gid,l);
				this.copy_vids_tid(P,gid,offset_v1);
			}
			else
			{
				int k1=0;

				for(int i=0; i< rmp_index; i++)
				{
							
					m = (Map)((((List)((List)(P.vat.get(gid))).toArray()[i])).toArray()[index]);					
					int key = ((Integer)(m.keySet().toArray()[0])).intValue();
					int value = ((Integer)(m.get(key))).intValue();
					if(l2_swap)
					{
						int t = key;
						key = value;
						value = t;
					}

					Map<Integer,Integer> m1 = new HashMap();
					m1.put(key,value); 
					
					((List)(((List)(this.vat.get(gid))).toArray()[i])).add(m1);
					
				}	
				this.copy_vids_hs(P,gid,offset_v1);
			}
		}

	}
	public boolean is_new_vertex(int v, int gid,int index,int rmp_index)
	{
		int k=0;
		int key=0;
		int value=0;
		
		if(rmp_index==0)
		{
			
			key = ((Integer)(((Map)(((List)(((List)(this.vat.get(gid))).toArray()[rmp_index])).toArray()[index])).keySet().toArray()[0])).intValue();
			
			value =  ((Integer)(((Map)(((List)(((List)(this.vat.get(gid))).toArray()[rmp_index])).toArray()[index])).get(key))).intValue();

			if(key == v  || value == v)
				return false;
		}
		else
		{
			
		
			for(int i=0; i < ((List)(((List)(this.vat.get(gid))))).size();i++)
			{		
					
				key = ((Integer)(((Map)(((List)(((List)(this.vat.get(gid))).toArray()[i])).toArray()[index])).keySet().toArray()[0])).intValue();
				value = ((Integer)(((Map)(((List)(((List)(this.vat.get(gid))).toArray()[i])).toArray()[index])).get(key))).intValue();

				if(key == v  || value == v)
					return false;
			}
		}
		return true;
	}
	public boolean is_new_vertex_modified(int vid, int gid, int offset)
	{
		
		if(((Set)(((List)(this.Vsets.get(gid))).toArray()[offset])).contains(vid))
		{
			return false;
		}
		return true;

	}
	public void copy_vids_tid(MR_Pattern P, int gid, int offset)
	{
		Set<Integer> s = (Set)(((List)(P.Vsets.get(gid))).toArray()[offset]);
		Set<Integer> s1 = new HashSet();
		for(int i=0; i< s.size();i++)
		{
			s1.add((Integer)s.toArray()[i]);
		}
		List<Set<Integer>> l = new ArrayList();
		l.add(s1);
		this.Vsets.put(gid,l);

	}
	public void copy_vids_hs(MR_Pattern P, int gid, int offset)
	{
		Set<Integer> s = (Set)(((List)(P.Vsets.get(gid))).toArray()[offset]);
		Set<Integer> s1 = new HashSet();
		for(int i=0; i< s.size();i++)
		{
			s1.add((Integer)s.toArray()[i]);
		}
		((List)(this.Vsets.get(gid))).add(s1);
	}

	public void insert_vid(int gid, int offset,int vid)
	{
		((Set)(((List)(this.Vsets.get(gid))).toArray()[offset])).add(vid);
	}
	public void fwd_intersect(MR_Pattern P, List<Map<Integer,Integer>> evat1, List<Map<Integer,Integer>> evat2, List<Map<Integer,List<List<Map<Integer,Integer>>>>> cand_vat,boolean is_fwd_chain, int rmp_index, int new_edge_state, int gid, boolean l2_eq)	
	{

	boolean swap_vids = false; // flag to denote if vids in evat_v2 should be swapped 
                    // before appending to v1
    boolean l2_swap=false; // flag to denote if vids in v1 should be swapped 
                    // before insertion to vat; this occurs in the special case when l2_eq=1 
	int v1=0;
	int v2=0;
	int v3=0;
	int v4=0;
	int i=0;
	int j=0;
	int k=0;
	int offset_v1=0;
	int off=-1;
	for(i=0; i < evat1.size();i++,offset_v1++)
	{
			v1 = ((Integer)(((Map)(evat1.toArray()[i])).keySet().toArray()[0])).intValue();
			v2 = ((Integer)(((Map)(evat1.toArray()[i])).get(v1))).intValue();
			
			for(j=0; j < evat2.size();j++)
			{
				v3 = ((Integer)(((Map)(evat2.toArray()[j])).keySet().toArray()[0])).intValue();
				v4 = ((Integer)(((Map)(evat2.toArray()[j])).get(v3))).intValue();	
	
			  	if(new_edge_state==0) { // both vertex labels of new edge are same
          			if(l2_eq) {
            // this is extension of level-2 with same labels
            // now, we have to check all four possibilities for a 
            // match here, since all four labels are equal
		        		if(v2==v3) {
		          			swap_vids=false;
		          			l2_swap=false;
		        		}
		        		else if(v2==v4) {
		          			swap_vids=true;
		          			l2_swap=false;
		        		}
		        		else if(v1==v3) {
		          			swap_vids=false;
		          			l2_swap=true;
		        		}
		        		else if(v1==v4) {
		          			swap_vids=true;
		          			l2_swap=true;
		        		}
		        		else
		          			continue;
          			}//if(l2_eq)
          			else {
            			if(is_fwd_chain) {
              				if(v2!=v3)
                				if(v2!=v4) // none of the vids in v2 matches
                  					continue;
                				else
                  					swap_vids=true;
              				else
                  				swap_vids=false;
            			}
            			else {
              				if(v1!=v3)
                				if(v1!=v4) // none of the vids in v2 matches
                  					continue;
                				else
                  					swap_vids=true;
              				else
                				swap_vids=false;
            			}
          		} //else l2_eq
        	}
			else { // vertex labels of new edge are different
				if(new_edge_state-1 ==0)
					swap_vids = false;
				if(new_edge_state-1 ==1)
					swap_vids = true;
		      //	swap_vids=(boolean)(new_edge_state-1); // swap if edge is of the form B-A
		                                  // but not if A-B

		      if(l2_eq) { // special case for L-2 with same labelled first edge
		        if(!swap_vids) {
		          if(v1!=v3)
		            if(v2!=v3)
		              continue; // no matching vids
		            else
		              l2_swap=false;
		          else
		            l2_swap=true;
		        }
		        else {
		          if(v1!=v4)
		            if(v2!=v4)
		              continue; // no matching vids
		            else
		              l2_swap=false;
		          else
		            l2_swap=true;
		        }
		      }
		      else {
		        if(is_fwd_chain) {
		          if(swap_vids && v2!=v4)
		            continue;
		          if(!swap_vids && v2!=v3)
		            continue;
		        }
		        else {
		          if(swap_vids) {

		            return;
		          }
		          if(v1!=v3)
		            continue;
		        }
		      }
        	} //else !new_edge_state

			if(!swap_vids) {
				Map<Integer,Integer> m = new HashMap();
				m.put(v3,v4);

		      	if(!P.is_new_vertex_modified(v4, gid,offset_v1))
		        	continue;
		    }
		    else
			{
				Map<Integer,Integer> m = new HashMap();
				m.put(v4,v3);
		      	if(!P.is_new_vertex_modified(v3, gid,offset_v1))
		        	continue;
			}

			if(rmp_index==0)
			{
				if(this.Vsets.isEmpty())
					this.copy_vids_tid(P,gid,offset_v1);
				else if(this.Vsets.get(gid)==null)
					this.copy_vids_tid(P,gid,offset_v1);
				else
					this.copy_vids_hs(P,gid,offset_v1);
				off++;
			}
			if(rmp_index>0 && this.vat.isEmpty())
			{
				this.copy_vats_entry(P,rmp_index,gid, l2_swap,i,offset_v1);
				off++;
				
			}
			else if(rmp_index>0)
			{
				this.copy_vats_entry(P,rmp_index,gid, l2_swap,i,offset_v1);
				off++;
				
			}

			Map<Integer, Integer> new_occurrence = new HashMap();
			int key=0;
			int value=0;
		    if(!l2_eq)
			{
				key = (is_fwd_chain?v2: v1);
			}
		    else
		    {
				key=(!l2_swap?v2: v1);
			}
		    value=(swap_vids?v3: v4);
			new_occurrence.put(key,value);

			if(!is_fwd_chain) {
				
				if(this.vat.containsKey(gid))
				{
			
					((List)((List)this.vat.get(gid)).toArray()[0]).add(new_occurrence);
					this.insert_vid(gid,off,key);
					this.insert_vid(gid,off,value);
		
				}
				else
				{
					List<Map<Integer,Integer>> l  =new ArrayList();
					l.add(new_occurrence);
					List<List<Map<Integer,Integer>>> l2 = new ArrayList();
					Map<Integer,List<List<Map<Integer,Integer>>>> m2 = new HashMap();
					l2.add(l);
					m2.put(gid,l2);
					this.vat.put(gid,l2);
					this.insert_vid(gid,off,key);
					this.insert_vid(gid,off,value);
		
				}
		  
		    }
			else { //assert: a new entry for this tid would have been created 
		           // by the copied vats
			
		      	if(((List)this.vat.get(gid)).size()== rmp_index) { 
		        // this is the first time this edge's evat is being inserted
					List<Map<Integer,Integer>> l = new ArrayList();
					l.add(new_occurrence);
					((List)(this.vat.get(gid))).add(l);
					this.insert_vid(gid,off,key);
					this.insert_vid(gid,off,value);
				
		      	}
		     	else {
					((List)((List)(this.vat.get(gid))).toArray()[rmp_index]).add(new_occurrence);
					this.insert_vid(gid,off,key);
					this.insert_vid(gid,off,value);
			
		      }
        	}
			
		}
	}

}
	public void back_intersect(MR_Pattern P, List<Map<Integer,Integer>> evat1, List<Map<Integer,Integer>> evat2, List<Map<Integer,List<List<Map<Integer,Integer>>>>> cand_vat, int new_edge_state, int back_idx,int gid)	
	{

		boolean swap_vids=false; // flag to denote if vids in evat_v2 should be swapped 
                    // before comparison with v1
		int v1=0;
		int v2=0;
		int v3=0;
		int v4=0;
		int i=0;
		int j=0;
		int k=0;
		int offset_v1=0;
		for(i=0; i < evat1.size();i++,offset_v1++)
		{
			v1 = ((Integer)(((Map)(evat1.toArray()[i])).keySet().toArray()[0])).intValue();
			v2 = ((Integer)(((Map)(evat1.toArray()[i])).get(v1))).intValue();
			
			for(j=0; j < evat2.size();j++)
			{
				v3 = ((Integer)(((Map)(evat2.toArray()[j])).keySet().toArray()[0])).intValue();
				v4 = ((Integer)(((Map)(evat2.toArray()[j])).get(v3))).intValue();	

				if(new_edge_state==0) {
          			if(v2!=v3)
            			if(v2!=v4) // none of the vids in v2 matches
              				continue;
           			 	else
              				swap_vids=true;
          			else
            			swap_vids=false;
        		}
				else {
          			if(new_edge_state-1 ==0)
						swap_vids = false;
					if(new_edge_state-1 ==1)
						swap_vids = true;

          			// check it's of the form A-B, B-C
          			if(!swap_vids && v2!=v3)
            			continue;
          			if(swap_vids && v2!=v4)
            			continue;
        		}

				// check that the back vertex is right one in this occurrence
							
				
        		if(!swap_vids && (Integer)(((Map)(((List)(((List)(P.vat.get(gid))).toArray()[back_idx])).toArray()[i])).keySet().toArray()[0])!=v4)
          			continue;
        		if(swap_vids && (Integer)(((Map)(((List)(((List)(P.vat.get(gid))).toArray()[back_idx])).toArray()[i])).keySet().toArray()[0])!=v3)
          			continue;

				// this is a valid back extension
        		// no new evat is prepared for a back extension
        		// simply copy the appropriate ones to cand_vat
				
				copy_vats_entry(P,((List)(P.vat.get(gid))).size(),gid, false,i,offset_v1);
        		
			}

		}
	}
	
	public List<Integer> getEmbeddedGraphId(){
		Iterator itr = this.vat.keySet().iterator();
		Object Obj1 = new Object();
		List<Integer> llTid = new ArrayList<>();
		while(itr.hasNext())
		{
			Obj1 = itr.next();				
			int tid = (Integer)Obj1;
			llTid.add(tid);			
		}
		return llTid;
	}
	public void vat_intersection(MR_Pattern P, Map<Integer,List<List<Map<Integer,Integer>>>> vat2)
	{

		this.Vsets = new HashMap();
		boolean is_fwd;
    	boolean is_fwd_chain=false; // flag to denote whether edge appended by 
    // intersection is at the root (which is when flag=0)
		List<Map<Integer,List<List<Map<Integer,Integer>>>>> cand_vat = new ArrayList();
		boolean l2_eq=(this.v_ids.size()==3) && ((Integer)(this.idtolabels.get(1))==(Integer)(this.idtolabels.get(2))); // special case in evat intersection for L-2 with first edge with equal vertex labels

		int rmp_index; // index of last vid on rmp common to cand_pat and its 
    	// parent's rmp
    	int new_edge_state=-1; // flag to denote if the new edge to be added has 
    	// same labeled vertices, of the form A-A (flag=0); or is of the form 
    	// A-B (flag=1); or is not canonical at all, of the form B-A (flag=2).
    	// evat intersection needs to take this into account

		int back_idx=-1;
		int rvid=(Integer)(this.right_most_path.toArray()[this.right_most_path.size()-1]);
    	int edge_vid=-1; // vid of the other vertex (other than rvid) connected 
    	// to rvid as to form the new edge

		int degree = (Integer)(((List)(this.adj_list.get(rvid))).size());

		if(degree>1)
      		is_fwd=false; // last edge was fwd edge only if outdegree of last vid=1
    	else
      		is_fwd=true;

		if(is_fwd) {
      		edge_vid=((Integer)(this.right_most_path.toArray()[this.right_most_path.size()-2])).intValue();

      		if(edge_vid==1) // rvid is attached to the root
        		is_fwd_chain=false;
      		else
        		is_fwd_chain=true;
    	}

		else {

      		edge_vid=(Integer)(((Map)(((List)(this.adj_list.get(rvid))).toArray()[degree-1])).keySet().toArray()[0]);

			  /// now determine the index of edge_vid on rmp of candidate. This is 
			  /// used by back_intersect.
			  // TO DO: this is currently a linear search through rmp, is there a 
			  // more efficient way??
     
      		for(int i=0; i<this.right_most_path.size(); i++) {
        		if(((Integer)(this.right_most_path.toArray()[i])).intValue() ==edge_vid) {
          			back_idx=i;
          			break;
        		}
      		}		

      		if(back_idx==-1) {
        		return ;
      		}
    	}

		if(is_fwd)
      		rmp_index=this.right_most_path.size()-2;
    	else
     		rmp_index=this.right_most_path.size()-1;

		if(((Integer)this.idtolabels.get(edge_vid)).intValue() == ((Integer)this.idtolabels.get(rvid)).intValue())
      	{	
			new_edge_state=0;
		}
    	else 
		{
			if(is_fwd)	
				if((Integer)this.idtolabels.get(edge_vid).intValue()>(Integer)this.idtolabels.get(rvid).intValue())
        			new_edge_state=2;
				else
					new_edge_state=1;
      		else
				if((Integer)this.idtolabels.get(rvid).intValue()>(Integer)this.idtolabels.get(edge_vid).intValue())
        			new_edge_state=2;
				else
					new_edge_state=1;
        		//new_edge_state=((Integer)this.idtolabels.get(rvid)>(Integer)this.idtolabels.get(edge_vid))+1;
		}
		int i=0;
		int j=0;
		int g1id=0;
		int g2id=0;
		int support=0;
		int get_val=0;

		List<Integer> Pvatgid = new ArrayList();
		List<Integer> vat2gid = new ArrayList();
		for(int i1=0; i1 <P.vat.keySet().size();i1++)
		{
			Pvatgid.add((Integer)P.vat.keySet().toArray()[i1]);
		}
		for(int i1=0; i1 <vat2.keySet().size();i1++)
		{
			vat2gid.add((Integer)vat2.keySet().toArray()[i1]);
		}
		
		Collections.sort(Pvatgid);
		Collections.sort(vat2gid);
		while(i<Pvatgid.size() && j<vat2gid.size())
		{
			
			g1id = Pvatgid.get(i);
			g2id = vat2gid.get(j);

			if(g1id < g2id)
			{
				i++;
				continue;
			}			
			if(g1id > g2id)
			{
				j++;
				continue;
			}

			List<Map<Integer,Integer>> evat1 = new ArrayList();
			List<Map<Integer,Integer>> evat2 = new ArrayList();

			evat2 = (List)(((List)(vat2.get(g1id))).toArray()[0]);

			if(!is_fwd) 
				evat1=(List)(((List)(P.vat.get(g1id))).toArray()[rmp_index-1]);	   
      		else {
        		if(is_fwd_chain)
					evat1=(List)(((List)(P.vat.get(g1id))).toArray()[rmp_index-1]);
        		else
					evat1=(List)(((List)(P.vat.get(g1id))).toArray()[0]);
      		}

			if(is_fwd) 
			{

        		fwd_intersect(P, evat1, evat2, cand_vat, is_fwd_chain, rmp_index, new_edge_state, g1id, l2_eq);
        			
      		}
			else 
			{

      			back_intersect(P, evat1, evat2, cand_vat, new_edge_state, back_idx, g1id);
      		}
			i++;
			j++;


		}

		this.support = this.vat.size();

	}
	public void print() {
		System.out.println("print pattern: adjList");
		this.print_adjlist();
		System.out.println("print pattern: rmp");
		this.print_rmp();
		System.out.println("print pattern:vat");
		this.print_vat();
	}

}

