package graphPartitioner.Method;

import example.spark_first.kHopGenerator_run;
import graphPartitioner.GraphPartitioning;

public class KhopPartitioning implements GraphPartitioning {

	private int k;
	private int numPart;
	private String inputPath;
	private String outputPath;
	public KhopPartitioning(String inputPath, String outputPath, int numPart, int k) {
		// TODO Auto-generated constructor stub
		this.k=k;
		this.numPart=numPart;
		this.inputPath=inputPath;
		this.outputPath=outputPath;
	}
	@Override
	public void doPartition() {
		// TODO Auto-generated method stub
		

		try {
			khopGeneration();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * execute the khop generation
	 */
	@SuppressWarnings({ "unused", "static-access" })
	private void khopGeneration() throws Exception {
		kHopGenerator_run run = new kHopGenerator_run();
		String args[] = {inputPath, String.valueOf(k),String.valueOf(numPart), outputPath};
		run.main(args);
	}

}
