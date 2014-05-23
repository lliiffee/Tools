#!/bin/sh
. /etc/profile
####vars
DDATE=`date +"%Y-%m-%d"`
nowtime=$(date "+%F")
logtime=$(date "+%F %T")
shellPath="/usr/local/member_static"
dataPath="/usr/local/member_static/files"
#shellPath="/home/mobile_shop/data-integration"
#dataPath="/usr/local/member_static/files"
mailserver="smtp.800pharm.com"
sender="alert@800pharm.com"
#receiver="yongfeng.li@800pharm.com"
receiver="yxy@800pharm.com"
sender_pwd="alert2012"
subject="vip coupon drawback log daily report"
alertbin=/home/shell/sendEmail
att_file="${dataPath}/drawback_daily_vipcoupon${DDATE}.csv"



#${shellPath}/pan.sh /file ${dataPath}/drawback_daily_vipcoupon.ktr /norep  -debug=debug  -log ${dataPath}/drawback_log.log

echo "begin gen report"
${shellPath}/pan.sh /file ${shellPath}/files/drawback_daily_vipcoupon.ktr /norep  -debug=debug  -log ${dataPath}/drawback_log.log
echo "end gen report"

if [  -s "$att_file" ]; then
  lineCnt=`awk 'NR>1'  ${att_file} | wc -l`
  body="hi \n\n\n total: ${lineCnt} records \n please ref attachment!\n\n"
  echo "${body}"
  echo "begin send mail"
  $alertbin -s $mailserver  -f $sender -t $receiver -u $subject -m $body -xu $sender -xp $sender_pwd -a $att_file  2>&1 >/dev/null
  echo "end send mail"
 else
  body="hi \n\n\n total: 0 records \n no attachment!\n\n"
  echo "${body}"
   echo "begin send mail"
  $alertbin -s $mailserver  -f $sender -t $receiver -u ${subject} -m "${body}" -xu $sender -xp $sender_pwd   2>&1 >/dev/null
fi



echo "begin delete file"
rm -f ${att_file}
echo "job done!"
