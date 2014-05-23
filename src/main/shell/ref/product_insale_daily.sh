#!/bin/sh
. /etc/profile
####vars
DDATE=`date +"%Y-%m-%d"`
nowtime=$(date "+%F")
logtime=$(date "+%F %T")
#shellPath="/usr/local/member_static"
#dataPath="/usr/local/member_static/files"
shellPath="/home/mobile_shop/data-integration"
dataPath="/usr/local/member_static/files"
mailserver="smtp.800pharm.com"
sender="alert@800pharm.com"
receiver="susan.huang@800pharm.com,yongfeng.li@800pharm.com"

sender_pwd="alert2012"
subject="product on sale summary daily report"
alertbin=/home/shell/sendEmail
att_file1="${dataPath}/product_sum_daily${DDATE}.xls"
att_file2="${dataPath}/product_sum_daily_by_shop${DDATE}.xls"
att_file3="${dataPath}/product_detail_daily${DDATE}.xls"

mysql_host="192.168.0.32"
db_username="root"
password="dcbicc106"
init_sql_file="${shellPath}/product_insale_daily.sql"

echo "init data"
mysql -u${db_username} -p${password} -D e_member -h${mysql_host} <${init_sql_file}
echo "init data end"

echo "begin gen product_sum_daily data to table"
${shellPath}/pan.sh /file ${shellPath}/files/product_summary.ktr /norep  -debug=debug  -log ${dataPath}/drawback_log.log
echo "end gen  product_sum_daily data to table"


echo "begin gen product_sum_daily report"
${shellPath}/pan.sh /file ${shellPath}/files/gen_product_sum.ktr /norep  -debug=debug  -log ${dataPath}/drawback_log.log
echo "end gen  product_sum_daily report"


echo "begin gen product_sum_daily_by_shop report"
${shellPath}/pan.sh /file ${shellPath}/files/product_summary_by_shop.ktr /norep  -debug=debug  -log ${dataPath}/drawback_log.log
echo "end gen product_sum_daily_by_shop report"


echo "begin gen product_detail_daily report"
${shellPath}/pan.sh /file ${shellPath}/files/product_detail_insale.ktr /norep  -debug=debug  -log ${dataPath}/drawback_log.log
echo "end gen product_detail_daily report"


if [  -s "$att_file1" ]; then
  
  body="hi \n\n\n \n please ref attachment!\n\n"
  echo "${body}"
  echo "begin send mail"
  $alertbin -s $mailserver  -f $sender -t $receiver -u $subject -m $body -xu $sender -xp $sender_pwd -a $att_file1 $att_file2 $att_file3  2>&1 >/dev/null
  echo "end send mail"
 else
  body="hi \n\n\n total: 0 records \n no attachment!\n\n"
  echo "${body}"
   echo "begin send mail"
  $alertbin -s $mailserver  -f $sender -t $receiver -u ${subject} -m "${body}" -xu $sender -xp $sender_pwd   2>&1 >/dev/null
fi



echo "begin delete file"
rm -f ${att_file1} ${att_file2} ${att_file3}
echo "job done!"
