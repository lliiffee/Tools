#!/bin/sh
set -x
#shijian=$(echo $(($RANDOM%1000)))
#echo $shijian
while true
do
curl http://www.baidu.com/s?wd=%E5%AF%BB%E5%8C%85%E7%BD%91 > /dev/null 2>&1
curl http://www.forbag.cn/sjpl.php > /dev/null 2>&1
#sleep $shijian
sleep $(($RANDOM%1000))
done
