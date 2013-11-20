mkdir ../package

echo "##############################"
echo "#           MySQL            #"
echo "##############################"
mkdir ../package/mysql
wget -t 10 -w 5 -i ../list/mysql.txt -P ../package/mysql/


