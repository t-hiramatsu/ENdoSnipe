mkdir ../package

echo "##############################"
echo "#            PHP             #"
echo "##############################"
mkdir ../package/php
wget -t 10 -w 5 -i ../list/php.txt -P ../package/php/
cp ../patch/php.patch ../package/php/

