#!/bin/sh
set -x 
if [ $# != 1 ];then
echo "$0 usage $0 catalog_url"
exit 
fi
catalog_url=$1
all_url=/tmp/all_url
curl $catalog_url | grep 'lazyload' | grep href | sed 's/=/\r\n/g' | grep html | awk '{print $1}' | awk -F'"' '{print $2}' | sed 's/^/http:\/\/www.bagtree.cn/g' > $all_url 
tmpfile=/tmp/tmp_f
#suolvetu_urlfile=/root/bag/shuolvetu
#geguitu_urlfile=/root/bag/guigetu
#xiangxitu_urlfile/root/bag/xiangxitu
pic_root=/root/bag/
mkdir -p $pic_root
rm -f $tmpfile
cat $all_url | while read line
do 
curl $line > $tmpfile

# 商品编号（同一款下有多个颜色不一样的）
gid=$(cat $tmpfile | grep '包包编号' | grep '</li>' | awk -F':' '{print $2}' | awk  '{print $1}'   | awk -F'-' '{print $1}') 
# 货号
pid=$(cat $tmpfile | grep '包包编号' | grep '</li>' | awk -F':' '{print $2}' | awk  '{print $1}') 

mkdir -p $pic_root/$gid/big/
mkdir -p $pic_root/$gid/guige/
mkdir -p $pic_root/$gid/detail/
#产品标题
title=$(cat $tmpfile  | grep -2 '包包编号'  | grep '</li>' | sed 2d | awk -F'>' '{print $2}' | awk  '{print $1}')
# 缩略图带有big
cd $pic_root/$gid/big/
cat $tmpfile | grep 'B_red' | awk  '{print $3}' | awk -F'"' '{print $2}' | xargs wget -c 

#规格图带有color
cd $pic_root/$gid/guige/
cat $tmpfile | grep "$gid" | grep color | sed 's/img/\r\n/g' | grep http | awk -F'"' '{print $2}' | xargs wget -c 
#详细图
cd $pic_root/$gid/detail/ 
cat $tmpfile  | grep "$gid" | egrep -v 'color|big' | sed 's/img/\r\n/g' | awk -F'"' '{print $4}' | egrep -v '43.jpg|44.jpg|45.jpg'  | xargs wget -c 

#详情html
gid_html="$gid.html"
pid_color=$(cat $tmpfile | grep '颜色' | grep span  |awk '{print $1}' | awk -F'：' '{print $2}')
echo "$gid" >> $pic_root/$gid/g_id
echo "$pid" >> $pic_root/$gid/p_id
echo "$pid_color" >> $pic_root/$gid/color
cat $tmpfile | grep -2 'span style'   |grep -2  'width:49' | grep -v 'td' > $pic_root/$gid/$gid_html
echo "</br>" >> $pic_root/$gid/$gid_html
cat $tmpfile  | grep "$gid" | egrep -v 'color|big' | sed 's/img/\r\n/g' | awk -F'"' '{print $4}' | sed '/^$/d' | grep '^http' | sed  '/src=""/d' | egrep -v '43.jpg|44.jpg|45.jpg' | sed "s/^/<img alt=\""$title"\" src=\"/" | sed 's/$/">/' >> $pic_root/$gid/$gid_html
done

#把颜色整合到商品详情
cd $pic_root/
all_color=$(cat $pic_root/$gid/color | sort -u | awk '{{printf"%s/",$0}}')
sed -i "/颜色/ c\\颜色：$all_color" $pic_root/$gid_html
