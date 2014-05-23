#!/bin/bash
cd /var/www/html/zhonglian/
rm -rf /var/www/html/zhonglian/trunk/var/cache*
svn up 
chown -R daemon:daemon /svnroot/
chown -R www:www /var/www/html
rm -rf /var/www/html/zhonglian/trunk/var/cache*
#service nginx restart
