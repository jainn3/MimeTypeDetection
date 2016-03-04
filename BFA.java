import java.io.File;

class ByteFreqAnalysis extends frequencyAnaysis
{
	private double oldByteCount[] = null;
	 

	public void setOldCount(double [] current)
	{
		oldByteCount = current;
	}
	
	public double[] analzeFile(byte [] data)
	{
		double[] count = super.analzeFile(data);		
		averageFPScore(count);		
		return count;
	}
	
	public void averageFPScore(double[] newFileScore)
	{	
		if(oldByteCount!=null)
			{
				for(int i = 0; i<newFileScore.length ; i++)
				{
					newFileScore[i] = ((oldByteCount[i]*(noOfFilesRead-1)) + newFileScore[i])/noOfFilesRead;
				}
			}
		
		setOldCount(newFileScore);
	}
	
	public void writeJsonFile(String pathToWriteJsonForGraph, String pathToWriteJson, String mimeType) {
		
		String jsonFileName = pathToWriteJson + "/" + mimeType + ".json";
		String jsonFORGraph = pathToWriteJsonForGraph + "/" + "G-" + mimeType + ".json";

		writeToJSON(oldByteCount,jsonFileName,mimeType);
		writeToJSONForGraph(oldByteCount,jsonFORGraph,mimeType, "BFA");
	}
}

public class BFA {

	public static void main(String[] args) {
		
		//args[0] directory to read files from.
		String pathToReadFiles = "";
		
			pathToReadFiles = args[0];
		
		String mimeType = "";
		
			mimeType = args[1];
		
		//args[path to write json]
		String pathToWriteJson = "";
		if(args.length <3)			
			pathToWriteJson = "BFAJsonFiles";
		else 
			pathToWriteJson = args[2];
		
		String pathToWriteJsonForGraph = "";
		if(args.length < 4)			
			pathToWriteJsonForGraph = "BFAJsonFilesForGraphs";
		else 
			pathToWriteJsonForGraph = args[3];
		
		
		File file = new File(pathToWriteJson);
		if(!file.exists())
			file.mkdirs();
		
		File file2 = new File(pathToWriteJsonForGraph);
		if(!file2.exists())
			file2.mkdirs();
				
				
		ByteFreqAnalysis obj = new ByteFreqAnalysis();
		
		obj.readFileNameFromDirectory(pathToReadFiles);
		obj.writeJsonFile(pathToWriteJsonForGraph,pathToWriteJson,mimeType);
	}	
}
