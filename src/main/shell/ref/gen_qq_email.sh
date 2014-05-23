#!/bin/sh
set -x
if [ $# != 2 ];then
echo "usage $0 start_qq_num nums_qq"
exit;
fi
start_qq=$1
num=$2
qq_email_f=/home/shell/qqemail.txt
cat /dev/null > $qq_email_f 
for((i=1;i<=$num;i++));
do 
target_qq=$(expr $start_qq + $i) 
echo "$target_qq@qq.com" >> $qq_email_f ;

done
