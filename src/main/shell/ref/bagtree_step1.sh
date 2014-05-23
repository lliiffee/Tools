#!/bin/sh
set -x 
if [ $# != 1 ];then
echo "$0 usage $0 catalog_url"
exit 
fi
catalog_url=$1
all_url=/tmp/all_url
cookie_file="/tmp/cookie.txt"
nowtime=$(date '+%F %T')
#模拟登入
curl -c $cookie_file -D d.txt -d "username=6541657&password=xiutuo&remember=0&return_act=./index.php&act=act_login" http://www.bagtree.cn/user.php
curl $catalog_url -b $cookie_file | grep 'lazyload' | grep href | sed 's/=/\r\n/g' | grep html | awk '{print $1}' | awk -F'"' '{print $2}' | sed 's/^/http:\/\/www.bagtree.cn/g' > $all_url 
tmpfile=/tmp/tmp_f
#suolvetu_urlfile=/root/bag/shuolvetu
#geguitu_urlfile=/root/bag/guigetu
#xiangxitu_urlfile/root/bag/xiangxitu
pic_root=/root/bag/
mkdir -p $pic_root
rm -f $tmpfile
#find $pic_root -type f -name "all"  | xargs rm -f 
#find $pic_root -type f -name "g_id"  | xargs rm -f  
#find $pic_root -type f -name "color"  | xargs rm -f
#find $pic_root -type f -name "p_id"  | xargs rm -f
#find $pic_root -type f -name "datu"  | xargs rm -f
#find $pic_root -type f -name "fengge"  | xargs rm -f
#find $pic_root -type f -name "nianlin"  | xargs rm -f
cat $all_url | while read line
do 
#用登入的cookie查看该网站，方便分析出相应的批发价格等
curl -b $cookie_file $line > $tmpfile

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
suolvtu=$(cat $tmpfile | grep 'B_red' | awk  '{print $3}' | awk -F'"' '{print $2}' | sed '/^$/d' |  awk -F'/'  '{print $NF}'  | sed "s/^/http:\/\/img\.800pharm\.com\/bag\/$gid\/big\//g")
#suolvtu=$(echo $suolvtu_url |  awk -F'/'  '{print $NF}'  | sed "s/^/http:\/\/img\.800pharm\.com\/bag\/$gid\/big\//g")
echo "$suolvtu"
cd $pic_root/$gid/big/
cat $tmpfile | grep 'B_red' | awk  '{print $3}' | awk -F'"' '{print $2}' | xargs wget -c 
#wget -c $suolvtu_url


#规格图带有color
guigetu_url=$(cat $tmpfile | grep "$pid" | grep color | sed 's/img/\r\n/g' | grep http | awk -F'"' '{print $2}')
guigetu=$(basename "$guigetu_url" | sed "s/^/http:\/\/img\.800pharm\.com\/bag\/$gid\/guige\//g")

cd $pic_root/$gid/guige/
#cat $tmpfile | grep "$pid" | grep color | sed 's/img/\r\n/g' | grep http | awk -F'"' '{print $2}' | xargs wget -c 
wget -c "$guigetu_url"



#详细图
cd $pic_root/$gid/detail/ 
cat $tmpfile  | grep "$gid" | egrep -v 'color|big' | sed 's/img/\r\n/g' | awk -F'"' '{print $4}' | egrep -v '43.jpg|44.jpg|45.jpg'  | xargs wget -c 
# 成本价格
chengbenjia=$(cat $tmpfile  | grep "memberprice1_1"   | sed 's/y1.gif/\r\n/g' | grep "absmiddle" | awk -F'>' '{print $2}' | awk -F'<' '{print $1}') 
# 销售价
xiaoshoujia=$(cat $tmpfile  | grep 'pro_list_price2'  | sed 's/rmb.gif/\r\n/g'   | grep absmiddle | awk -F'>' '{print $2}' | awk -F'<' '{print $1}')
# 市场价
shichangjia=$(cat $tmpfile | grep -4 'pro_list_price2'  | grep pro_list_price1 | awk -F';' '{print $2}' | awk -F'<' '{print $1}')
# 产品重量
yuanshizhongliang=$(cat /tmp/tmp_f | grep '商品重量:'  | awk '{print $1}'  |awk -F '>' '{print $3}' | sed -re 's/[^0-9]*([0-9]*).*$/\1/;')
# 把小于10的认为是千克
if [ $yuanshizhongliang -lt 10 ];then
zhongliang=$(echo "scale=4;$yuanshizhongliang*1000" | bc )
else
zhongliang=$yuanshizhongliang
fi
#品牌ID 欧时纳60,纽芝兰61,萨蒙斯62,梦梵思63
yuanshipingpai=$(cat /tmp/tmp_f | grep 'brandlogo'  |sed 's/img/\r\n/g'  | sed 1d  |awk -F'"' '{print $2}' |  awk -F'/' '{print $3}' | awk -F'.' '{print $1}')
case "$yuanshipingpai" in
#纽之兰
1294972287832160367)
pingpai="61"
;;
#欧时纳
1294972278572582082)
pingpai="60"
;;
#萨蒙斯
1294972247590437666)
pingpai="62"
;;
#梦凡思
13281188411705598050)
pingpai="63"
;;
*)
pingpai="nopingpai"
;;
esac

##风格
fengge=$(cat /tmp/tmp_f | grep '风格' | awk -F'<' '{print $1}' | awk -F'：' '{print $2}')

##年龄
nianlin=$(cat /tmp/tmp_f | grep '适用年龄' | awk -F'<' '{print $1}' | awk -F'：' '{print $2}')

#详情html
gid_html="$gid.html"
pid_color=$(cat $tmpfile | grep '颜色' | grep span  |awk '{print $1}' | awk -F'：' '{print $2}')
echo "$gid" >> $pic_root/$gid/g_id
echo "$pid" >> $pic_root/$gid/p_id
echo "$pid_color" >> $pic_root/$gid/color
echo "$title" >> $pic_root/$gid/title
echo "$gid,$fengge" >>$pic_root/$gid/fengge
echo "$gid,$nianlin" >>$pic_root/$gid/nianlin
echo "$gid $pid $title $guigetu $chengbenjia $xiaoshoujia $shichangjia $zhongliang $pingpai $pic_root$gid/$gid_html " >> $pic_root/$gid/all
echo "$suolvtu" | sed "s/^/$gid $title /g" >> $pic_root/$gid/datu
cat $tmpfile | grep -2 'span style'   |grep -2  'width:49' | grep -v 'td' > $pic_root/$gid/$gid_html
sed -re "s/_/img.800pharm.com\/bag\/$pid\/detail\/$pid\_/g" /home/shell/sample.html >> $pic_root/$gid/$gid_html
cat $tmpfile  | grep "$gid" | egrep -v 'color|big' | sed 's/img/\r\n/g' | awk -F'"' '{print $4}' | sed '/^$/d' | grep '^http' | sed  '/src=""/d' | egrep -v '43.jpg|44.jpg|45.jpg' | sed "s/^/<img alt=\""$title"\" src=\"/" | sed 's/$/">/' >> $pic_root/$gid/$gid_html
sed -i 's/images.bagtree.cn/img.800pharm.com\/bag/g' $pic_root/$gid/$gid_html
done
