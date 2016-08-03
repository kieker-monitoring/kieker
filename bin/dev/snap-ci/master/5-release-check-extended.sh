#!/usr/bin/env bash

if [ "${SNAP_BRANCH}" == "master" ]; then
  echo "We are in master - executing the extended release archive check."

  sudo docker run -v ${SNAP_WORKING_DIR}:/opt/kieker kieker/kieker-build:openjdk6 /bin/bash -c "cd /opt/kieker; ./gradlew checkReleaseArchives"

  STAGE_RESULT=$?
else
  echo "We are not in master - skipping the extended release archive check."
  STAGE_RESULT=0
fi

exit $STAGE_RESULT
