#!/bin/bash

./gradlew -S clean -x apidoc

rm -rf kieker-analysis/bin/*
rm -rf kieker-checkstyle-extension/bin/*
rm -rf kieker-common/bin/*
rm -rf kieker-extension-kafka/bin/*
rm -rf kieker-monitoring/bin/*
rm -rf kieker-tools/bin/*

./gradlew -S compileJava compileTestJava jar -x apidoc

echo "compile complete -> test"

./gradlew -S test cloverAggregateReports cloverGenerateReport -x apidoc

echo "test -> check"

./gradlew -S check -x apidoc

echo "check -> distribute"

./gradlew -S distribute

echo "distribute -> release short"

./gradlew checkReleaseArchivesShort -x apidoc

#archiveArtifacts artifacts: 'build/distributions/*,kieker-documentation/userguide/kieker-userguide.pdf,build/libs/*.jar', fingerprint: true

echo "check -> release archives"

./gradlew checkReleaseArchives -x apidoc

# end
