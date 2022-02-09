#!/usr/bin/env groovy

pipeline {

  agent none

  environment {
    DOCKER_ARGS = ''
  }

  options {
    buildDiscarder logRotator(artifactNumToKeepStr: '3', artifactDaysToKeepStr: '5', daysToKeepStr: '4', numToKeepStr: '10')
    timeout(time: 150, unit: 'MINUTES')
    retry(1)
    parallelsAlwaysFailFast()
  }

  triggers {
    cron(env.BRANCH_NAME == 'master' ? '@daily' : '')
  }

  stages {
    stage('Precheck') {
      when {
        changeRequest target: 'stable'
      }
      steps {
        error "It is not allowed to create pull requests towards the 'stable' branch. Create a new pull request towards the 'master' branch please."
      }
    }
    stage('Default Docker Stages') {
      agent {
        docker {
          image 'kieker/kieker-build:openjdk8'
          alwaysPull false
          args env.DOCKER_ARGS
        }
      }
      stages {
        stage('Initial Cleanup') {
          steps {
            // Make sure that no remainders from previous builds interfere.
            sh 'df'
            sh './gradlew clean'
          }
        }

        stage('Change Version Number') {
          steps {
            sh 'cat gradle.properties | sed s/"kiekerVersion = .*"/"kiekerVersion = 99.9"/g > gradle.properties'
          }
        }

        stage('Compile') {
          steps {
            sh './gradlew compileJava'
            sh './gradlew compileTestJava'
            sh 'df'
          }
        }

        stage('Distribution Build') {
          steps {
            sh './gradlew -x test build publishToMavenLocal distribute'
            stash includes: 'build/libs/*.jar', name: 'jarArtifacts'
            stash includes: 'build/distributions/*', name: 'distributions'
          }
        }
      }
    }

    stage('Release Checks') {
      parallel {
        stage('Release Check Short') {
          agent {
            docker {
              image 'kieker/kieker-build:openjdk8'
              args env.DOCKER_ARGS
            }
          }
          steps {
            unstash 'distributions'
            sh 'bin/dev/release-check-short.sh'
          }
          post {
            cleanup {
              deleteDir()
            }
          }
        }

        stage('Release Check Extended') {
          agent {
            docker {
              image 'kieker/kieker-build:openjdk8'
              args env.DOCKER_ARGS
            }
          }
          when {
            beforeAgent true
            anyOf {
              branch 'master';
              branch '*-RC';
              changeRequest target: 'master'
            }
          }
          steps {
            unstash 'distributions'
            sh 'bin/dev/release-check-extended.sh'
          }
          post {
            cleanup {
              deleteDir()
            }
          }
        }
      }
    }

    stage('Archive Artifacts') {
      agent {
        docker {
          image 'kieker/kieker-build:openjdk8'
          args env.DOCKER_ARGS
        }
      }
      steps {
        unstash 'jarArtifacts'
        unstash 'distributions'
        archiveArtifacts artifacts: 'build/distributions/*,build/libs/*.jar',
            fingerprint: true,
            onlyIfSuccessful: true
      }
      post {
        cleanup {
          deleteDir()
          cleanWs()
        }
      }
    }

  }
}

