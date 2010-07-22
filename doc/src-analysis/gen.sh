#!/bin/bash

# This script inserts an additional column 'pathid' to the 
# accesslog file. The 'pathid' is a unique identifier for
# the request URL. It is used in the R analysis script.

postfix="-accesslog-main_filtered.csv"
    
# Extract all paths from one file.
reqnames=$(grep -v "\"path\"" 20080828-174456-tpmon081-accesslog-main_filtered.csv | awk  '{ print  $5 }' | sort | uniq | tr "\n" " ")

# Insert column 'pathid' in all files.
for fn in *${postfix}; do
    echo "== Converting File '${fn}'"
    sedstr="cat ${fn} | sed s/'\"path\"'/'\"path\" \"pathid\"'/g"
    i=1
    for r in ${reqnames}; do
	sedstr="${sedstr} | sed s/\" ${r} \"/\" $r $i \"/g"
	i=$((i+1))
    done
    echo ${sedstr}
    echo $i
    eval "${sedstr} > ${fn}-pathids"
done