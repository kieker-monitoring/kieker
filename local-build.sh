#!/bin/bash

./gradlew -s clean

rm -rf kieker-analysis/bin/*
rm -rf kieker-checkstyle-extension/bin/*
rm -rf kieker-common/bin/*
rm -rf kieker-extension-kafka/bin/*
rm -rf kieker-monitoring/bin/*
rm -rf kieker-tools/bin/*

./gradlew -S compileJava compileTestJava -x apidoc
./gradlew -S test cloverAggregateReports cloverGenerateReport -x apidoc
./gradlew -S check -x apidoc
./gradlew checkReleaseArchivesShort -x apidoc

#archiveArtifacts artifacts: 'build/distributions/*,kieker-documentation/userguide/kieker-userguide.pdf,build/libs/*.jar', fingerprint: true

./gradlew checkReleaseArchives -x apidoc

# end
