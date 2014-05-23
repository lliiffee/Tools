ps -ef | grep tomcat_quartz | grep -v grep | grep java | awk '{print $2}' | xargs kill -9 
sleep 5
ps -ef | grep tomcat_quartz | grep -v grep | grep java | awk '{print $2}' | xargs kill -9 
sleep 3
#rm -f /usr/local/tomcat_quartz/logs/*
rm -rf  /usr/local/tomcat_quartz/work/Catalina/*
#rm -f /var/log/tomcat_quartz/*
/usr/local/tomcat_quartz/bin/startup.sh 
