#!/bin/bash

FSDIR=$1
MAXDIRS=$2
MAXDEPTH=$3
MAXFILES=$4
MAXSIZE=$5

TOP=`pwd|tr -cd '/'|wc -c`

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