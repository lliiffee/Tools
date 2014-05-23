ps -ef | grep tomcat_shop | grep -v grep | grep java | awk '{print $2}' | xargs kill -9
sleep 5
ps -ef | grep tomcat_shop | grep -v grep | grep java | awk '{print $2}' | xargs kill -9
sleep 3
rm -rf /usr/local/tomcat_shop/logs/*
rm -rf /usr/local/tomcat_shop/work/Catalina/localhost/*
rm -rf /usr/local/tomcat_shop/temp/*
/usr/local/tomcat_shop/bin/startup.sh 
