#!/bin/bash
# Created by Ben Okopnik on Wed Jul 16 18:04:33 EDT 2008

########    User settings     ############
MAXDIRS=10
MAXDEPTH=5
MAXFILES=10
MAXSIZE=10000
######## End of user settings ############

# How deep in the file system are we now?
TOP=`pwd|tr -cd '/'|wc -c`

FSDIR=$1

populate() {
	cd $1
	curdir=$PWD

	files=$(($RANDOM*$MAXFILES/32767))
	for n in `seq $files`
	do
	  f=`tr -dc 'a-zA-Z0-9' < /dev/urandom | head -c 10`
		size=$(($RANDOM*$MAXSIZE/32767))
		head -c $size /dev/urandom > "$f.java"
	done

	depth=`pwd|tr -cd '/'|wc -c`
	if [ $(($depth-$TOP)) -ge $MAXDEPTH ]
	then
		return
	fi

	unset dirlist
	dirs=$(($RANDOM*$MAXDIRS/32767))
	for n in `seq $dirs`
	do
		d=`mktemp -d XXXXXX`
		dirlist="$dirlist${dirlist:+ }$PWD/$d"
	done

	for dir in $dirlist
	do
		populate "$dir"
	done
}

populate "${PWD}/${FSDIR}"