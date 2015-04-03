#!/bin/sh
set -x
tmp_mysql_process="/tmp/tmp_mysql_process"
cat /dev/null > $tmp_mysql_process
/usr/local/mysql/bin/mysql -hlocalhost -uroot -p'xxxxxxx' -e "show processlist;" |grep -E  '(Sleep|Query)' | awk '{print $6" "$5" "$1}' | sort -n >$tmp_mysql_process
cat $tmp_mysql_process |  awk '$1>5 {print $3}'  | while read line
do
/usr/local/mysql/bin/mysqladmin  -hlocalhost -uroot -pdcbicc106 kill $line
done
