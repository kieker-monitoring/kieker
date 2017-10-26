BINDIR=$(cd "$(dirname "$0")"; pwd)/
kiekerJarName="kieker-1.14-SNAPSHOT"

# remove previous kieker directory
rm -rf ${kiekerJarName}

echo "Downloading Kieker's binaries distribution..."
wget -O kieker.zip https://build.se.informatik.uni-kiel.de/jenkins/job/kieker-monitoring/job/kieker/job/master/lastSuccessfulBuild/artifact/build/distributions/${kiekerJarName}-binaries.zip

echo "Unzipping Kieker's binaries distribution..."
unzip kieker.zip
cd ${kiekerJarName}

${BINDIR}/executeRemoteMicroBenchmark.sh
