mkdir ../package

echo "##############################"
echo "#           Dtrace           #"
echo "##############################"
mkdir ../package/dtrace
wget -t 10 -w 5 -i ../list/dtrace.txt -P ../package/dtrace/

echo "##############################"
echo "#         SystemTap          #"
echo "##############################"
mkdir ../package/systemtap
wget -t 10 -w 5 -i ../list/systemtap.txt -P ../package/systemtap/


