#!groovy

node('kieker-slave-docker') {
    
    stage ('Checkout') {
        checkout scm
    }

    stage ('1-compile logs') {
        sh 'docker run --rm -u `id -u` -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7-small /bin/bash -c "cd /opt/kieker; ./gradlew -S compileJava compileTestJava"'
    }

    stage ('2-unit-test logs') {
        sh 'docker run --rm -u `id -u` -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7-small /bin/bash -c "cd /opt/kieker; ./gradlew -S test"'
    }

    stage ('3-static-analysis logs') {
        sh 'docker run --rm -u `id -u` -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7-small /bin/bash -c "cd /opt/kieker; ./gradlew -S check"'    
    }

    stage ('4-release-check-short logs') {
        sh 'docker run --rm -u `id -u` -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7-small /bin/bash -c "cd /opt/kieker; ./gradlew checkReleaseArchivesShort"'

        archiveArtifacts artifacts: 'build/distributions/*,kieker-documentation/userguide/kieker-userguide.pdf,build/libs/*.jar', fingerprint: true
    }

    stage ('5-release-check-extended logs') {
        if (env.BRANCH_NAME == "master" || env.CHANGE_TARGET != null) {
            sh 'echo "We are in master or in a PR - executing the extended release archive check."'
    
            sh 'docker run --rm -u `id -u` -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7-small /bin/bash -c "cd /opt/kieker; ./gradlew checkReleaseArchives"'
        } else {
            sh 'echo "We are not in master or in a PR - skipping the extended release archive check."'
        }
    }

    stage ('push-to-stable') {
        if (env.BRANCH_NAME == "master") {
            sh 'echo "We are in master branch."'

	    sh 'echo "Pushing to stable branch."'
            sh 'git push git@github.com:kieker-monitoring/kieker.git $(git rev-parse HEAD):stable'

	    sh 'echo "Uploading snapshot archives to oss.sonatype.org."'
            withCredentials([usernamePassword(credentialsId: 'artifactupload', usernameVariable: 'kiekerMavenUser', passwordVariable: 'kiekerMavenPassword')]) {
                sh 'docker run --rm -u `id -u` -e kiekerMavenUser=$kiekerMavenUser -e kiekerMavenPassword=$kiekerMavenPassword -v ' + env.WORKSPACE + ':/opt/kieker kieker/kieker-build:openjdk7-small /bin/bash -c "cd /opt/kieker; ./gradlew uploadArchives"'
            }
        } else {
            sh 'echo "We are not in  master - skipping."'
        }
    }

    stage ('Cleanup')
    {
	deleteDir()
    }
}
