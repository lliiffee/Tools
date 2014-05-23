#!/bin/sh
#set -x
if [ $# != 1 ];then
echo "useage:$0 输入文件列表文件"
exit;
fi

filelist="/tmp/filelist_tmp"

##shop svn地址：如svn://192.168.0.106/bbf_shop/,最后有斜杠哦，切记
svn_shop_url="svn://192.168.0.106/bbf_new_shop/branches/Freight/"

##请修改这个参数:shop checkout 后对应的路径
svn_up_dir="/usr/local/tomcat_all/webapps/weight"

##真实的shop环境路径，无需修改
tartget_dir="/usr/local/tomcat_shop/webapps/shop"


cat $1 | grep -v '#' | sed  '/^$/d' | sort -u > $filelist

sed -i "s#$svn_shop_url##g" $filelist
sed -i 's/^src/shop\/WEB-INF\/classes/g' $filelist
sed -i  's/^WebContent/shop/g' $filelist 
sed -i 's/.java/.class/g' $filelist 

cat $filelist
