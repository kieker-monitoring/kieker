#!/usr/bin/env groovy

pipeline {

  agent none

  environment {
    DOCKER_ARGS = ''
  }

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

        stage('Compile') {
          steps {
            sh './gradlew compileJava'
            sh './gradlew compileTestJava'
            sh 'df'
          }
        }

        stage('Unit Test') {
          steps {
            sh './gradlew --parallel test jacocoTestReport'
            jacoco(
               sourcePattern: '**/src/**',
               exclusionPattern: '**/test/**'
            )
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
                  //spotBugs(
                  //  pattern: '**/build/reports/findbugs/*.xml'
                  //)
                ]
              )
            }
          }
        }
        
        stage('Distribution Build') {
          steps {
            sh './gradlew build distribute'
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

    stage('Master Specific Stages') {
      when {
        beforeAgent true
        branch 'master'
      }
      parallel {
        stage('Push to Stable') {
          agent {
             label 'build-node4'
          }
          steps {
            sshagent(credentials: ['kieker-key']) {
              sh('''
                    #!/usr/bin/env bash
                    set +x
                    export GIT_SSH_COMMAND="ssh -oStrictHostKeyChecking=no"
                    git push git@github.com:kieker-monitoring/kieker.git $(git rev-parse HEAD):stable
                 ''')
            }
          }
          post {
            cleanup {
              deleteDir()
              cleanWs()
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
              sh './gradlew publish'
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

