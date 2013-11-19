mkdir ../package

echo "##############################"
echo "#         PostgreSQL         #"
echo "##############################"
mkdir ../package/postgres
wget -t 10 -w 5 -i ../list/postgres.txt -P ../package/postgres/

