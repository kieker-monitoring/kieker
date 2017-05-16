#!groovy

node {
    checkout scm

    stage ('1-compile logs') {
        try {
            sh 'sudo docker run --name 1_compile -v ${WORKSPACE}:/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew -S compileJava compileTestJava"'
        } finally {
            sh 'sudo docker rm 1_compile'
        }
    }

    stage ('2-unit-test logs') {
        try {
            sh 'sudo docker run --name 2_unit_test -v ${WORKSPACE}:/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew -S test"'
        } finally {
            sh 'sudo docker rm 2_unit_test'
        }
    }

    stage ('3-static-analysis logs') {
        try {
            sh 'sudo docker run --name 3_static_analysis -v ${WORKSPACE}:/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew -S check"'
        } finally {
            sh 'sudo docker rm 3_static_analysis'
        }
    }

    stage ('4-release-check-short logs') {
        try {
            sh 'sudo docker run --name 4_release_check_short -v ${WORKSPACE}:/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew checkReleaseArchivesShort"'
        } finally {
            sh 'sudo docker rm 4_release_check_short'
        }
    }

    stage ('5-release-check-extended logs') {
        if (env.BRANCH_NAME == "master") {
            sh 'echo "We are in master - executing the extended release archive check."'
            try {
                sh 'sudo docker run --name 5_release_check_extended -v ${WORKSPACE}:/opt/kieker kieker/kieker-build:openjdk7 /bin/bash -c "cd /opt/kieker; ./gradlew checkReleaseArchives"'
            } finally {
                sh 'sudo docker rm 5_release_check_extended'
            }
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