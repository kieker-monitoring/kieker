#!/bin/bash

## Converts the tpmon logging timestamp to a human-readable
## format using the date tool

if [ -z "$1" ]; then
    echo "No logging timestamp given"
    exit 1
fi

for tstamp in $*; do
    echo -n "${tstamp}: "
    date -d "1970-01-01 $((${tstamp}/(1000*1000*1000))) secs"
done