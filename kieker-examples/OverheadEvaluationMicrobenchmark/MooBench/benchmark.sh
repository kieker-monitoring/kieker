#!/bin/bash

# configure base dir
BASE_DIR=$(cd "$(dirname "$0")"; pwd)

if [ ! -d "${BASE_DIR}" ] ; then
	echo "Base directory ${BASE_DIR} does not exist."
	exit 1
fi

# load configuration and common functions
if [ -f "${BASE_DIR}/config" ] ; then
	. ${BASE_DIR}/config
else
	echo "Missing configuration: ${BASE_DIR}/config"
	exit 1
fi
if [ -f "${BASE_DIR}/common-functions.sh" ] ; then
	. ${BASE_DIR}/common-functions.sh
else
	echo "Missing configuration: ${BASE_DIR}/common-functions.sh"
	exit 1
fi


# check command line parameters
if [ "$1" == "" ] ; then
	MODE="execute"
else
	if [ "$1" == "execute" ] ; then
		MODE="execute"
	else
		mode="test"
	fi
	OPTION="$2"
fi

# test input parameters and configuration
checkFile R-script "${RSCRIPT_PATH}"
checkDirectory DATA_DIR "${DATA_DIR}" create

PARENT=`dirname "${RESULTS_DIR}"
checkDirectory result-base "$PARENT"
checkExecutable ApsectJ-Agent "${AGENT}"
checkFile moobench "${MOOBENCH}"

echo "----------------------------------"
echo "Running benchmark..."
echo "----------------------------------"

FIXED_PARAMETERS="--quickstart -a moobench.monitoredApplication.MonitoredClassSimple"

TIME=`expr ${METHOD_TIME} \* ${TOTAL_NUM_OF_CALLS} / 1000000000 \* 4 \* ${RECURSION_DEPTH} \* ${NUM_OF_LOOPS} + ${SLEEP_TIME} \* 4 \* ${NUM_OF_LOOPS}  \* ${RECURSION_DEPTH} + 50 \* ${TOTAL_NUM_OF_CALLS} / 1000000000 \* 4 \* ${RECURSION_DEPTH} \* ${NUM_OF_LOOPS} `
echo "Experiment will take circa ${TIME} seconds."

echo "Removing and recreating '$RESULTS_DIR'"
(rm -rf ${RESULTS_DIR}) && mkdir -p ${RESULTS_DIR}

# Clear kieker.log and initialize logging
rm -f ${DATA_DIR}/kieker.log
touch ${DATA_DIR}/kieker.log

RAWFN="${RESULTS_DIR}/raw"

# general server arguments
JAVA_ARGS="-server"
JAVA_ARGS="${JAVA_ARGS} -d64"
JAVA_ARGS="${JAVA_ARGS} -Xms1G -Xmx4G"

JAVA_PROGRAM="-jar ${MOOBENCH} ${FIXED_PARAMETERS}"

LTW_ARGS="-javaagent:${AGENT} -Dorg.aspectj.weaver.showWeaveInfo=false -Daj.weaving.verbose=false -Dkieker.monitoring.skipDefaultAOPConfiguration=true -Dorg.aspectj.weaver.loadtime.configuration=${AOP}"

KIEKER_ARGS="-Dlog4j.configuration=log4j.cfg -Dkieker.monitoring.name=KIEKER-BENCHMARK -Dkieker.monitoring.adaptiveMonitoring.enabled=false -Dkieker.monitoring.periodicSensorsExecutorPoolSize=0"

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
WRITER_CONFIG[2]="-Dkieker.monitoring.enabled=true -Dkieker.monitoring.writer=kieker.monitoring.writer.dump.DumpWriter"

TITLE[3]="Logging (ASCII)"
WRITER_CONFIG[3]="-Dkieker.monitoring.enabled=true -Dkieker.monitoring.writer=kieker.monitoring.writer.filesystem.AsciiFileWriter -Dkieker.monitoring.writer.filesystem.AbstractFileWriter.customStoragePath=${DATA_DIR}/"

TITLE[4]="Logging (Generic Text)"
WRITER_CONFIG[4]="-Dkieker.monitoring.enabled=true -Dkieker.monitoring.writer=kieker.monitoring.writer.filesystem.FileWriter -Dkieker.monitoring.writer.filesystem.FileWriter.logStreamHandler=kieker.monitoring.writer.filesystem.TextLogStreamHandler -Dkieker.monitoring.writer.filesystem.FileWriter.customStoragePath=${DATA_DIR}/"

TITLE[5]="Logging (Bin)"
WRITER_CONFIG[5]="-Dkieker.monitoring.enabled=true -Dkieker.monitoring.writer=kieker.monitoring.writer.filesystem.BinaryFileWriter -Dkieker.monitoring.writer.filesystem.AbstractFileWriter.customStoragePath=${DATA_DIR}/"

TITLE[6]="Logging (Generic Bin)"
WRITER_CONFIG[6]="-Dkieker.monitoring.enabled=true -Dkieker.monitoring.writer=kieker.monitoring.writer.filesystem.FileWriter -Dkieker.monitoring.writer.filesystem.FileWriter.logStreamHandler=kieker.monitoring.writer.filesystem.BinaryLogStreamHandler -Dkieker.monitoring.writer.filesystem.FileWriter.bufferSize=8192 -Dkieker.monitoring.writer.filesystem.FileWriter.customStoragePath=${DATA_DIR}/"

TITLE[7]="Logging (Dual TCP)"
WRITER_CONFIG[7]="-Dkieker.monitoring.writer=kieker.monitoring.writer.tcp.DualSocketTcpWriter -Dkieker.monitoring.writer.tcp.DualSocketTcpWriter.port1=2345 -Dkieker.monitoring.writer.tcp.DualSocketTcpWriter.port2=2346"
RECEIVER[7]="${BASE_DIR}/collector-2.0/bin/collector -p 2345 -p 2346"

TITLE[8]="Logging (Single TCP)"
WRITER_CONFIG[8]="-Dkieker.monitoring.writer=kieker.monitoring.writer.tcp.SingleSocketTcpWriter -Dkieker.monitoring.writer.tcp.SingleSocketTcpWriter.port=2345"
RECEIVER[8]="${BASE_DIR}/collector-2.0/bin/collector -p 2345"

export COLLECTOR_OPTS="-Dlog4j.configuration=file://${BASE_DIR}/log4j.cfg"

# Create R labels
LABELS=""
for I in "${TITLE[@]}" ; do
	title="$I"
	if [ "$LABELS" == "" ] ; then
		LABELS="\"$title\""
	else
		LABELS="${LABELS}, \"$title\""
	fi
done

## Write configuration
uname -a >${RESULTS_DIR}/configuration.txt
${JAVA_BIN} ${JAVAARGS} -version 2>>${RESULTS_DIR}/configuration.txt
cat << EOF >>${RESULTS_DIR}/configuration.txt
JAVAARGS: ${JAVAARGS}

Runtime: circa ${TIME} seconds

SLEEP_TIME=${SLEEP_TIME}
NUM_OF_LOOPS=${NUM_OF_LOOPS}
TOTAL_NUM_OF_CALLS=${TOTAL_NUM_OF_CALLS}
METHOD_TIME=${METHOD_TIME}
RECURSION_DEPTH=${RECURSION_DEPTH}
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
    kieker_parameters="$5"

    echo " # ${i}.${j}.${k} ${title}"
    echo " # ${i}.${j}.${k} ${title}" >> ${DATA_DIR}/kieker.log

    if [  "${kieker_parameters}" = "" ] ; then
       COMPLETE_ARGS=${JAVA_ARGS}
    else
       COMPLETE_ARGS="${JAVA_ARGS} ${LTW_ARGS} ${KIEKER_ARGS} ${kieker_parameters}"
    fi

    ${JAVA_BIN} ${COMPLETE_ARGS} ${JAVA_PROGRAM} \
        --output-filename ${RAWFN}-${i}-${j}-${k}.csv \
        --total-calls ${TOTAL_NUM_OF_CALLS} \
        --method-time ${METHOD_TIME} \
        --total-threads 1 \
        --recursion-depth ${j}

    rm -rf ${DATA_DIR}/kieker-*

    [ -f ${DATA_DIR}/hotspot.log ] && mv ${DATA_DIR}/hotspot.log ${RESULTS_DIR}hotspot-${i}-${j}-${k}.log
    echo >> ${DATA_DIR}/kieker.log
    echo >> ${DATA_DIR}/kieker.log
    sync
    sleep ${SLEEP_TIME}
}

## Execute Benchmark
function execute-benchmark() {
  for ((i=1;i<=${NUM_OF_LOOPS};i+=1)); do
    j=${RECURSION_DEPTH}

    echo "## Starting iteration ${i}/${NUM_OF_LOOPS}"
    echo "## Starting iteration ${i}/${NUM_OF_LOOPS}" >>${DATA_DIR}/kieker.log

    for ((index=0;index<${#WRITER_CONFIG[@]};index+=1)); do
      execute-benchmark-body $index $i $j
    done
  done

  mv ${DATA_DIR}/kieker.log ${RESULTS_DIR}/kieker.log
  [ -f ${RESULTS_DIR}/hotspot-1-${RECURSION_DEPTH}-1.log ] && grep "<task " ${RESULTS_DIR}/hotspot-*.log > ${RESULTS_DIR}/log.log
  [ -f ${DATA_DIR}/errorlog.txt ] && mv ${DATA_DIR}/errorlog.txt ${RESULTS_DIR}
}

function execute-benchmark-body() {
  index="$1"
  i="$2"
  j="$3"
  if [[ ${RECEIVER[$index]} ]] ; then
     echo "receiver ${RECEIVER[$index]}"
     ${RECEIVER[$index]} & #>> ${DATA_DIR}/kieker.receiver-$i-$index.log &
     RECEIVER_PID=$!
  fi

  execute-experiment "$i" "$j" "$index" "${TITLE[$index]}" "${WRITER_CONFIG[$index]}"

  if [[ $RECEIVER_PID ]] ; then
     wait $RECEIVER_PID
     unset RECEIVER_PID
  fi
}

## Generate Results file
function run-r() {
R --vanilla --silent << EOF
results_fn="${RAWFN}"
outtxt_fn="${RESULTS_DIR}/results-text.txt"
outcsv_fn="${RESULTS_DIR}/results-text.csv"
configs.loop=${NUM_OF_LOOPS}
configs.recursion=${RECURSION_DEPTH}
configs.labels=c($LABELS)
results.count=${TOTAL_NUM_OF_CALLS}
results.skip=${TOTAL_NUM_OF_CALLS}/2
source("${RSCRIPT_PATH}")
EOF
}

## Clean up raw results
function cleanup-results() {
  zip -jqr ${RESULTS_DIR}/results.zip ${RAWFN}*
#  rm -f ${RAWFN}*
  [ -f ${DATA_DIR}/nohup.out ] && cp ${DATA_DIR}/nohup.out ${RESULTS_DIR}
  [ -f ${DATA_DIR}/nohup.out ] && > ${DATA_DIR}/nohup.out
}

## Execute benchmark
if [ "$MODE" == "execute" ] ; then
   if [ "$OPTION" == "" ] ; then
     execute-benchmark
   else
     execute-benchmark-body $OPTION 1 1
   fi
   run-r
   cleanup-results
else
   execute-benchmark-body $OPTION 1 1
fi

echo "Done."

# end
