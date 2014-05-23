#!/bin/sh
set -x
svn_checkout_target_dir_pre="/usr/local/tomcat_all/webapps"
svn_update_file_list="/tmp/svn_update_filelist"
cat /dev/null > $svn_update_file_list
svn_user="lanh"
svn_pwd="lh2013"
sample_build_xml_file="/home/shell/sample_build.xml"
svn_checkout_target_dir="/usr/local/tomcat_all/webapps/shop"
svn_repo_url="svn://192.168.0.106/bbf_new_shop/branches/Freight/"

check_svn_url_the_same(){
workspace_svn_url=$(svn info --username $svn_user --password $svn_pwd $svn_checkout_target_dir | grep 'URL' | awk '{print $2}')
quxiegan_workspace_svn_url=$(echo $workspace_svn_url |  sed 's#/##g')
if [ "$(echo $svn_repo_url |  sed 's#/##g')" = "$quxiegan_workspace_svn_url" ];then
flag_svn_the_same=1
echo $flag_svn_the_same
else
flag_svn_the_same=0
echo $flag_svn_the_same
fi

}
check_svn_url_the_same

