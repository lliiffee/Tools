#!/bin/bash
cd /var/www/html/kzj-guahao
svn up 
chown -R daemon:daemon /svnroot/
chown -R www:www /var/www/html
#service nginx restart
