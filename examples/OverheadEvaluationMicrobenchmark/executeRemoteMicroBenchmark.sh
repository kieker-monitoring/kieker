#!/bin/bash

REMOTE_NODE=192.168.48.60

KIEKER_JAR=kieker-1.10-SNAPSHOT_aspectj.jar 

# Copy Kieker and MooBench to the remote node
scp dist/${KIEKER_JAR} ubuntu@${REMOTE_NODE}:.
scp -r examples/OverheadEvaluationMicrobenchmark/MooBench ubuntu@${REMOTE_NODE}:.

# Execute MooBench
ssh ubuntu@${REMOTE_NODE} 'cd MooBench; mv ../kieker*.jar lib; chmod +x benchmark.sh; ./benchmark.sh; exit'

# Copy results to workspace
scp ubuntu@${REMOTE_NODE}:MooBench/tmp/results-kieker/results-text.csv plot.csv

# Clean up
ssh ubuntu@${REMOTE_NODE} 'rm -rf *; exit'