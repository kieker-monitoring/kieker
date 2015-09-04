#!/bin/bash

BINDIR=$(cd "$(dirname "$0")"; pwd)

<<<<<<< HEAD
CLASSPATH=$CLASSPATH:"${BINDIR}/target/de.cau.cs.se.instrumentation.rl.cli-1.1.0-SNAPSHOT.jar"
=======
CLASSPATH="$CLASSPATH:${BINDIR}/target/de.cau.cs.se.instrumentation.rl.cli-1.1.jar"
>>>>>>> updated irl compiler

java -cp ${CLASSPATH} de.cau.cs.se.instrumentation.rl.cli.CLICompilerMain "$@"

# end
