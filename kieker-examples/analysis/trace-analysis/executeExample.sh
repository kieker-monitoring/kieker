#!/bin/bash

# This scripts automates the execution of the trace-analysis example

rm kieker.log

./gradlew runAnalysis

processedCount=$(cat kieker.log | grep "Records processed in total" | awk '{print $NF}' | tr -d ".")

if (( $processedCount == 6541 )) ; then
	echo "Run was sucessful"
else
	echo "Run had $processedCount log entries; error occured"
	exit 1
fi
