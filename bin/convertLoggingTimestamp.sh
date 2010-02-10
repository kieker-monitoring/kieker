#!/bin/bash

## Converts the tpmon logging timestamp to a human-readable
## format using the date tool

BINDIR=$(dirname $0)

JAVAARGS="-Dlog4j.configuration=${BINDIR}/log4j.properties -Xms56m -Xmx1024m"
MAINCLASSNAME=kieker.tpan.tools.LoggingTimestampConverterTool
CLASSPATH=$(ls lib/*.jar | tr "\n" ":")$(ls dist/*.jar | tr "\n" ":")

java ${JAVAARGS} -cp "${CLASSPATH}" ${MAINCLASSNAME} $*
