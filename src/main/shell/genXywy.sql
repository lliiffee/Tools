 select concat('<item><uuid>',p.pid,'</uuid>' ,'<name>',PRONAME,'</name>' ,'<approvalnumber>',pbi.pzwh,'</approvalnumber>','<price>', shop_price
 ,'</price>','<url>http://www.800pharm.com/shop/product-',p.shop_code,'-',p.pid,'.html?from_url=xywy</url>','<specification>',ps.specification,'</specification>'
 ,'<image></image>' ,'<picurl>http://img.800pharm.com/images/',psi.mainImg,'</picurl>'
 ,'<goodsdescription></goodsdescription><quantity>',p.storage,'</quantity><state>1</state><contactnumber>400-8855171</contactnumber></item>' )
 
 INTO OUTFILE '/home/ana_data/tmp/result.txt'  LINES TERMINATED BY '\n'
 
 from tb_product_info p
left join tb_product_sku_info psi on p.did=psi.did
left JOIN  tb_product_spec ps ON ps.id = psi.sku_spec
 left JOIN tb_product_base_info pbi ON pbi.id = ps.base_info_id
 where p.isDel=0 and  p.alive=1 and  p.verify=1 and (pbi.ischufang=1 or pbi.ischufang=2) and ps.specification is not null and ps.specification!='' and p.storage > 0;
