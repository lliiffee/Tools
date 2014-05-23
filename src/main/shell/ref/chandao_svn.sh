#!/bin/sh
set -x
cd /var/www/html/chandao/bin
/usr/local/php/bin/php ztcli 'http://192.168.0.32/chandao/www/?m=svn&f=run'
