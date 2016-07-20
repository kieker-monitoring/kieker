#!/usr/bin/env bash

ARTIFACT_DIR="${SNAP_WORKING_DIR}/snap-artifacts"
KIEKER_DIR="${ARTIFACT_DIR}/kieker"
TMP_DIR=/tmp/kieker

cp -r ${SNAP_WORKING_DIR}/. ${TMP_DIR}
mkdir -p ${ARTIFACT_DIR}/
mv ${TMP_DIR}/ ${ARTIFACT_DIR}
