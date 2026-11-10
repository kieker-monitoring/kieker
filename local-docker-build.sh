#!/usr/bin/env bash

####
# This script allows to run the all tasks in the same environment as it is
# executed on the Kieker build server.
#
# Example:
# ./runLocalPipeline.sh
# This would execute all tasks that are also executed in the Jenkins pipeline.
#
#
# Requirements:
# - Docker
###

# Check if user is in docker group
groups | grep docker >/dev/null

if [ $? -eq 0 ]; then
  docker run -u `id -u` --rm -v `pwd`:/opt/kieker kieker/kieker-build:temurin17 /bin/sh -c "\
    cd /opt/kieker;\
    export GRADLE_USER_HOME=/opt/kieker;\
    ./gradlew clean &&\
    ./gradlew compileJava compileTestJava &&\
    ./gradlew check &&\
    ./gradlew build distribute &&\
    bin/dev/release-check-extended.sh
"
else
  echo "You are not in the docker group."
fi

# end
