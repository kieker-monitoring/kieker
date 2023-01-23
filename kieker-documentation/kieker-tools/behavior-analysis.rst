.. kieker-tools-behavior-analysis:

Behavior Analysis
=================

The **behavior analysis** tool processes monitoring data for users
sessions, builds behavior models from the sessions and clusters these
behaviors utilizing MTree and DBScan.

The tool uses a configuration file, i.e., it is called with
``behavior-analysis -c configuration``.

The configuration file may have the following settings:

+-----------------------------------------+-------------------------------------------------+
| Parameter                               | Description                                     |
+=========================================+=================================================+
| clusterOutputPath                       | file path for the result clusters               |
+-----------------------------------------+-------------------------------------------------+
| medoidOutputPath                        | directory path for all medoids                  |
+-----------------------------------------+-------------------------------------------------+
| classSignatureAcceptancePatternFile     | acceptance patterns for class names in events.  |
+-----------------------------------------+-------------------------------------------------+
| operationSignatureAcceptancePatternFile | acceptance patterns for operation signatures in |
|                                         | events.                                         |
+-----------------------------------------+-------------------------------------------------+
| acceptanceMatcherMode                   | define how event matches should be handled:     |
|                                         | normal or inverse.                              |
+-----------------------------------------+-------------------------------------------------+
| traceSignatureProcessor                 |                                                 |
+-----------------------------------------+-------------------------------------------------+
| clusteringDistance                      |                                                 |
+-----------------------------------------+-------------------------------------------------+
| minPts                                  |                                                 |
+-----------------------------------------+-------------------------------------------------+
| maxAmount                               |                                                 |
+-----------------------------------------+-------------------------------------------------+
| userSessionTimeout                      |                                                 |
+-----------------------------------------+-------------------------------------------------+
| nodeInsertCost                          | node insertion cost for the graph edit distance |
+-----------------------------------------+-------------------------------------------------+
| edgeInsertCost                          | edge insertion cost for the graph edit distance |
+-----------------------------------------+-------------------------------------------------+
| eventGroupInsertCost                    | cost for event groups                           |
+-----------------------------------------+-------------------------------------------------+
| parameterWeighting                      | when events contain payloads, these can be      |
|                                         | included when calculating the GED. Default is   |
|                                         | NaiveParameterWeighting which ignores the       |
|                                         | payload.
+-----------------------------------------+-------------------------------------------------+
| directories                             | one or more Kieker log directories, separated   |
|                                         | by a path separator (:/; on unix/windows)       |
+-----------------------------------------+-------------------------------------------------+
| dataBufferSize                          | read file buffer size                           |
+-----------------------------------------+-------------------------------------------------+
| verbose                                 | if set be more verbose regarding the analysis.  |
+-----------------------------------------+-------------------------------------------------+

The ``classSignatureAcceptancePatternFile`` and ``operationSignatureAcceptancePatternFile``
contain Java regular expressions to match class and operation signatures
in ``BeforeOperationEvent``s.



