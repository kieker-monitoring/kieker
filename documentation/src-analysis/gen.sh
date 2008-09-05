#!/bin/bash

accesslogs="\
    20080904-183447-noinstr-accesslog-main_filtered.csv \
    20080828-174456-tpmon081-accesslog-main_filtered.csv \
    20080903-061257-tpmon090-accesslog-main_filtered.csv"

#    20080902-225445-tpmon090-accesslog-main_filtered.csv \
    

reqnames=$(grep -v "\"path\"" 20080828-174456-tpmon081-accesslog-main_filtered.csv | awk  '{ print  $5 }' | sort | uniq | tr "\n" " ")

for fn in ${accesslogs}; do
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