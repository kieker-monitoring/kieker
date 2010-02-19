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

PIC_FILES=$(find ${DIRNAME} -name "*.pic")
DOT_FILES=$(find ${DIRNAME} -name "*.dot")

PIC_COUNTER=0
DOT_COUNTER=0

if (echo "$ext" | grep pdf ) && !(echo "$ext" | grep ps); then
    echo must generate ps first
    exit 1
fi

for f in ${PIC_FILES}; do 
    BASENAME=$(echo $f. | cut -d'.' -f1); 
    for ext in $*; do 
	pic2plot -T $ext $f > $BASENAME.$ext ;
    done; 
    PIC_COUNTER=$(($PIC_COUNTER+1))
done

for f in ${DOT_FILES}; do 
    BASENAME=$(echo $f. | cut -d'.' -f1); 
    for ext in $*; do 
	dot -T $ext $f > $BASENAME.$ext ; 
    done; 
    DOT_COUNTER=$(($DOT_COUNTER+1))
done

echo 
echo "Processed ${DOT_COUNTER} .dot files"
echo "Processed ${PIC_COUNTER} .pic files"
