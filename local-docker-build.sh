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
  docker run -u `id -u` --rm -v `pwd`:/opt/kieker kieker/kieker-build:openjdk8 /bin/bash -c "\
    cd /opt/kieker;\
    ./gradlew clean && \
    ./gradlew compileJava compileTestJava build distribute &&\
    ./gradlew test &&\
    ./gradlew check &&\
    ./gradlew checkReleaseArchivesShort &&\
    ./gradlew checkReleaseArchives"
else
  echo "You are not in the docker group."
fi

# end
