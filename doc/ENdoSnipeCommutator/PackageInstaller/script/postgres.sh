#PostgreSQL
echo '##################################'
echo '#           PostgreSQL           #'
echo '##################################'
sudo rpm -ivh /mnt/media/Packages/pkgconfig-0.23-9.1.el6.x86_64.rpm 
sudo rpm -ivh /mnt/media/Packages/ncurses-devel-5.7-3.20090208.el6.x86_64.rpm
sudo rpm -ivh /mnt/media/Packages/readline-devel-6.0-4.el6.x86_64.rpm
sudo rpm -ivh /mnt/media/Packages/zlib-devel-1.2.3-29.el6.x86_64.rpm
sudo rpm -ivh /mnt/media/Packages/make-3.81-20.el6.x86_64.rpm
cd ../package/postgres
tar zxvf postgresql-9.2.4.tar.gz
cd postgresql-9.2.4
./configure --enable-dtrace --enable-debug --prefix=/usr/local/pgsql-9.2.4
sudo make
sudo make install

