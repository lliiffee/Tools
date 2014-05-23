#!/bin/sh
set -x 


####产品sql
## datu格式 $gid $title $suolvtu
## all文件的格式$gid $pid $title $guigetu $chengbenjia $xiaoshoujia $shichangjia $zhongliang $pingpai $pic_root/$gid/$gid_html
pic_root=/root/bag/

cd $pic_root
rm -f $pic_root/goods.sql 
find ./ -type f -name "*html" | while read html
do
gid=$(basename $html | awk -F'.' '{print $1}')
altpic=$(cat $(dirname $html)/title | sed -n 1p)
echo $altpic
sed '/<br>/d' $html
sed -i '/jpg/d'   $html
sed -i '/gif/d'  $html
sed -re "s/_/img.800pharm.com\/bag\/$gid\/detail\/$gid\_/g" /home/shell/sample.html >> $html
sed -i "s/myaltpic/$altpic/g" $html
done


find ./ -type f -name "datu" | sed '/^$/d' | sort -u  |   while read line 
do

pic_num=$(cat $line | sort -u | wc -l)
shu=$( expr $pic_num \* 40 )
pre="a:$pic_num:{i:0;a:4:{s:2:\""id\"";s:2:\""spec_id"\";s:5:\""value\"";s:$shu:\""
pic=$(cat $line |  sort -u |awk '{print $3}' | sed 's/$/,/g' | awk '{{printf"%s",$0}}')
pst="\";s:4:\"type\";s:1:\"2\";s:4:\"name\";s:6:\"颜色\";}}"

name=$(cat $line | sort -u | awk '{print $2}'| sed -n 1p)
goods_no=$(cat $line | sort -u | awk '{print $1}'| sed -n 1p)
#model_id 模块id:女包=188,男包=189,钱包手包=190,户外旅行包=192,电脑包=193
model_id="188"
sell_price=$(cat $pic_root/$goods_no/all | grep "$goods_no" | sort -u | awk '{print $6}' | sed -n 1p )
market_price=$(cat $pic_root/$goods_no/all | grep "$goods_no" | sort -u | awk '{print $7}' | sed -n 1p )
cost_price=$(cat $pic_root/$goods_no/all | grep "$goods_no" | sort -u | awk '{print $5}' | sed -n 1p )
store_nums="100"
brand_id=$(cat $pic_root/$goods_no/all | grep "$goods_no" | sort -u | awk '{print $9}' | sed -n 1p )

#把颜色整合到商品详情
cd $pic_root/
all_color=$(cat $pic_root/$goods_no/color | sort -u | awk '{{printf"%s",$0}}')
num_color=$(cat $pic_root/$goods_no/color | sort -u | wc -l)
sed -i "/颜色/ c\\颜色：$all_color" $pic_root/$gid_html
pre_desc="提供$num_color中颜色：$all_color,"

content=$(cat $pic_root/$goods_no/all | grep "$goods_no" | sort -u | awk '{print $10}' | sed -n 1p |  xargs cat)
is_del="0"
create_time=$(date '+%F-%T')
keywords=$(cat $pic_root/$goods_no/all | grep "$goods_no" | sort -u | awk '{print $3}' | sed -n 1p )
description="$name:$pre_desc"
weight=$(cat $pic_root/$goods_no/all | grep "$goods_no" | sort -u | awk '{print $8}' | sed -n 1p )
unit=""
sort="99"
spec_array="$pre$pic$pst"
visit="1000"
favorite="368"
point="0"
exp="0"
small_img=$(cat $line |awk '{print $3}' | sort -u | sed -n 1p | sed 's/\.jpg/_100_100\.jpg/g')
img=$(cat $line |awk '{print $3}' | sort -u | sed -n 1p )
list_img=$(cat $line |awk '{print $3}' | sort -u | sed -n 1p | sed 's/\.jpg/_175_175\.jpg/g')
cat /dev/null >  /tmp/tmp.sql

echo "'"$name"'," > /tmp/tmp.sql
echo "'"$goods_no"'," >> /tmp/tmp.sql
echo "'"$model_id"'," >> /tmp/tmp.sql
echo "'"$sell_price"'," >> /tmp/tmp.sql
echo "'"$market_price"'," >> /tmp/tmp.sql
echo "'"$cost_price"'," >> /tmp/tmp.sql
echo "'"$store_nums"'," >> /tmp/tmp.sql
echo "'"$brand_id"'," >> /tmp/tmp.sql
echo "'"$content"'," >> /tmp/tmp.sql
echo "'"$is_del"',"  >> /tmp/tmp.sql
echo "'"$create_time"'," >> /tmp/tmp.sql
echo "'"$keywords"'," >> /tmp/tmp.sql
echo "'"$description"'," >> /tmp/tmp.sql
echo "'"$weight"'," >> /tmp/tmp.sql
echo "'"$unit"'," >> /tmp/tmp.sql
echo "'"$sort"'," >> /tmp/tmp.sql
echo "'"$spec_array"'," >> /tmp/tmp.sql
echo "'"$visit"'," >> /tmp/tmp.sql
echo "'"$favorite"'," >> /tmp/tmp.sql
echo "'"$point"'," >> /tmp/tmp.sql
echo "'"$exp"'," >> /tmp/tmp.sql
echo "'"$small_img"'," >> /tmp/tmp.sql
echo "'"$img"'," >> /tmp/tmp.sql
echo "'"$list_img"'" >> /tmp/tmp.sql

#sed -i "s/^/\'/g" /tmp/tmp.sql 
#sed -i "s/$/,\'/g" /tmp/tmp.sql 
#sed -i 's/.$//'/tmp/tmp.sql 
cat >> "$pic_root"/goods.sql<<EOF
INSERT INTO iwebshop_goods 
( 
name, 
goods_no,
model_id,
sell_price,
market_price, 
cost_price,   
store_nums,   
brand_id,    
content,      
is_del,       
create_time,  
keywords,     
description, 
weight,        
unit,         
sort,         
spec_array,   
visit,       
favorite,     
point,      
exp,         
small_img,     
img,           
list_img)    
VALUES (
EOF
cat /tmp/tmp.sql >> $pic_root/goods.sql
echo ");" >>$pic_root/goods.sql
done




