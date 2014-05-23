#!/bin/sh
#set -x
. /etc/profile
svn_checkout_target_dir_pre="/usr/local/tomcat_all/webapps"
svn_update_file_list="/tmp/svn_update_filelist"
cat /dev/null > $svn_update_file_list
svn_user="lanh"
svn_pwd="lh2013"
sample_build_xml_file="/home/shell/sample_build.xml"


nowtime=$(date '+%F')
updatedir=/tmp/$nowtime

help_msg(){
echo "$0: 开始版本号 结束版本号 svn_url地址 项目名 是否重启"
echo "开始版本号如果为n,则默认是该项目的最小版本号"
echo "结束版本号如果为n,则默认是该项目的最大版本号"
echo "svn_url地址：如shop:svn://192.168.0.106/bbf_new_shop/branches/Freight/"
echo "svn_url地址：如b2c: svn://192.168.0.106/ecommerce/b2b/trunk/code"
echo "svn_url地址：如soyao:svn://192.168.0.106/bbf_soyao/trunk"
echo "项目名如果是adminshop：请使用shop为项目名，你懂的！不要问为什么"
echo "是否重启：重启y,不重启n"


}

checkout_newest(){
svn co --username $svn_user --password $svn_pwd $svn_repo_url $svn_checkout_target_dir --force --no-auth-cache 
}

checkout_rivision_range(){
svn co  --username $svn_user --password $svn_pwd -r $to_rivision $svn_repo_url $svn_checkout_target_dir --force --no-auth-cache
}

update_svn_to_rivision(){
svn up  --username $svn_user --password $svn_pwd -r $to_rivision $svn_repo_url $svn_checkout_target_dir --force --no-auth-cache
}

update_svn(){
svn up  --username $svn_user --password $svn_pwd  $svn_repo_url $svn_checkout_target_dir --force --no-auth-cache
}

get_now_rivision(){
now_rivision=$(svn info --username $svn_user --password $svn_pwd $svn_checkout_target_dir  --no-auth-cache | grep "Revision" | awk '{print $2}' | sed '/^$/d')
echo "本地工作空间svn版本：$now_rivision"

}

get_max_rivision(){
max_rivision=$(svn info --username $svn_user --password $svn_pwd $svn_repo_url  --no-auth-cache  | grep "Revision" | awk '{print $2}')
echo "该仓库在svn服务端的当前最大版本号：$max_rivision"

}
get_min_rivision(){
min_rivision=$(svn status -v --username $svn_user --password $svn_pwd    $svn_checkout_target_dir  --no-auth-cache | awk '{print $2}' | grep -v '/' | sort -u | sort -n |   sed -n '1p')
echo "该仓库在svn服务端的当前最小版本号：$min_rivision"
}


check_svn_url_the_same(){
workspace_svn_url=$(svn info --username $svn_user --password $svn_pwd $svn_checkout_target_dir  --no-auth-cache | grep 'URL' | awk '{print $2}')
quxiegan_workspace_svn_url=$(echo $workspace_svn_url |  sed 's#/##g')
if [ "$(echo $svn_repo_url |  sed 's#/##g')" = "$quxiegan_workspace_svn_url" ];then
flag_svn_the_same="1"
#echo $flag_svn_the_same
else
flag_svn_the_same="0"
#echo $flag_svn_the_same
fi

}

cp_build_xml_file(){
scp $sample_build_xml_file  $svn_checkout_target_dir/build.xml
}
get_svn_update_file_list(){
if [ $from_rivision != "n" ];then
org_rivision=$from_rivision
else
org_rivision=$min_rivision
#org_rivision=$now_rivision

fi
cd $svn_checkout_target_dir_pre;
svn diff --username $svn_user --password $svn_pwd --summarize -r$org_rivision:$target_rivision $svn_checkout_target_dir_name | grep "^[AM]"| sed "s#$svn_checkout_target_dir_name/#\n#g" | grep -v ^[AM] | grep -v doc |  sed -n '/\./p' | sort -u > $svn_update_file_list
#sed -i "s#$svn_checkout_target_dir_name/##g" $svn_update_file_list
sed -i "s/^src/$svn_checkout_target_dir_name\/WEB-INF\/classes/g"  $svn_update_file_list
sed -i "s/^WebContent/$svn_checkout_target_dir_name/g"  $svn_update_file_list
sed -i  's/.java/.class/g'  $svn_update_file_list
}

comiple_project(){
cd $svn_checkout_target_dir
ant clean
ant

}


rsync_file_to_real_project(){
 if [  -d $real_project_dir ]; then
 #rm -rf $real_project_dir
  echo "$real_project_dir目录存在"
 else 
 mkdir -p $real_project_dir
 fi
rsync -avg --delete --exclude=.svn $svn_checkout_target_dir/WebContent/ $real_project_dir/
rsync -avg --delete --exclude=.svn $svn_checkout_target_dir/src/resources/ $real_project_dir/WEB-INF/classes/resources/
}

make_realproject_update_files(){

 if [  -d $updatedir ]; then
 rm -rf $updatedir/*
 else 
 mkdir -p $updatedir
 fi

cat $svn_update_file_list | while read line
do

dirname=$(dirname $line)
mkdir -p $updatedir/$dirname/
filename=$(basename $line)
scp $org_dir/$line $updatedir/$dirname/

done

}


rsync_file_to_test_env(){
rsync -avg $updatedir/$svn_checkout_target_dir_name/ 192.168.0.102:$real_project_dir/

}

restart_real_project(){
ssh -f 192.168.0.102 "/home/shell/start_$svn_checkout_target_dir_name.sh"

}



######所有功能模块函数定义结束#####


####组装需要的功能##########

if [ $# != 5 ];then
help_msg;
exit;
fi

from_rivision="$1"
to_rivision="$2"
svn_repo_url="$3"
svn_checkout_target_dir="$svn_checkout_target_dir_pre/$4"
svn_checkout_target_dir_name="$4"
real_project_dir="/usr/local/tomcat_$svn_checkout_target_dir_name/webapps/$svn_checkout_target_dir_name"
org_dir="/usr/local/tomcat_$svn_checkout_target_dir_name/webapps/"
flag_restart="$5"

get_max_rivision;
##判断本地的svn_url地址跟现在传入的地址是否一致，如果不一致。则删除本地workspace
###判断checkout目录是否存在，如果存在，比较workspace版本号now_rivision和传入的版本号to_rivision，
###now_rivision 大于 to_rivision，删除本地的workspace，重新svn checkout
###否则svn update

if [  -d $svn_checkout_target_dir ]; then
  check_svn_url_the_same
  if [ $flag_svn_the_same = 1 ];then

   get_now_rivision;
   if [ $to_rivision != "n" ]; then
      if [ $now_rivision -gt $to_rivision ];then
       rm -rf $svn_checkout_target_dir
      checkout_rivision_range
      target_rivision=$to_rivision     
      else 
      update_svn_to_rivision
       target_rivision=$to_rivision
      fi
   else 
   update_svn
   target_rivision=$max_rivision
   fi
 else
 rm -rf $svn_checkout_target_dir
   if [ $to_rivision != "n" ]; then
   checkout_rivision_range
   target_rivision=$to_rivision
   else
   checkout_newest
   target_rivision=$max_rivision
   echo " $target_rivision"
   fi
 fi
else
   if [ $to_rivision != "n" ]; then
   checkout_rivision_range
   target_rivision=$to_rivision
   else
   checkout_newest
   target_rivision=$max_rivision
   echo " $target_rivision"
   fi
fi


get_min_rivision;
echo "----------创建编译配置文件$svn_checkout_target_dir/build.xml--------------"
cp_build_xml_file;
echo "----------生成更新列表文件$svn_update_file_list-----------------"
get_svn_update_file_list;
echo "----------编译项目$4---------------------------------------------"
comiple_project;
echo "----------模拟$4项目的生成环境-----------------------------------"
rsync_file_to_real_project;
echo "----------生成增量更新文件列表-----------------------------------"
make_realproject_update_files;
#rsync_file_to_test_env;

#if [ $flag_restart = y ];then
#restart_real_project
#echo "项目$svn_checkout_target_dir_name已经更新，并已重启"
#else
#echo "项目$svn_checkout_target_dir_name已经更新，没有重启"
#fi
