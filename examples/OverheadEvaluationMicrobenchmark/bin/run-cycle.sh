#!/bin/bash

SUDOCMD="pfexec"

SLEEPTIME=30            ## 30
NUM_LOOPS=10            ## 10
MAXRECURSIONDEPTH=1     ## 10
TOTALCALLS=2000000      ## 2000000
METHODTIME=500000       ## 500000

TIME=`expr ${METHODTIME} \* ${TOTALCALLS} / 1000000000 \* 4 \* ${MAXRECURSIONDEPTH} \* ${NUM_LOOPS} + ${SLEEPTIME} \* 4 \* ${NUM_LOOPS}  \* ${MAXRECURSIONDEPTH}`
echo "Each experiment will take circa ${TIME} seconds."

${SUDOCMD} psrset -c -F 0 1 2 3 # one chip no hyperthreading
BINDJAVA="${SUDOCMD} psrset -e 1"
for ((cthreads=1;cthreads<=16;cthreads+=1)); do
  THREADS=${cthreads}
  source bin/run-benchmark-cycle-async.sh
  ${SUDOCMD} mv tmp/results-benchmark-recursive tmp/results-A$cthreads
done
${SUDOCMD} psrset -d 1

${SUDOCMD} psrset -c -F 0 1 2 3 8 9 10 11 # one chip with hyperthreading
BINDJAVA="${SUDOCMD} psrset -e 1"
for ((cthreads=1;cthreads<=16;cthreads+=1)); do
  THREADS=${cthreads}
  source bin/run-benchmark-cycle-async.sh
  ${SUDOCMD} mv tmp/results-benchmark-recursive tmp/results-B$cthreads
done
${SUDOCMD} psrset -d 1

${SUDOCMD} psrset -c -F 0 1 2 3 4 5 6 7 # two chip no hyperthreading
BINDJAVA="${SUDOCMD} psrset -e 1"
for ((cthreads=1;cthreads<=16;cthreads+=1)); do
  THREADS=${cthreads}
  source bin/run-benchmark-cycle-async.sh
  ${SUDOCMD} mv tmp/results-benchmark-recursive tmp/results-C$cthreads
done
${SUDOCMD} psrset -d 1
