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
    cron(env.BRANCH_NAME == 'main' ? '@daily' : '')
  }

  stages {
    stage('Precheck') {
      when {
        changeRequest target: 'stable'
      }
      steps {
        error "It is not allowed to create pull requests towards the 'stable' branch. Create a new pull request towards the 'main' branch please."
      }
    }
    stage('Default Docker Stages') {
      agent {
        docker {
          image 'kieker/kieker-build:temurin17'
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

        stage('Compile') {
          steps {
            sh './gradlew compileJava'
            sh './gradlew compileTestJava'
            sh 'df'
          }
        }

        stage('Static Analysis') {
          steps {
            sh './gradlew --parallel -x test check'
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
        
        stage('Unit Test') {
          steps {
            sh 'export JAVA_OPTS="-Djava.util.prefs.systemRoot=$WORKSPACE/.java -Djava.util.prefs.userRoot=$WORKSPACE/.java/.userPrefs"; ./gradlew --info --parallel test jacocoTestReport'
            recordCoverage(tools: [[parser: 'JACOCO', pattern: '**/jacocoTestReport.xml']],
              id: 'jacoco', name: 'JaCoCo Coverage',
              sourceCodeRetention: 'EVERY_BUILD',
              sourceDirectories: [[path: './tools/sar/src'],
                [path: './tools/fxca/src'],
                [path: './tools/maa/src'],
                [path: './tools/behavior-analysis/src'],
                [path: './tools/cmi/src'],
                [path: './tools/restructuring/src'],
                [path: './tools/mvis/src'],
                [path: './tools/mktable/src'],
                [path: './tools/convert-logging-timestamp/src'],
                [path: './tools/rewrite-log-entries/src'],
                [path: './tools/trace-analysis/src'],
                [path: './tools/src'],
                [path: './tools/resource-monitor/src'],
                [path: './tools/otel-transformer/src'],
                [path: './tools/allen-upper-limit/src'],
                [path: './tools/mop/src'],
                [path: './tools/delta/src'],
                [path: './tools/mt/src'],
                [path: './tools/opad/src'],
                [path: './tools/trace-analysis-gui/src'],
                [path: './tools/relabel/src'],
                [path: './tools/collector/src'],
                [path: './tools/runtime-analysis/src'],
                [path: './tools/log-replayer/src'],
                [path: './tools/dar/src'],
                [path: './extension-kafka/src'],
                [path: './analysis/src'],
                [path: './model/src'],
                [path: './common/src'],
                [path: './monitoring/disl/src'],
                [path: './monitoring/aspectj/src'],
                [path: './monitoring/aspectj/integrationTest-resources/example-projects-aspectj/example-beforeafteroperationevent/src'],
                [path: './monitoring/aspectj/integrationTest-resources/example-projects-aspectj/example-beforeafterconstructorevent/src'],
                [path: './monitoring/aspectj/integrationTest-resources/example-projects-aspectj/example-operationexecutionrecord/src'],
                [path: './monitoring/aspectj/integrationTest-resources/example-projects-aspectj/example-pure/src'],
                [path: './monitoring/javassist/src'],
                [path: './monitoring/spring/src'],
                [path: './monitoring/core/src'],
                [path: './monitoring/bytebuddy/src'],
                [path: './extension-cassandra/src'],
                [path: './tools/restructuring/src-gen'],
                [path: './tools/opad/src-gen'],
                [path: './analysis/src-gen'],
                [path: './model/src-gen'],
                [path: './common/src-gen']])
            // Generated by:
            // for folder in $(find . -name "src" | grep -v "examples"); do echo "                [path: '"$folder"'],"; done
            // for folder in $(find . -name "src-gen" | grep -v "examples"); do echo "                [path: '"$folder"'],"; done
          }
          post {
            always {
              junit '**/build/test-results/test/*.xml'
            }
          }
        }

        stage('Distribution Build') {
          steps {
            sh './gradlew -x test -x signMavenJavaPublication -x signArchives build publishToMavenLocal distribute'
            sh 'bin/dev/assemble-tools.sh'
            stash includes: 'build/libs/*.jar', name: 'jarArtifacts'
            stash includes: 'build/distributions/*', name: 'distributions'
            stash includes: 'build/architecture-recovery*.*', name: 'architecture-recovery'
            stash includes: 'build/tools/*', name: 'tools'
            stash includes: 'build/trace-analysis*.*', name: 'trace-analysis'
          }
        }
      }
    }

    stage('Archive Artifacts') {
      agent {
        docker {
          image 'kieker/kieker-build:openjdk11'
          args env.DOCKER_ARGS
        }
      }
      steps {
        unstash 'jarArtifacts'
        unstash 'distributions'
        unstash 'architecture-recovery'
        unstash 'tools'
        unstash 'trace-analysis'
        archiveArtifacts artifacts: 'build/distributions/*,build/libs/*.jar,build/architecture-recovery*.*,build/tools/*,build/trace-analysis*.*',
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

    stage('Main Specific Stages') {
      when {
        beforeAgent true
        branch 'main'
      }
      parallel {
        stage('Push to Stable') {
          agent any
          steps {
            sshagent(credentials: ['kieker-key']) {
              sh('''
                    #!/usr/bin/env bash
                    set +x
                    export GIT_SSH_COMMAND="ssh -oStrictHostKeyChecking=no"
                    git push -v git@github.com:kieker-monitoring/kieker.git $(git rev-parse HEAD):stable
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
              image 'kieker/kieker-build:openjdk11'
              args env.DOCKER_ARGS
            }
          }
          steps {
            unstash 'jarArtifacts'
            withCredentials([
              usernamePassword(
                credentialsId: 'OSSRH-Token',
                usernameVariable: 'kiekerMavenUser', 
                passwordVariable: 'kiekerMavenPassword'
              )
            ]) {
              sh './gradlew -x signMavenJavaPublication -x signArchives publish'
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
    
    stage('RC Specific Stage') {
      when {
        beforeAgent true
        branch '*-RC'
      }
      
      agent {
        docker {
          image 'kieker/kieker-build:openjdk11'
          args env.DOCKER_ARGS
        }
      }

      steps {
            unstash 'jarArtifacts'
            withCredentials([
              usernamePassword(
                credentialsId: 'OSSRH-Token', 
                usernameVariable: 'kiekerMavenUser', 
                passwordVariable: 'kiekerMavenPassword'
              ),
              string(
                credentialsId: 'kieker-pgp-passphrase',
                variable: 'PASSPHRASE'
              ),
              file(
                credentialsId: 'kieker-pgp-key-2', 
                variable: 'KEY_FILE'),
              string(
                credentialsId: 'kieker-key-id',
                variable: 'KEY_ID')
              ]) {
              sh './gradlew signMavenJavaPublication publish -Psigning.secretKeyRingFile=${KEY_FILE} -Psigning.password=${PASSPHRASE} -Psigning.keyId=${KEY_ID}'
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

