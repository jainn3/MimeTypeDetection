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

class ByteFreqCorrelation extends frequencyAnaysis
{
	private double oldByteCount[] = null;
	private double [] fingerPrint = new double[256];
	
	public void setOldCount(double [] current)
	{
		oldByteCount = current;
	}
	//read .json fingerprint to local double array of 256 size.
	public void readJsonToArray(String jsonPath)
	{
		JSONParser parser = new JSONParser();
		try {
			
			Object obj = parser.parse(new FileReader(jsonPath));
			JSONObject jsonObject = (JSONObject) obj;
			
			String mimeType = (String)jsonObject.get("MimeType");
			System.out.println(mimeType);
			
			JSONArray msg = (JSONArray) jsonObject.get("data");
			Iterator<Double> iterator = msg.iterator();
			int index = 0;
			while (iterator.hasNext()) 
			{
				fingerPrint[index++] = iterator.next();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
	
	
	
	public double[] analzeFile(byte [] data)
	{
		double[] count = super.analzeFile(data);
		//This is BFC
		double[]correlationArr = getCorrelationArray(count, fingerPrint);
		double []correlationFactor = correlationFactorFunction(correlationArr);
		
		//computer average of correlation Factor matrix
		averageFPScore(correlationFactor);
		//end of BFC
		
		return count;
	}
	
	public double[] correlationFactorFunction(double[] correlationArr) {
		double correaltedFuncArr[] = new double[256];
		for(int index = 0; index<256; index++)
		{
			correaltedFuncArr[index] = 1 - Math.abs(correlationArr[index]);
		}
		return correaltedFuncArr;
	}

	//finding the correlated array
	public double[] getCorrelationArray(double[] input, double[] fingerPrintArr) {
		double correaltedArr[] = new double[256];
		for(int index = 0; index<256; index++)
		{
			correaltedArr[index] = fingerPrintArr[index] - input[index];
		}
		return correaltedArr;
	}

	public void averageFPScore(double[] newFileScore)
	{	
		if(oldByteCount!=null)
			{
				for(int i = 0; i<newFileScore.length; i++)
				{
					newFileScore[i] = ((oldByteCount[i]*(noOfFilesRead-1)) + newFileScore[i])/noOfFilesRead;
					//System.out.println(newFileScore[i]);
				}
			}
		
		setOldCount(newFileScore);
	}

	
	public void writeJsonFile(String pathToWriteJsonForGraph, String pathToWriteJson, String mimeType) {
		
		String jsonFileName = pathToWriteJson + "/" + mimeType + ".json";
		String jsonFORGraph = pathToWriteJsonForGraph + "/" + "G-" + mimeType + ".json";

		writeToJSON(oldByteCount,jsonFileName,mimeType);
		writeToJSONForGraph(oldByteCount,jsonFORGraph,mimeType, "BFC");
	}
}

public class BFC {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String pathToReadFiles = "";
		
		
			pathToReadFiles = args[0];
	
		String mimeType = args[1];
		
		//writing path to json
		String pathToWriteJson = "";
		if(args.length <3)			
			pathToWriteJson = "BFCJsonFiles";
		else 
			pathToWriteJson = args[2];
		
		//writing path to graph json
		String pathToWriteJsonForGraph = "";
		if(args.length <4)			
			pathToWriteJsonForGraph = "BFCJsonFilesForGraphs";
		else 
			pathToWriteJsonForGraph = args[3];
		
		
		File file = new File(pathToWriteJson);
		if(!file.exists())
			file.mkdirs();
		
		File file2 = new File(pathToWriteJsonForGraph);
		if(!file2.exists())
			file2.mkdirs();
		
		//reading json file.
		String JsonFileReadPath = "";
		
		if(args.length <5)			
			JsonFileReadPath = "BFAJsonFiles";
		else 
			JsonFileReadPath = args[4];
				
		JsonFileReadPath+="/" + mimeType + ".json";
		
		ByteFreqCorrelation obj = new ByteFreqCorrelation();
		
		obj.readJsonToArray(JsonFileReadPath);
		obj.readFileNameFromDirectory(pathToReadFiles);
		obj.writeJsonFile(pathToWriteJsonForGraph,pathToWriteJson,mimeType);

		
	}

}
