ps -ef | grep tomcat_b2c | grep -v grep | grep java | awk '{print $2}' | xargs kill -9 
sleep 5
ps -ef | grep tomcat_b2c | grep -v grep | grep java | awk '{print $2}' | xargs kill -9 
sleep 3
rm -f /usr/local/tomcat_b2c/logs/*
rm -rf /usr/local/tomcat_b2c/work/Catalina/localhost/*
/usr/local/tomcat_b2c/bin/startup.sh 
