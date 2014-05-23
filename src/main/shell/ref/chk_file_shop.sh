#!/bin/sh
#set -x
nowtime=$(date '+%F')
updatedir=/root/$nowtime
org_dir=/usr/local/tomcat_shop/webapps/
#find shop/ -type f -name "*" > filelist_shop
if [ $# != 1 ];then
echo "useage:$0 filename"
exit
fi
cat "$1" | while read line
do
mkdir -p $updatedir
rm -rf $updatedir/*
dirname=$(dirname $line)
mkdir -p $updatedir/$dirname/
filename=$(basename $line)
scp $org_dir/$line $updatedir/$dirname/

done
