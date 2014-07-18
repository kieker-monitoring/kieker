#!/bin/bash

REMOTE_NODE=192.168.48.60

# Copy Kieker and MooBench to the remote node
scp /srv/jenkins/workspace/kieker-nightly-release/dist/kieker-1.10-SNAPSHOT_aspectj.jar ubuntu@${REMOTE_NODE}:.
scp /srv/jenkins/workspace/kieker-nightly-release/examples/OverheadEvaluationMicrobenchmark/MooBench.zip ubuntu@${REMOTE_NODE}:.

# Unzip and execute MooBench
ssh ubuntu@${REMOTE_NODE} 'unzip MooBench.zip; mv kieker*.jar lib; chmod +x benchmark.sh; ./benchmark.sh; exit'

# Copy results to workspace
scp ubuntu@${REMOTE_NODE}:tmp/results-kieker/results-text.csv /srv/jenkins/workspace/kieker-nightly-release/plot.csv

# Clean up
ssh ubuntu@${REMOTE_NODE} 'rm -rf *; exit'