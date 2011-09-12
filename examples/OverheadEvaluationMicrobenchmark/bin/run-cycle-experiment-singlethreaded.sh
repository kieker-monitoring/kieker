#!/bin/bash

SLEEPTIME=30            ## 30
NUM_LOOPS=10            ## 10
THREADS=1               ## 1
MAXRECURSIONDEPTH=1     ## 10
TOTALCALLS=2000000      ## 200000
METHODTIME=500000       ## 500000

TIME=`expr ${METHODTIME} \* ${TOTALCALLS} / 1000000000 \* 4 \* ${MAXRECURSIONDEPTH} \* ${NUM_LOOPS} + ${SLEEPTIME} \* 4 \* ${NUM_LOOPS}  \* ${MAXRECURSIONDEPTH}`
echo "Each experiment will take circa ${TIME} seconds."


####
#### S1
####
pfexec psrset -c -F 1
BINDJAVA="pfexec psrset -e 1"
source bin/run-benchmark-cycle-sync.sh
pfexec psrset -d 1
pfexec mv tmp/results-benchmark-recursive tmp/results-S1

####
#### S2
####
pfexec psrset -c -F 1 9
BINDJAVA="pfexec psrset -e 1"
source bin/run-benchmark-cycle-sync.sh
pfexec psrset -d 1
pfexec mv tmp/results-benchmark-recursive tmp/results-S2

####
#### A1
####
pfexec psrset -c -F 1
BINDJAVA="pfexec psrset -e 1"
source bin/run-benchmark-cycle-async.sh
pfexec psrset -d 1
pfexec mv tmp/results-benchmark-recursive tmp/results-A1

####
#### A2
####
pfexec psrset -c -F 1 9
BINDJAVA="pfexec psrset -e 1"
source bin/run-benchmark-cycle-async.sh
pfexec psrset -d 1
pfexec mv tmp/results-benchmark-recursive tmp/results-A2

####
#### A3
####
pfexec psrset -c -F 1 2
BINDJAVA="pfexec psrset -e 1"
source bin/run-benchmark-cycle-async.sh
pfexec psrset -d 1
pfexec mv tmp/results-benchmark-recursive tmp/results-A3

####
#### A4
####
pfexec psrset -c -F 1 5
BINDJAVA="pfexec psrset -e 1"
source bin/run-benchmark-cycle-async.sh
pfexec psrset -d 1
pfexec mv tmp/results-benchmark-recursive tmp/results-A4

####
#### A5
####
BINDJAVA=""
source bin/run-benchmark-cycle-async.sh
pfexec mv tmp/results-benchmark-recursive tmp/results-A5
