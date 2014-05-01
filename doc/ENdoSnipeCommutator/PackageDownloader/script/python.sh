mkdir ../package

echo "##############################"
echo "#           Python           #"
echo "##############################"
mkdir ../package/python
wget -t 10 -w 5 -i ../list/python.txt -P ../package/python/

