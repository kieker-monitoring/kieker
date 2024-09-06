#!/usr/bin/env sh

BIN_DIR=$(cd "$(dirname "$0")"; pwd)

BASE_DIR="${BIN_DIR}/../.."
KIEKER_VERSION="2.0.0"

"${BIN_DIR}/assemble-tools-architecture-recovery.sh"

mkdir "${BASE_DIR}/build/tools"
for I in behavior-analysis collector convert-logging-timestamp log-replayer rewrite-log-entries ; do
	cp "${BASE_DIR}/tools/$I/build/distributions/$I-${KIEKER_VERSION}.zip" "${BASE_DIR}/build/tools"
done

"${BIN_DIR}/assemble-tools-trace-analysis.sh"

# end

