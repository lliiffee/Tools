/usr/local/amoeba-mysql/bin/amoeba stop

sleep 5
/usr/local/amoeba-mysql/bin/amoeba stop
sleep 3
rm -f /usr/local/amoeba-mysql/logs/*
/usr/local/amoeba-mysql/bin/amoeba start




