#!/bin/bash

# This scripts automates the execution of the Spring Boot example

set -e

function tryRegularRun() {
	echo
	echo
	echo "Executing Spring Boot Without Instrumentation..."

	java -javaagent:spring-instrument-5.3.37.jar -jar build/libs/demo-spring-boot-0.0.1-SNAPSHOT.jar &

	sleep 5

	curl localhost:8080

	pkill -P $$
}

function tryInstrumentedRun() {
	echo
	echo
	echo "Executing Spring Boot With Instrumentation..."

        mkdir -p monitoring-logs/
	rm monitoring-logs/* -rf

	java -Dkieker.monitoring.writer.filesystem.FileWriter.customStoragePath=monitoring-logs \
		-Dkieker.monitoring.skipDefaultAOPConfiguration=true \
		-javaagent:spring-instrument-5.3.37.jar \
		-javaagent:lib/kieker-2.0.0-SNAPSHOT-aspectj.jar \
		-jar build/libs/demo-spring-boot-0.0.1-SNAPSHOT.jar &

	sleep 5

	for i in {1..15}
	do
		curl localhost:8080
	done
	
	echo
	echo
	echo "Killing background process..."
	pkill -P $$
	sleep 1 # Increases the probability that killing is finished and the following output is correct

	logCount=$(cat monitoring-logs/kieker-*/kieker-*.dat | wc -l)

	if [ $logCount -eq 17 ] # needs to be calls (15) + constructor calls (1) + metadata (1)
	then
		echo "Run was sucessful"
	else
		echo "Run had $logCount log entries; error occured"
		exit 1
	fi
}

echo
echo
echo "Initializing Project..."

./gradlew clean assemble

# We need to download spring instrument to avoid having it on the repo

if [ ! -f spring-instrument-5.3.37.jar ]
then
	wget https://repo1.maven.org/maven2/org/springframework/spring-instrument/5.3.37/spring-instrument-5.3.37.jar
fi

tryRegularRun
tryInstrumentedRun



