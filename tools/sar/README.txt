Infos regarding Bachelor Thesis Simon Ohlsen, Yannick Illmann:

All bachelor relevant classes are placed in package: org.oceandsl.tools.sar.bsc.dataflow

It contains:

 - TeetimeBscDataflowConfiguration: class for pipeline config and further setup preparation
 - package model: contains all object, initialized and used in a pipeline run
 - package stages: contains all defined pipeline stages

Prerequisite: kieker dependency

Directory 'Bsc Kieker' contains:
 - updated Ecore architecture meta-model
 - m2 repository dependency -> please unzip the content at the following path: '${PATH_TO_USER_DIR}\.m2\repository\net\kieker-monitoring\'


To run the SAR as standalone bachelor configuration make sure non empty 
 
 - dataflow.csv
 - filecontents.csv
 - esm_map.csv*

is provided, according to ESM Dataflow Analysis tool standard.

Use the run configuration: '-bsc-j "./esm/dataflow.csv" -bsc-c "./esm/filecontents.csv" -bsc-p "./esm/esm_map.csv" -o "./output" -E "testrun A" -l "ich bin ein Label"'

Make sure every path and file is modified to your directory properties.

*a file containing all package and component relations like:

packageA;componentX
packageB;componentY


Authors:
Simon Ohlsen, Yannick Illmann
