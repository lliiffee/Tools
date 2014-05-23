#!/bin/sh
set -x 

pic_root=/root/bag/

cd $pic_root
rm -f $pic_root/spec.sql
find ./ -type f -name "datu" | sort -u |   while read line 
do
  cat $line | sort -u |  sed '/^$/d' |  while read num
    do
	gid=$(echo $num  | awk '{print $1}')
	pic=$(echo $num  | awk '{print $3}'| sed 's/http:\/\/img.800pharm.com\/bag/upload/g')
        pre='2|'
	spec_value="$pre$pic"
	model_id="188"
	spec_id_num=$(mysql -h192.168.0.34 -uroot -pdcbicc106 iwebshop -e "select id from  iwebshop_spec where value like '%$gid%'" | sed 1d | sed '/^$/d' | wc -l)
    spec_id=$(mysql -h192.168.0.34 -uroot -pdcbicc106 iwebshop -e "select id from  iwebshop_spec where value like '%$gid%'" | sed 1d | sed '/^$/d')
    if [ "$spec_id_num" -gt 1 ];then
    echo "有多个gid对应多个spec_id，数据有重复"
    exit
    fi
#     mysql -h192.168.0.34 -uroot -pdcbicc106 iwebshop -e "insert into iwebshop_goods_attribute ( goods_id,spec_id,spec_value,model_id ) VALUES ($gid,$spec_id,$model_id,'\"$spec_value\"');"
   
cat /dev/null >  /tmp/tmp.sql

echo "'"$gid"'," > /tmp/tmp.sql
echo "'"$spec_id"'," >> /tmp/tmp.sql
echo "'"$spec_value"'," >> /tmp/tmp.sql
echo "'"$model_id"'" >> /tmp/tmp.sql

cat >> $pic_root/spec.sql<<EOF
insert into iwebshop_goods_attribute 
( 
goods_id,
spec_id,
spec_value,
model_id 
) 
VALUES
(
EOF
cat /tmp/tmp.sql >> $pic_root/spec.sql
echo ');' >>$pic_root/spec.sql

	done
done
