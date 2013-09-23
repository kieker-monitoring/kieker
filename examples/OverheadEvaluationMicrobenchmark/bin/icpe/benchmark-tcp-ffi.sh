#!/bin/bash

JAVABIN="/localhome/ffi/jdk1.7.0_25/bin/"

RSCRIPTDIR=bin/icpe/r/
BASEDIR=./
RESULTSDIR="${BASEDIR}tmp/results-benchmark-tcp-ffi/"

SLEEPTIME=30            ## 30
NUM_LOOPS=10            ## 10
THREADS=1               ## 1
RECURSIONDEPTH=10       ## 10
TOTALCALLS=20000000     ## 20000000
METHODTIME=0            ## 0

MOREPARAMS=""
#MOREPARAMS="--quickstart"

TIME=`expr ${METHODTIME} \* ${TOTALCALLS} / 1000000000 \* 4 \* ${RECURSIONDEPTH} \* ${NUM_LOOPS} + ${SLEEPTIME} \* 4 \* ${NUM_LOOPS}  \* ${RECURSIONDEPTH} + 50 \* ${TOTALCALLS} / 1000000000 \* 4 \* ${RECURSIONDEPTH} \* ${NUM_LOOPS} `
echo "Experiment will take circa ${TIME} seconds."

echo "Removing and recreating '$RESULTSDIR'"
(rm -rf ${RESULTSDIR}) && mkdir ${RESULTSDIR}
mkdir ${RESULTSDIR}stat/

RAWFN="${RESULTSDIR}raw"

JAVAARGS="-server"
JAVAARGS="${JAVAARGS} -d64"
JAVAARGS="${JAVAARGS} -Xms1G -Xmx4G"
JAVAARGS="${JAVAARGS} -verbose:gc -XX:+PrintCompilation"
#JAVAARGS="${JAVAARGS} -XX:+PrintInlining"
#JAVAARGS="${JAVAARGS} -XX:+UnlockDiagnosticVMOptions -XX:+LogCompilation"
#JAVAARGS="${JAVAARGS} -Djava.compiler=NONE"
JARNoInstru="-jar dist/OverheadEvaluationMicrobenchmarkTCPffiNoInstru.jar"
JARDeactived="-jar dist/OverheadEvaluationMicrobenchmarkTCPffiDeactivated.jar"
JARCollecting="-jar dist/OverheadEvaluationMicrobenchmarkTCPffiCollecting.jar"
JARNORMAL="-jar dist/OverheadEvaluationMicrobenchmarkTCPffiNormal.jar"

JAVAARGS_NOINSTR="${JAVAARGS}"
JAVAARGS_LTW="${JAVAARGS} -javaagent:${BASEDIR}lib/aspectjweaver.jar -Dorg.aspectj.weaver.showWeaveInfo=false -Daj.weaving.verbose=false"

## Write configuration
uname -a >${RESULTSDIR}configuration.txt
${JAVABIN}java ${JAVAARGS} -version 2>>${RESULTSDIR}configuration.txt
echo "JAVAARGS: ${JAVAARGS}" >>${RESULTSDIR}configuration.txt
echo "" >>${RESULTSDIR}configuration.txt
echo "Runtime: circa ${TIME} seconds" >>${RESULTSDIR}configuration.txt
echo "" >>${RESULTSDIR}configuration.txt
echo "SLEEPTIME=${SLEEPTIME}" >>${RESULTSDIR}configuration.txt
echo "NUM_LOOPS=${NUM_LOOPS}" >>${RESULTSDIR}configuration.txt
echo "TOTALCALLS=${TOTALCALLS}" >>${RESULTSDIR}configuration.txt
echo "METHODTIME=${METHODTIME}" >>${RESULTSDIR}configuration.txt
echo "THREADS=${THREADS}" >>${RESULTSDIR}configuration.txt
echo "RECURSIONDEPTH=${RECURSIONDEPTH}" >>${RESULTSDIR}configuration.txt
sync

## Execute Benchmark

for ((i=1;i<=${NUM_LOOPS};i+=1)); do
    j=${RECURSIONDEPTH}
    k=0
    echo "## Starting iteration ${i}/${NUM_LOOPS}"

    # No instrumentation
    k=`expr ${k} + 1`
    echo " # ${i}.${j}.${k} No instrumentation"
    mpstat 1 > ${RESULTSDIR}stat/mpstat-${i}-${j}-${k}.txt &
    vmstat 1 > ${RESULTSDIR}stat/vmstat-${i}-${j}-${k}.txt &
    iostat -xn 10 > ${RESULTSDIR}stat/iostat-${i}-${j}-${k}.txt &
    ${JAVABIN}java  ${JAVAARGS_NOINSTR} ${JARNoInstru} \
        --output-filename ${RAWFN}-${i}-${j}-${k}.csv \
        --totalcalls ${TOTALCALLS} \
        --methodtime ${METHODTIME} \
        --totalthreads ${THREADS} \
        --recursiondepth ${j} \
        ${MOREPARAMS}
    kill %mpstat
    kill %vmstat
    kill %iostat
    [ -f ${BASEDIR}hotspot.log ] && mv ${BASEDIR}hotspot.log ${RESULTSDIR}hotspot-${i}-${j}-${k}.log
    sync
    sleep ${SLEEPTIME}

    # Deactivated Probe
    k=`expr ${k} + 1`
    echo " # ${i}.${j}.${k} Deactivated Probe"
    mpstat 1 > ${RESULTSDIR}stat/mpstat-${i}-${j}-${k}.txt &
    vmstat 1 > ${RESULTSDIR}stat/vmstat-${i}-${j}-${k}.txt &
    iostat -xn 10 > ${RESULTSDIR}stat/iostat-${i}-${j}-${k}.txt &
    ${JAVABIN}java -jar dist/explorviz_worker.jar >${RESULTSDIR}worker-${i}-${j}-${k}.log &
    sleep 5
    ${JAVABIN}java  ${JAVAARGS_LTW} ${JARDeactived} \
        --output-filename ${RAWFN}-${i}-${j}-${k}.csv \
        --totalcalls ${TOTALCALLS} \
        --methodtime ${METHODTIME} \
        --totalthreads ${THREADS} \
        --recursiondepth ${j} \
        ${MOREPARAMS}
    kill %mpstat
    kill %vmstat
    kill %iostat
    pkill -f 'java -jar'
    rm -rf ${BASEDIR}tmp/kieker-*
    [ -f ${BASEDIR}hotspot.log ] && mv ${BASEDIR}hotspot.log ${RESULTSDIR}hotspot-${i}-${j}-${k}.log
    sync
    sleep ${SLEEPTIME}
	
    # Collecting
    k=`expr ${k} + 1`
    echo " # ${i}.${j}.${k} Collecting"
    mpstat 1 > ${RESULTSDIR}stat/mpstat-${i}-${j}-${k}.txt &
    vmstat 1 > ${RESULTSDIR}stat/vmstat-${i}-${j}-${k}.txt &
    iostat -xn 10 > ${RESULTSDIR}stat/iostat-${i}-${j}-${k}.txt &
    ${JAVABIN}java -jar dist/explorviz_worker.jar >${RESULTSDIR}worker-${i}-${j}-${k}.log &
    sleep 5
    ${JAVABIN}java  ${JAVAARGS_LTW} ${JARCollecting} \
        --output-filename ${RAWFN}-${i}-${j}-${k}.csv \
        --totalcalls ${TOTALCALLS} \
        --methodtime ${METHODTIME} \
        --totalthreads ${THREADS} \
        --recursiondepth ${j} \
        ${MOREPARAMS}
    kill %mpstat
    kill %vmstat
    kill %iostat
    pkill -f 'java -jar'
    rm -rf ${BASEDIR}tmp/kieker-*
    [ -f ${BASEDIR}hotspot.log ] && mv ${BASEDIR}hotspot.log ${RESULTSDIR}hotspot-${i}-${j}-${k}.log
    sync
    sleep ${SLEEPTIME}

    # Logging
    k=`expr ${k} + 1`
    echo " # ${i}.${j}.${k} Logging"
    mpstat 1 > ${RESULTSDIR}stat/mpstat-${i}-${j}-${k}.txt &
    vmstat 1 > ${RESULTSDIR}stat/vmstat-${i}-${j}-${k}.txt &
    iostat -xn 10 > ${RESULTSDIR}stat/iostat-${i}-${j}-${k}.txt &
    ${JAVABIN}java -jar dist/explorviz_worker.jar >${RESULTSDIR}worker-${i}-${j}-${k}.log &
    sleep 5
    ${JAVABIN}java  ${JAVAARGS_LTW} ${JARNORMAL} \
        --output-filename ${RAWFN}-${i}-${j}-${k}.csv \
        --totalcalls ${TOTALCALLS} \
        --methodtime ${METHODTIME} \
        --totalthreads ${THREADS} \
        --recursiondepth ${j} \
        ${MOREPARAMS}
    kill %mpstat
    kill %vmstat
    kill %iostat
    pkill -f 'java -jar'
    rm -rf ${BASEDIR}tmp/kieker-*
    [ -f ${BASEDIR}hotspot.log ] && mv ${BASEDIR}hotspot.log ${RESULTSDIR}hotspot-${i}-${j}-${k}.log
    sync
    sleep ${SLEEPTIME}

done
zip -jqr ${RESULTSDIR}stat.zip ${RESULTSDIR}stat
rm -rf ${RESULTSDIR}stat/
[ -f ${RESULTSDIR}hotspot-1-${RECURSIONDEPTH}-1.log ] && grep "<task " ${RESULTSDIR}hotspot-*.log >${RESULTSDIR}log.log
[ -f ${BASEDIR}errorlog.txt ] && mv ${BASEDIR}errorlog.txt ${RESULTSDIR}

## Generate Results file
# Timeseries
R --vanilla --silent <<EOF
results_fn="${RAWFN}"
output_fn="${RESULTSDIR}results-timeseries.pdf"
configs.loop=${NUM_LOOPS}
configs.recursion=c(${RECURSIONDEPTH})
configs.labels=c("No Probe","Deactivated Probe","Collecting Data","TCP Writer")
configs.colors=c("black","red","blue","green")
results.count=${TOTALCALLS}
tsconf.min=(${METHODTIME}/1000)
tsconf.max=(${METHODTIME}/1000)+40
source("${RSCRIPTDIR}timeseries.r")
EOF
# Timeseries-Average
R --vanilla --silent <<EOF
results_fn="${RAWFN}"
output_fn="${RESULTSDIR}results-timeseries-average.pdf"
configs.loop=${NUM_LOOPS}
configs.recursion=c(${RECURSIONDEPTH})
configs.labels=c("No Probe","Deactivated Probe","Collecting Data","TCP Writer")
configs.colors=c("black","red","blue","green")
results.count=${TOTALCALLS}
tsconf.min=(${METHODTIME}/1000)
tsconf.max=(${METHODTIME}/1000)+40
source("${RSCRIPTDIR}timeseries-average.r")
EOF
# Throughput
R --vanilla --silent <<EOF
results_fn="${RAWFN}"
output_fn="${RESULTSDIR}results-throughput.pdf"
configs.loop=${NUM_LOOPS}
configs.recursion=c(${RECURSIONDEPTH})
configs.labels=c("No Probe","Deactivated Probe","Collecting Data","TCP Writer")
configs.colors=c("black","red","blue","green")
results.count=${TOTALCALLS}
source("${RSCRIPTDIR}throughput.r")
EOF
# Throughput-Average
R --vanilla --silent <<EOF
results_fn="${RAWFN}"
output_fn="${RESULTSDIR}results-throughput-average.pdf"
configs.loop=${NUM_LOOPS}
configs.recursion=c(${RECURSIONDEPTH})
configs.labels=c("No Probe","Deactivated Probe","Collecting Data","TCP Writer")
configs.colors=c("black","red","blue","green")
results.count=${TOTALCALLS}
source("${RSCRIPTDIR}throughput-average.r")
EOF
# Bars
R --vanilla --silent <<EOF
results_fn="${RAWFN}"
output_fn="${RESULTSDIR}results-bars.pdf"
outtxt_fn="${RESULTSDIR}results-text.txt"
configs.loop=${NUM_LOOPS}
configs.recursion=c(${RECURSIONDEPTH})
configs.labels=c("No Probe","Deactivated Probe","Collecting Data","TCP Writer")
results.count=${TOTALCALLS}
results.skip=${TOTALCALLS}/2
bars.minval=(${METHODTIME}/1000)
bars.maxval=(${METHODTIME}/1000)+40
source("${RSCRIPTDIR}bar.r")
EOF

## Clean up raw results
zip -jqr ${RESULTSDIR}results.zip ${RAWFN}*
rm -f ${RAWFN}*
zip -jqr ${RESULTSDIR}worker.zip ${RESULTSDIR}worker*.log
rm -f ${RESULTSDIR}worker*.log
[ -f ${BASEDIR}nohup.out ] && mv ${BASEDIR}nohup.out ${RESULTSDIR}
