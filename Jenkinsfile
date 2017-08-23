#!groovy

node('kieker-slave-docker') {
    
    stage ('Checkout') {
        checkout scm
    }

    stage ('Credential-Test') {
        withCredentials([usernamePassword(credentialsId: 'testcredentials', usernameVariable: 'TEST_USERNAME', passwordVariable: 'TEST_PASSWORD')]) {
	  sh "echo user_${TEST_USERNAME}_"
	  sh "echo pass_${TEST_PASSWORD}_"
	}
    } 

    stage ('1-compile logs') {
        sh 'docker run --rm -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew -S compileJava compileTestJava"'
    }

    stage ('2-unit-test logs') {
        sh 'docker run --rm -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew -S test"'
    }

    stage ('3-static-analysis logs') {
        sh 'docker run --rm -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew -S check"'    
    }

    stage ('4-release-check-short logs') {
        sh 'docker run --rm -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew checkReleaseArchivesShort"'

        archiveArtifacts artifacts: 'build/distributions/*,kieker-documentation/userguide/kieker-userguide.pdf,build/libs/*.jar', fingerprint: true
    }

    stage ('5-release-check-extended logs') {
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
            sh 'docker run --rm -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew uploadArchives"'
        } else {
            sh 'echo "We are not in  master - skipping."'
        }
    }
}
