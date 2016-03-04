import org.apache.tika.Tika;
import java.io.*;

class TypeDetector {
	public static void main(String args[]) throws IOException{
		// String dirPath = "/home/siddharth/Desktop/Data/TestData/octetstream";
		File directory1 = new File(args[0]);
		File files1[] = directory1.listFiles();
		File directory2 = new File(args[1]);
		File files2[] = directory2.listFiles();
		
		int count = 0;
		Tika t = new Tika();
		String mime = "";
		PrintWriter pw_count =new PrintWriter("FileDistributionCount.txt");
		PrintWriter pw_fileName =new PrintWriter("FileNameDistribution.txt");
		for(File f : files1) {
			// System.out.println(f.getName());
			try{
				mime = t.detect(f);
				pw.println(f.getName()+" "+mime);
			} catch(Exception e) {
				//e.printStackTrace();
			}
		}

		for(File f : files2) {
			// System.out.println(f.getName());
			try{
				mime = t.detect(f);
				pw.println(f.getName()+" "+mime);
			} catch(Exception e) {
				//e.printStackTrace();
			}
		}

		pw
	}
}