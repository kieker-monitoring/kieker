#!/usr/bin/env bash

if [ "${SNAP_BRANCH}" == "master" ]; then
  echo "We are in master - pushing to stable branch."

  # Push content of master branch to stable branch
  git push git@github.com:kieker-monitoring/kieker.git master:stable

  STAGE_RESULT=$?
else
  echo "We are not in master - skipping."
  STAGE_RESULT=0
fi

exit $STAGE_RESULT
