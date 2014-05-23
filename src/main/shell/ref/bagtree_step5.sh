#!/bin/sh
set -x 
if [ $# != 1 ];then
echo "$0 usage $0 calalog_extend id"
echo "产品对应分类ID,女包中斜挎包=118,手提包=119，单肩/斜挎两用包120，手提/斜挎两用包121，双肩包122，多用包123"
######## 更新扩展分类###
###产品对应分类ID,女包中斜挎包=118,手提包=119，单肩/斜挎两用包120，手提/斜挎两用包121，双肩包122，多用包123
exit
fi
cid=$1
find ./ -type f -name "g_id" | xargs cat  |sort -u |sed '/^$/d' | while read gno
do
flag=$(mysql -h192.168.0.34 -uroot -pdcbicc106 iwebshop -e "select id from  iwebshop_goods where goods_no=$gno and is_del=0" | sed 1d | sed '/^$/d')
if [ "$flag" -gt 0 ];then
mysql -h192.168.0.34 -uroot -pdcbicc106 iwebshop -e "update iwebshop_category_extend set category_id=$cid where goods_id=$flag"
else
echo "no match good_no"
fi
done
