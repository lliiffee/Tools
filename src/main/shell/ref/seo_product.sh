 mysql -uroot -pdcbicc106 opencart -e "select concat('product_id=',product_id) as qurey,model as keyword from product ;" | sed 1d | sed 's/\t/","/g' | sed '{s/^/insert into url_alias (query,keyword) VALUES(\"/g;s/$/\");/g}'> seo.txt
mysql -uroot -pdcbicc106 opencart -e "delete from url_alias where query like 'product%';"
mysql -uroot -pdcbicc106 opencart  < seo.txt

