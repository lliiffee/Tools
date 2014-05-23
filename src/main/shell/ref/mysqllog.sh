#!/bin/sh
datadir="/var/lib/mysql"
logfile_pre="mysql-bin."
time=$(date '+%F-%k-%M')
if [ $# != 3 ];then
echo "$0 usage $0 starttime stoptime logfilename"
exit
fi
starttime="$1"
stoptime="$2"
logfile="$datadir/$logfile_pre$3"
sql=/root/sql.$time
mysqlbinlog   --start-datetime="$starttime"  --stop-datetime="$stoptime" $logfile | grep  '^INSERT INTO' > $sql
cat $sql
