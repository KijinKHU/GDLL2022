package example.graphPartitoning;

import java.util.Properties;

import org.joda.time.DateTime;

import config.ParameterSetting.ParaName;
import config.ParameterSetting.PartioningMethod;
import graphPartitioner.GraphPartitioning;
import graphPartitioner.Method.GraphPartitioningFactory;


public final class PartitionGraph {
	
	public static void main(String[] args) throws Exception {
	{
		if(args.length<4)
		{
			System.out.println("using: partitionMethod[khop,KernighanLin] input output numberPart numberKhop[optional]");
			return;
		}
		else
		{
			String partitioningMethodName= args[0];
			String inputPath = args[1];
			String outputPath = args[2] + "\\" + partitioningMethodName+"_out_"+DateTime.now().getYear()+DateTime.now().getMonthOfYear()+DateTime.now().getDayOfMonth()+"_"+DateTime.now().getHourOfDay()+DateTime.now().getMinuteOfHour();
			int numPart = Integer.parseInt(args[3]);
			int khopNumber = 5; // default
			if(partitioningMethodName.contains("khop"))
			{
				khopNumber = Integer.parseInt(args[4]);
				System.out.println(khopNumber);
			}
			else if(partitioningMethodName.contains("KernighanLin"))
			{
				System.out.println("Our KernighanLin currently supports 2 or 4 parts");
				if(numPart <=3)
					numPart =2;
				else
					numPart = 4;
			}
			
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
}
	
