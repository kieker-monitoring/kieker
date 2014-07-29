#!/bin/bash

# The following constant specifies user name and address of the remote node executing the benchmark.
# We recommend to use SSH keys for the access and to install Java (>= 1.6) and R (> 3.0) on the remote node.
REMOTE_NODE=jenkins@blade1

# The following constant specifies location and name of the benchmarked jar file relative to the workspace. 
BENCHMARKED_JAR=dist/kieker-1.10-SNAPSHOT_aspectj.jar 

# The following constants specify location and name of the MooBench folder
MOOBENCH_FOLDER=examples/OverheadEvaluationMicrobenchmark/MooBench

# The following constant specifies the name of the folder containing the results from MooBench
RESULTS_FOLDER_NAME=results-kieker

# This constant specifies the target file for the results
RESULTS_TARGET_FILE=plot.csv


# Copy the benchmarked file and MooBench to the remote node
scp -r ${MOOBENCH_FOLDER} ${REMOTE_NODE}:MooBench
scp ${BENCHMARKED_JAR} ${REMOTE_NODE}:MooBench/lib

# Execute MooBench
ssh ${REMOTE_NODE} 'cd MooBench; chmod +x benchmark.sh; ./benchmark.sh; exit'

# Copy results to workspace
scp ${REMOTE_NODE}:MooBench/tmp/${RESULTS_FOLDER_NAME}/results-text.csv ${RESULTS_TARGET_FILE}

# Save up to 100 results from previous benchmarks on the remote node and clean up
ssh ${REMOTE_NODE} "cp MooBench/tmp/${RESULTS_FOLDER_NAME}/results.zip old-results --backup=t; exit"
ssh ${REMOTE_NODE} "cd old-results; ls -A1t | sed -e '1,100d' | xargs -d '\n' rm; exit"
ssh ${REMOTE_NODE} 'mv old-results /tmp/; exit'
ssh ${REMOTE_NODE} 'rm -rf MooBench; exit'
ssh ${REMOTE_NODE} 'mv /tmp/old-results .; exit'