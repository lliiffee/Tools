#!/bin/sh

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
                    #convert_image $file
                        echo "file: $file"
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
                     loopAllFolder  $file

               else
                    echo "conver file: $file"
               fi
         done

}

loopConvertTodayFolder $1
