package graphPartitioner.Method;

import graphPartitioner.GraphPartitioning;
import kernighanLin.KernighanLin;
import kernighanLin.KernighanLinProgram;
import kernighanLin.Vertex;

public class KernighanLinPartitioning implements GraphPartitioning {

	private int numPart;
	private String inputPath;
	private String outputPath;
	public KernighanLinPartitioning(String inputPath, String outputPath, int numPart)
	{
		this.numPart=numPart;
		this.inputPath=inputPath;
		this.outputPath=outputPath;
	}
	@Override
	public void doPartition() {
		String args[] = {inputPath,outputPath, String.valueOf(numPart)};
		KernighanLinProgram.main(args);
	}
	
}
