#!/bin/bash

./gradlew -S clean -x apidoc

rm -rf kieker-analysis/bin/*
rm -rf kieker-checkstyle-extension/bin/*
rm -rf kieker-common/bin/*
rm -rf kieker-extension-kafka/bin/*
rm -rf kieker-monitoring/bin/*
rm -rf kieker-tools/bin/*

./gradlew -S compileJava compileTestJava -x apidoc

echo "compile complete -> test"
read A

./gradlew -S test cloverAggregateReports cloverGenerateReport -x apidoc

echo "test -> check"
read A

./gradlew -S check -x apidoc

echo "check -> release short"
read A

./gradlew checkReleaseArchivesShort -x apidoc

#archiveArtifacts artifacts: 'build/distributions/*,kieker-documentation/userguide/kieker-userguide.pdf,build/libs/*.jar', fingerprint: true

echo "check -> release archives"
read A

./gradlew checkReleaseArchives -x apidoc

# end
