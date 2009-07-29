#!/bin/bash

## Converts the tpmon logging timestamp to a human-readable
## format using the date tool

if [ -z "$1" ]; then
    echo "No logging timestamp given"
    exit 1
fi

date -d "1970-01-01 $((${1}/(1000*1000*1000))) secs"