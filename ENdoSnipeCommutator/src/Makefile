#
# Make file for ENdoSnipe Commutator
#

all : ens_commutator

CC=gcc
LDFLAGS=
CFLAGS= -I.
LDLIBS=

%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

ens_commutator: ens_commutator.o
	$(CC) $(LDFLAGS) $< -o $@
	cp $@ ..

clean :
	-rm *.o

