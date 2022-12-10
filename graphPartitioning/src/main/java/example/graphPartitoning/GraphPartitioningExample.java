package example.graphPartitoning;

import java.util.Properties;

import org.joda.time.DateTime;

import config.ParameterSetting.ParaName;
import config.ParameterSetting.PartioningMethod;
import graphPartitioner.GraphPartitioning;
import graphPartitioner.Method.GraphPartitioningFactory;


public final class GraphPartitioningExample {
	
	public static void main(String[] args) throws Exception {
	{
		String partitioningMethodName= PartioningMethod.khop.toString();
		String inputPath = "dataset\\cora.txt";
		String outputPath ="dataset\\" + partitioningMethodName+"_out_"+DateTime.now().getYear()+DateTime.now().getMonthOfYear()+DateTime.now().getDayOfMonth()+"_"+DateTime.now().getHourOfDay()+DateTime.now().getMinuteOfHour();
		int numPart = 4;
		int khopNumber = 5;
		
		Properties parameters = new Properties();
		parameters.setProperty(ParaName.graph_partioning_class.toString(), partitioningMethodName);
		parameters.setProperty(ParaName.numPart.toString(), String.valueOf(numPart));
		parameters.setProperty(ParaName.inputPath.toString(), inputPath);
		parameters.setProperty(ParaName.outputPath.toString(), outputPath);
		parameters.setProperty(ParaName.khopNumber.toString(), String.valueOf(khopNumber));
		GraphPartitioningFactory factory = new GraphPartitioningFactory();
		GraphPartitioning graphPartitiongMethod = factory.createGraphPartitioning(parameters) ;
		graphPartitiongMethod.doPartition();

	}
}
}
	
