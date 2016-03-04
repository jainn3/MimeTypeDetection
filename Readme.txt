Compile frequencyAnalysis.java which is the base class for all the below algorithm.
Also compile all the files with the libraries provided in the lib folder. These are used for general io and JSON file processing.

Execution of BFA :

java -cp .:lib/json-simple-1.1.1.jar BFA /absolute/path/to/TrainingData/png png

Input : 
Pass the following arguments to BFA.
1. The directory of Training data according to mime type. Eg. /home/TrainingData/png
2. The current mime type. Eg. application/png
3. Optional - json file path. If this parameter is absent then the json will be created in the folder named BFAJsonFiles in the current directory. Note: This json file will be used as an input to other program.
4. Optional - json file path for d3 graphs.  If this parameter is absent then the json will be created in the folder named BFAJsonFilesForGraphs in the current directory. Note : This json is been created for d3 graph and has been formatted according to the d3 specifications. 

Output: 
Output consists of 2 json files in BFAJsonFiles and BFAJsonFilesForGraphs directory.

Execution of BFC : 

java -cp .:lib/json-simple-1.1.1.jar BFC /absolute/path/to/TestData/png /BFAJsonFiles/png.json

Input: 
Pass the following arguments to BFC.
1. The directory of Test data according to mime type. Eg. /home/TestData//png
2. The current mime type. Eg. application/png
3. Optional - The path to json file written on disk by BFA. If this parameter is empty the json file will be picked up from the BFAJsonFiles directory based on mimetype.
4. Optional - json file path. If this parameter is absent then the json will be created in the folder named BFCJsonFiles in the current directory. Note: This json file will be used as an input to other program.
5. Optional - json file path for d3 graphs.  If this parameter is absent then the json will be created in the folder named BFCJsonFilesForGraphs in the current directory. Note : This json is been created for d3 graph and has been formatted according to the d3 specifications.

Output: 
Output consists of 2 json files in BFCJsonFiles and BFCJsonFilesForGraphs directory namely “currentmimetype”.json and G-”currentmimetype”.json


Execution of BFCC : 

java -cp .:lib/json-simple-1.1.1.jar BFCC html /absolute/path/to/TrainingData/html /absolute/path/to/TestData/html

Input : 
1. The current mime type. Eg. application/html
2. The directory to Classified data according to mimetype. Eg. /home/ClassifiedData/html
3. Optional - Additional data directory for BFCC analysis
4. Optional - json file path. If this parameter is absent then the json will be created in the folder named BFCCJsonFiles in the current directory. Note: This json file will be used as an input to other program.
5. Optional - json file path for d3 graphs.  If this parameter is absent then the json will be created in the folder named BFCCJsonFilesForGraphs in the current directory. Note : This json is been created for d3 graph and has been formatted according to the d3 specifications.

Output : 
Output consist of single json file namely G-”currentmimetype”.json in BFCCJsonFiles directory.


Execution of FHT

java -cp .:lib/json-simple-1.1.1.jar:commons-io.jar FHT jpeg /absolute/path/to/TrainingData/mpeg /absolute/path/to/TestData/jpeg

Input :
1. The current mime type. Eg. application/html
2. The directory to Classified data according to mimetype. Eg. /home/ClassifiedData/jpeg
3. Optional - Additional data directory for FHT analysis

Output : 
Output consist of single json file namely “currentmimetype”-FHT.json in current directory.

Execution of D3 Graphs : 

All the visualizations are in the Graph folder with self explanatory file names. For BFA, BFC, BFCC and FHT we have added html file in the respective folder under Graphs and have given a dropdown list to select graph based on mimetype.

For mime type distribution we have added an html file which shows distribution of mimetype in four graphs based on the frequency count. The ranges are >100, > 850, >  33000. There is drop down to check the visualization before and after the updation of mimetype xml.

Execution of Utility Code : 

These are the Java Code, Python Code and Shell Scripts which we used to download and partition the data according to the mimetypes.
* TypeDetector.java
* Partition.sh
* PartitionData.sh
* DownloadData.py

One has to go through the script to understand its working.

Files in Tika modified to add x-zerosize mimetype: 

Following are the files that were modified to added x-zerosize mimetype:
* org.apache.tika.detect.CompositeDetecter.java
* org.apache.tika.mime.MediaType.java
* tika-mimetypes.xml

We have added the updated-tika.jar and the copied these 3 three files in the zip folder.
