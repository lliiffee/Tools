#!/bin/sh
ps -ef | grep java | grep logstash | grep 'shiper' | awk '{print $2 }' | xargs kill -9
sleep 30 
#redis-cli flushall
#sleep 10
#ps -ef | grep java | grep logstash | grep 'indexer' | awk '{print $2 }' | xargs kill -9
#sleep 20
#ps -ef | grep java | grep elasticsearch  | awk '{print $2 }' | xargs kill -9
#rm -rf /usr/local/elasticsearch/data/*

#/usr/local/elasticsearch/bin/elasticsearch
#java -jar /usr/local/loghash/logstash-1.1.10-flatjar.jar agent -f /etc/logstash/apache_indexer.conf &
java -jar /usr/local/loghash/logstash-1.1.10-flatjar.jar agent -f /etc/logstash/apache_shiper.conf &
