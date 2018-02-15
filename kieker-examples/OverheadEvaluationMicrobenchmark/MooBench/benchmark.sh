#!/bin/bash

# internal parameter configuration

JAVABIN=""

BASE_DIR=$(cd "$(dirname "$0")"; pwd)/

RSCRIPTDIR=r/

DATA_DIR="/data/reiner/"
RESULTS_DIR="${DATA_DIR}/results-kieker/"
AGENT="${BASE_DIR}lib/kieker-1.14-SNAPSHOT-aspectj.jar"
AOP="META-INF/kieker.aop.xml"

SLEEPTIME=30            ## 30
NUM_LOOPS=10            ## 10
THREADS=1               ## 1
RECURSIONDEPTH=10       ## 10
TOTALCALLS=2000000      ## 2000000
METHODTIME=0       ## 500000

# test input parameters and configuration
if [ ! -d "${BASE_DIR}" ] ; then
	echo "Base directory ${BASE_DIR} does not exist."
	exit 1
fi
if [ ! -d "${DATA_DIR}" ] ; then
	echo "Data directory ${DATA_DIR} does not exist."
	exit 1
fi
if [ ! -f "${AGENT}" ] ; then
	echo "Kieker agent for AspectJ ${AGENT} is missing."
	exit 1
fi

MOREPARAMS="--quickstart"
MOREPARAMS="${MOREPARAMS} -r kieker.Logger"

TIME=`expr ${METHODTIME} \* ${TOTALCALLS} / 1000000000 \* 4 \* ${RECURSIONDEPTH} \* ${NUM_LOOPS} + ${SLEEPTIME} \* 4 \* ${NUM_LOOPS}  \* ${RECURSIONDEPTH} + 50 \* ${TOTALCALLS} / 1000000000 \* 4 \* ${RECURSIONDEPTH} \* ${NUM_LOOPS} `
echo "Experiment will take circa ${TIME} seconds."

echo "Removing and recreating '$RESULTS_DIR'"
(rm -rf ${RESULTS_DIR}) && mkdir -p ${RESULTS_DIR}
#mkdir ${RESULTS_DIR}stat/

# Clear kieker.log and initialize logging
rm -f ${DATA_DIR}kieker.log
touch ${DATA_DIR}kieker.log

RAWFN="${RESULTS_DIR}raw"

# general server arguments
JAVAARGS="-server"
JAVAARGS="${JAVAARGS} -d64"
JAVAARGS="${JAVAARGS} -Xms1G -Xmx4G"
#JAVAARGS="${JAVAARGS} -verbose:gc -XX:+PrintCompilation"
#JAVAARGS="${JAVAARGS} -XX:+PrintInlining"
#JAVAARGS="${JAVAARGS} -XX:+UnlockDiagnosticVMOptions -XX:+LogCompilation"
#JAVAARGS="${JAVAARGS} -Djava.compiler=NONE"
JAR="-jar MooBench.jar -a mooBench.monitoredApplication.MonitoredClassSimple"

JAVAARGS_LTW="${JAVAARGS} -javaagent:${AGENT} -Dorg.aspectj.weaver.showWeaveInfo=false -Daj.weaving.verbose=false -Dkieker.monitoring.skipDefaultAOPConfiguration=true -Dorg.aspectj.weaver.loadtime.configuration=${AOP}"

# configure different experiments
JAVAARGS_NOINSTR="${JAVAARGS}"

JAVAARGS_KIEKER_DEACTV="-Dkieker.monitoring.enabled=false -Dkieker.monitoring.writer=kieker.monitoring.writer.dump.DumpWriter"
JAVAARGS_KIEKER_NOLOGGING="-Dkieker.monitoring.writer=kieker.monitoring.writer.dump.DumpWriter"
JAVAARGS_KIEKER_LOGGING_ASCII="-Dkieker.monitoring.writer=kieker.monitoring.writer.filesystem.AsciiFileWriter -Dkieker.monitoring.writer.filesystem.AsciiFileWriter.customStoragePath=${DATA_DIR}"
JAVAARGS_KIEKER_LOGGING_BIN="-Dkieker.monitoring.writer=kieker.monitoring.writer.filesystem.BinaryFileWriter -Dkieker.monitoring.writer.filesystem.BinaryFileWriter.customStoragePath=${DATA_DIR}"
JAVAARGS_KIEKER_LOGGING_GENERIC_TEXT="-Dkieker.monitoring.writer=kieker.monitoring.writer.filesystem.FileWriter -Dkieker.monitoring.writer.filesystem.FileWriter.logStreamHandler=kieker.monitoring.writer.filesystem.TextLogStreamHandler -Dkieker.monitoring.writer.filesystem.FileWriter.customStoragePath=${DATA_DIR}"
JAVAARGS_KIEKER_LOGGING_GENERIC_BIN="-Dkieker.monitoring.writer=kieker.monitoring.writer.filesystem.FileWriter -Dkieker.monitoring.writer.filesystem.FileWriter.logStreamHandler=kieker.monitoring.writer.filesystem.BinaryLogStreamHandler -Dkieker.monitoring.writer.filesystem.FileWriter.customStoragePath=${DATA_DIR}"
JAVAARGS_KIEKER_LOGGING_TCP="-Dkieker.monitoring.writer=kieker.monitoring.writer.tcp.TCPWriter"

## Write configuration
uname -a >${RESULTS_DIR}configuration.txt
${JAVABIN}java ${JAVAARGS} -version 2>>${RESULTS_DIR}configuration.txt
cat << EOF >>${RESULTS_DIR}configuration.txt
JAVAARGS: ${JAVAARGS}

Runtime: circa ${TIME} seconds

SLEEPTIME=${SLEEPTIME}
NUM_LOOPS=${NUM_LOOPS}
TOTALCALLS=${TOTALCALLS}
METHODTIME=${METHODTIME}
THREADS=${THREADS}
RECURSIONDEPTH=${RECURSIONDEPTH}
EOF

sync

#################################
# function: execute an experiment
#
# $1 = i iterator
# $2 = j iterator
# $3 = k iterator
# $4 = title
# $5 = writer parameters
function execute-experiment() {
    i="$1"
    j="$2"
    k="$3"
    title="$4"
    writer_parameters="$5"

    echo " # ${i}.${j}.${k} ${title}"
    echo " # ${i}.${j}.${k} ${title}" >>${DATA_DIR}kieker.log
    #sar -o ${RESULTS_DIR}stat/sar-${i}-${j}-${k}.data 5 2000 1>/dev/null 2>&1 &
    ${JAVABIN}java  ${writer_parameters} ${JAR} \
        --output-filename ${RAWFN}-${i}-${j}-${k}.csv \
        --totalcalls ${TOTALCALLS} \
        --methodtime ${METHODTIME} \
        --totalthreads ${THREADS} \
        --recursiondepth ${j} \
        ${MOREPARAMS}
    #kill %sar
    [ -f ${DATA_DIR}hotspot.log ] && mv ${DATA_DIR}hotspot.log ${RESULTS_DIR}hotspot-${i}-${j}-${k}.log
    echo >>${DATA_DIR}kieker.log
    echo >>${DATA_DIR}kieker.log
    sync
    sleep ${SLEEPTIME}
}

## Execute Benchmark
for ((i=1;i<=${NUM_LOOPS};i+=1)); do
    j=${RECURSIONDEPTH}
    
    echo "## Starting iteration ${i}/${NUM_LOOPS}"
    echo "## Starting iteration ${i}/${NUM_LOOPS}" >>${DATA_DIR}kieker.log

    # No instrumentation
    execute-experiment "$i" "$j" "1" "No instrumentation" "${JAVAARGS_NOINSTR}"

    # Deactivated probe
    execute-experiment "$i" "$j" "2" "Deactivated probe" "${JAVAARGS_LTW} ${JAVAARGS_KIEKER_DEACTV}"

    # No logging
    execute-experiment "$i" "$j" "3" "No logging (null writer)" "${JAVAARGS_LTW} ${JAVAARGS_KIEKER_NOLOGGING}"
    
    # Old ASCII writer
    execute-experiment "$i" "$j" "4" "Logging (ASCII)" "${JAVAARGS_LTW} ${JAVAARGS_KIEKER_LOGGING_ASCII}"
    
    # New Text writer
    execute-experiment "$i" "$j" "5" "Logging (Generic Text)" "${JAVAARGS_LTW} ${JAVAARGS_KIEKER_LOGGING_GENERIC_TEXT}"

    # Old bin writer
	execute-experiment "$i" "$j" "6" "Logging (Bin)" "${JAVAARGS_LTW} ${JAVAARGS_KIEKER_LOGGING_BIN}"
    
    # New bin writer
    execute-experiment "$i" "$j" "7" "Logging (Generic Bin)" "${JAVAARGS_LTW} ${JAVAARGS_KIEKER_LOGGING_GENERIC_BIN}" 
    	
    # TCP writer
	${JAVABIN}java -classpath MooBench.jar kieker.tcp.TestExperiment0 >> ${DATA_DIR}kieker.tcp.log &
	execute-experiment "$i" "$j" "8" "Logging (TCP)" ${JAVAARGS_LTW} ${JAVAARGS_KIEKER_LOGGING_TCP}
 	
done
#zip -jqr ${RESULTS_DIR}stat.zip ${RESULTS_DIR}stat
#rm -rf ${RESULTS_DIR}stat/
mv ${DATA_DIR}kieker.log ${RESULTS_DIR}kieker.log
[ -f ${RESULTS_DIR}hotspot-1-${RECURSIONDEPTH}-1.log ] && grep "<task " ${RESULTS_DIR}hotspot-*.log >${RESULTS_DIR}log.log
[ -f ${DATA_DIR}errorlog.txt ] && mv ${DATA_DIR}errorlog.txt ${RESULTS_DIR}

## Generate Results file
R --vanilla --silent <<EOF
results_fn="${RAWFN}"
outtxt_fn="${RESULTS_DIR}results-text.txt"
outcsv_fn="${RESULTS_DIR}results-text.csv"
configs.loop=${NUM_LOOPS}
configs.recursion=c(${RECURSIONDEPTH})
configs.labels=c("No Probe","Deactivated Probe","Collecting Data","Writer (ASCII)", "Writer (Bin)", "Writer (TCP)")
results.count=${TOTALCALLS}
results.skip=${TOTALCALLS}/2
source("${RSCRIPTDIR}stats.csv.r")
EOF

## Clean up raw results
zip -jqr ${RESULTS_DIR}results.zip ${RAWFN}*
rm -f ${RAWFN}*
[ -f ${DATA_DIR}nohup.out ] && cp ${DATA_DIR}nohup.out ${RESULTS_DIR}
[ -f ${DATA_DIR}nohup.out ] && > ${DATA_DIR}nohup.out
