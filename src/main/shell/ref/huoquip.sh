#!/bin/sh
#set -x
ip_url="http://cps.800pharm.com/ip.php"
api_url="http://cps.800pharm.com/api.php"
target_ipfile="/tmp/ip.txt"
#elinks $api_url | sed 's/ //g' > $target_ipfile
n=$(which curl)
flag=$(echo $n | grep -c curl)
if [ $flag -gt 0 ];then
echo "curl aleard installed"
else 

yum -y install curl
fi
curl http://cps.800pharm.com/ip.php > $target_ipfile
myip=$(cat $target_ipfile | egrep -o '([0-9]{1,3}\.){3}[0-9]{1,3}')
echo $myip
if [ -n $myip ];then
  flag=$(curl http://cps.800pharm.com/ip.txt | grep -c "$myip")
  if [ $flag -gt 0 ];then 
    echo "$myip 该ip已经存在，无需提交"
  else
  echo "正在提交ip....."
  curl -d "ip=$myip&pass=myippasswd" $api_url
  fi
else 
echo "没有获取到ip,请重新执行该脚本"
fi



