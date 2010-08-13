#!/bin/bash

# Converts the logging timestamp to a human-readable
# format using the date tool
#
# @author Andre van Hoorn

BINDIR=$(cd "$(dirname "$0")"; pwd)/

JAVAARGS="-Dlog4j.configuration=./log4j.properties -Xms56m -Xmx1024m"
MAINCLASSNAME=kieker.tools.loggingTimestampConverter.LoggingTimestampConverterTool
CLASSPATH=$(ls "${BINDIR}/../lib/"*.jar | tr "\n" ":")$(ls "${BINDIR}/../dist/"*.jar | tr "\n" ":")${BINDIR}

java ${JAVAARGS} -cp "${CLASSPATH}" ${MAINCLASSNAME} $*
