#!/usr/bin/env groovy

def dockerBase = 'docker run --rm -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c '

pipeline {
  agent {
    node {
      label 'kieker-slave-docker'
    }
  }
  
  triggers {
    cron{}
  }

  stages {
    stage('Precheck') {
      steps {
        echo "BRANCH_NAME: " + env.BRANCH_NAME
        echo "CHANGE_TARGET: " + env.CHANGE_TARGET
        echo "NODE_NAME: " + env.NODE_NAME
        echo "NODE_LABELS: " + env.NODE_LABELS
        script {
          if((env.CHANGE_TARGET != null) && env.CHANGE_TARGET == 'stable') {
            echo "It is not allowed to create pull requests towards the 'stable' branch. Create a new pull request towards the 'master' branch please."
            currentBuild.result = "FAILURE"
          }
        }
      }
    }

    stage('Clean workspace') {
      steps {
        deleteDir()
      }
    }
    
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Compile') {
      steps {
        sh dockerBase + '"cd /opt/kieker; ./gradlew -S compileJava compileTestJava"'
      }
    }
  
    stage('Unit Test') {
      steps {
        sh dockerBase + '"cd /opt/kieker; ./gradlew -S test"'
      }
    }

    stage('Static Analysis') {
      steps {
        sh dockerBase + '"cd /opt/kieker; ./gradlew -S check"'
      }
    }

    stage('Release Check Short') {
      steps {
        sh dockerBase + '"cd /opt/kieker; ./gradlew checkReleaseArchivesShort"'
        archiveArtifacts artifacts: 'build/distributions/*,kieker-documentation/userguide/kieker-userguide.pdf,build/libs/*.jar', fingerecho: true
      }
    }

    stage('Release Check Extended') {
      steps {
        script {
          if (env.BRANCH_NAME == 'master') {
            echo "We are in master - executing the extended release archive check."
            sh dockerBase + '"cd /opt/kieker; ./gradlew checkReleaseArchives"'
          } else {
            echo "We are not in master - skipping the extended release archive check."
          }
        }
      }
    }

    stage('Push Stable') {
      steps {
        script {
          if (env.BRANCH_NAME == 'master') {
            echo "We are in master - pushing to stable branch."
            sh 'git push git@github.com:kieker-monitoring/kieker.git $(git rev-parse HEAD):stable'
          } else {
            echo "We are not in master - skipping."
          }
        }
      }
    }

    stage('Fix permissions') {
      steps {      
        sh 'docker run --rm -v ' + env.WORKSPACE + ':/opt/kieker alpine /bin/sh -c "chmod -R ugo+rwx /opt/kieker"'
      }
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
