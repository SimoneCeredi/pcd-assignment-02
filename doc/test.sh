#!/bin/bash

seed="$1"
random_string=$(cat /dev/urandom | LC_ALL=C tr -dc 'a-zA-Z0-9' | fold -w 10 | head -n 1)

echo "Random string generated using seed '$seed': $random_string"

