#!/usr/bin/env groovy

node('kieker-slave-docker') {
    
    stage('Clean workspace'){
	deleteDir()
    }  

    stage ('Checkout') {
        checkout scm
    }

    stage ('compile') {
        sh 'docker run --rm -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew -S compileJava compileTestJava"'
    }

    stage ('unit-test') {
        sh 'docker run --rm -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew -S test"'
    }

    stage ('static-analysis') {
        sh 'docker run --rm -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew -S check"'    
    }

    stage ('release-check-short') {
        sh 'docker run --rm -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew checkReleaseArchivesShort"'

        archiveArtifacts artifacts: 'build/distributions/*,kieker-documentation/userguide/kieker-userguide.pdf,build/libs/*.jar', fingerprint: true
    }

    stage ('release-check-extended') {
        if (env.BRANCH_NAME == "master") {
            sh 'echo "We are in master - executing the extended release archive check."'
    
            sh 'docker run --rm -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew checkReleaseArchives"'
        } else {
            sh 'echo "We are not in master - skipping the extended release archive check."'
        }
    }

    stage ('push-to-stable') {
        if (env.BRANCH_NAME == "master") {
            sh 'echo "We are in master - pushing to stable branch."'

            sh 'git push git@github.com:kieker-monitoring/kieker.git $(git rev-parse HEAD):stable'
        } else {
            sh 'echo "We are not in  master - skipping."'
        }
    }

    stage ('Permission fixing') {
	sh 'docker run --rm -v ' + env.WORKSPACE + ':/opt/kieker alpine /bin/sh -c "chmod -R ugo+rwx /opt/kieker"'
    }

    // Clean workspace
    step([$class: 'WsCleanup'])
}
