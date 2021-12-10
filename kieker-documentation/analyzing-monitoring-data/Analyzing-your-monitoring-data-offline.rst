.. _analyzing-your-monitoring-data-offline:

Analyzing your monitoring data offline 
======================================

Kieker comes with a trace diagnosis tool which allows to analyze
monitoring logs offline and produce different outputs. In detail they
are:

-  Deployment-level UML sequence diagrams (.pic)
-  Assembly-level UML sequence diagrams (.pic)
-  Deployment-level component dependency graph (.dot) -- including
   deployment boundaries
-  Assembly-level component dependency graph (.dot) -- without
   deployment boundaries
-  Container dependency graph (.dot)
-  Deployment-level operation dependency graph (.dot)
-  Assembly operation dependency graph (.dot)
-  Aggregated deployment call tree (.dot)
-  Aggregated assembly call tree (.dot)
-  Call trees (.txt)
-  Message traces (.txt)
-  Execution traces (.txt)
-  Invalid execution traces (.txt)
-  System model (.html)
-  Deployment equivalence classes
-  Assembly equivalence classes

The pic and dot files can be transformed into SVG and PDF documents with
a provided conversion script. Please consult the trace analysis page for
more details on the generated output and conversion tooling.

Furthermore, Kieker is supplemented by different tools to inspect and
analyze Kieker logs.

