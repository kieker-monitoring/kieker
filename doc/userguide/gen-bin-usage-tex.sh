#!/bin/bash

BIN_DIR=${HOME}/svn_work/kieker/software/kieker/trunk/bin/
SCRIPTS="convertLoggingTimestamp.sh dotPic-fileConverter.sh logReplay.sh mappingGenerator.sh trace-analysis.sh"

for s in ${SCRIPTS}; do
    eval ${BIN_DIR}/$s > Appendix-usage-$s.inc.tex
done 