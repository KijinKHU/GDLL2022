//package example.spark_first;
//
//import org.apache.hadoop.io.BytesWritable;
//import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapred.OutputCollector;
//import org.apache.spark.SparkConf;
//import org.apache.spark.SparkContext;
//import org.apache.spark.api.java.JavaPairRDD;
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.api.java.Optional;
//import org.apache.spark.api.java.function.FlatMapFunction;
//import org.apache.spark.api.java.function.Function;
//import org.apache.spark.sql.SparkSession;
//
//import com.esotericsoftware.kryo.io.Output;
//
//import dataset.Node;
//import kHop.Edge_Inf;
////import kHop.Mapper_Key;
//import kHop.Mapper_Value;
//import kHop.Node_Info;
//import kHop.Node_information;
//import kHop.Self_Inf;
//import kHop.Util.Util_functions_khop;
//import scala.Tuple2;
//
//import java.io.IOException;
//import java.lang.reflect.Array;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Pattern;
//
//public final class kHopGenerator_1103 {
//	private static final Pattern SPACE = Pattern.compile(" ");
//	
//	static JavaSparkContext sc;
//	
//	static int k = 0;
//	public static void main(String[] args) throws Exception {
//		System.setProperty("hadoop.home.dir", "c:\\hadoop\\winutil\\");
//		SparkConf sparkConf = new SparkConf().setAppName("JavaWordCount").setMaster("local[*]");
//		sc = new JavaSparkContext(sparkConf);
//		if (args.length < 2) {
//		  System.err.println("Usage: JavaWordCount <file>");
//		  System.exit(1);
//		}
//
//		SparkSession spark = SparkSession      
//				.builder()
//			      .appName("JavaWordCount")
//	                .master("local[*]")
//	                .config("spark.driver.bindAddress", "localhost")
//			      .getOrCreate();
//
//		System.out.println("Start:");
//		JavaRDD<String> lines = spark.read().textFile(args[0]).javaRDD();
//		int K = Integer.valueOf(args[1]);
//		
//		JavaRDD<Edge_Inf> edgeList = lines.map(line -> getEdge(line));
//		int i=0;
//		
//		JavaRDD<Node_Info> node_infos = edgeList.flatMap(s-> createNode_Infor(s));
//		
//		JavaPairRDD<Integer, Node_Info> node_infor_pair = node_infos.mapToPair(s->new Tuple2<>(s.getNode_id(), s));
//		
//		//at k= 1
//		int k=1;
//		JavaPairRDD<Integer, Node_Info> result_1 = node_infor_pair.reduceByKey((i1, i2) -> inEdge_Merging_(i1,i2));
//		
//		System.out.println("Print result_1: ");
//		List<Tuple2<Integer, Node_Info>> outslist_1 = result_1.collect();
//		for (Tuple2<Integer,Node_Info> tuple : outslist_1) {
//		 //System.out.println(tuple._1() + ": " + tuple._2());
//			tuple._2.printInfor();
//		}
//		
//		
//		
//		while(k<=K)
//		{
//			k=k+1;
//			JavaRDD<Node_Info> node_infos_propa = result_1.flatMap(s-> outPropagation_(s));
//			// Done k=1;
//			List<Node_Info> results1List = node_infos_propa.collect();
//			System.out.println("Print results1List: ");
//			for(Node_Info node: results1List)
//			{
//				node.printInfor();
//			}
//			
//			// For k>1
//			JavaPairRDD<Integer, Node_Info> new_node_infor_pair = node_infos_propa.mapToPair(s -> new Tuple2<>(s.getNode_id(),s));
//			
//			System.out.println("Print new_node_infor_pair: ");
//			List<Tuple2<Integer, Node_Info>> outslist_2 = new_node_infor_pair.collect();
//			for (Tuple2<Integer,Node_Info> tuple : outslist_2) {
//			 //System.out.println(tuple._1() + ": " + tuple._2());
//				tuple._2.printInfor();
//			}
//			
//			
//			JavaPairRDD<Integer, Node_Info> result_2 = new_node_infor_pair.reduceByKey((i1, i2) -> inEdge_Merging_(i1,i2));
//					
//			System.out.println("Print result_2: ");
//			List<Tuple2<Integer, Node_Info>> outslist = result_2.collect();
//			for (Tuple2<Integer,Node_Info> tuple : outslist) {
//			 //System.out.println(tuple._1() + ": " + tuple._2());
//				tuple._2.printInfor();
//			}
//		}
//		
//		/*
//		
//		System.out.println("ins");
//		JavaPairRDD<Integer, Node_information> ins = edgeList.mapToPair(s -> new Tuple2<>(s.getNodeB().getId(), createNode_inf(s,i, "in")));
//		List<Tuple2<Integer, Node_information>> inslist = ins.collect();
//		for (Tuple2<Integer,Node_information> tuple : inslist) {
//		 //System.out.println(tuple._1() + ": " + tuple._2());
//			tuple._2.printInfor();
//		}
//		
//		
//		System.out.println("outs");
//		JavaPairRDD<Integer, Node_information> outs = edgeList.mapToPair(s -> new Tuple2<>(s.getNodeA().getId(), createNode_inf(s,i, "out")));
//		
//		List<Tuple2<Integer, Node_information>> outslist = outs.collect();
//		for (Tuple2<Integer,Node_information> tuple : outslist) {
//		 //System.out.println(tuple._1() + ": " + tuple._2());
//			tuple._2.printInfor();
//		}
//		
//		//Join ins and outs
//		System.out.println("join");
//		JavaPairRDD<Integer, Node_information> result = join2Pair(i, ins, outs);
//		
//		
//		
//		k=1;
//		System.out.println("For k: "+ k);
//		JavaPairRDD<Integer, Node_information> khops = result.reduceByKey((i1, i2) -> inEdge_Merging(i1,i2));
//		
//		
//		if(K>1)
//		{
//			k=k+1;
//			while(k<=K)
//			{
//				JavaRDD<Node_information> newNodes = khops.flatMap(s-> outPropagation(s));
//				
//				JavaPairRDD<Integer, Node_information> newNodesPair = newNodes.mapToPair(s -> new Tuple2<>(s.getSelf().getNode().getId(),s));
//				
//				JavaPairRDD<Integer, Node_information> newResult = join2Pair(k+1, khops, newNodesPair);
//				khops= newResult.reduceByKey((i1, i2) -> inEdge_Merging(i1,i2));
//			}
//		
//		}
//		
//		
//
//		List<Tuple2<Integer, Node_information>> final_output = khops.collect();
//		for (Tuple2<Integer,Node_information> tuple : final_output) {
//		 System.out.println(tuple._1() + ": " + tuple._2());
//			tuple._2.printInfor();
//		}*/
//		spark.stop();
//		}
//
//
//
//
//
//	private static JavaPairRDD<Integer, Node_Info> join2Pair_(JavaPairRDD<Integer, Node_Info> list1,
//			JavaPairRDD<Integer, Node_Info> list2) {
//		
//		JavaPairRDD<Integer, Tuple2<Optional<Node_Info>, Optional<Node_Info>>> total = list1.fullOuterJoin(list2);	
//		JavaPairRDD<Integer, Node_Info> result = total.mapToPair(tuple ->{
//			
//			Node_Info node_info = null;
//			if(!tuple._2._1.isPresent())
//			{
//				node_info = tuple._2._2.get();
//				
//			}
//			else if(!tuple._2._2.isPresent())
//			{
//				node_info = tuple._2._1.get();
//			}
//			else if(tuple._2._2.isPresent() && tuple._2._1.isPresent())
//			{
//				node_info = inEdge_Merging_(tuple._2._2.get(), tuple._2._1.get());
//			}
//			
//			return new Tuple2<>(tuple._1, node_info); 
//			
//		});
//		
//		return result;
//	}
//
//
//
//
//
//	private static Iterator<Node_Info> outPropagation_(Tuple2<Integer, Node_Info> s) {
//		
//		int nodeId = s._1;
//		Node_Info nodeInfo= s._2;
//		
//		List<Node_Info> newNodeInfoList= new ArrayList<Node_Info>();
//		newNodeInfoList.add(nodeInfo);
//		if(nodeInfo.getOutEdge()!=null)
//		{
//			for(Edge_Inf edge: nodeInfo.getOutEdge())
//			{
//				Node_Info newNode = new Node_Info(edge.getNodeB().getId(), nodeInfo.getkhops(), null);
//				newNodeInfoList.add(newNode);
//			}
//			
//		}
//		return newNodeInfoList.iterator();
//	}
//
//
//
//
//
//	private static Node_Info inEdge_Merging_(Node_Info i1, Node_Info i2) {
//		List<Edge_Inf> inedge = new ArrayList<Edge_Inf>();
//		List<Edge_Inf> outedge = new ArrayList<Edge_Inf>();
//		List<Edge_Inf> khop = new ArrayList<Edge_Inf>();
//		if(i1.getInEdge()!=null)
//			inedge.addAll(i1.getInEdge());
//		
//		if(i2.getInEdge()!=null)
//			inedge.addAll(i2.getInEdge());
//		
//		if(i1.getOutEdge()!=null)
//			outedge.addAll(i1.getOutEdge());
//		
//		if(i2.getOutEdge()!=null)
//			outedge.addAll(i2.getOutEdge());
//		khop = inedge;
//		i1.setInEdge(inedge);
//		i1.setkhops(inedge);
//		i1.setOutEdge(outedge);
//		
//		return i1;
//	}
//
//
//
//
//
//	private static Iterator<Node_Info> createNode_Infor(Edge_Inf s) {
//		// TODO Auto-generated method stub
//		List<Node_Info> infoList = new ArrayList<Node_Info>();
//		List<Edge_Inf> edgeList = new ArrayList<Edge_Inf>();
//		edgeList.add(s);
//		Node_Info inNode = new Node_Info(s.getNodeB().getId(),edgeList,null );
//		Node_Info outNode = new Node_Info(s.getNodeA().getId(),null,edgeList);
//		
//		infoList.add(inNode);
//		infoList.add(outNode);
//		return infoList.iterator();
//	}
//
//
//
//
//
//	private static JavaPairRDD<Integer, Node_information> join2Pair(int k, JavaPairRDD<Integer, Node_information> ins,
//			JavaPairRDD<Integer, Node_information> outs) {
//		JavaPairRDD<Integer, Tuple2<Optional<Node_information>, Optional<Node_information>>> total = ins.fullOuterJoin(outs);				
//		
///*		List<Tuple2<Integer, Tuple2<Optional<Node_information>, Optional<Node_information>>>> totallist = total.collect();
//		for (Tuple2<Integer,Tuple2<Optional<Node_information>, Optional<Node_information>>> tuple : totallist) {
//		 System.out.println(tuple._1() + ": " + tuple._2());			
//		}
//		*/
//		
//		JavaPairRDD<Integer, Node_information> result = total.mapToPair(tuple ->{
//			Node_information node_info = null;
//			//System.out.println("tuple: "+ tuple);
//			//System.out.println("tuple._2: "+ tuple._2);
//			
//			if(!tuple._2._1.isPresent())
//			{
//				node_info = tuple._2._2.get();
//				
//			}
//			else if(!tuple._2._2.isPresent())
//			{
//				node_info = tuple._2._1.get();
//			}
//			else if(tuple._2._2.isPresent() && tuple._2._1.isPresent())
//			{
//				
//				//System.out.println("tuple._2._1: "+ tuple._2._1);
//				//System.out.println("tuple._2._1" + tuple._2._1.get());
//				
//				Node_information node_info1 = tuple._2._1.get();
//				Node_information node_info2 = tuple._2._2.get();
//				
//				List<Edge_Inf> khops = node_info1.getSelf().getKhop();
//				addUniqueEdgeList(khops, node_info2.getSelf().getKhop());
//				
//				
//				Map<Integer,List<Edge_Inf>> k_inEdges = node_info1.getK_inEdge();
//				Map<Integer,List<Edge_Inf>> k_inEdges2 = node_info2.getK_inEdge();
//				
//				addUniqueEdgeList(k_inEdges.get(k), k_inEdges2.get(k));
//				
//				List<Edge_Inf> outEdge = node_info1.getOutEdge();
//				List<Edge_Inf> outEdge2 = node_info2.getOutEdge();
//				addUniqueEdgeList(outEdge, outEdge2);
//				
//				node_info1.setK_inEdge(k_inEdges);
//				node_info1.setOutEdge(outEdge);
//				node_info1.getSelf().setKhop(khops);
//				
//				Map<Integer,List<Edge_Inf>> k_khop = new HashMap<Integer, List<Edge_Inf>>();
//				k_khop.put(k, khops);
//				node_info1.getSelf().setK_hop(k_khop);
//				node_info = node_info1;
//			}
//			
//			//System.out.println("after joining inside: ");
//			//System.out.println("tuple key: "+ tuple._1());
//			//node_info.printInfor();
//			return new Tuple2<>(tuple._1, node_info); 
//		});
//		return result;
//	}
//
//	
//	private static Node_information agg(Node_information i1, Node_information i2) {
//		// TODO Auto-generated method stub
//		JavaPairRDD<Integer, Node_information> newOnes = propa();
//		
//		return null;
//	}
//
//
//	private static JavaPairRDD<Integer, Node_information> propa() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//
//	private static Node_information createNode_inf(Edge_Inf s, int k, String type) {
//		
//		Self_Inf self;
//		Node_information node_inf;
//		List<Edge_Inf> outEdge = new ArrayList<Edge_Inf>();
//		Map<Integer,List<Edge_Inf>> k_inEdge = new HashMap<Integer, List<Edge_Inf>>();
//		List<Edge_Inf> inEdge = new ArrayList<Edge_Inf>();
//		if(type =="in") {
//			self = new Self_Inf(s.getNodeB(), new ArrayList());
//			// set khop at time k
//			Map<Integer,List<Edge_Inf>> k_hop= new HashMap<Integer, List<Edge_Inf>>();
//			List<Edge_Inf> edge_k_hop = new ArrayList();
//			edge_k_hop.add(s);
//			k_hop.put(k, edge_k_hop);
//			self.setK_hop(k_hop);
//			// set khop
//			self.setKhop(edge_k_hop);
//			
//			// set inEdge at time k
//			inEdge.add(s);
//			k_inEdge.put(k, inEdge);
//		}
//		else
//		{
//
//			self = new Self_Inf(s.getNodeA(), new ArrayList());
//			
//			Map<Integer,List<Edge_Inf>> k_hop= new HashMap<Integer, List<Edge_Inf>>();
//			List<Edge_Inf> edge_k_hop = new ArrayList();
//			
//			k_hop.put(k, edge_k_hop);
//			self.setK_hop(k_hop); // empty
//			// set khop
//			self.setKhop(edge_k_hop); // empty 
//			
//			// set inEdge at time k // empty
//			k_inEdge.put(k, edge_k_hop);
//			
//			
//			//set outEdge
//			outEdge.add(s);
//			
//		}
//		
//		
//		node_inf = new Node_information(self, outEdge);
//		
//		node_inf.setK_inEdge(k_inEdge);
//		return node_inf;
//	}
//
//
//	public static Edge_Inf getEdge(String line)
//	{
//		//System.out.println("line: "+ line);
//		int nodeAid = Integer.parseInt(SPACE.split(line)[0]);
//		int nodeBid = Integer.parseInt(SPACE.split(line)[1]);
//		
//		Node nodeA = new Node(nodeAid);
//		Node nodeB = new Node(nodeBid);
//		// TODO Auto-generated method stub
//		Edge_Inf edge = new Edge_Inf(nodeA, nodeB);
//		return edge;
//	}
//	
//	private static void addUniqueEdgeList(List<Edge_Inf> edgeList, List<Edge_Inf> newList)
//	{
//		if(newList!=null && edgeList!=null)
//		{
//			for(Edge_Inf newEdge: newList)
//			{
//				int count =0;
//				int total = edgeList.size();
//				for(Edge_Inf edge: edgeList)
//				{
//					if(newEdge.getNodeA().getId()==edge.getNodeA().getId() && newEdge.getNodeB().getId()==edge.getNodeB().getId())
//					{
//						break;
//					}
//					else
//						count++;
//						
//				}
//				if(count == total)
//					edgeList.add(newEdge);
//			}
//		}
//	}
//	
//	// reduce function
//	private static Node_information inEdge_Merging(Node_information final_node_inf, Node_information node_inf) throws IOException {
//
//		System.out.print("Merging for key: " + final_node_inf.getSelf().getNode().getId());
//		System.out.println("print k: "+ k);
//		int preK = k - 1;
//		Self_Inf self = final_node_inf.getSelf();
//		List<Edge_Inf> edgeList_preK1= self.getK_hop().get(preK);
//		
//		if(self.getK_hop()==null)
//			self.setK_hop(new HashMap<Integer, List<Edge_Inf>>());
//		
//		Self_Inf newSelf = node_inf.getSelf();
//		List<Edge_Inf> edgeList_preK2= newSelf.getK_hop().get(preK);
//		
//		List<Edge_Inf> edgeList_preK3 = new ArrayList();	
//		
//		addUniqueEdgeList(edgeList_preK3, edgeList_preK1);
//		addUniqueEdgeList(edgeList_preK3, edgeList_preK2);
//		
//		List<Edge_Inf> edgeList_K;
//		if(self.getK_hop().get(k)==null)
//		{
//			edgeList_K= new ArrayList();
//			self.getK_hop().put(k,edgeList_K );
//		}
//		else
//		{
//			edgeList_K= self.getK_hop().get(k);
//		}
//		
//		int old_edgeSize = edgeList_K.size();
//		
//		addUniqueEdgeList(edgeList_K,node_inf.getK_inEdge().get(k));
//		addUniqueEdgeList(edgeList_K,edgeList_preK3);
//		
//		List<Edge_Inf> outList= final_node_inf.getOutEdge();
//		addUniqueEdgeList(outList,node_inf.getOutEdge());
//		final_node_inf.setOutEdge(outList);
//		
//		
//		/*System.out.println("---Start filling khop value:");
//		final_node_inf.printInfor();
//		System.out.println("---End filling khop value:");*/
//		
//		
//
//		
//		return final_node_inf;
//	}
//	
//	private static Iterator<Node_information> outPropagation(Tuple2<Integer, Node_information> s) {
//		// TODO Auto-generated method stub
//		
//		System.out.println("out_propagation");
//		Self_Inf self = s._2.getSelf();
//		
//		List<Edge_Inf> outEdges = s._2.getOutEdge();
//		List<Node_information> listTuple = new ArrayList<Node_information>();
//
//		
//		
//		for(Edge_Inf edge: outEdges)
//		{
//			Node nodeB = edge.getNodeB();
//			Self_Inf self_B = new Self_Inf(nodeB, new ArrayList());
//			Map<Integer,List<Edge_Inf>> k_hop= new HashMap<Integer, List<Edge_Inf>>();
//			List<Edge_Inf> edge_k_hop = new ArrayList();
//			k_hop.put(k, edge_k_hop);
//			self_B.setK_hop(k_hop);
//				
//				
//  		    Util_functions_khop util = new Util_functions_khop();
//			String suffix= util.getSuffix(nodeB);
//			
//			Mapper_Key outKey = new Mapper_Key(nodeB.getId(),suffix);
//			
//			// Add in_edge at level k for next k-hop.
//			Node_information node_inf = new Node_information(self_B,new ArrayList());
//			
//			Map<Integer, List<Edge_Inf>> k_inEdge = new HashMap<Integer, List<Edge_Inf>>();
//			List<Edge_Inf> edgeList = new ArrayList<>();
//			edgeList.addAll(self.getK_hop().get(k));
//			// Add in_edge at level k for next k-hop for node B
//			k_inEdge.put(k+1, edgeList);
//			node_inf.setK_inEdge(k_inEdge);
//
//			listTuple.add(node_inf);
//
//		}
//	
//		
//		
//		return listTuple.iterator();
//	}
//	
//	
//	private static List<Tuple2<Integer, Node_information>> outEdge_Propagation(int k, Node_information final_node_inf) throws IOException {
//		
//		System.out.println("out_propagation");
//		Self_Inf self = final_node_inf.getSelf();
//		
//		List<Edge_Inf> outEdges = final_node_inf.getOutEdge();
//		List<Tuple2<Integer, Node_information>> listTuple = new ArrayList<Tuple2<Integer,Node_information>>();
//
//		
//		
//		for(Edge_Inf edge: outEdges)
//		{
//			Node nodeB = edge.getNodeB();
//			Self_Inf self_B = new Self_Inf(nodeB, new ArrayList());
//			Map<Integer,List<Edge_Inf>> k_hop= new HashMap<Integer, List<Edge_Inf>>();
//			List<Edge_Inf> edge_k_hop = new ArrayList();
//			k_hop.put(k, edge_k_hop);
//			self_B.setK_hop(k_hop);
//				
//				
//  		    Util_functions_khop util = new Util_functions_khop();
//			String suffix= util.getSuffix(nodeB);
//			
//			Mapper_Key outKey = new Mapper_Key(nodeB.getId(),suffix);
//			
//			// Add in_edge at level k for next k-hop.
//			Node_information node_inf = new Node_information(self_B,new ArrayList());
//			
//			Map<Integer, List<Edge_Inf>> k_inEdge = new HashMap<Integer, List<Edge_Inf>>();
//			List<Edge_Inf> edgeList = new ArrayList<>();
//			edgeList.addAll(self.getK_hop().get(k));
//			// Add in_edge at level k for next k-hop for node B
//			k_inEdge.put(k+1, edgeList);
//			node_inf.setK_inEdge(k_inEdge);
//			
//			
//			Tuple2<Integer, Node_information> t = new Tuple2<>(nodeB.getId(), node_inf);
//			listTuple.add(t);
//
//			
//
//		}
//	
//		return listTuple;
//		
//		
//		
//	}
//
//	
//}