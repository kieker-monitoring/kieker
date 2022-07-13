#!/bin/bash

# This scripts automates the execution of the AspectJ example

rm monitoring-logs/* -rf

cp ../../../build/libs/kieker-2.0.0-SNAPSHOT-aspectj.jar lib/
./gradlew runMonitoring

logCount=$(cat monitoring-logs/kieker-*/kieker-*.dat | wc -l)

if (( $logCount == 13 )) ; then
	echo "Run was sucessful"
else
	echo "Run had $logCount log entries; error occured"
	exit 1
fi
