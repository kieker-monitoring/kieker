#!/bin/bash

#
# Wrapper script needs improvement!
#
# @author Andre van Hoorn

BINDIR=$(cd "$(dirname "$0")"; pwd)/

JAVAARGS="-Dkieker.common.logging.Log=JDK -Djava.util.logging.config.file=${BINDIR}/logging.properties -Xms56m -Xmx1024m"
MAINCLASSNAME=kieker.tools.KaxRun
CLASSPATH=$(ls "${BINDIR}/../lib/"*.jar | tr "\n" ":")$(ls "${BINDIR}/../dist/"*.jar | tr "\n" ":")${BINDIR}

#echo java ${JAVAARGS} -cp "${CLASSPATH}" ${MAINCLASSNAME} "$@"

time java ${JAVAARGS} -cp "${CLASSPATH}" ${MAINCLASSNAME} "$@"