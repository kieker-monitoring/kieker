#!/bin/sh

# This scripts automates the execution of the Spring example

rm monitoring-logs/* -rf

./gradlew runMonitoring

logCount=$(cat monitoring-logs/kieker-*/kieker-*.dat | wc -l)

if (( $logCount == 5 )) ; then
	echo "Run was sucessful"
else
	echo "Run had $logCount log entries; error occured"
	exit 1
fi
