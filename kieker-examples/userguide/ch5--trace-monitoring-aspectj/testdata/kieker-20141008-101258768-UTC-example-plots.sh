#!/bin/bash

KIEKER_DIR="$1"
OUTDIR="$2"
EXAMPLE_LOG=${KIEKER_DIR}/examples/userguide/ch5--trace-monitoring-aspectj/testdata/kieker-20141008-101258768-UTC

TOOL_DIR=`mktemp -d`

ZIP_NAME="${KIEKER_DIR}/tools/trace-analysis-1.14.zip"

( cd ${TOOL_DIR} ; unzip ${ZIP_NAME} )

TOOL_NAME=`basename ${ZIP_NAME} | sed 's/\.zip$//g'`

TRACE_ANALYSIS_SH=${TOOL_DIR}/$TOOL_NAME/bin/trace-analysis

FILE_CONVERTER_SH=${KIEKER_DIR}/bin/dotPic-fileConverter.sh

# Should be enabled only if the reference pdfs shall be created (otherwise the release test script is broken):
#KIEKER_DIR=${HOME}/git_work/kieker/
#OUTDIR=$(basename "${EXAMPLE_LOG}")"-example-plots77"
#EXAMPLE_LOG=${KIEKER_DIR}/examples/userguide/ch5--trace-monitoring-aspectj/testdata/kieker-20140821-113933692-UTC
#TRACE_ANALYSIS_SH=${KIEKER_DIR}/bin/trace-analysis.sh
#FILE_CONVERTER_SH=${KIEKER_DIR}/bin/dotPic-fileConverter.sh
#

if ! test -x "${TRACE_ANALYSIS_SH}"; then
    echo "${TRACE_ANALYSIS_SH} does not exist or is not executable"
    exit 1
fi

if ! test -x "${FILE_CONVERTER_SH}"; then
    echo "${TRACE_ANALYSIS_SH} does not exist or is not executable"
    exit 1
fi

if ! test -s "${EXAMPLE_LOG}/kieker.map"; then
    echo "${EXAMPLE_LOG}/ is no monitoring log"
    exit 1 
fi

# Should be enabled only if the reference pdfs shall be created (otherwise the release test script is broken):
#if test -e "${OUTDIR}"; then
#    echo "${OUTDIR} exists; remove it"
#    exit 1
#fi
#mkdir "${OUTDIR}"
#

# Dependency graphs, equivalence classes
${TRACE_ANALYSIS_SH} \
    --verbose \
    --inputdirs "${EXAMPLE_LOG}" --outputdir "./${OUTDIR}" \
    --plot-Deployment-Component-Dependency-Graph none\
    --plot-Assembly-Component-Dependency-Graph none\
    --plot-Container-Dependency-Graph \
    --plot-Deployment-Operation-Dependency-Graph none\
    --plot-Assembly-Operation-Dependency-Graph none\
    --plot-Aggregated-Deployment-Call-Tree \
    --plot-Aggregated-Assembly-Call-Tree \
    --print-Deployment-Equivalence-Classes \
    --print-Assembly-Equivalence-Classes \
    --short-labels
	
# Dependency graphs with responseTimes
#${TRACE_ANALYSIS_SH} \
#    --verbose \
#    --inputdirs "${EXAMPLE_LOG}" --outputdir "./${OUTDIR}" \
#	-p responseTime- \
#	--plot-Deployment-Component-Dependency-Graph responseTimes-ms \
#	--plot-Assembly-Component-Dependency-Graph responseTimes-ns  \
#	--plot-Deployment-Operation-Dependency-Graph responseTimes-us  \
#	--plot-Assembly-Operation-Dependency-Graph responseTimes-ns \
#   --short-labels

# Should be enabled only if the reference pdfs shall be created (otherwise the release test script is broken):
#${FILE_CONVERTER_SH} "./${OUTDIR}" pdf
#for f in "./${OUTDIR}"/*.pdf; do
#    pdfcrop "$f"
#done
#
