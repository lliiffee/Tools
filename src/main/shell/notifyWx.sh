#!/bin/sh
#set -x
###微信支付发货通知
libPath="/home/ana_data/wxpay/lib/"
binPath="/home/ana_data/wxpay/classes"
cpStr="${libPath}aopalliance-1.0.jar:${libPath}commons-beanutils-1.8.0.jar:${libPath}commons-collections-3.2.1.jar:${libPath}commons-dbcp-1.4.jar:${libPath}commons-io-2.4.jar:${libPath}commons-lang-2.5.jar:${libPath}commons-logging-1.1.1.jar:${libPath}commons-pool-1.5.4.jar:${libPath}druid-1.0.1.jar:${libPath}ezmorph-1.0.6.jar:${libPath}fastjson-1.1.37.jar:${libPath}jettison-1.1.jar:${libPath}json-lib-2.4-jdk15.jar:${libPath}mysql-connector-java-5.1.27.jar:${libPath}snakeyaml-1.13.jar:${libPath}spring-aop-3.2.5.RELEASE.jar:${libPath}spring-batch-core-2.2.1.RELEASE.jar:${libPath}spring-batch-infrastructure-2.2.1.RELEASE.jar:${libPath}spring-beans-3.2.5.RELEASE.jar:${libPath}spring-context-3.2.5.RELEASE.jar:${libPath}spring-core-3.2.5.RELEASE.jar:${libPath}spring-expression-3.2.5.RELEASE.jar:${libPath}spring-jdbc-3.2.5.RELEASE.jar:${libPath}spring-retry-1.0.2.RELEASE.jar:${libPath}spring-tx-3.2.0.RELEASE.jar:${binPath}:."

#echo  ${cpStr}

java -classpath ${cpStr} com.bbf.wxpay.NotifyDeliveryAction
