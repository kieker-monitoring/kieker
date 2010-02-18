#!/bin/bash

#
# For all .dot and .pic files in the directory passed
# as parameter $1, this script generates image files
# in the format passed as $2..$n by calling the 
# tools dot and pic2plot.
#
# Example: $ dotPic-fileConverter tmp/ svg png ps
#
# @author Andre van Hoorn


if [ ! -d "$1" ]; then
    echo "'$1' is not a directory"
    exit 1
fi

if [ -z "$2" ]; then
    echo "Missing file extensions"
    exit 1
fi

DIRNAME="$1"
shift

for f in $DIRNAME/*.pic; do 
    BASENAME=$(echo $f. | cut -d'.' -f1); 
    for ext in svg png; do 
	pic2plot -T $ext $f > $BASENAME.$ext ; 
    done; 
done
