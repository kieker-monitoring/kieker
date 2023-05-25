#!/bin/bash

# This scripts automates the execution of the trace-analysis example

rm -f kieker.log

./gradlew runAnalysis

processedCount=$(cat kieker.log | grep "Records processed in total" | awk '{print $NF}' | tr -d ".")

if (( $processedCount == 6541 )) ; then
	echo "[INFO] Run was sucessful"
else
	echo "[ERROR] Run had $processedCount log entries; error occured"
	exit 1
fi

# end
