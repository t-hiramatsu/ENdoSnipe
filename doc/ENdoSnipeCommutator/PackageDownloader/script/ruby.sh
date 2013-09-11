mkdir ../package

echo "##############################"
echo "#            Ruby            #"
echo "##############################"
mkdir ../package/ruby
wget -t 10 -w 5 -i ../list/ruby.txt -P ../package/ruby/


