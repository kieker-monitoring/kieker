#!/usr/bin/env groovy

pipeline {

  environment {
    DOCKER_ARGS = ''
  }

  agent none

  options {
    buildDiscarder logRotator(artifactNumToKeepStr: '10')
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
          alwaysPull true
//          args env.DOCKER_ARGS
          args '-v ' + env.WORKSPACE + '/pw:/etc/passwd'
        }
      }
      stages {
        stage('Initial Cleanup') {
          steps {
            // Make sure that no remainders from previous builds interfere.
            sh 'df'
            sh 'printenv | sort'
            sh './gradlew clean'
          }
        }

        stage('V') {
          steps {
            sshagent(['kieker-key']) {
              sh 'git push git@github.com:kieker-monitoring/kieker.git $(git rev-parse HEAD):experiment'            
            }
          }
        }

        stage('Compile') {
          steps {
            sh './gradlew compileJava'
            sh './gradlew compileTestJava'
          }
        }

        stage('Unit Test') {
          steps {
            sh './gradlew --parallel test'
            sh 'df'
            step([
                $class              : 'CloverPublisher',
                cloverReportDir     : env.WORKSPACE + '/build/reports/clover',
                cloverReportFileName: 'clover.xml',
                healthyTarget       : [methodCoverage: 70, conditionalCoverage: 80, statementCoverage: 80],
                unhealthyTarget     : [methodCoverage: 50, conditionalCoverage: 50, statementCoverage: 50], // optional, default is none
            ])
          }
          post {
            always {
              junit '**/build/test-results/test/*.xml'
            }
          }
        }

        stage('Static Analysis') {
          steps {
            sh './gradlew check'
            sh 'df'
          }
          post {
            always {
              // Report results of static analysis tools
            
              recordIssues(
                enabledForFailure: true,
                tools: [
                  java(),
                  javaDoc(),
                  checkStyle(
                    pattern: '**/build/reports/checkstyle/*.xml'
                  ),
                  pmdParser(
                    pattern: '**/build/reports/pmd/*.xml'
                  ),
                  spotBugs(
                    pattern: '**/build/reports/findbugs/*.xml'
                  )
                ]
              )
            }
          }
        }
        
        stage('Distribution Build') {
          steps {
            sh './gradlew build distribute'
            sh 'df'
            stash includes: 'build/libs/*.jar', name: 'jarArtifacts'
            stash includes: 'build/distributions/*', name: 'distributions'
            stash includes: 'kieker-documentation/userguide/kieker-userguide.pdf', name: 'userguide'
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
            sh './gradlew checkReleaseArchivesShort'
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
            sh './gradlew checkReleaseArchives'
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
        unstash 'userguide'
        archiveArtifacts artifacts: 'build/distributions/*,kieker-documentation/userguide/kieker-userguide.pdf,build/libs/*.jar',
            fingerprint: true,
            onlyIfSuccessful: true
      }
      post {
        cleanup {
          deleteDir()
        }
      }
    }

    stage('Master Specific Stages') {
      when {
        beforeAgent true
        branch 'master'
      }
      parallel {
        stage('Push to Stable') {
          agent {
            docker {
              image 'kieker/kieker-build:openjdk8'
              args env.DOCKER_ARGS
            }
          }
          steps {
            sh 'git push git@github.com:kieker-monitoring/kieker.git $(git rev-parse HEAD):stable'
          }
          post {
            cleanup {
              deleteDir()
            }
          }
        }

        stage('Upload Snapshot Version') {
          agent {
            docker {
              image 'kieker/kieker-build:openjdk8'
              args env.DOCKER_ARGS
            }
          }
          steps {
            unstash 'jarArtifacts'
            withCredentials([
              usernamePassword(
                credentialsId: 'artifactupload', 
                usernameVariable: 'kiekerMavenUser', 
                passwordVariable: 'kiekerMavenPassword'
              )
            ]) {
              sh './gradlew uploadArchives'
            }
          }
          post {
            cleanup {
              deleteDir()
            }
          }
        }
      }
    }
  }
}

