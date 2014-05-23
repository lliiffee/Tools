#!/bin/sh
rsync -avg --delete search.800pharm.com:/web/soyao/ /web/soyao/
/home/shell/start_shop.sh
