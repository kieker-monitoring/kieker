#!/bin/sh

KIEKER_DIR="$1"
OUTDIR="$2"
EXAMPLE_LOG=${KIEKER_DIR}/examples/userguide/ch5--trace-monitoring-aspectj/testdata/kieker-20100830-082225522-UTC

KIEKER_VERSION="2.0.0-SNAPSHOT"

TOOL_DIR=`mktemp -d`

ZIP_NAME="${KIEKER_DIR}/tools/trace-analysis-${KIEKER_VERSION}.zip"

( cd ${TOOL_DIR} ; unzip -o ${ZIP_NAME} )

TOOL_NAME=`basename ${ZIP_NAME} | sed 's/\.zip$//g'`

TRACE_ANALYSIS_SH=${TOOL_DIR}/$TOOL_NAME/bin/trace-analysis
FILE_CONVERTER_SH=${KIEKER_DIR}/bin/dotPic-fileConverter.sh

# Should be enabled only if the reference pdfs shall be created (otherwise the release test script is broken):
#KIEKER_DIR=${HOME}/git_work/kieker/
#EXAMPLE_LOG=${KIEKER_DIR}/examples/userguide/ch5--trace-monitoring-aspectj/testdata/kieker-20100830-082225522-UTC
#TRACE_ANALYSIS_SH=${KIEKER_DIR}/bin/trace-analysis.sh
#FILE_CONVERTER_SH=${KIEKER_DIR}/bin/dotPic-fileConverter.sh
#OUTDIR=$(basename "${EXAMPLE_LOG}")"-example-plots77"

if ! test -x "${TRACE_ANALYSIS_SH}"; then
    echo "${TRACE_ANALYSIS_SH} does not exist or is not executable"
    exit 1
fi

if ! test -x "${FILE_CONVERTER_SH}"; then
    echo "${FILE_CONVERTER_SH} does not exist or is not executable"
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
${TRACE_ANALYSIS_SH} \
    --verbose \
    --inputdirs "${EXAMPLE_LOG}" --outputdir "./${OUTDIR}" \
    -p responseTime- \
    --plot-Deployment-Component-Dependency-Graph responseTimes-ms \
    --plot-Assembly-Component-Dependency-Graph responseTimes-ns  \
    --plot-Deployment-Operation-Dependency-Graph responseTimes-us  \
    --plot-Assembly-Operation-Dependency-Graph responseTimes-ns \
    --short-labels

# Deployment-level representatives: 6488138950668976141 6488138950668976129 6488138950668976130 6488138950668976131
# Assembly-level representative:    6488138950668976129

${TRACE_ANALYSIS_SH} \
    --verbose \
    --inputdirs "${EXAMPLE_LOG}" --outputdir "./${OUTDIR}" \
    --select-traces  6488138950668976141 6488138950668976129 6488138950668976130 6488138950668976131 \
    --plot-Deployment-Sequence-Diagrams \
    --plot-Assembly-Sequence-Diagrams \
    --plot-Call-Trees \
    --print-Message-Traces \
    --print-Execution-Traces  \
    --short-labels

# Should be enabled only if the reference pdfs shall be created (otherwise the release test script is broken):
#${FILE_CONVERTER_SH} "./${OUTDIR}" pdf
#for f in "./${OUTDIR}"/*.pdf; do
#    pdfcrop "$f"
#done
