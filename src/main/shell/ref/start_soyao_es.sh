ps -ef | grep tomcat_soyao_es | grep -v grep | grep java | awk '{print $2}' | xargs kill -9 
sleep 5
ps -ef | grep tomcat_soyao_es | grep -v grep | grep java | awk '{print $2}' | xargs kill -9 
sleep 3
rm -f /usr/local/tomcat_soyao_es/logs/*
rm -rf  /usr/local/tomcat_soyao_es/work/Catalina/localhost/*
#rm -f /var/log/tomcat_soyao_es/*
/usr/local/tomcat_soyao_es/bin/startup.sh 
