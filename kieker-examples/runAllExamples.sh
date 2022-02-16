#!/bin/bash

set -e

start=$(pwd)
for example in $(find . -name "executeExample.sh"); do
	echo "Executing $example"
	cd $(dirname "$example")
	./executeExample.sh
	cd $start
done 
