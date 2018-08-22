
#!/bin/bash

# internal parameter configuration

JAVA_BIN="java"

BASE_DIR=$(cd "$(dirname "$0")"; pwd)

RSCRIPT_DIR="r/stats.csv.r"

DATA_DIR="${BASE_DIR}/data"
RESULTS_DIR="${DATA_DIR}/results-kieker"
AGENT="${BASE_DIR}/lib/kieker-1.14-SNAPSHOT-aspectj.jar"
AOP="META-INF/kieker.aop.xml"

SLEEP_TIME=30                   ## 30
NUM_OF_LOOPS=10                 ## 10
RECURSION_DEPTH=10              ## 10
TOTAL_NUM_OF_CALLS=8000000      ## 2000000
METHOD_TIME=5                   ## 500000

# test input parameters and configuration
if [ ! -d "${BASE_DIR}" ] ; then
	echo "Base directory ${BASE_DIR} does not exist."
	exit 1
fi
if [ ! -d "${DATA_DIR}" ] ; then
	mkdir "${DATA_DIR}"
fi
if [ ! -f "${AGENT}" ] ; then
	echo "Kieker agent for AspectJ ${AGENT} is missing."
	exit 1
fi

echo "----------------------------------"
echo "Running benchmark..."
echo "----------------------------------"

MORE_PARAMS="--quickstart"
MORE_PARAMS="${MORE_PARAMS} -r kieker.Logger"

TIME=`expr ${METHOD_TIME} \* ${TOTAL_NUM_OF_CALLS} / 1000000000 \* 4 \* ${RECURSION_DEPTH} \* ${NUM_OF_LOOPS} + ${SLEEP_TIME} \* 4 \* ${NUM_OF_LOOPS}  \* ${RECURSION_DEPTH} + 50 \* ${TOTAL_NUM_OF_CALLS} / 1000000000 \* 4 \* ${RECURSION_DEPTH} \* ${NUM_OF_LOOPS} `
echo "Experiment will take circa ${TIME} seconds."

echo "Removing and recreating '$RESULTS_DIR'"
(rm -rf ${RESULTS_DIR}) && mkdir -p ${RESULTS_DIR}

# Clear kieker.log and initialize logging
rm -f ${DATA_DIR}/kieker.log
touch ${DATA_DIR}/kieker.log

RAWFN="${RESULTS_DIR}/raw"

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
WRITER_CONFIG[3]="-Dkieker.monitoring.writer=kieker.monitoring.writer.filesystem.AsciiFileWriter -Dkieker.monitoring.writer.filesystem.AsciiFileWriter.customStoragePath=${DATA_DIR}/"

TITLE[4]="Logging (Generic Text)"
WRITER_CONFIG[4]="-Dkieker.monitoring.writer=kieker.monitoring.writer.filesystem.FileWriter -Dkieker.monitoring.writer.filesystem.FileWriter.logStreamHandler=kieker.monitoring.writer.filesystem.TextLogStreamHandler -Dkieker.monitoring.writer.filesystem.FileWriter.customStoragePath=${DATA_DIR}/"

TITLE[5]="Logging (Bin)"
WRITER_CONFIG[5]="-Dkieker.monitoring.writer=kieker.monitoring.writer.filesystem.BinaryFileWriter -Dkieker.monitoring.writer.filesystem.BinaryFileWriter.customStoragePath=${DATA_DIR}/"

TITLE[6]="Logging (Generic Bin)"
WRITER_CONFIG[6]="-Dkieker.monitoring.writer=kieker.monitoring.writer.filesystem.FileWriter -Dkieker.monitoring.writer.filesystem.FileWriter.logStreamHandler=kieker.monitoring.writer.filesystem.BinaryLogStreamHandler -Dkieker.monitoring.writer.filesystem.FileWriter.customStoragePath=${DATA_DIR}/"

TITLE[7]="Logging (TCP)"
WRITER_CONFIG[7]="-Dkieker.monitoring.writer=kieker.monitoring.writer.tcp.TCPWriter"
RECEIVER[7]="${JAVA_BIN} -classpath MooBench.jar kieker.tcp.TestExperiment0"

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
    writer_parameters="$5"

    echo " # ${i}.${j}.${k} ${title}"
    echo " # ${i}.${j}.${k} ${title}" >>${DATA_DIR}/kieker.log

    ${JAVA_BIN} ${writer_parameters} ${JAVA_PROGRAM} \
        --output-filename ${RAWFN}-${i}-${j}-${k}.csv \
        --totalcalls ${TOTAL_NUM_OF_CALLS} \
        --methodtime ${METHOD_TIME} \
        --totalthreads 1 \
        --recursiondepth ${j} \
        ${MORE_PARAMS}

    [ -f ${DATA_DIR}/hotspot.log ] && mv ${DATA_DIR}/hotspot.log ${RESULTS_DIR}hotspot-${i}-${j}-${k}.log
    echo >> ${DATA_DIR}/kieker.log
    echo >> ${DATA_DIR}/kieker.log
    sync
    sleep ${SLEEP_TIME}
}

## Execute Benchmark
for ((i=1;i<=${NUM_OF_LOOPS};i+=1)); do
    j=${RECURSION_DEPTH}

    echo "## Starting iteration ${i}/${NUM_OF_LOOPS}"
    echo "## Starting iteration ${i}/${NUM_OF_LOOPS}" >>${DATA_DIR}/kieker.log

    for ((index=0;index<${#WRITER_CONFIG[@]};index+=1)); do
      if [[ ${RECEIVER[$index]} ]] ; then
         echo "receiver ${RECEIVER[$index]}"
         ${RECEIVER[$index]} >> ${DATA_DIR}/kieker.receiver-$i-$index.log &
         RECEIVER_PID=$!
      fi
      execute-experiment "$i" "$j" "$index" "${TITLE[$index]}" "${WRITER_CONFIG[$index]}"
      if [[ $RECEIVER_PID ]] ; then
         kill -TERM $RECEIVER_PID
         kill -9 $RECEIVER_PID
         unset RECEIVER_PID
      fi
    done
done

mv ${DATA_DIR}/kieker.log ${RESULTS_DIR}/kieker.log
[ -f ${RESULTS_DIR}/hotspot-1-${RECURSION_DEPTH}-1.log ] && grep "<task " ${RESULTS_DIR}/hotspot-*.log > ${RESULTS_DIR}/log.log
[ -f ${DATA_DIR}/errorlog.txt ] && mv ${DATA_DIR}/errorlog.txt ${RESULTS_DIR}

## Generate Results file
#R --vanilla --silent << EOF
cat > a << EOF
results_fn="${RAWFN}"
outtxt_fn="${RESULTS_DIR}/results-text.txt"
outcsv_fn="${RESULTS_DIR}/results-text.csv"
configs.loop=${NUM_OF_LOOPS}
configs.recursion=${RECURSION_DEPTH}
configs.labels=c($LABELS)
results.count=${TOTAL_NUM_OF_CALLS}
results.skip=${TOTAL_NUM_OF_CALLS}/2
source("${RSCRIPT}")
EOF

exit 1

## Clean up raw results
zip -jqr ${RESULTS_DIR}/results.zip ${RAWFN}*
rm -f ${RAWFN}*
[ -f ${DATA_DIR}/nohup.out ] && cp ${DATA_DIR}/nohup.out ${RESULTS_DIR}
[ -f ${DATA_DIR}/nohup.out ] && > ${DATA_DIR}/nohup.out

echo "Done."

# end
