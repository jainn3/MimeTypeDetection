import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

// Arguments : /home/siddharth/Desktop/Data/TestData/gif/ gif
public class FHT {
	
	private int numOfFiles = 0;
	private double [][] headerFP = new double[128][256];
	private double [][] tailerFP = new double[128][265];
	private String mime="";
	
	private void analyze(String dirPath, String mime, boolean isSecondDir) throws IOException{
		this.mime = mime;
		File directory = new File(dirPath);
		File files[] = directory.listFiles();
		int count = 0;
		if(isSecondDir)
			count = numOfFiles;
		for(File f : files) {
			if(!f.isDirectory()) {
				count+=1;
				byte[] data = IOUtils.toByteArray(new FileInputStream(f));
				double[][] fileHeaderFP = createNewHeaderFingerPrint(data, count);
				updateHeaderFP(fileHeaderFP);
				double[][] fileTailFP = createNewTailFingerPrint(data, count);
				updateTailFP(fileTailFP);
				if(count%1000 == 0)
					System.out.print(".");
			}
		}
		System.out.println();
	}
	
	double[][] createNewTailFingerPrint(byte[] data, int count) {
		//get HeaderFP for current file
		int break_condition = 128;
		if(data.length < 128)
			break_condition = data.length;
		double fileTailFP[][] = new double[128][256];
		int insert_idx = 0;
		for(int i=data.length-1; i>=0; i--) {
			if(insert_idx == break_condition)
				break;
			int idx = (int) data[i] & 0xFF;
			fileTailFP[insert_idx][idx] = (double) 1;
			insert_idx +=1;
		}
		numOfFiles = count;
		return fileTailFP;
	}
	
	void updateTailFP(double[][] fileTailFP) {
		if (numOfFiles == 1) {
			for(int i=0;i<128;i++) {
				for(int j=0; j<256; j++) { 
					tailerFP[i][j] = fileTailFP[i][j];
				}
			}
		}
		else {
			for(int i=0;i<128;i++) {
				for(int j=0; j<256; j++) {
					double numerator = (tailerFP[i][j] * (numOfFiles-1)) + fileTailFP[i][j]; 
					tailerFP[i][j] = numerator/numOfFiles;
				}
			}
		}
	}
	
	void print() {
		System.out.println("File Header FP:");
		for(int i=0;i<128;i++) {
			for(int j=0;j<128;j++){
				if(headerFP[i][j] == 1)
				System.out.println(i+" "+j);
			} 
		}
		
		System.out.println("File Tail FP:");
		for(int i=0;i<128;i++) {
			for(int j=0;j<256;j++){
				if(tailerFP[i][j] == 1)
				System.out.println(i+" "+j);
			} 
		}
	}
	void updateHeaderFP(double[][] fileHeaderFP) {
		if (numOfFiles == 1) {
			for(int i=0;i<128;i++) {
				for(int j=0; j<256; j++) { 
					headerFP[i][j] = fileHeaderFP[i][j];
				}
			}
		}
		else {
			for(int i=0;i<128;i++) {
				for(int j=0; j<256; j++) {
					double numerator = (headerFP[i][j] * (numOfFiles-1)) + fileHeaderFP[i][j]; 
					headerFP[i][j] = numerator/numOfFiles;
				}
			}
		}
	}
	
	double[][] createNewHeaderFingerPrint(byte[] data, int count) {
		//get HeaderFP for current file
		double fileHeaderFP[][] = new double[128][256];
		for(int i=0; i<data.length; i++) {
			if(i == 128)
				break;
			int idx = (int) data[i] & 0xFF;
			fileHeaderFP[i][idx] = (double) 1;
		}
		numOfFiles = count;
		return fileHeaderFP;
	}
	
	void writeToFile() throws IOException{
		JSONObject obj = new JSONObject();
		obj.put("graphs", getAllGraphs(mime));
		
		try (FileWriter file = new FileWriter(mime+"_FHT.json")) 
		{
			file.write("var data = ");
			file.write(obj.toJSONString());
			System.out.println("Successfully Copied JSON Object to File...");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	JSONArray getAllGraphs(String mime) {
		JSONArray graphList = new JSONArray();
		JSONObject graph = new JSONObject();
		
		graph.put("mime", mime);
		graph.put("title", "Header Analysis for 4 Bytes");
		graph.put("data", getMatrixData(headerFP, 4));
		graphList.add(graph);
		
		graph = new JSONObject();
		graph.put("mime", mime);
		graph.put("title", "Tail Analysis for 4 Bytes");
		graph.put("data", getMatrixData(tailerFP, 4));
		graphList.add(graph);
		
		graph = new JSONObject();
		graph.put("mime", mime);
		graph.put("title", "Header Analysis for 8 Bytes");
		graph.put("data", getMatrixData(headerFP, 8));
		graphList.add(graph);
		
		graph = new JSONObject();
		graph.put("mime", mime);
		graph.put("title", "Tail Analysis for 8 Bytes");
		graph.put("data", getMatrixData(tailerFP, 8));
		graphList.add(graph);
		
		graph = new JSONObject();
		graph.put("mime", mime);
		graph.put("title", "Header Analysis for 16 Bytes");
		graph.put("data", getMatrixData(headerFP, 16));
		graphList.add(graph);
		
		graph = new JSONObject();
		graph.put("mime", mime);
		graph.put("title", "Tail Analysis for 16 Bytes");
		graph.put("data", getMatrixData(tailerFP, 16));
		graphList.add(graph);
		
		graph = new JSONObject();
		graph.put("mime", mime);
		graph.put("title", "Header Analysis for 32 Bytes");
		graph.put("data", getMatrixData(headerFP, 32));
		graphList.add(graph);
		
		graph = new JSONObject();
		graph.put("mime", mime);
		graph.put("title", "Tail Analysis for 32 Bytes");
		graph.put("data", getMatrixData(tailerFP, 32));
		graphList.add(graph);
		return graphList;
	}
	
	JSONObject getMatrixData(double[][] data, int n){
		JSONObject obj = new JSONObject();
		for(int i=0;i<n;i++){
			JSONArray list = new JSONArray();
			for(int j=0;j<256;j++){
				list.add(data[i][j]);
			}
			obj.put(i+"",list);
		}
		return obj;
	}
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		FHT fht = new FHT();
		String mime = args[0];
		String dirPath = args[1];
		fht.analyze(dirPath, mime, false);
		if(args.length == 3){
			dirPath = args[2];
			fht.analyze(dirPath, mime, true);
		}
		fht.writeToFile();
	}
}
