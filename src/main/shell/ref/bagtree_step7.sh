update  iwebshop_goods set spec_array="(select replace(spec_array,'spec_id',(select  a.id  from iwebshop_spec a,iwebshop_goods b where a.value like CONCAT('%',b.goods_no,'%'))) from iwebshop_goods)"
