#!/bin/bash

# Converts the logging timestamp to a human-readable
# format using the date tool
#
# @author Andre van Hoorn

BINDIR=$(cd "$(dirname "$0")"; pwd)/

JAVAARGS="-Dkieker.common.logging.Log=JDK -Dkieker.common.logging.Log=JDK -Djava.util.logging.config.file=${BINDIR}/logging.properties -Xms56m -Xmx1024m"
MAINCLASSNAME=kieker.tools.loggingTimestampConverter.LoggingTimestampConverterTool

java ${JAVAARGS} -cp "${BINDIR}/../lib/*":"${BINDIR}/../dist/*":"${BINDIR}" ${MAINCLASSNAME} $*
