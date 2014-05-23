#!/bin/sh
set -x 
pic_root=/root/bag/
rm -f $pic_root/sql_datu
###构造产品图片
## datu格式 $gid $title $suolvtu
cd $pic_root
find ./ -type f -name "datu" |   while read line 
do
imgs=$(cat $line | sort -u | awk '{print $3}')
echo "$imgs" >>$pic_root/sql_datu
done
pre="INSERT INTO iwebshop_goods_photo ( id,img ) VALUES ( "
myuuid="replace(UUID(),'-','')"
mid=",'"
last="');"
sed -i '/^$/d' $pic_root/sql_datu
sed -i "s/^/$pre$myuuid$mid/g" $pic_root/sql_datu
sed -i "s/$/$last/g" $pic_root/sql_datu
sed -i "s/http:\/\/img.800pharm.com\/bag/upload/g" $pic_root/sql_datu


