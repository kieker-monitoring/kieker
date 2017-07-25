#!groovy

node('kieker-slave-docker') {
    
    stage ('Checkout') {
        checkout scm

        sh 'printenv'
    }

    stage ('1-compile logs') {
        sh 'docker run -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew -S compileJava compileTestJava" --rm'
    }

    stage ('2-unit-test logs') {
        sh 'docker run -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew -S test" --rm'
    }

    stage ('3-static-analysis logs') {
        sh 'docker run -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew -S check" --rm'    
    }

    stage ('4-release-check-short logs') {
        sh 'docker run -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew checkReleaseArchivesShort" --rm'

        archiveArtifacts artifacts: 'build/distributions/*,kieker-documentation/userguide/kieker-userguide.pdf,build/libs/*.jar', fingerprint: true
    }

    stage ('5-release-check-extended logs') {
        if (env.BRANCH_NAME == "master") {
            sh 'echo "We are in master - executing the extended release archive check."'
    
            sh 'docker run -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew checkReleaseArchives" --rm'
        } else {
            sh 'echo "We are not in master - skipping the extended release archive check."'
        }
    }

    stage ('push-to-stable') {
        if (env.BRANCH_NAME == "master") {
            sh 'echo "We are in master - pushing to stable branch."'

            def gitCommit = sh 'git rev-parse HEAD'

            sh 'git push git@github.com:fachstudieRSS/kieker.git ' + gitCommit + ':stable'
        } else {
            sh 'echo "We are not in  master - skipping."'
        }
    }
}