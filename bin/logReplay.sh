#!/bin/bash

#
# Wrapper script needs improvement!
#
# @author Andre van Hoorn

BINDIR=$(cd "$(dirname "$0")"; pwd)/

JAVAARGS="-Dkicker.common.logging.Log=JDK -Djava.util.logging.config.file=${BINDIR}/logging.properties"
MAINCLASSNAME=kicker.tools.logReplayer.FilesystemLogReplayerStarter
CLASSPATH=$(ls "${BINDIR}/../lib/"*.jar | tr "\n" ":")$(ls "${BINDIR}/../dist/"*.jar | tr "\n" ":")${BINDIR}

java ${JAVAARGS} -cp "${CLASSPATH}" ${MAINCLASSNAME} "$@"