#!/bin/sh




function convert_image
{
		
		 #大小/面积 定义为 精度比
		 #标准值定义：
		 #大小/面积 定义为 精度比=quality_ratio
		 #面积 300 X 300 = 90000
		 #大小 15 k  =  15360  x 100
		 #标准（大小 / 面积 ） =1536000 / 90000 = 17
		 
		  fileName=$1;
		
		nameStart=${fileName%.*}
		nameDefine=${nameStart##*_}
		nameSfx="bbfm"
		
		if [ $nameDefine != $nameSfx ]; then
		                identify -format "%w,%h,%b" ${fileName} | awk -F"," '{print $1, $2, $3}' | while read  width height file_size
		                                 do
		                                #read  width height file_size << `identify -format "%w,%h,%b" ${fileName} | awk -F"," '{print $1, $2, $3}'`
		                                 echo a=$width b=$height c=$file_size
		                                 standard_ratio=17
		                                 standard_width=480
		
		                                 control_quality=100
		                                 control_width=0
		
		                                 quality_ratio=`expr \( ${file_size} \* 100 \) / \( ${width} \* ${height} \)`
		
		                                if [ ${fileName##*.} = "jpg" -o ${fileName##*.} = "JPG" ]; then
		                                                  if [ ${quality_ratio} -gt ${standard_ratio} ];
		                                                  then
		                                                  rto=`expr $quality_ratio / $standard_ratio`
		
		                                                        if [ $rto -ge 3 ];then
		                                                            control_quality=55
		                                                        elif [ $rto -ge 2 ];then
		                                                            control_quality=55
		                                                        else
		                                                            control_quality=65
		                                                        fi
		
		                                                  fi
		
		                                                  if [ $width -gt $standard_width ];then
		                                                        control_width=$standard_width
		                                                  fi
		                                                  args_str=""
		                                                  if [ $control_width -eq $standard_width ];
		                                                    then
		                                                       args_str=" $args_str -resize $control_width "
		                                                  fi
		
		                                                  if [ $control_quality -lt 100 ];
		                                                    then
		                                                     args_str=" $args_str -quality $control_quality "
		                                                  fi
		                                                   echo "$args_str  rto=$rto"
		
		                                                        if [ $control_width -eq $standard_width ] || [ $control_quality -lt 100 ];
		                                                        then
		                                                          echo "convert jpg begin"
		                                                          convert $args_str $fileName  "${fileName%.*}_${nameSfx}.jpg"
		                                                          echo "convert done ${fileName%.*}_${nameSfx}.jpg"
		                                                        else
		                                                          convert  $fileName  "${fileName%.*}_${nameSfx}.jpg"
		                                                          echo "convert done ${fileName%.*}_${nameSfx}.jpg"
		                                                        fi
		                                elif [ ${fileName##*.} = "png" -o ${fileName##*.} = "PNG" -o ${fileName##*.} = "gif" -o ${fileName##*.} = "GIF" ];then
		
		                                        if [ $width -gt $standard_width ];then
		                                                        control_width=$standard_width
		                                                  fi
		                                                  args_str=""
		
		                                                  if [ $control_width -eq $standard_width ];
		                                                    then
		                                                       args_str=" $args_str -resize $control_width "
		                                                  fi
		
		
		                                                          echo "conver png begin"
		                                                          convert $args_str $fileName  "${fileName%.*}_${nameSfx}.jpg"
		                                                         echo "convert png done ${fileName%.*}_${nameSfx}.jpg"
		                                fi
		                done
		fi
}

function loopConvertTodayFile()
{
   folderName=$1
	
	#filelist=$(ls -lR $folderName)
	dateStr=`date +"%Y%m%d"`
	
	filelist=`find -depth $folderName -type f -name "*${dateStr}*"`
	for file in $filelist
	do
	
	       if [ -f $file ]
	        then
	            #convert_image $file
	                echo "file: $file"
	       fi
	 done
	   
}


function loopConvertTodayFolder()
{
    folderName=$1

        dateStr=`date +"%Y%m%d"`

        filelist=`find $folderName  -depth -type d -name "*${dateStr}*"`
        for file in $filelist
        do
               if [ -d $file ];
                then
                     echo $file

               else
                    echo "file: $file"
               fi
         done

}

function loopAllFolder()
{
   folderName=$1
	
	#filelist=$(ls -lR $folderName)
	dateStr=`date +"%Y%m%d"`
	
	filelist=`find -depth $folderName -type f `
	for file in $filelist
	do
	
	       if [ -f $file ]
	        then
	            #convert_image $file
	                echo "file: $file"
	       fi
	 done
	   
}


                                                  