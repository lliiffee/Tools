#!/bin/sh
. /etc/profile
ps -ef | grep tomcat_mobile | grep -v grep | grep java | awk '{print $2}' | xargs kill -9 
sleep 5
ps -ef | grep tomcat_mobile | grep -v grep | grep java | awk '{print $2}' | xargs kill -9 
sleep 3
rm -f /usr/local/tomcat_mobile/logs/*
rm -rf  /usr/local/tomcat_mobile/work/Catalina/localhost/*
#rm -f /var/log/tomcat_mobile/*
/usr/local/tomcat_mobile/bin/startup.sh 
