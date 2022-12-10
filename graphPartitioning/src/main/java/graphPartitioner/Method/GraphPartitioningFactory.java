package graphPartitioner.Method;

import java.util.Properties;

import config.ParameterSetting.ParaName;
import graphPartitioner.GraphPartitioning;

public class GraphPartitioningFactory {
	public GraphPartitioning createGraphPartitioning(Properties config) {
		String type = config.getProperty("graph_partioning_class");
		switch (type) {
		case "KernighanLin":
			return createKernighanLin(config);
		case "khop":
			return createkHop(config);
		case "metic":
			return createMetic(config);
		default:
			return createKernighanLin(config);
		}
	}
	
	private GraphPartitioning createkHop(Properties config) {
		int numPart= Integer.parseInt((String) config.getProperty("numPart"));
		int k= Integer.parseInt((String) config.get("khopNumber"));
		KhopPartitioning khopPartition= new KhopPartitioning(config.getProperty("inputPath"),config.getProperty("outputPath"), numPart,k);
		return khopPartition;
	}

	private GraphPartitioning createKernighanLin(Properties config) {
		int numPart= Integer.parseInt(config.getProperty(ParaName.numPart.toString()));
		KernighanLinPartitioning kernighanLin= new KernighanLinPartitioning(config.getProperty("inputPath"),config.getProperty("outputPath"),numPart);
		return kernighanLin;
	}

	private GraphPartitioning createMetic(Properties config) {
		MetisPartitioning metis = new MetisPartitioning(config.getProperty("inputPath"),config.getProperty("outputPath"), Integer.parseInt(config.getProperty("numPart")));
		return metis;
	}


}
