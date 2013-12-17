mkdir ../package

echo "##############################"
echo "#           Apache           #"
echo "##############################"
mkdir ../package/apache
wget -t 10 -w 5 -i ../list/apache.txt -P ../package/apache/


