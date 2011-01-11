#!/bin/bash

BINDIR=$(dirname $0)/
BASEDIR=${BINDIR}../

RESULTSDIR="${BASEDIR}tmp/results-mcWarmupThreaded/"
RESULTSFN="${RESULTSDIR}results.csv"

## Generate Results file
R --vanilla --silent <<EOF
results_fn="${RESULTSFN}"
output_fn="${RESULTSDIR}results3.pdf"
source("${BINDIR}r-scripts/mcWarmupContinuous.r")
EOF
echo "results written to '${RESULTSDIR}results3.pdf'"
