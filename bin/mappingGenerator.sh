#!/bin/bash

#
# Wrapper script needs improvement!
#
# @author Andre van Hoorn

BINDIR=$(cd "$(dirname "$0")"; pwd)/

JAVAARGS=-Dlog4j.configuration=./log4j.properties
MAINCLASSNAME=kieker.tools.mappingGenerator.MethodExtractorTool
CLASSPATH=$(ls "${BINDIR}/../lib/"*.jar | tr "\n" ":")$(ls "${BINDIR}/../dist/"*.jar | tr "\n" ":")${BINDIR}

java ${JAVAARGS} -cp "${CLASSPATH}" ${MAINCLASSNAME} "$@"