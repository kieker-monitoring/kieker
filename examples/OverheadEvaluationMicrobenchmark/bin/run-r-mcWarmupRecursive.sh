#!/bin/bash

BINDIR=$(dirname $0)/
BASEDIR=${BINDIR}../

RESULTSDIR="${BASEDIR}tmp/results-mcWarmupRecursive/"
RESULTSFN="${RESULTSDIR}results.csv"

## Generate Results file
R --vanilla --silent <<EOF
results_fn="${RESULTSFN}"
output_fn="${RESULTSDIR}results1.pdf"
source("${BINDIR}r-scripts/mcWarmupRecursiveCompareEachAndAll.r")
EOF
echo "results written to '${RESULTSDIR}results.pdf'"
