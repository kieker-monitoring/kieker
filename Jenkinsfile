#!/usr/bin/env groovy

pipeline {

  environment {
    DOCKER_ARGS = '--rm -u `id -u`'
  }

  agent none

  options {
    buildDiscarder logRotator(artifactNumToKeepStr: '10')
    timeout(time: 1, unit: 'HOURS')
    retry(1)
    parallelsAlwaysFailFast()
  }

  triggers {
    cron(env.BRANCH_NAME == 'master' ? '@daily' : '')
  }

  stages {
    stage('Default Docker Stages') {
      agent {
        docker {
          image 'kieker/kieker-build:openjdk8'
          args env.DOCKER_ARGS
          label 'kieker-slave-docker'
        }
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

        stage('Compile') {
          steps {
            sh './gradlew compileJava'
            sh './gradlew compileTestJava'
          }
        }

        stage('Static Analysis') {
          steps {
            sh './gradlew check'

            // Report results of static analysis tools
            checkstyle canComputeNew: false,
                defaultEncoding: '',
                healthy: '',
                pattern: 'kieker-analysis\\build\\reports\\checkstyle\\*.xml,kieker-tools\\build\\reports\\checkstyle\\*.xml,kieker-monitoring\\build\\reports\\checkstyle\\*.xml,kieker-common\\build\\reports\\checkstyle\\*.xml',
                unHealthy: ''

            findbugs canComputeNew: false,
                defaultEncoding: '',
                excludePattern: '',
                healthy: '',
                includePattern: '',
                pattern: 'kieker-analysis\\build\\reports\\findbugs\\*.xml,kieker-tools\\build\\reports\\findbugs\\*.xml,kieker-monitoring\\build\\reports\\findbugs\\*.xml,kieker-common\\build\\reports\\findbugs\\*.xml',
                unHealthy: ''

            pmd canComputeNew: false,
                defaultEncoding: '',
                healthy: '',
                pattern: 'kieker-analysis\\build\\reports\\pmd\\*.xml,kieker-tools\\build\\reports\\pmd\\*.xml,kieker-monitoring\\build\\reports\\pmd\\*.xml,kieker-common\\build\\reports\\pmd\\*.xml',
                unHealthy: ''
          }
        }
        
        stage('Unit Test') {
          steps {
            sh './gradlew test'
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


        stage('Distribution Build') {
          steps {
            sh './gradlew distribute'
            stash includes: 'build/libs/*.jar', name: 'upload'
          }
        }

        stage('Release Checks') {
          parallel {
            stage('Release Check Short') {
              steps {
                sh './gradlew checkReleaseArchivesShort'
              }
            }

            stage('Release Check Extended') {
              when {
                beforeAgent true
                anyOf {
                  branch 'master';
                  changeRequest target: 'master'
                }
              }
              steps {
                sh './gradlew checkReleaseArchives'
              }
            }
          }
        }

        stage('Archive Artifacts') {
          steps {
            archiveArtifacts artifacts: 'build/distributions/*,kieker-documentation/userguide/kieker-userguide.pdf,build/libs/*.jar',
              fingerprint: true,
              onlyIfSuccessful: true
          }
        }
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
            label 'kieker-slave-docker'
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
              label 'kieker-slave-docker'
            }
          }
          steps {
            unstash 'upload'
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
