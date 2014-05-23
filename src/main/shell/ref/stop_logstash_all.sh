#!/bin/sh
ps -ef | grep java | grep logstash | grep 'shiper' | awk '{print $2 }' | xargs kill -9
#java -jar /usr/local/loghash/logstash-1.1.10-flatjar.jar agent -f /etc/logstash/apache_shiper.conf &
