#!/bin/sh
#set -x
nowtime=$(date '+%F')
updatedir=/root/$nowtime
org_dir=/var/www/html/opencart/
find ./ -type f -name "*" > filelist_b2c
cat filelist_b2c | while read line
do
mkdir -p $updatedir
dirname=$(dirname $line)
mkdir -p $updatedir/$dirname/
filename=$(basename $line)
scp $org_dir/$line $updatedir/$dirname/

done
