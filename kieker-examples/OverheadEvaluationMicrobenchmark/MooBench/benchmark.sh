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

JAVA_PROGRAM="-jar MooBench.jar -a mooBench.monitoredApplication.MonitoredClassSimple"

JAVAARGS_LTW="${JAVAARGS} -javaagent:${AGENT} -Dorg.aspectj.weaver.showWeaveInfo=false -Daj.weaving.verbose=false -Dkieker.monitoring.skipDefaultAOPConfiguration=true -Dorg.aspectj.weaver.loadtime.configuration=${AOP}"


# JAVAARGS used to configure and setup a specific writer
declare -a WRITER_CONFIG
# Receiver setup if necessary
declare -a RECEIVER
# Title
declare -a TITLE

# Configurations
TITLE[0]="No instrumentation"
WRITER_CONFIG[0]=""

TITLE[1]="Deactivated probe"
WRITER_CONFIG[1]="-Dkieker.monitoring.enabled=false -Dkieker.monitoring.writer=kieker.monitoring.writer.dump.DumpWriter"

TITLE[2]="No logging (null writer)"
WRITER_CONFIG[2]="-Dkieker.monitoring.writer=kieker.monitoring.writer.dump.DumpWriter"

TITLE[3]="Logging (ASCII)"
WRITER_CONFIG[3]="-Dkieker.monitoring.writer=kieker.monitoring.writer.filesystem.AsciiFileWriter -Dkieker.monitoring.writer.filesystem.AsciiFileWriter.customStoragePath=${DATA_DIR}"

TITLE[4]="Logging (Generic Text)"
WRITER_CONFIG[4]="-Dkieker.monitoring.writer=kieker.monitoring.writer.filesystem.FileWriter -Dkieker.monitoring.writer.filesystem.FileWriter.logStreamHandler=kieker.monitoring.writer.filesystem.TextLogStreamHandler -Dkieker.monitoring.writer.filesystem.FileWriter.customStoragePath=${DATA_DIR}"

TITLE[5]="Logging (Bin)"
WRITER_CONFIG[5]="-Dkieker.monitoring.writer=kieker.monitoring.writer.filesystem.BinaryFileWriter -Dkieker.monitoring.writer.filesystem.BinaryFileWriter.customStoragePath=${DATA_DIR}"

TITLE[6]="Logging (Generic Bin)"
WRITER_CONFIG[6]="-Dkieker.monitoring.writer=kieker.monitoring.writer.filesystem.FileWriter -Dkieker.monitoring.writer.filesystem.FileWriter.logStreamHandler=kieker.monitoring.writer.filesystem.BinaryLogStreamHandler -Dkieker.monitoring.writer.filesystem.FileWriter.customStoragePath=${DATA_DIR}"

TITLE[7]="Logging (TCP)"
WRITER_CONFIG[7]="-Dkieker.monitoring.writer=kieker.monitoring.writer.tcp.TCPWriter"
RECEIVER[single-tcp]="${JAVABIN}java -classpath MooBench.jar kieker.tcp.TestExperiment0"

# Create R labels
LABELS=""
for I in ${WRITER_TYPE[@]} ; do
	title="${TITLE[$I]}"
	if [ "$LABELS" == "" ] ; then
		LABELS="\"$title\""
	else
		LABELS="${LABELS}, \"$title\""
	fi	
done

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

    ${JAVABIN}java  ${writer_parameters} ${JAVA_PROGRAM} \
        --output-filename ${RAWFN}-${i}-${j}-${k}.csv \
        --totalcalls ${TOTALCALLS} \
        --methodtime ${METHODTIME} \
        --totalthreads ${THREADS} \
        --recursiondepth ${j} \
        ${MOREPARAMS}

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

	for ((index=0;index<${#WRITER_CONFIG[@]};index+=1)); do
		execute-experiment "$i" "$j" "$index" "${TITLE[$index]}" "${WRITER_CONFIG[$index]}"
		if [[ ${RECEIVER[$index]} ]] ; then
			${RECEIVER[$index_name]} >> ${DATA_DIR}kieker.receiver-$i-$index.log
		fi
	done
done

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
configs.labels=c($LABELS)
results.count=${TOTALCALLS}
results.skip=${TOTALCALLS}/2
source("${RSCRIPTDIR}stats.csv.r")
EOF

## Clean up raw results
zip -jqr ${RESULTS_DIR}results.zip ${RAWFN}*
rm -f ${RAWFN}*
[ -f ${DATA_DIR}nohup.out ] && cp ${DATA_DIR}nohup.out ${RESULTS_DIR}
[ -f ${DATA_DIR}nohup.out ] && > ${DATA_DIR}nohup.out
