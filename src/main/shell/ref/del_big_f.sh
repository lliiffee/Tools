#!/bin/sh
set -x

#cd /root/share/finished/detail-wushuiying/
#cd /root/share/7_finished
#cd /root/share/9qi-new/big
#cd /root/11/big-finished
cd /root/15/big_finished

ls -1 | while read line
do
dir=$(echo $line | awk -F'-' '{print $1}')
mkdir -p $dir/big/
mv $line $dir/big/

done
