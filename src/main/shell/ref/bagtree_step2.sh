#!/bin/sh
set -x 

####生成插入规格图片的sql
pic_root=/root/bag/
sql_guigetu=/root/bag/sql_guigetu
rm -f $sql_guigetu

cd $pic_root
find ./ -type d -name "guige" | sort -u| while read line 
do 
find $line -type f | sed 's/^\./http:\/\/img\.800pharm\.com\/bag/g' | sort -u | while read line
#find $line -type f | sed 's/./upload/g' | while read line

do
jpgname=$(basename $line)
nowtime=$(date '+%F-%T')
echo "INSERT INTO iwebshop_spec_photo ( address,name,create_time ) VALUES (\"$line\",\"$jpgname\",\"$nowtime\");" >> $sql_guigetu
done
done



#####生成插入规格分类的sql
###all文件的格式$gid $pid $title $guigetu $chengbenjia $xiaoshoujia $shichangjia $zhongliang $pingpai $pic_root/$gid/$gid_html
cd $pic_root 

sql_guigetu_catalog=/root/bag/sql_guigetu_catalog
rm -f $sql_guigetu_catalog
spec_name="颜色"
type="2"

#元素格式 a:2:{i:0;s:39:"upload/2012/05/02/20120502103008844.jpg";i:1;s:39:"upload/2012/05/02/20120502103014478.jpg";}


find ./ -type f -name "all" | while read line
do
rm -f /tmp/spec_value_yuanshu
note=$(cat "$line" | sort -u | awk '{print $3}' | sed -n 1p )
spec_value_yuanshu_num=$(cat $line | sort -u| wc -l)
pre="'a:$spec_value_yuanshu_num:{"
pst="}'"
yuanshu=$(cat $line | sort -u| awk '{print $4}')

for ((i=0;i<$spec_value_yuanshu_num;i++));do
#echo $i;

g=$(expr $i + 1)
npic=$(echo "$yuanshu" | sed -rn "$g"p)
nys="i:$i;s:36:\"$npic\";"
echo "$nys" >> /tmp/spec_value_yuanshu
done;
ysall=$(cat /tmp/spec_value_yuanshu | awk '{{printf"%s",$0}}')
spec_value="$pre$ysall$pst"
echo "INSERT INTO iwebshop_spec ( name,value,type,note ) VALUES (\'$spec_name\',"$spec_value",\'$type\',\'$note\');" >>$sql_guigetu_catalog
sed -i 's/\\//g' $sql_guigetu_catalog
done

######## 更新扩展分类###
###产品对应分类ID,女包中斜挎包=118,手提包=119，单肩/斜挎两用包120，手提/斜挎两用包121，双肩包122，多用包123
genxin(){
find ./ -type f -name "g_id" | xargs cat  |sort -u |sed '/^$/d' | while read gno
do
flag=$(mysql -uroot -pdcbicc106 iwebshop -e "select id from  iwebshop_goods where goods_no=$gno" | sed 1d | sed '/^$/d')
if [ $flag -gt 0 ];then
mysql -uroot -pdcbicc106 iwebshop -e "update iwebshop_category_extend set category_id=188 where goods_id=$flag"
else
echo "no match good_no"
fi
done
}

sed -i 's/http:\/\/img.800pharm.com\/bag/upload/g' $sql_guigetu
sed -i 's/http:\/\/img.800pharm.com\/bag/upload/g' $sql_guigetu_catalog
