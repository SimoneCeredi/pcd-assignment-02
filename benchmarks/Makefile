SHELL=/bin/bash

fs:
	# TODO: optionally tune parameters in gen_random_filesystem.sh
	mkdir ./fs
	./gen_random_filesystem.sh ./fs $(foreach param,$(subst ' ','#',$(ARGS)), $(subst '#',' ',$(param)))

all:
	mkdir ./fs
	./gen_random_filesystem.sh ./fs

clean:
	rm -rf ./fs