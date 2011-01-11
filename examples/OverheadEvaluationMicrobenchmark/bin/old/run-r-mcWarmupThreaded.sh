#!/bin/bash

BINDIR=$(dirname $0)/
BASEDIR=${BINDIR}../

RESULTSDIR="${BASEDIR}tmp/results-mcWarmupThreaded/"
RESULTSFN="${RESULTSDIR}results.csv"

## Generate Results file
R --vanilla --silent <<EOF
results_fn="${RESULTSFN}"
output_fn="${RESULTSDIR}results1.pdf"
source("${BINDIR}r-scripts/mcWarmup.r")
EOF
echo "results written to '${RESULTSDIR}results1.pdf'"

## Generate Results file
R --vanilla --silent <<EOF
results_fn="${RESULTSFN}"
output_fn="${RESULTSDIR}results2.pdf"
source("${BINDIR}r-scripts/mcWarmupStapel.r")
EOF
echo "results written to '${RESULTSDIR}results2.pdf'"
