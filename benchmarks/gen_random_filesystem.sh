#!/bin/bash

FSDIR=$1
MAXDIRS=$2
MAXDEPTH=$3
MAXFILES=$4
MAXSIZE=$5

TOP=`pwd|tr -cd '/'|wc -c`
RANDOM=42

populate() {
	cd $1
	curdir=$PWD

  # Pseudorandom number of files given seed
	files=$(($RANDOM*$MAXFILES/32767))
	#echo $files
	if [ $files -le $(($MAXFILES/2)) ]
	then
	  files=$(($MAXFILES/2))
  fi
	for n in `seq $files`
	do
	  # Pure Pseudorandom name
	  rnd=$RANDOM
	  f=$(echo $rnd | md5sum | head -c 10);
	  #f=`tr -dc 'a-zA-Z0-9' < /dev/urandom | head -c 10`
	  # Pseudorandom size given seed
		size=$(($RANDOM*$MAXSIZE/32767))
		#echo $size
		# Pure pseudorandom content
		head -c $size /dev/urandom > "$f.java"
	done

  # Reach always the maximum depth
	depth=`pwd|tr -cd '/'|wc -c`
	if [ $(($depth-$TOP)) -ge $MAXDEPTH ]
	then
		return
	fi

  # Pseudorandom number of directories
	unset dirlist
	dirs=$(($RANDOM*$MAXDIRS/32767))
	#echo $dirs
	if [ $dirs -le $(($MAXDIRS/2)) ]
  	then
  	  dirs=$(($MAXDIRS/2))
  fi
	for n in `seq $dirs`
	do
		rnd=$RANDOM
		d=$(echo $rnd | md5sum | head -c 6)
		mkdir $d
		dirlist="$dirlist${dirlist:+ }$PWD/$d"
	done

	for dir in $dirlist
	do
		populate "$dir"
	done
}

populate "${PWD}/${FSDIR}"