#!/bin/sh
#set -x
mysql -h192.168.0.32  -uroot -pdcbicc106 ecommerce -e "select MERCHANT_CODE,DOMAIN from tb_merchant;" | sed 1d > /tmp/merchant.txt
conf_file='/tmp/vhost.txt'
cat /tmp/merchant.txt | while read line
do
shop_code=`echo $line | awk '{print $1}'`
domain=`echo $line | awk '{print $2}' | awk -F'/' '{print $3}'`
echo "$domain"
echo "$shop_code"
flag=$(cat $conf_file | grep -c "\$shop_code")
if [ $flag -lt 1 ];then

cat >> $conf_file <<EOF
###################$domain  ##############################################
<VirtualHost *:80>
      DocumentRoot "/usr/local/apache/htdocs"
      ServerName $domain
ProxyRequests Off      
<proxy balancer://cluster>      
BalancerMember ajp://192.168.1.102:8009 loadfactor=1 route=node102 
BalancerMember ajp://192.168.1.103:8009 loadfactor=1 route=node103  
BalancerMember ajp://192.168.1.104:8009 loadfactor=1 route=node104
BalancerMember ajp://192.168.1.105:8009 loadfactor=1 route=node105
</proxy> 
ProxyPass / balancer://cluster/shop/index-$shop_code.html stickysession=jsessionid nofailover=On      
ProxyPassReverse / balancer://cluster/shop/index-$shop_code.html  
</VirtualHost>
EOF
#tail -n 12 $conf_file
else 
echo "merchant id=$shop_code aleardy exit,domain:$domain exit,please check!"
fi

done

