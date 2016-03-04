import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class ByteFreqCrossCorrelation extends frequencyAnaysis
{
	//no need to use this

	double prevMatrix[][] = null;
	

	public void readFromAllDirectories(String path1, String path2)
	{
		if(path1!=null && !path1.equals(""))
		{
			System.out.println("Path1 is : " + path1);
			readFileNameFromDirectory(path1);
			System.out.println("Count from file in path1 is : " + noOfFilesRead);

		}
				
		if(path2!=null && !path2.equals(""))
		{
			System.out.println("Count from file in path1 is : " + noOfFilesRead);
			System.out.println("Path2 is : " + path2);
			readFileNameFromDirectory(path2);
			System.out.println("Count from file in path2 is : " + noOfFilesRead);

		}
	}
	
	public void setOldMtrix(double matrix[][])
	{
		prevMatrix = matrix;
	}
	
	public double[] analzeFile(byte[] data) {

		//Do BFA
		double[] count = super.analzeFile(data);
		//compute BFCC
		double matrix[][] = computeBFCC(count);
		computeAverage(matrix);
		return count;
		
	}

	public double[][] computeBFCC(double []freqCount)
	{
		double matrix[][] = new double[256][256];
		for(int i = 0; i<matrix.length; i++)
			for(int j = 0; j<i;j++)
			{
				double avgFreqDiff = freqCount[i] - freqCount[j];
				matrix[i][j] = avgFreqDiff;
				matrix[j][i] = computeCorrelationStrength(avgFreqDiff);
			}
		return matrix;
	}

	public double computeCorrelationStrength(double avgFreqDiff) {
		return 1 - Math.abs(avgFreqDiff);
	}
	
	public void computeAverage(double[][] current)
	{
		if(prevMatrix != null)
		{
			for(int i = 0 ; i<current.length ; i++)
				for(int j = 0; j <i ; j++)
				{
					current[i][j] =  ((prevMatrix[i][j] * (noOfFilesRead-1)) + current[i][j])/noOfFilesRead;
					current[j][i] =  ((prevMatrix[j][i] * (noOfFilesRead-1)) + current[j][i])/noOfFilesRead;
				}
		}
		setOldMtrix(current);
	}
	public void writeJsonFile(String pathToWriteJsonForGraph, String pathToWriteJson, String mimeType)	{
		
		String jsonFileName = pathToWriteJson + "/" + mimeType + ".json";
		String jsonFORGraph = pathToWriteJsonForGraph + "/" + "G-" + mimeType + ".json";
		writeToJSONForGraph(prevMatrix,jsonFORGraph,mimeType,"BFCC");

	}
	
	public void writeToJSONForGraph(double [][] input, String path, String mimeType, String typeOfAnalysis)
	{
		prevMatrix[0][0] = noOfFilesRead;
		JSONObject mainObj = new JSONObject();
		JSONArray mainList = new JSONArray();
		
			JSONObject obj = new JSONObject();
			JSONObject parentJsonArray = new JSONObject();
			    // loop through your elements
			    for (int i=0; i<256; i++){
			    	JSONArray childJsonArray = new JSONArray();
					// _obj.put(i, childJsonArray);
			        for (int j =0; j<256; j++){
			            childJsonArray.add(input[i][j]);
			        }
			        // parentJsonArray.add(_obj);
			        parentJsonArray.put(i,childJsonArray);
			    }
			    obj.put("data", parentJsonArray);
			    obj.put("mime", mimeType);
				obj.put("title", typeOfAnalysis);
			
		    mainList.add(obj);
			mainObj.put("graphs", mainList); 
 
		try (FileWriter file = new FileWriter(path)) 
		{
			file.write("data = " + mainObj.toJSONString() + ";");
			System.out.println("Successfully Copied JSON Object to File...");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}

public class BFCC {

	public static void main(String[] args) {
				
		String mimeType = "";
		
			mimeType = args[0];
		
		//args[0] directory to read files from.
		String pathToReadFiles = "";
		
			pathToReadFiles = args[1];
		
		String pathToReadFilesFromOtherDir = "";
				if(args.length <3)
					pathToReadFilesFromOtherDir = "";
				else 
					pathToReadFilesFromOtherDir = args[2];
		
		//args[path to write json]
		String pathToWriteJson = "";
		if(args.length <4)			
			pathToWriteJson = "BFCCJsonFiles";
		else 
			pathToWriteJson = args[3];
		
		String pathToWriteJsonForGraph = "";
		if(args.length <5)			
			pathToWriteJsonForGraph = "BFCCJsonFilesForGraphs";
		else 
			pathToWriteJsonForGraph = args[4];
		
		
		File file = new File(pathToWriteJson);
		if(!file.exists())
			file.mkdirs();
		
		File file2 = new File(pathToWriteJsonForGraph);
		if(!file2.exists())
			file2.mkdirs();
				
				
		ByteFreqCrossCorrelation obj = new ByteFreqCrossCorrelation();
		
		//obj.readFileNameFromDirectory(pathToReadFiles);
		obj.readFromAllDirectories(pathToReadFiles, pathToReadFilesFromOtherDir);
		obj.writeJsonFile(pathToWriteJsonForGraph,pathToWriteJson,mimeType);

	}

}
