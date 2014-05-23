#!/bin/sh
set -x

#cd /root/share/finished/detail-wushuiying/
#cd /root/share/7_finished
#cd /root/share/9qi-new/big
#cd /root/share/big_finished
#cd /root/share/detail_finished
cd /root/15/detail_finished

ls -1 | while read line
do
dir=$(echo $line | awk -F'_' '{print $1}')
mkdir -p $dir/detail/
mv $line $dir/detail/

done
