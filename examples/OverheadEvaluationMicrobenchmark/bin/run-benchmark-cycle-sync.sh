#!/bin/bash

BINDIR=bin/
BASEDIR=

# determine correct classpath separator
CPSEPCHAR=":" # default :, ; for windows
if [ ! -z "$(uname | grep -i WIN)" ]; then CPSEPCHAR=";"; fi
# echo "Classpath separator: '${CPSEPCHAR}'"

RESULTSDIR="${BASEDIR}tmp/results-benchmark-recursive/"
echo "Removing and recreating '$RESULTSDIR'"
(pfexec rm -rf ${RESULTSDIR}) && mkdir ${RESULTSDIR}
mkdir ${RESULTSDIR}stat/

# Clear kieker.log and initialize logging
rm -f ${BASEDIR}kieker.log
touch ${BASEDIR}kieker.log

RESULTSFN="${RESULTSDIR}results.csv"

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
JAVAARGS_KIEKER_DEACTV="${JAVAARGS_LTW} ${AOPXML_INSTR_DEACTPROBE} ${JAVAARGS_KIEKER} -Dkieker.monitoring.writer=kieker.monitoring.writer.DummyWriter"
JAVAARGS_KIEKER_NOLOGGING="${JAVAARGS_LTW} ${AOPXML_INSTR_PROBE} ${JAVAARGS_KIEKER} -Dkieker.monitoring.writer=kieker.monitoring.writer.DummyWriter"
JAVAARGS_KIEKER_LOGGING="${JAVAARGS_LTW} ${AOPXML_INSTR_PROBE} ${JAVAARGS_KIEKER} -Dkieker.monitoring.writer=kieker.monitoring.writer.filesystem.SyncFsWriter -Dkieker.monitoring.writer.filesystem.SyncFsWriter.storeInJavaIoTmpdir=false -Dkieker.monitoring.writer.filesystem.SyncFsWriter.customStoragePath=${BASEDIR}tmp"

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

#        # 1 No instrumentation
#        echo " # ${i}.1 No instrumentation"
#        mpstat 1 > ${RESULTSDIR}stat/mpstat-${i}-${j}-1.txt &
#        vmstat 1 > ${RESULTSDIR}stat/vmstat-${i}-${j}-1.txt &
#        iostat -xn 10 > ${RESULTSDIR}stat/iostat-${i}-${j}-1.txt &
#        ${BINDJAVA} java  ${JAVAARGS_NOINSTR} ${JAR} \
#            --output-filename ${RESULTSFN}-${i}-${j}-1.csv \
#            --totalcalls ${TOTALCALLS} \
#            --methodtime ${METHODTIME} \
#            --totalthreads ${THREADS} \
#            --recursiondepth ${j}
#        kill %mpstat
#        kill %vmstat
#        kill %iostat
#        [ -f ${BASEDIR}hotspot.log ] && mv ${BASEDIR}hotspot.log ${RESULTSDIR}hotspot-${i}-${j}-1.log
#        sync
#        sleep ${SLEEPTIME}
#
#        # 2 Deactivated probe
#        echo " # ${i}.2 Deactivated probe"
#        mpstat 1 > ${RESULTSDIR}stat/mpstat-${i}-${j}-2.txt &
#        vmstat 1 > ${RESULTSDIR}stat/vmstat-${i}-${j}-2.txt &
#        iostat -xn 10 > ${RESULTSDIR}stat/iostat-${i}-${j}-2.txt &
#        ${BINDJAVA} java  ${JAVAARGS_KIEKER_DEACTV} ${JAR} \
#            --output-filename ${RESULTSFN}-${i}-${j}-2.csv \
#            --totalcalls ${TOTALCALLS} \
#            --methodtime ${METHODTIME} \
#            --totalthreads ${THREADS} \
#            --recursiondepth ${j}
#        kill %mpstat
#        kill %vmstat
#        kill %iostat
#        [ -f ${BASEDIR}hotspot.log ] && mv ${BASEDIR}hotspot.log ${RESULTSDIR}hotspot-${i}-${j}-2.log
#        echo >>${BASEDIR}kieker.log
#        echo >>${BASEDIR}kieker.log
#        sync
#        sleep ${SLEEPTIME}
#
#        # 3 No logging
#        echo " # ${i}.3 No logging (null writer)"
#        mpstat 1 > ${RESULTSDIR}stat/mpstat-${i}-${j}-3.txt &
#        vmstat 1 > ${RESULTSDIR}stat/vmstat-${i}-${j}-3.txt &
#        iostat -xn 10 > ${RESULTSDIR}stat/iostat-${i}-${j}-3.txt &
#        ${BINDJAVA} java  ${JAVAARGS_KIEKER_NOLOGGING} ${JAR} \
#            --output-filename ${RESULTSFN}-${i}-${j}-3.csv \
#            --totalcalls ${TOTALCALLS} \
#            --methodtime ${METHODTIME} \
#            --totalthreads ${THREADS} \
#            --recursiondepth ${j}
#        kill %mpstat
#        kill %vmstat
#        kill %iostat
#        [ -f ${BASEDIR}hotspot.log ] && mv ${BASEDIR}hotspot.log ${RESULTSDIR}hotspot-${i}-${j}-3.log
#        echo >>${BASEDIR}kieker.log
#        echo >>${BASEDIR}kieker.log
#        sync
#        sleep ${SLEEPTIME}

        # 4 Logging
        echo " # ${i}.4 Logging"
        mpstat 1 > ${RESULTSDIR}stat/mpstat-${i}-${j}-4.txt &
        vmstat 1 > ${RESULTSDIR}stat/vmstat-${i}-${j}-4.txt &
        iostat -xn 10 > ${RESULTSDIR}stat/iostat-${i}-${j}-4.txt &
        ${BINDJAVA} java  ${JAVAARGS_KIEKER_LOGGING} ${JAR} \
            --output-filename ${RESULTSFN}-${i}-${j}-1.csv \
            --totalcalls ${TOTALCALLS} \
            --methodtime ${METHODTIME} \
            --totalthreads ${THREADS} \
            --recursiondepth ${j}
        kill %mpstat
        kill %vmstat
        kill %iostat
        mkdir -p ${RESULTSDIR}kiekerlog/
        mv ${BASEDIR}tmp/kieker-* ${RESULTSDIR}kiekerlog/
        [ -f ${BASEDIR}hotspot.log ] && mv ${BASEDIR}hotspot.log ${RESULTSDIR}hotspot-${i}-${j}-4.log
        echo >>${BASEDIR}kieker.log
        echo >>${BASEDIR}kieker.log
        sync
        sleep ${SLEEPTIME}
    
    done

done
tar cf ${RESULTSDIR}kiekerlog.tar ${RESULTSDIR}kiekerlog
pfexec rm -rf ${RESULTSDIR}kiekerlog/
gzip -9 ${RESULTSDIR}kiekerlog.tar
tar cf ${RESULTSDIR}stat.tar ${RESULTSDIR}stat
rm -rf ${RESULTSDIR}stat/
gzip -9 ${RESULTSDIR}stat.tar
mv ${BASEDIR}kieker.log ${RESULTSDIR}kieker.log
[ -f ${RESULTSDIR}hotspot-1-1-1.log ] && grep "<task " ${RESULTSDIR}hotspot-*.log >${RESULTSDIR}log.log
[ -f ${BASEDIR}nohup.out ] && cp ${BASEDIR}nohup.out ${RESULTSDIR}
echo -n "" > ${BASEDIR}nohup.out
