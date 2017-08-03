#!/usr/bin/env groovy

pipeline {
  agent {
    node {
      label 'kieker-slave-docker'
    }
  }

  stages {
    def dockerBase = 'docker run --rm -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c '
    

    stage('Precheck') {
      print "BRANCH_NAME: " + env.BRANCH_NAME
      print "CHANGE_TARGET: " + env.CHANGE_TARGET
      print "NODE_NAME: " + env.NODE_NAME
      print "NODE_LABELS: " + env.NODE_LABELS
      if((env.CHANGE_TARGET != null) && env.CHANGE_TARGET == 'stable') {
        print "It is not allowed to create pull requests towards the 'stable' branch. Create a new pull request towards the 'master' branch please."
        currentBuild.result = "FAILURE"
      }
    }

    stage('Clean workspace') {
      deleteDir()
    }
    
    stage('Checkout') {
      checkout scm
    }

    stage('Compile') {
      sh dockerBase + '"cd /opt/kieker; ./gradlew -S compileJava compileTestJava"'
    }
  
    stage('Unit Test') {
      sh dockerBase + '"cd /opt/kieker; ./gradlew -S test"'
    }

    stage('Static Analysis') {
      sh dockerBase + '"cd /opt/kieker; ./gradlew -S check"'
    }

    stage('Release Check Short') {
      sh dockerBase + '"cd /opt/kieker; ./gradlew checkReleaseArchivesShort"'
      archiveArtifacts artifacts: 'build/distributions/*,kieker-documentation/userguide/kieker-userguide.pdf,build/libs/*.jar', fingerprint: true
    }

    stage('Release Check Extended') {
      if (env.BRANCH_NAME == 'master') {
        print "We are in master - executing the extended release archive check."
	sh dockerBase + '"cd /opt/kieker; ./gradlew checkReleaseArchives"'
      } else {
        print "We are not in master - skipping the extended release archive check."
      }
    }

    stage('Push Stable') {
      if (env.BRANCH_NAME == 'master') {
        print "We are in master - pushing to stable branch."
        sh 'git push git@github.com:kieker-monitoring/kieker.git $(git rev-parse HEAD):stable'
      } else {
        print "We are not in master - skipping."
      }
    }

    stage('Fix permissions') {
      sh 'docker run --rm -v ' + env.WORKSPACE + ':/opt/kieker alpine /bin/sh -c "chmod -R ugo+rwx /opt/kieker"'
    }
  }

  post {
    always {
    }
   
    changed {
      mail to: 'ci@kieker-monitoring.net', subject: 'Pipeline outcome has changed.'
    }


    failure {
      mail to: 'ci@kieker-monitoring.net', subject: 'Pipeline build failed.'
    }
  
    success {
    }

    unstable {
    }
  }
}
