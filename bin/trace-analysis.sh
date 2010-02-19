#!/bin/bash

#
# Wrapper script needs improvement!
#
# @author Andre van Hoorn

BINDIR=$(dirname $0)

JAVAARGS="-Dlog4j.configuration=${BINDIR}/log4j.properties -Xms56m -Xmx1024m"
MAINCLASSNAME=kieker.tpan.tools.TraceAnalysisTool
CLASSPATH=$(ls lib/*.jar | tr "\n" ":")$(ls dist/*.jar | tr "\n" ":")

time java ${JAVAARGS} -cp "${CLASSPATH}" ${MAINCLASSNAME} $*