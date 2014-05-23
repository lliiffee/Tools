#!/bin/sh
/usr/local/apache/bin/apachectl stop
sleep 5
/usr/local/apache/bin/apachectl stop
sleep 3
chown -R daemon:daemon /svnroot/
chown -R daemon:daemon /usr/local/apache/
/usr/local/apache/bin/apachectl start
