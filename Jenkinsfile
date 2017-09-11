#!/usr/bin/env groovy

pipeline {
  agent {
    //node {
      label 'kieker-slave-docker'
    //}
  }
 
  environment {
    DOCKER_BASE = "docker run --rm -v ${env.WORKSPACE}:/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "
  }
  
  //triggers {
  //  cron{}
  //}

  stages {
    stage('Precheck') {
      when {
        expression {
          (env.CHANGE_TARGET != null) && (env.CHANGE_TARGET == 'stable')
        }
      }
      steps {
        echo "BRANCH_NAME: " + env.BRANCH_NAME
        echo "CHANGE_TARGET: " + env.CHANGE_TARGET
        echo "NODE_NAME: " + env.NODE_NAME
        echo "NODE_LABELS: " + env.NODE_LABELS
        error "It is not allowed to create pull requests towards the 'stable' branch. Create a new pull request towards the 'master' branch please."
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
    //   agent {
    //     docker {
    //       reuseNode true 
    //       image 'kieker/kieker-build:openjdk7'
    //       args ' --rm -v ${env.WORKSPACE}:/opt/kieker'
    //       label 'kieker-slave-docker'
    //     }
    //   }
      steps {
        // sh './kieker/gradlew -S -p kieker compileJava compileTestJava'
        //stash 'everything'
        sh DOCKER_BASE + '"cd /opt/kieker; ./gradlew -S compileJava compileTestJava"'
      }
    }
  
/**
    stage('Parallel') {
      steps {
        parallel (
          'Unit Test' : {
            unstash 'everything'
            sh DOCKER_BASE + '"cd /opt/kieker; ./gradlew -S test"'
          },
          'Static Analysis' : {
            unstash 'everything'
            sh DOCKER_BASE + '"cd /opt/kieker; ./gradlew -S check"'
            stash 'everything'
          }
        )
      }
    }
**/
    stage('Unit Test') {
      steps {
        sh DOCKER_BASE + '"cd /opt/kieker; ./gradlew -S test"'
      }
    }

    stage('Static Analysis') {
      steps {
        sh DOCKER_BASE + '"cd /opt/kieker; ./gradlew -S check"'
      }
    }
    
    stage('Release Check Short') {
      steps {
        sh DOCKER_BASE + '"cd /opt/kieker; ./gradlew checkReleaseArchivesShort"'
        
        archiveArtifacts artifacts: 'build/distributions/*,kieker-documentation/userguide/kieker-userguide.pdf,build/libs/*.jar', fingerprint: true

        checkstyle canComputeNew: false, defaultEncoding: '', healthy: '', pattern: 'kieker-analysis\\build\\reports\\checkstyle\\*.xml,kieker-tools\\build\\reports\\checkstyle\\*.xml,kieker-monitoring\\build\\reports\\checkstyle\\*.xml,kieker-common\\build\\reports\\checkstyle\\*.xml', unHealthy: ''

        findbugs canComputeNew: false, defaultEncoding: '', excludePattern: '', healthy: '', includePattern: '', pattern: 'kieker-analysis\\build\\reports\\findbugs\\*.xml,kieker-tools\\build\\reports\\findbugs\\*.xml,kieker-monitoring\\build\\reports\\findbugs\\*.xml,kieker-common\\build\\reports\\findbugs\\*.xml', unHealthy: ''

        pmd canComputeNew: false, defaultEncoding: '', healthy: '', pattern: 'kieker-analysis\\build\\reports\\pmd\\*.xml,kieker-tools\\build\\reports\\pmd\\*.xml,kieker-monitoring\\build\\reports\\pmd\\*.xml,kieker-common\\build\\reports\\pmd\\*.xml', unHealthy: ''
      }
    }

    stage('Release Check Extended') {
      when {
        branch 'master'
      }
      steps {
        echo "We are in master - executing the extended release archive check."
        sh DOCKER_BASE + '"cd /opt/kieker; ./gradlew checkReleaseArchives -x test -x check "'
      }
    }

    stage('Push Stable') {
      when {
        branch 'master'
      }
      steps {
        echo "We are in master - pushing to stable branch."
        sh 'git push git@github.com:kieker-monitoring/kieker.git $(git rev-parse HEAD):stable'
      }
    }

    //stage('Fix permissions') {
    //  steps {         
    //  }
    //}
  }

  post {
    always {
      sh 'docker run --rm -v ' + env.WORKSPACE + ':/opt/kieker alpine /bin/sh -c "chmod -R ugo+rwx /opt/kieker"'
      deleteDir()
    }
   
    changed {
      mail to: 'ci@kieker-monitoring.net', subject: 'Pipeline outcome has changed.', body: 'no text'
    }


    failure {
      mail to: 'ci@kieker-monitoring.net', subject: 'Pipeline build failed.', body: 'no text'
    }
  
    //success {
    //}

    //unstable {
    //}
  }
}