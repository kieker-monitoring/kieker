#!/bin/bash

JAVABIN="/localhome/ffi/jdk1.7.0_25/bin/"

RSCRIPTDIR=bin/icpe/r/
BASEDIR=./
RESULTSDIR="${BASEDIR}tmp/results-benchmark-zip/"

SLEEPTIME=30            ## 30
NUM_LOOPS=10            ## 10
THREADS=1               ## 1
RECURSIONDEPTH=10       ## 10
TOTALCALLS=20000000     ## 20000000
METHODTIME=0            ## 0

TIME=`expr ${METHODTIME} \* ${TOTALCALLS} / 1000000000 \* 4 \* ${RECURSIONDEPTH} \* ${NUM_LOOPS} + ${SLEEPTIME} \* 4 \* ${NUM_LOOPS}  \* ${RECURSIONDEPTH} + 50 \* ${TOTALCALLS} / 1000000000 \* 4 \* ${RECURSIONDEPTH} \* ${NUM_LOOPS} `
echo "Experiment will take circa ${TIME} seconds."

# determine correct classpath separator
CPSEPCHAR=":" # default :, ; for windows
if [ ! -z "$(uname | grep -i WIN)" ]; then CPSEPCHAR=";"; fi
# echo "Classpath separator: '${CPSEPCHAR}'"

echo "Removing and recreating '$RESULTSDIR'"
(rm -rf ${RESULTSDIR}) && mkdir ${RESULTSDIR}
mkdir ${RESULTSDIR}stat/

# Clear kieker.log and initialize logging
rm -f ${BASEDIR}kieker.log
touch ${BASEDIR}kieker.log

RAWFN="${RESULTSDIR}raw"

JAVAARGS="-server"
JAVAARGS="${JAVAARGS} -d64"
JAVAARGS="${JAVAARGS} -Xms1G -Xmx4G"
JAVAARGS="${JAVAARGS} -verbose:gc -XX:+PrintCompilation"
#JAVAARGS="${JAVAARGS} -XX:+PrintInlining"
#JAVAARGS="${JAVAARGS} -XX:+UnlockDiagnosticVMOptions -XX:+LogCompilation"
#JAVAARGS="${JAVAARGS} -Djava.compiler=NONE"
JAR="-jar dist/OverheadEvaluationMicrobenchmark.jar"

JAVAARGS_NOINSTR="${JAVAARGS}"
JAVAARGS_LTW="${JAVAARGS} -javaagent:${BASEDIR}lib/kieker-1.8-SNAPSHOT_aspectj.jar -Dorg.aspectj.weaver.showWeaveInfo=false -Daj.weaving.verbose=false -Dkieker.monitoring.adaptiveMonitoring.enabled=false"
JAVAARGS_KIEKER_DEACTV="${JAVAARGS_LTW} -Dkieker.monitoring.enabled=false -Dkieker.monitoring.writer=kieker.monitoring.writer.DummyWriter"
JAVAARGS_KIEKER_NOLOGGING="${JAVAARGS_LTW} -Dkieker.monitoring.writer=kieker.monitoring.writer.DummyWriter"
JAVAARGS_KIEKER_LOGGING="${JAVAARGS_LTW} -Dkieker.monitoring.writer=kieker.monitoring.writer.filesystem.AsyncBinaryZipWriter -Dkieker.monitoring.writer.filesystem.AsyncBinaryZipWriter.customStoragePath=${BASEDIR}tmp -Dkieker.monitoring.writer.filesystem.AsyncBinaryZipWriter.QueueFullBehavior=1"

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
    echo "## Starting iteration ${i}/${NUM_LOOPS}"
    echo "## Starting iteration ${i}/${NUM_LOOPS}" >>${BASEDIR}kieker.log

    # 1 No instrumentation
    echo " # ${i}.1 No instrumentation"
    echo " # ${i}.1 No instrumentation" >>${BASEDIR}kieker.log
    mpstat 1 > ${RESULTSDIR}stat/mpstat-${i}-${RECURSIONDEPTH}-1.txt &
    vmstat 1 > ${RESULTSDIR}stat/vmstat-${i}-${RECURSIONDEPTH}-1.txt &
    iostat -xn 10 > ${RESULTSDIR}stat/iostat-${i}-${RECURSIONDEPTH}-1.txt &
    ${JAVABIN}java  ${JAVAARGS_NOINSTR} ${JAR} \
        --output-filename ${RAWFN}-${i}-${RECURSIONDEPTH}-1.csv \
        --totalcalls ${TOTALCALLS} \
        --methodtime ${METHODTIME} \
        --totalthreads ${THREADS} \
        --recursiondepth ${RECURSIONDEPTH}
    kill %mpstat
    kill %vmstat
    kill %iostat
    [ -f ${BASEDIR}hotspot.log ] && mv ${BASEDIR}hotspot.log ${RESULTSDIR}hotspot-${i}-${RECURSIONDEPTH}-1.log
    echo >>${BASEDIR}kieker.log
    echo >>${BASEDIR}kieker.log
    sync
    sleep ${SLEEPTIME}

    # 2 Deactivated probe
    echo " # ${i}.2 Deactivated probe"
    echo " # ${i}.2 Deactivated probe" >>${BASEDIR}kieker.log
    mpstat 1 > ${RESULTSDIR}stat/mpstat-${i}-${RECURSIONDEPTH}-2.txt &
    vmstat 1 > ${RESULTSDIR}stat/vmstat-${i}-${RECURSIONDEPTH}-2.txt &
    iostat -xn 10 > ${RESULTSDIR}stat/iostat-${i}-${RECURSIONDEPTH}-2.txt &
    ${JAVABIN}java  ${JAVAARGS_KIEKER_DEACTV} ${JAR} \
        --output-filename ${RAWFN}-${i}-${RECURSIONDEPTH}-2.csv \
        --totalcalls ${TOTALCALLS} \
        --methodtime ${METHODTIME} \
        --totalthreads ${THREADS} \
        --recursiondepth ${RECURSIONDEPTH}
    kill %mpstat
    kill %vmstat
    kill %iostat
    [ -f ${BASEDIR}hotspot.log ] && mv ${BASEDIR}hotspot.log ${RESULTSDIR}hotspot-${i}-${RECURSIONDEPTH}-2.log
    echo >>${BASEDIR}kieker.log
    echo >>${BASEDIR}kieker.log
    sync
    sleep ${SLEEPTIME}

    # 3 No logging
    echo " # ${i}.3 No logging (null writer)"
    echo " # ${i}.3 No logging (null writer)" >>${BASEDIR}kieker.log
    mpstat 1 > ${RESULTSDIR}stat/mpstat-${i}-${RECURSIONDEPTH}-3.txt &
    vmstat 1 > ${RESULTSDIR}stat/vmstat-${i}-${RECURSIONDEPTH}-3.txt &
    iostat -xn 10 > ${RESULTSDIR}stat/iostat-${i}-${RECURSIONDEPTH}-3.txt &
    ${JAVABIN}java  ${JAVAARGS_KIEKER_NOLOGGING} ${JAR} \
        --output-filename ${RAWFN}-${i}-${RECURSIONDEPTH}-3.csv \
        --totalcalls ${TOTALCALLS} \
        --methodtime ${METHODTIME} \
        --totalthreads ${THREADS} \
        --recursiondepth 1
    kill %mpstat
    kill %vmstat
    kill %iostat
    [ -f ${BASEDIR}hotspot.log ] && mv ${BASEDIR}hotspot.log ${RESULTSDIR}hotspot-${i}-${RECURSIONDEPTH}-3.log
    echo >>${BASEDIR}kieker.log
    echo >>${BASEDIR}kieker.log
    sync
    sleep ${SLEEPTIME}

    # 4 Logging
    echo " # ${i}.4 Logging"
    echo " # ${i}.4 Logging" >>${BASEDIR}kieker.log
    mpstat 1 > ${RESULTSDIR}stat/mpstat-${i}-${RECURSIONDEPTH}-4.txt &
    vmstat 1 > ${RESULTSDIR}stat/vmstat-${i}-${RECURSIONDEPTH}-4.txt &
    iostat -xn 10 > ${RESULTSDIR}stat/iostat-${i}-${RECURSIONDEPTH}-4.txt &
    ${JAVABIN}java  ${JAVAARGS_KIEKER_LOGGING} ${JAR} \
        --output-filename ${RAWFN}-${i}-${RECURSIONDEPTH}-4.csv \
        --totalcalls ${TOTALCALLS} \
        --methodtime ${METHODTIME} \
        --totalthreads ${THREADS} \
        --recursiondepth ${RECURSIONDEPTH}
    kill %mpstat
    kill %vmstat
    kill %iostat
    rm -rf ${BASEDIR}tmp/kieker-*
    [ -f ${BASEDIR}hotspot.log ] && mv ${BASEDIR}hotspot.log ${RESULTSDIR}hotspot-${i}-${RECURSIONDEPTH}-4.log
    echo >>${BASEDIR}kieker.log
    echo >>${BASEDIR}kieker.log
    sync
    sleep ${SLEEPTIME}
done
zip -jqr ${RESULTSDIR}stat.zip ${RESULTSDIR}stat
rm -rf ${RESULTSDIR}stat/
mv ${BASEDIR}kieker.log ${RESULTSDIR}kieker.log
[ -f ${RESULTSDIR}hotspot-1-1-1.log ] && grep "<task " ${RESULTSDIR}hotspot-*.log >${RESULTSDIR}log.log
[ -f ${BASEDIR}errorlog.txt ] && mv ${BASEDIR}errorlog.txt ${RESULTSDIR}

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

## Clean up raw results
zip -jqr ${RESULTSDIR}results.zip ${RAWFN}*
rm -f ${RAWFN}*
[ -f ${BASEDIR}nohup.out ] && mv ${BASEDIR}nohup.out ${RESULTSDIR}
