#!/bin/bash

BASE_DIR=$(cd "$(dirname "$0")"; pwd)

NUM_OF_REPEATS=100
export RESULT_FILE="${BASE_DIR}/MooBench/results-kieker/results-text.csv"
COLLECTED_DATA_FILE="${BASE_DIR}/results.csv"
BENCHMARK="${BASE_DIR}/MooBench/benchmark.sh"
#BENCHMARK="${BASE_DIR}/ex.sh"

rm -f ${COLLECTED_DATA_FILE}

for ((v=1;v<=${NUM_OF_REPEATS};v+=1)); do
	echo "++++++++++++++++++++++++++"
	echo "Rerun $v"
	echo "++++++++++++++++++++++++++"
	${BENCHMARK} # > /dev/null 2>&1
	HEAD=`head -1 $RESULT_FILE`
	VALUE=`tail -1 $RESULT_FILE`
	if [ -f "${COLLECTED_DATA_FILE}" ] ; then
		echo "$VALUE" >> ${COLLECTED_DATA_FILE}
	else
		echo "$HEAD" > ${COLLECTED_DATA_FILE}
                echo "$VALUE" >> ${COLLECTED_DATA_FILE}
	fi
done

# end

