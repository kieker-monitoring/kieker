#!/usr/bin/env bash

WORK_DIR=/tmp/kieker

if [ "${SNAP_BRANCH}" == "master" ]; then
  echo "We are in master - pushing to stable branch."
  cd ${WORK_DIR}

  # Push content of master branch to stable branch
  git push git@github.com:kieker-monitoring/kieker.git master:stable

  STAGE_RESULT=$?
else
  echo "We are not in master - skipping."
  STAGE_RESULT=0
fi

cd /

exit $STAGE_RESULT
