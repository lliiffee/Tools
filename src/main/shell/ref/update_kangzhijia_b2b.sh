#!/bin/bash
#rm -rf /var/www/html/kangzhijia3/b2c/media/catalog/product/cache/*
cd /var/www/html/kzj_b2b_v2/
svn up 
chown -R daemon:daemon /svnroot/
chown -R www:www /var/www/html
#rm -rf /var/www/html/kangzhijia3/b2c/var/cache/*
#service nginx restart
