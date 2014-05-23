#!/bin/sh
set -x 
nowtime=$(date '+%F')
updatedir=/root/$nowtime
if [ $# != 2 ];then
echo "$0 usage: $0 org_dir url "
exit
fi
tmpfile=/tmp/iwebshp_files
org_dir=$1
url=$2
cd $org_dir/
#find views/default/ -type f  > $tmpfile 
cat $tmpfile | while read line
do
dirname=$(dirname $line)
mkdir -p $updatedir/$dirname/
filename=$(basename $line)
cd $updatedir/$dirname/
wget -t 3 -c $url/$dirname/$filename 


done




