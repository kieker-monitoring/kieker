#!/bin/bash

Dependency graphs, equivalence classes

~/svn_work/kieker/software/kieker/trunk/bin/trace-analysis.sh --inputdirs ~/svn_work/kieker/software/kieker/trunk/examples/userguide/ch5--aspectj-instrumentation/testdata/tpmon-20100830-082225522-UTC/ --outputdir . --plot-Deployment-Component-Dependency-Graph --plot-Assembly-Component-Dependency-Graph --plot-Container-Dependency-Graph --plot-Deployment-Operation-Dependency-Graph --plot-Assembly-Operation-Dependency-Graph --plot-Aggregated-Deployment-Call-Tree --plot-Aggregated-Assembly-Call-Tree --print-Deployment-Equivalence-Classes --print-Assembly-Equivalence-Classes --plot-Aggregated-Deployment-Call-Tree --plot-Aggregated-Assembly-Call-Tree --short-labels

Deployment-level representatives: 6488138950668976141 6488138950668976129 6488138950668976130 6488138950668976131
Assembly-level representative:    6488138950668976129

~/svn_work/kieker/software/kieker/trunk/bin/trace-analysis.sh --inputdirs ~/svn_work/kieker/software/kieker/trunk/examples/userguide/ch5--aspectj-instrumentation/testdata/tpmon-20100830-082225522-UTC/ --outputdir . --select-traces  6488138950668976141 6488138950668976129 6488138950668976130 6488138950668976131 --plot-Deployment-Sequence-Diagrams --plot-Assembly-Sequence-Diagrams --plot-Call-Trees --print-Message-Traces --print-Execution-Traces  --short-labels

~/svn_work/kieker/software/kieker/trunk/bin/dotPic-fileConverter.sh . pdf

for f in *.pdf; do
    pdfcrop "$f"
done