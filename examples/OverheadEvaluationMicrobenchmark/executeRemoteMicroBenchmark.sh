#!/bin/bash

# The following constants specify user name and address of the remote node executing the benchmark
# Java (>= 1.6) and R (> 3.0) should be installed on the remote node
USERNAME=ubuntu
REMOTE_NODE=192.168.48.60

# These constants specify the location and the name of the jar file to benchmark
BENCHMARKED_JAR_FOLDER=dist
BENCHMARKED_JAR_NAME=kieker-1.10-SNAPSHOT_aspectj.jar 

# These constants specify the location and the name of the MooBench folder
MOOBENCH_FOLDER=examples/OverheadEvaluationMicrobenchmark
MOOBENCH_FOLDER_NAME=MooBench

# This constant specifies the name of the folder containing the results from MooBench
RESULTS_FOLDER_NAME=results-kieker

# This constant specifies the target file for the results
RESULTS_TARGET_FILE=plot.csv

# Copy Kieker and MooBench to the remote node
scp ${BENCHMARKED_JAR_FOLDER}/${BENCHMARKED_JAR_NAME} ${USERNAME}@${REMOTE_NODE}:.
scp -r ${MOOBENCH_FOLDER}/${MOOBENCH_FOLDER_NAME} ${USERNAME}@${REMOTE_NODE}:.

# Execute MooBench
ssh ${USERNAME}@${REMOTE_NODE} 'cd ${MOOBENCH_FOLDER_NAME}; mv ../${BENCHMARKED_JAR_NAME} lib; chmod +x benchmark.sh; ./benchmark.sh; exit'

# Copy results to workspace
scp ${USERNAME}@${REMOTE_NODE}:${MOOBENCH_FOLDER_NAME}/tmp/${RESULTS_FOLDER_NAME}/results-text.csv ${RESULTS_TARGET_FILE}

# Save old results from previous benchmarks on the remote node and clean up
ssh ${USERNAME}@${REMOTE_NODE} 'cp ${MOOBENCH_FOLDER_NAME}/tmp/${RESULTS_FOLDER_NAME}/results.zip old-results --backup=t; exit'
ssh ${USERNAME}@${REMOTE_NODE} "cd old-results; ls -A1t | sed -e '1,100d' | xargs -d '\n' rm; exit"
ssh ${USERNAME}@${REMOTE_NODE} 'mv old-results /tmp/; exit'
ssh ${USERNAME}@${REMOTE_NODE} 'rm -rf *; exit'
ssh ${USERNAME}@${REMOTE_NODE} 'mv /tmp/old-results .; exit'