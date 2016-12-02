#!/usr/bin/env bash

sudo docker run -v ${SNAP_WORKING_DIR}:/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew -S test"

exit $?
