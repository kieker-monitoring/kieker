#!/usr/bin/env bash

./x-variables.sh

TMP_DIR=`mktemp -d`

cp -r ${SNAP_WORKING_DIR}/. ${TMP_DIR}

mkdir -p ${ARTIFACT_DIR}/

mv ${TMP_DIR} ${ARTIFACT_DIR}

exit $?
