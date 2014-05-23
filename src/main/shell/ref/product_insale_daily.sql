 delete from  e_member.tb_prod_sales_static_history where rec_date=DATE_FORMAT( date_add(SYSDATE(), interval -1 day),'%Y-%m-%d');
 delete from e_member.tb_prod_sale_detail    where rec_date=DATE_FORMAT( SYSDATE(),'%Y-%m-%d');
 delete from e_member.tb_prod_sale_sum   where rec_date=DATE_FORMAT( SYSDATE(),'%Y-%m-%d');
 delete from e_member.tb_product_sale_shop  where rec_date=DATE_FORMAT( SYSDATE(),'%Y-%m-%d');

 insert into e_member.tb_prod_sales_static_history (select *, DATE_FORMAT( date_add(SYSDATE(), interval -1 day),'%Y-%m-%d') as rec_date from e_member.tb_prod_sales_static);
 truncate table  e_member.tb_prod_sales_static;
 insert into e_member.tb_prod_sales_static  (
      SELECT p.pid ,p.shop_code,  pbi.pzwh ,pbi.proname,tm.merchant_name   FROM  ecommerce.tb_product_info p  
      left join ecommerce.tb_merchant tm on p.shop_code=tm.merchant_code 
			LEFT JOIN ecommerce.tb_product_sku_info psi ON p.did = psi.did
      left join ecommerce.tb_product_spec ps on psi.sku_spec=ps.id
     left join ecommerce. tb_product_base_info pbi on ps.base_info_id=pbi.id   	WHERE p.alive = 1 	AND p.verify = 1  and pbi.ISCHUFANG IN(1,2)   );


