#!/bin/bash

BINDIR=$(cd "$(dirname "$0")"; pwd)

CLASSPATH="${BINDIR}/target/de.cau.cs.se.instrumentation.rl.cli-1.2.0-SNAPSHOT.jar"

java -cp ${CLASSPATH} de.cau.cs.se.instrumentation.rl.cli.CLICompilerMain "$@"

# end
