mysql -uroot -pdcbicc106 ecommerce -e "UPDATE tb_member SET email='test800pahrm@800pahrm.com';"

mysql -uroot -pdcbicc106 ecommerce -e "UPDATE tb_config SET CONFVALUE='test800pahrm@800pahrm.com' WHERE CONFNAME='order_alert_email';"

mysql -uroot -pdcbicc106 ecommerce -e "SELECT * FROM tb_config WHERE CONFVALUE LIKE '%@%';"
