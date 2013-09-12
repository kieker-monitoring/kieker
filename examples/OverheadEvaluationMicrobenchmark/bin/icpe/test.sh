#!/bin/bash

RSCRIPTDIR=bin/icpe/r/
BASEDIR=./
RESULTSDIR="${BASEDIR}tmp/results-benchmark-binary/"

SLEEPTIME=30            ## 30
NUM_LOOPS=10            ## 10
THREADS=1               ## 1
RECURSIONDEPTH=10       ## 10
TOTALCALLS=20000000     ## 20000000
METHODTIME=0            ## 0

RAWFN="${RESULTSDIR}raw"

## Generate Results file
# Timeseries
R --vanilla --silent <<EOF
results_fn="${RAWFN}"
output_fn="${RESULTSDIR}results-timeseries.pdf"
configs.loop=${NUM_LOOPS}
configs.recursion=c(${RECURSIONDEPTH})
results.count=${TOTALCALLS}
tsconf.min=0
tsconf.max=50
source("${RSCRIPTDIR}timeseries.r")
EOF
# Timeseries-Average
R --vanilla --silent <<EOF
results_fn="${RAWFN}"
output_fn="${RESULTSDIR}results-timeseries-average.pdf"
configs.loop=${NUM_LOOPS}
configs.recursion=c(${RECURSIONDEPTH})
results.count=${TOTALCALLS}
tsconf.min=0
tsconf.max=50
source("${RSCRIPTDIR}timeseries-average.r")
EOF
# Bars
R --vanilla --silent <<EOF
results_fn="${RAWFN}"
output_fn="${RESULTSDIR}results-bars.pdf"
outtxt_fn="${RESULTSDIR}results-text.txt"
configs.loop=${NUM_LOOPS}
configs.recursion=c(${RECURSIONDEPTH})
results.count=${TOTALCALLS}
results.skip=${TOTALCALLS}/2
bars.minval=${METHODTIME}
source("${RSCRIPTDIR}bar.r")
EOF
