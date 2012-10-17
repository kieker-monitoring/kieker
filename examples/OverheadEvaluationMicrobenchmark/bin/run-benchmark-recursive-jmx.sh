#!/bin/bash

#SUDOCMD="pfexec"
SUDOCMD=""
#BINDJAVA="${SUDOCMD} psrset -e 1"
BINDJAVA=""

BINDIR=bin/
BASEDIR=

SLEEPTIME=30            ## 30
NUM_LOOPS=10            ## 10
THREADS=1               ## 1
MAXRECURSIONDEPTH=10    ## 10
TOTALCALLS=2000000      ## 200000
METHODTIME=500000       ## 500000

TIME=`expr ${METHODTIME} \* ${TOTALCALLS} / 1000000000 \* 2 \* ${MAXRECURSIONDEPTH} \* ${NUM_LOOPS} + ${SLEEPTIME} \* 2 \* ${NUM_LOOPS}  \* ${MAXRECURSIONDEPTH}`
echo "Experiment will take circa ${TIME} seconds."

# determine correct classpath separator
CPSEPCHAR=":" # default :, ; for windows
if [ ! -z "$(uname | grep -i WIN)" ]; then CPSEPCHAR=";"; fi
# echo "Classpath separator: '${CPSEPCHAR}'"

RESULTSDIR="${BASEDIR}tmp/results-benchmark-recursive/"
echo "Removing and recreating '$RESULTSDIR'"
(${SUDOCMD} rm -rf ${RESULTSDIR}) && mkdir ${RESULTSDIR}
mkdir ${RESULTSDIR}stat/

# Clear kieker.log and initialize logging
rm -f ${BASEDIR}kieker.log

RESULTSFN="${RESULTSDIR}results"

AOPXML_INSTR_DEACTPROBE="-Dorg.aspectj.weaver.loadtime.configuration=META-INF/aop-deactivatedProbe.xml"
AOPXML_INSTR_PROBE="-Dorg.aspectj.weaver.loadtime.configuration=META-INF/aop-probe.xml"

KIEKER_MONITORING_CONF="${BASEDIR}configuration/kieker.monitoring.properties"

JAVAARGS="-server"
JAVAARGS="${JAVAARGS} -d64"
JAVAARGS="${JAVAARGS} -Xms1G -Xmx1G"
JAVAARGS="${JAVAARGS} -verbose:gc -XX:+PrintCompilation"
#JAVAARGS="${JAVAARGS} -XX:+PrintInlining"
#JAVAARGS="${JAVAARGS} -XX:+UnlockDiagnosticVMOptions -XX:+LogCompilation"
#JAVAARGS="${JAVAARGS} -Djava.compiler=NONE"
JAR="-jar dist/OverheadEvaluationMicrobenchmark.jar"

JAVAARGS_NOINSTR="${JAVAARGS}"
JAVAARGS_LTW="${JAVAARGS} -javaagent:${BASEDIR}lib/kieker-1.6_aspectj.jar -Dorg.aspectj.weaver.showWeaveInfo=false -Daj.weaving.verbose=false"
JAVAARGS_KIEKER="-Djava.util.logging.config.file=${BASEDIR}configuration/logging.properties -Dkieker.monitoring.configuration=${KIEKER_MONITORING_CONF}"
JAVAARGS_KIEKER_NOLOGGING="${JAVAARGS_LTW} ${AOPXML_INSTR_PROBE} ${JAVAARGS_KIEKER} -Dkieker.monitoring.writer=kieker.monitoring.writer.DummyWriter"
JAVAARGS_KIEKER_LOGGING="${JAVAARGS_LTW} ${AOPXML_INSTR_PROBE} ${JAVAARGS_KIEKER} -Dkieker.monitoring.jmx=true -Dkieker.monitoring.jmx.remote=true -Dkieker.monitoring.writer=kieker.monitoring.writer.jmx.JMXWriter"

## Write configuration
uname -a >${RESULTSDIR}configuration.txt
java ${JAVAARGS} -version 2>>${RESULTSDIR}configuration.txt
echo "JAVAARGS: ${JAVAARGS}" >>${RESULTSDIR}configuration.txt
echo "" >>${RESULTSDIR}configuration.txt
echo "Runtime: circa ${TIME} seconds" >>${RESULTSDIR}configuration.txt
echo "" >>${RESULTSDIR}configuration.txt
echo "SLEEPTIME=${SLEEPTIME}" >>${RESULTSDIR}configuration.txt
echo "NUM_LOOPS=${NUM_LOOPS}" >>${RESULTSDIR}configuration.txt
echo "TOTALCALLS=${TOTALCALLS}" >>${RESULTSDIR}configuration.txt
echo "METHODTIME=${METHODTIME}" >>${RESULTSDIR}configuration.txt
echo "THREADS=${THREADS}" >>${RESULTSDIR}configuration.txt
echo "MAXRECURSIONDEPTH=${MAXRECURSIONDEPTH}" >>${RESULTSDIR}configuration.txt
sync

## Execute Benchmark

for ((i=1;i<=${NUM_LOOPS};i+=1)); do
    echo "## Starting iteration ${i}/${NUM_LOOPS}"

    for ((j=1;j<=${MAXRECURSIONDEPTH};j+=1)); do
        echo "# Starting recursion ${i}.${j}/${MAXRECURSIONDEPTH}"

        # 1 No logging
        echo " # ${i}.1 No logging (null writer)"
        mpstat 1 > ${RESULTSDIR}stat/mpstat-${i}-${j}-1.txt &
        vmstat 1 > ${RESULTSDIR}stat/vmstat-${i}-${j}-1.txt &
        iostat -xn 10 > ${RESULTSDIR}stat/iostat-${i}-${j}-1.txt &
        ${BINDJAVA} java  ${JAVAARGS_KIEKER_NOLOGGING} ${JAR} \
            --output-filename ${RESULTSFN}-${i}-${j}-1.csv \
            --totalcalls ${TOTALCALLS} \
            --methodtime ${METHODTIME} \
            --totalthreads ${THREADS} \
            --recursiondepth ${j}
        kill %mpstat
        kill %vmstat
        kill %iostat
        [ -f ${BASEDIR}hotspot.log ] && mv ${BASEDIR}hotspot.log ${RESULTSDIR}hotspot-${i}-${j}-1.log
        echo >>${BASEDIR}kieker.log
        echo >>${BASEDIR}kieker.log
        sync
        sleep ${SLEEPTIME}

        # 2 Logging
        echo " # ${i}.2 Logging"
        mpstat 1 > ${RESULTSDIR}stat/mpstat-${i}-${j}-2.txt &
        vmstat 1 > ${RESULTSDIR}stat/vmstat-${i}-${j}-2.txt &
        iostat -xn 10 > ${RESULTSDIR}stat/iostat-${i}-${j}-2.txt &
        ${BINDJAVA} java  ${JAVAARGS_KIEKER_LOGGING} ${JAR} \
            --output-filename ${RESULTSFN}-${i}-${j}-2.csv \
            --totalcalls ${TOTALCALLS} \
            --methodtime ${METHODTIME} \
            --totalthreads ${THREADS} \
            --recursiondepth ${j}
        kill %mpstat
        kill %vmstat
        kill %iostat
        mkdir -p ${RESULTSDIR}kiekerlog/
        [ -f ${BASEDIR}hotspot.log ] && mv ${BASEDIR}hotspot.log ${RESULTSDIR}hotspot-${i}-${j}-2.log
        echo >>${BASEDIR}kieker.log
        echo >>${BASEDIR}kieker.log
        sync
        sleep ${SLEEPTIME}
    
    done

done
tar cf ${RESULTSDIR}stat.tar ${RESULTSDIR}stat
rm -rf ${RESULTSDIR}stat/
gzip -9 ${RESULTSDIR}stat.tar
mv ${BASEDIR}kieker.log ${RESULTSDIR}kieker.log
[ -f ${RESULTSDIR}hotspot-1-1-1.log ] && grep "<task " ${RESULTSDIR}hotspot-*.log >${RESULTSDIR}log.log
[ -f ${BASEDIR}nohup.out ] && mv ${BASEDIR}nohup.out ${RESULTSDIR}
