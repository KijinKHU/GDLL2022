package config;

public class ParameterSetting {
	
	public static enum ParaName{
		graph_partioning_class,
		numPart,
		inputPath,
		outputPath,
		khopNumber,
	}
	
	public static enum PartioningMethod{
		KernighanLin,
		khop,
		Metis,
	}
}
