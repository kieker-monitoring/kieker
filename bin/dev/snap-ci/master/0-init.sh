#!/usr/bin/env sh

WORK_DIR=/tmp/kieker

sudo cp -r ${SNAP_WORKING_DIR}/ ${WORK_DIR}/

STAGE_RESULT=$?

cd /

exit $STAGE_RESULT
