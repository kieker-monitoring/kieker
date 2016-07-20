#!/usr/bin/env bash

ARTIFACT_DIR="${SNAP_WORKING_DIR}/snap-artifacts"
KIEKER_DIR="${ARTIFACT_DIR}/kieker"

sudo docker run -v ${KIEKER_DIR}:/opt/kieker kieker/kieker-build:openjdk6 /bin/bash -c "cd /opt/kieker; ./gradlew -S test"

exit $?
