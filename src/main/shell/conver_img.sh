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
                                                # echo a=$width b=$height c=$file_size
                                                 standard_ratio=17
                                                 standard_width=418

                                                 control_quality=100
                                                 control_width=0
                                                #放大10陪
                                                 quality_ratio=`expr \( ${file_size} \* 1000 \) / \( ${width} \* ${height} \)`

                                                if [ ${fileName##*.} = "jpg" -o ${fileName##*.} = "JPG" ]; then
                                                                  if [ ${quality_ratio} -gt ${standard_ratio} ];
                                                                  then
                                                                  rto=`expr $quality_ratio / $standard_ratio`

                                                                     if [ $width -ge $standard_width ];then
                                                                       if [ $rto -ge 40 ];then
                                                                            control_quality=55
                                                                        elif [ $rto -ge 30 ];then
                                                                            control_quality=56
                                                                        elif [ $rto -ge 20 ];then
                                                                            control_quality=58
                                                                        elif [ $rto -ge 10 ];then
                                                                            control_quality=60
                                                                        elif [ $rto -ge 5 ];then
                                                                            control_quality=62
                                                                        else
                                                                             control_quality=65
                                                                        fi
                                                                     else
                                                                        if [ $rto -ge 50 ];then
                                                                            control_quality=65
                                                                        elif [ $rto -ge 30 ];then
                                                                            control_quality=70
                                                                        elif [ $rto -ge 15 ];then
                                                                            control_quality=75
                                                                        else
                                                                             control_quality=100
                                                                        fi
                                                                     fi
                                                                   #小于10k不进行压缩。
                                                                  if [ ${file_size} -lt 10240 ];then
                                                                     control_quality=100
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
                                                 #                  echo "$args_str  rto=$rto"

                                                                        if [ $control_width -eq $standard_width ] || [ $control_quality -lt 100 ];
                                                                        then
                                                                     #     echo "convert jpg begin: $fileName"
                                                                          convert $args_str $fileName  "${fileName%.*}_${nameSfx}.jpg"
                                                                    #      echo "convert done ${fileName%.*}_${nameSfx}.jpg"
                                                                        else
                                                                          convert  $fileName  "${fileName%.*}_${nameSfx}.jpg"
                                                                   #       echo "convert done ${fileName%.*}_${nameSfx}.jpg"
                                                                        fi

                                                elif [ ${fileName##*.} = "png" -o ${fileName##*.} = "PNG" ];then

                                                        if [ $width -gt $standard_width ];then
                                                                        control_width=$standard_width
                                                                  fi
                                                                  args_str=""

                                                                  if [ $control_width -eq $standard_width ];
                                                                    then
                                                                       args_str=" $args_str -resize $control_width "
                                                                  fi

                                                                          convert $args_str $fileName  "${fileName%.*}_${nameSfx}.jpg"

                                                elif [ ${fileName##*.} = "gif" -o ${fileName##*.} = "GIF" ];then

                                                        if [ $width -gt $standard_width ];then
                                                                        control_width=$standard_width
                                                                  fi
                                                                  args_str=" -quality 80 "

                                                                  if [ $control_width -eq $standard_width ];
                                                                    then
                                                                       args_str=" $args_str -resize $control_width "
                                                                  fi

                                                                          convert $args_str $fileName  "${fileName%.*}_${nameSfx}.jpg"

                                                fi
                                          echo "convert  $args_str : ${fileName%.*}_${nameSfx}.jpg"
                                        # identify -format "%w,%h,%b"  ${fileName%.*}_${nameSfx}.jpg
                                done
                fi
}
function loopConvertTodayFile()
{
   folderName=$1

        #filelist=$(ls -lR $folderName)
        dateStr=`date +"%Y%m%d"`

        filelist=`find $folderName -type f -name "*${dateStr}*"`
        for file in $filelist
        do

               if [ -f $file ]
                then
                    convert_image $file
               fi
         done

}
function loopAllFolder()
{
   folderName=$1

        #filelist=$(ls -lR $folderName)
        dateStr=`date +"%Y%m%d"`

        filelist=`find $folderName -depth -type f `
        for file in $filelist
        do

               if [ -f $file ]
                then
                    convert_image $file
                    #    echo "file: $file"
               fi
         done

}

function loopConvertTodayFolder()
{
    folderName=$1

        dateStr=`date +"%Y%m%d"`

        filelist=`find $folderName  -depth  -type d -name "*${dateStr}*"`
        for file in $filelist
        do
               if [ -d $file ];
                then
                     loopAllFolder  $file
               fi
         done

}
type="t_"$2

if [ $type = "t_all" ];
then
        #用于一次性转换网站所有旧图片。
        loopAllFolder $1
elif [ $type = "t_d_folder" ];then
        #转换文件夹名为当天的folder下所有文件
        loopConvertTodayFolder $1
elif [ $type = "t_d_file" ];then
        #转换文件名为当天日期的文件
        loopConvertTodayFile $1
else
 echo  " 命令： 文件名 命令类型 （all=所有文件进行转换）(d_folder =命名为当天日期文件夹) （d_file = 命名为当天日期的图片文件）"
fi
