#!/usr/bin/env bash

ARTIFACT_DIR="${SNAP_WORKING_DIR}/snap-artifacts"
KIEKER_DIR="${ARTIFACT_DIR}/kieker"

if [ "${SNAP_BRANCH}" == "master" ]; then
  echo "We are in master - pushing to stable branch."
  cd ${KIEKER_DIR}

  # Push content of master branch to stable branch
  git push git@github.com:kieker-monitoring/kieker.git master:stable

  STAGE_RESULT=$?
else
  echo "We are not in master - skipping."
  STAGE_RESULT=0
fi

exit $STAGE_RESULT
