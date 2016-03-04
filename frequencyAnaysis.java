import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class frequencyAnaysis {

	int noOfFilesRead = 0;
	public void readFileNameFromDirectory(String path)
	{
		File directory = new File(path);
		File[] files = directory.listFiles();
		for(File f:files){
			if(!f.isDirectory()) {
				noOfFilesRead++;
				Path filePath = Paths.get(f.getAbsolutePath());
		        //System.out.println(filePath);
		        readFile(filePath);
			}
		}
	}
	
	public void readFile(Path filePath)
	{
		try {
			byte[] data = Files.readAllBytes(filePath);
			analzeFile(data);
			//for(byte b:data)
				//System.out.println(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public double[] analzeFile(byte[] data) {
		double[] count = new double[256];
		double max = countAndFindMax(data,count);
		normalizeAndCompand(max, count);	
		return count;
	}
	
	public double countAndFindMax(byte[]input, double[]count)
	{
		double maxCount = Integer.MIN_VALUE;
		for(byte b:input)
		{
			int index = (int)b & 0xFF;
			count[index]++;
			if(count[index] > maxCount)
			{
				maxCount = count[index];
			}
		}
		return maxCount;
	}
	
	public void normalizeAndCompand(double max, double[]count)
	{
		for(int i = 0; i<count.length;i++)
		{
			count[i] = count[i]/max;
			count[i] = Math.pow(count[i], 0.5);
		}
	}
	
	public void writeToJSONForGraph(double [] input, String path, String mimeType, String typeOfAnalysis)
	{
		JSONObject mainObj = new JSONObject();
		JSONArray mainList = new JSONArray();
				
			JSONObject obj = new JSONObject();
			obj.put("mime", mimeType);
			obj.put("title", typeOfAnalysis);
			JSONArray list = new JSONArray();
			
			for(int i = 0; i<input.length;i++)
			{
				list.add(input[i]);
			}
		
			
			obj.put("data", list);
		
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
	
	public void writeToJSON(double [] input, String path, String mimeType)
	{
		JSONObject obj = new JSONObject();

		JSONArray list = new JSONArray();
		
		for(int i = 0; i<input.length;i++)
		{
			list.add(input[i]);
		}
	
		obj.put("MimeType", mimeType);
		obj.put("data", list);
 
		try (FileWriter file = new FileWriter(path)) 
		{
			file.write(obj.toJSONString());
			System.out.println("Successfully Copied JSON Object to File... Normal");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


}
