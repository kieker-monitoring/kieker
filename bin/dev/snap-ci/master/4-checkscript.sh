#!/usr/bin/env sh

WORK_DIR=/tmp/kieker

sudo docker run -v ${WORK_DIR}:/opt/kieker kieker/kieker-build:openjdk6 /bin/bash -c "cd /opt/kieker; ./bin/dev/check-release-archives.sh"

STAGE_RESULT=$?

cd /

exit $STAGE_RESULT
