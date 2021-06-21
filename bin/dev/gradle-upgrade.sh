#!/usr/bin/env bash
# Script that searches for gradlew executables in the directory structure and updates the wrappers to the designated version.

# Usage:
# gradle-upgrade.sh <Kieker-Root-Dir> <Gradle-Target-Version>

# Kieker base directory
KIEKER_ROOT="$1"

# Gradle target version (passed via args)
GTV=$2

cd $KIEKER_ROOT
KIEKER_ROOT_FULL=`pwd`

cd $KIEKER_ROOT_FULL
for gw in `find $KIEKER_ROOT_FULL -name "gradlew" -type f`; do
	cd $(dirname $gw)
	gradle wrapper --gradle-version $GTV --distribution-type=bin
	cd $KIEKER_ROOT_FULL
done

