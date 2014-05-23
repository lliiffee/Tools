cat lst | while read line
do
fileurl="$1/$line"
echo $fileurl
dir=`dirname $line`
echo $dir
mkdir -p $dir
wget -c $fileurl -P $dir
sleep 3

done
