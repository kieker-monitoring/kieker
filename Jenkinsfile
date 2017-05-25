#!groovy

node('docker') {
    checkout scm

    stage ('1-compile logs') {
        sh 'sudo docker run -v ${WORKSPACE}:/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew -S compileJava compileTestJava"'
    }

    stage ('2-unit-test logs') {
        sh 'sudo docker run --name 2_unit_test -v ${WORKSPACE}:/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew -S test"'
    }

    stage ('3-static-analysis logs') {
        sh 'sudo docker run --name 3_static_analysis -v ${WORKSPACE}:/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew -S check"'   
    }

    stage ('4-release-check-short logs') {
        sh 'sudo docker run --name 4_release_check_short -v ${WORKSPACE}:/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew checkReleaseArchivesShort"'
    }

    stage ('5-release-check-extended logs') {
        if (env.BRANCH_NAME == "master") {
            sh 'echo "We are in master - executing the extended release archive check."'
    
            sh 'sudo docker run --name 5_release_check_extended -v ${WORKSPACE}:/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew checkReleaseArchives"'
        } else {
            sh 'echo "We are not in master - skipping the extended release archive check."'
        }
    }

    stage ('push-to-stable') {
        if (env.BRANCH_NAME == "master") {
            sh 'echo "We are in master - pushing to stable branch."'

            sh 'git push git@github.com:kieker-monitoring/kieker.git master:stable'
        } else {
            sh 'echo "We are not in master - skipping."'
        }
    }
}