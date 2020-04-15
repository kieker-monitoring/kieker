#!/bin/bash

BASE_DIR=$(dirname $0)

if [ "$1" == "" ] ; then
	echo "Error: No computation node specified."
	exit 1
fi

# The following constant specifies user name and address of the remote node executing the benchmark.
# We recommend to use SSH keys for the access and to install Java (>= 1.6) and R (> 3.0) on the remote node.
REMOTE_NODE="$1"

# The following constant specifies location and name of the benchmarked jar file relative to the workspace.
BENCHMARKED_JAR=build/libs/kieker-1.14-aspectj.jar

# The following constants specify location and name of the MooBench folder
MOOBENCH_FOLDER=${BASE_DIR}/MooBench

# The following constant specifies the name of the folder containing the results from MooBench
RESULTS_FOLDER_NAME=results-kieker

# This constant specifies the target file for the results
RESULTS_TARGET_FILE=plot.csv

# Copy the benchmarked file and MooBench to the remote node
scp -r ${MOOBENCH_FOLDER} ${REMOTE_NODE}:MooBench
# fix for KIEKER-1559: create empty folders "data" and "lib"
ssh ${REMOTE_NODE} 'mkdir -p MooBench/data; mkdir -p MooBench/lib; exit'
scp ${BENCHMARKED_JAR} ${REMOTE_NODE}:MooBench/lib

# Execute MooBench
ssh ${REMOTE_NODE} 'cd MooBench; chmod +x benchmark.sh; ./benchmark.sh; exit'

# Copy results to workspace
scp ${REMOTE_NODE}:MooBench/${RESULTS_FOLDER_NAME}/results-text.csv ${RESULTS_TARGET_FILE}

# Save up to 100 results from previous benchmarks on the remote node and clean up
BACKUP_FOLDER_NAME="$(date +%Y-%m-%d-%H-%M-%S)"
ssh ${REMOTE_NODE} "mkdir old-results/${BACKUP_FOLDER_NAME}; cp MooBench/${RESULTS_FOLDER_NAME}/results.zip old-results/${BACKUP_FOLDER_NAME}/; exit"
# A Non-Solaris OS could require "-d '\n'" for the xargs command
ssh ${REMOTE_NODE} "cd old-results; ls -A1t | sed -e '1,100d' | xargs rm -rf; exit"
ssh ${REMOTE_NODE} 'rm -rf MooBench; exit'
