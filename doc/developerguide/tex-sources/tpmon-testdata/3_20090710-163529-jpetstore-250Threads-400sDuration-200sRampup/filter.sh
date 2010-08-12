#!/bin/bash
#
# Remove all records with type $2

SRC_DIR=tpmon-20090710-163529/
DST_DIR=tpmon-20090710-163529-filtered/

for f in ${SRC_DIR}/*.dat; do
    BASE_FN=$(basename $f)
    echo "${SRC_DIR}/${BASE_FN} -> ${DST_DIR}/${BASE_FN}"
    grep -e "^\$1" ${SRC_DIR}/${BASE_FN} > ${DST_DIR}/${BASE_FN}
done