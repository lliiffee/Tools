#!/bin/sh
. /etc/profile
ps -ef | grep tomcat_soyao | grep -v grep | grep java | awk '{print $2}' | xargs kill -9 
sleep 5
ps -ef | grep tomcat_soyao | grep -v grep | grep java | awk '{print $2}' | xargs kill -9 
sleep 3
rm -f /usr/local/tomcat_soyao/logs/*
rm -rf  /usr/local/tomcat_soyao/work/Catalina/localhost/*
#rm -f /var/log/tomcat_soyao/*
/usr/local/tomcat_soyao/bin/startup.sh 
