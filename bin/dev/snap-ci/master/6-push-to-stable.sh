#!/usr/bin/env bash

WORK_DIR=/tmp/kieker

if [ "${SNAP_BRANCH}" == "master" ]; then
  echo "We are in master - pushing to stable branch."
  snap-shell
  cd ${WORK_DIR}
#  git remote add github https://${KIEKER_USER}:${KIEKER_PASS}@github.com/kieker-monitoring/kieker.git
#  git checkout stable
#  git merge master
#  git push github master:stable

  STAGE_RESULT=$?
else
  echo "We are not in master - skipping."
  STAGE_RESULT=0
fi

cd /

exit $STAGE_RESULT
