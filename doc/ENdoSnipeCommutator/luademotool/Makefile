#
# Make file for luademotool
#

all : luademotool

CC=gcc
LDFLAGS=
CFLAGS=-I.
LDLIBS=

%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

luademotool: luademotool.o
	$(CC) $(LDFLAGS) $< -o $@

clean :
	-rm *.o

