old_IFS=$IFS      # save the field separator           
IFS=$'\n'     # new field separator, the end of line           
for line in $(cat java_output.txt)          
do 
   file=$(echo $line | cut -d' ' -f 1)         
   mime_type=$(echo $line | cut -d' ' -f 2)
   mime=$(echo $mime_type | cut -d'/' -f 2)
   # echo $mime
   # echo $file
   cd d1
   file_path=$(realpath $file)
   cd ..
   if [ -d ./ClassifiedData/"$mime" ]; then
	destinationPath="./ClassifiedData/$mime"
#	echo $destinationPath
	mv $file_path $destinationPath
	# Control will enter here if $DIRECTORY exists.
   else
	cd ClassifiedData
	mkdir $mime
	echo $mime
	cd ..
	destinationPath="./ClassifiedData/$mime"
	mv $file_path $destinationPath
#	echo $destinationPath	
   fi
             
done          
IFS=$old_IFS     # restore default field separator 