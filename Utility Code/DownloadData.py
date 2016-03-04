import json
import argparse
import boto
import sys
from boto.s3.connection import S3Connection

def downloadFile(srcName, destName, conn):
	key = conn.get_bucket('polar-fulldump').get_key(srcName)
	key.get_contents_to_filename(destName)

conn = S3Connection('AKIAIYQGAK2RABXX2GKA', 'DNXUyl3SygMR3b+2QVpsvEKKmae6Vkobk9twUrXH')
parser = argparse.ArgumentParser()
parser.add_argument("names_file")
args = parser.parse_args()
input_file=open(args.names_file,"r+")
output_file=open("emptyFileNames.txt","a")
tracking_file=open("trackingFile.txt","a")
count=0
total=0
file_name_map = dict()
for line in input_file:
	line = line[20:].strip()
	data = line.split(" ")
	file_name=""
	total+=1
	# print(data[2])
	# print(data[0])
	if total%100000 == 0 :
		print(".")
	if data[2].rindex("/") > 0 :
		file_name = data[2][(data[2].rindex("/") + 1):]
		# print(file_name)
		downloadFile(data[2],"downloadedData/"+file_name,conn)
	if float(data[0]) <= 0.0 :
		count+=1
		output_file.write(file_name+"\n")
	tracking_file.write(file_name+"\n")
output_file.close()
print(count)		
	