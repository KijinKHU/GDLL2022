package graphPartitioner.Method;

import graphPartitioner.GraphPartitioning;

public class MetisPartitioning implements GraphPartitioning{

	private int numPart;
	private String inputPath;
	private String outputPath;
	public MetisPartitioning(String inputPath, String outputPath, int numPart)
	{
		this.numPart=numPart;
		this.inputPath=inputPath;
		this.outputPath=outputPath;
	}
	@Override
	public void doPartition() {
		// TODO Auto-generated method stub
		
	}

}
