declare -i max_count=500
declare -i count=0
for file in $(ls d1)          
do          
   cd d1
   file_path=$(realpath $file)
   cd ..
   mime_type=$(java -cp .:tika-app-1.11.jar SimpleTypeDetection $file_path)
   mime=$(echo $mime_type | cut -d'/' -f 2)
#   echo "$file $mime"
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
   count=count+1
  # echo $file 
done



#$DIRECTORY="Testing"
#if [ -d ./"$DIRECTORY" ]; then
#	echo "Helloe"  
#	# Control will enter here if $DIRECTORY exists.
#fi


