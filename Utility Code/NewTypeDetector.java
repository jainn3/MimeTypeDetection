import org.apache.tika.Tika;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.PrintWriter;

public class NewTypeDetector {
	public static void main(String args[]) throws IOException{
		// String dirPath = "/home/siddharth/Desktop/Data/octet";
		File directory = new File(args[0]);
		File files[] = directory.listFiles();
		int count = 0;
		Tika t = new Tika();
		String mime = "";
		Map<String,ArrayList<String>> fileMap = new HashMap<String,ArrayList<String>>();
		Map<String,Integer> map = new HashMap<String,Integer>();
		for(File f:files) {
			if(f.isDirectory()) {
				getMimeTypes(t,f,map,fileMap);
			}
		}

		if(args.length > 1) {
			directory = new File(args[1]);
			files = directory.listFiles();
			for(File f:files) {
				if(f.isDirectory()) {
					getMimeTypes(t,f,map,fileMap);
				}
			}
		}

		PrintWriter pw =new PrintWriter("NewFileCountDistribution.txt");
		System.out.println(map.toString());
		pw.println(map.toString());
		pw.close();
		pw =new PrintWriter("NewFileNameDistribution.txt");
		pw.println(fileMap.toString());
		pw.close();
		// System.out.println(fileMap.toString());
	}

	private static void getMimeTypes(Tika t, File f, Map<String, Integer> map, Map<String, ArrayList<String>> fileMap) {
		// TODO Auto-generated method stub
		File files[] = f.listFiles();
		String mime="";
		for(File file : files) {
			if(file.isDirectory())
				continue;
			try	{ 
				mime = t.detect(file);
			}
			catch(Exception e) {}
			if(map.containsKey(mime)) 
				map.put(mime, map.get(mime)+1);
			else
				map.put(mime, 1);
			
			if(fileMap.containsKey(mime)) {
				fileMap.get(mime).add(file.getName());
			}
			else {
				ArrayList<String> list = new ArrayList<String>();
				list.add(file.getName());
				fileMap.put(mime, list);
			}
		}
	}
}
