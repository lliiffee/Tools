#!/bin/sh
###生成寻医问药对接数据###
#set -x
. /etc/profile
dateStr=`date +"%Y-%m-%d %H:%M:%S"`
mysqlBin="/usr/local/mysql/bin"
data_path="/home/ana_data/tmp"
shell_path="/home/ana_data"

echo "gen xywy API begin....."
rm -rf ${data_path}/*
${mysqlBin}/mysql -uroot -pdcbicc106 -D ecommerce <  ${shell_path}/genXywy.sql


sed 's/&/*/g' ${data_path}/result.txt > ${data_path}/result_tmp.txt 
split -l 4000 ${data_path}/result_tmp.txt ${data_path}/bbf_prod
rm -f ${data_path}/result*.txt
sed -i '1i\<?xml version="1.0" encoding="UTF-8"?> <urlset>' ${data_path}/bbf*
sed -i '$a\</urlset>' ${data_path}/bbf*

echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?><sitemapindex><lastmod>${dateStr}</lastmod><changefreq>24</changefreq>" > ${data_path}/sitemapindex.xml

for filename in ` find ${data_path}/bbf* -type f `
do
  mv $filename  ${filename}.xml
  echo  "<sitemap><loc>http://www.800pharm.com/xywy_api/${filename##*/}.xml</loc></sitemap>" >> ${data_path}/sitemapindex.xml
done
echo "</sitemapindex>" >> ${data_path}/sitemapindex.xml

echo "gen xywy API done....."
