#!/bin/bash

cd "$(dirname $0)/../tmp/"

# Create a tmp subdirectory
TMPDIR=$(mktemp -d --tmpdir=.)
echo ${TMPDIR}
cd ${TMPDIR}

SRCZIP=$(ls ../../dist/release/*_sources.zip)
echo ${SRCZIP}
unzip ${SRCZIP}
cd kieker-*
 
if ! ant; then
    echo "Build failed"
    exit 1
fi