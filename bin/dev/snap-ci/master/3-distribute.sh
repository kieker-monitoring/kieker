#!/usr/bin/env sh

WORK_DIR=/tmp/kieker

sudo docker run -v ${WORK_DIR}:/opt/kieker kieker/kieker-build:openjdk6 /bin/bash -c "cd /opt/kieker; ./gradlew -S distribute -x test -x check"

STAGE_RESULT=$?

cd /

exit $STAGE_RESULT
