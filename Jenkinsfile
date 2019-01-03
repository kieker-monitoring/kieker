#!/usr/bin/env groovy

pipeline {

  environment {
    DOCKER_ARGS = '--rm -u `id -u`'
  }

  agent {
    docker {
      image 'kieker/kieker-build:openjdk8'
      args env.DOCKER_ARGS
      label 'kieker-slave-docker'
    }
  }

  options {
    buildDiscarder logRotator(artifactNumToKeepStr: '10')
    timeout(time: 1, unit: 'HOURS')
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
        echo "BRANCH_NAME: ${BRANCH_NAME}"
        echo "CHANGE_TARGET: ${CHANGE_TARGET}"
        echo "NODE_NAME: ${NODE_NAME}"
        echo "NODE_LABELS: ${NODE_LABELS}"
        error "It is not allowed to create pull requests towards the 'stable' branch. Create a new pull request towards the 'master' branch please."
      }
    }

    stage('Compile') {
      steps {
        dir(env.WORKSPACE) {
          sh './gradlew compileJava'
          sh './gradlew compileTestJava'
        }
      }
    }


    stage('Unit Test') {
      steps {
        dir(env.WORKSPACE) {
          sh './gradlew test'
          junit '**/build/test-results/test/*.xml'

          step([
		        $class: 'CloverPublisher',
		        cloverReportDir: env.WORKSPACE + '/build/reports/clover',
		        cloverReportFileName: 'clover.xml',
		        healthyTarget: [methodCoverage: 70, conditionalCoverage: 80, statementCoverage: 80],   // optional, default is: method=70, conditional=80, statement=80
		        unhealthyTarget: [methodCoverage: 50, conditionalCoverage: 50, statementCoverage: 50], // optional, default is none
		        //failingTarget: [methodCoverage: 0, conditionalCoverage: 0, statementCoverage: 0]     // optional, default is none
          ])
        }
      }
    }

    stage('Static Analysis') {
      steps {
        dir(env.WORKSPACE) {
          sh './gradlew check'

          // Report results of static analysis tools
          checkstyle 
            canComputeNew: false, 
            defaultEncoding: '', 
            healthy: '', 
            pattern: 'kieker-analysis\\build\\reports\\checkstyle\\*.xml,kieker-tools\\build\\reports\\checkstyle\\*.xml,kieker-monitoring\\build\\reports\\checkstyle\\*.xml,kieker-common\\build\\reports\\checkstyle\\*.xml', 
            unHealthy: ''

          findbugs 
            canComputeNew: false, 
            defaultEncoding: '', 
            excludePattern: '', 
            healthy: '', 
            includePattern: '', 
            pattern: 'kieker-analysis\\build\\reports\\findbugs\\*.xml,kieker-tools\\build\\reports\\findbugs\\*.xml,kieker-monitoring\\build\\reports\\findbugs\\*.xml,kieker-common\\build\\reports\\findbugs\\*.xml', 
            unHealthy: ''

          pmd 
            canComputeNew: false, 
            defaultEncoding: '', 
            healthy: '', 
            pattern: 'kieker-analysis\\build\\reports\\pmd\\*.xml,kieker-tools\\build\\reports\\pmd\\*.xml,kieker-monitoring\\build\\reports\\pmd\\*.xml,kieker-common\\build\\reports\\pmd\\*.xml', 
            unHealthy: ''
        }
      }
    }

    stage('Distribution Build') {
      steps {
        dir(env.WORKSPACE) {
          sh './gradlew distribute'
        }
      }
    }

    stage('Release Check Short') {
      steps {
        dir(env.WORKSPACE) {
          sh './gradlew checkReleaseArchivesShort'
        }
      }
    }

    stage('Release Check Extended') {
      when {
        beforeAgent true
        branch 'master'
      }
      steps {
        dir(env.WORKSPACE) {
          echo "We are in master - executing the extended release archive check."
          sh './gradlew checkReleaseArchives'
        }
      }
    }

    stage('Archive Artifacts') {
      steps {
        dir(env.WORKSPACE) {
          archiveArtifacts 
            artifacts: 'build/distributions/*,kieker-documentation/userguide/kieker-userguide.pdf,build/libs/*.jar', 
            fingerprint: true, 
            onlyIfSuccessful: true
        }
      }
    }

    stage('Push to Stable') {
      agent {
        label 'kieker-slave-docker'
      }
      when {
        beforeAgent true
        branch 'master'
      }
      steps {
        echo "We are in master - pushing to stable branch."
        sh 'git push git@github.com:kieker-monitoring/kieker.git $(git rev-parse HEAD):stable'
      }
    }

    stage('Upload Snapshot Version') {
      when { 
        beforeAgent true
        branch 'master'
      }
      steps {
        dir(env.WORKSPACE) {
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
      }
    }
  }

  post {
    cleanup {
      deleteDir()
    }
  }
}
