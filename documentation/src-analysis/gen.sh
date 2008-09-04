#!/bin/bash

reqnames=$(grep -v "\"path\"" acceslog-noInstr.csv | awk  '{ print  $5 }' | sort | uniq | tr "\n" " ")

for fn in acceslog-noInstr.csv; do
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