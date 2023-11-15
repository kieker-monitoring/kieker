.. _kieker-tools-mvis:

Model Visualization and Statistics Tool
=======================================

mvis allows to generate graphics and apply metrics to architecture models.

===== ===================== ======== ======================================================
Short Long                  Required Description
===== ===================== ======== ======================================================
-i    --input               yes      Input model directory
-o    --output              yes      Output directory to store graphics and statistics
-M    --component-map                Component, file and function map file
-s    --selector                     Set architecture graph selector
                                     all, diff, subtract, intersect
-g    --graphs                       Specify which output graphs must be generated
                                     odt-op, graphml, dot-component
-m    --mode                         Mode deciding whether an edge is added when its nodes
                                     are not selected add-nodes, only
-c    --compute-statistics           Generate the listed statistics
      --eol                          Set end of line character for CSV files
                                     Default: system's standard symbol                               
===== ===================== ======== ======================================================

Selectors
---------

- **all** selects all nodes regardless of the node and edge labels
- **diff** diff:label1,label2 
- **subtract** subtract:label1,label2
- **interstect** intersect:label1,label2

 
Outputs
-------
 
**Counting Metrics**
%s/%s-%s output-directory, file-prefix, operation-calls.csv
%s/%s-%s output-directory, file-prefix, distinct-operation-degree.csv
%s/%s-%s output-directory, file-prefix, distinct-module-degree.csv

**Allen Metric**
%s/%s output-directory, model-complexity.csv


