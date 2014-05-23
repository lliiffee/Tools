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

svn co $svn_shop_url $svn_up_dir
cd $svn_up_dir
ant clean
ant

cat "$1" | grep -v '#' | sed  '/^$/d' | sort -u > $filelist

sed -i "s#$svn_shop_url##g" $filelist
sed -i 's/^src/shop\/WEB-INF\/classes/g' $filelist
sed -i  's/^WebContent/shop/g' $filelist 
sed -i 's/.java/.class/g' $filelist 


mkdir -p $tartget_dir

rsync -avg --delete --exclude=.svn $svn_up_dir/WebContent/ $tartget_dir
rsync -avg --delete --exclude=.svn $svn_up_dir/src/resources/ $tartget_dir/WEB-INF/classes/resources/

nowtime=$(date '+%F')
updatedir=/root/$nowtime
org_dir=/usr/local/tomcat_shop/webapps/
mkdir -p $updatedir
rm -rf $updatedir/*

cat $filelist | while read line
do

dirname=$(dirname $line)
mkdir -p $updatedir/$dirname/
filename=$(basename $line)
scp $org_dir/$line $updatedir/$dirname/

done


rsync -avg $updatedir/shop/ 192.168.0.102:/usr/local/tomcat_shop/webapps/shop/
rsync -avg $updatedir/shop/ 192.168.0.102:/usr/local/apache/htdocs/shop/
#ssh -f 192.168.0.102 "/home/shell/start_shop.sh"
sleep 20
ps -ef | grep 'ssh -f' | grep start | awk '{print $2}' | xargs kill -9
