#!/bin/bash

#
# Wrapper script needs improvement!
#
# @author Andre van Hoorn

BINDIR=$(cd "$(dirname "$0")"; pwd)

JAVAARGS="-Dlog4j.configuration=./log4j.properties -Xms56m -Xmx1024m"
MAINCLASSNAME=kieker.tools.bridge.cli.CLIServerMain

echo java ${JAVAARGS} -cp "${BINDIR}/../lib/*":"${BINDIR}/../build/libs/*":"${BINDIR}" ${MAINCLASSNAME} "$@"

time java ${JAVAARGS} -cp "${BINDIR}/../lib/*":"${BINDIR}/../build/libs/*":"${BINDIR}" ${MAINCLASSNAME} "$@"
