#!/usr/bin/env bash

# include common variables and functions
source "$(dirname $0)/release-check-common.sh"

# 50=Java 1.6
# 51=Java 1.7
# 52=Java 1.8
# 53=Java 1.9
javaVersion="major version: 51"

# build with ant (target may be passed as $1)
function run_gradle {
	information "Trying to invoke gradle with target '$1' ..."
	if ! ./gradlew -S $1; then
		error "Gradle build failed"
		exit 1
	fi
}

# extract archive to a specific location
function extract_archive_to {
	if [ -z "$1" ]; then
		error "No archive provided"
		exit 1
	fi

    # Create destination folder if it does not exist already
	if ! [ -d "$2" ]; then
	  mkdir "$2"
	fi

	if echo "$1" | grep "zip"; then
		unzip -q "$1" -d "$2"
	elif echo "$1" | grep "tar.gz"; then
		tar -xzf "$1" -C "$2"
	else
		error "Archive '$1' is neither zip nor .tar.gz"
		exit 1
	fi
}

function check_bin_archive {
	if [ -z "$1" ]; then
		error "No source archive provided"
		exit 1
	fi

	information "Decompressing archive '$1' ..."
	extract_archive_n_cd "$1"
	touch $(basename "$1") # just to mark where this dir comes from

	# check bytecode version of classes contained in jar
	information "Making sure that bytecode version of class in jar is $javaVersion"
	MAIN_JAR=$(ls "${DIST_JAR_DIR}/kieker-"*".jar" | grep -E "kieker-[^-]*(-SNAPSHOT)?.jar")
	assert_file_exists_regular ${MAIN_JAR}
	VERSION_CLASS_IN_JAR=$(unzip -l	 ${MAIN_JAR} | grep Version.class | awk '{ print $4 }')
	unzip "${MAIN_JAR}" "${VERSION_CLASS_IN_JAR}"
	assert_file_exists_regular "${VERSION_CLASS_IN_JAR}"

	if ! javap -verbose ${VERSION_CLASS_IN_JAR} | grep -q "${javaVersion}"; then
		error "Unexpected bytecode version: ${javaVersion}"
		exit 1
	else
		information "Found bytecode version ${javaVersion}, OK"
	fi

	CONVERTER_NAME=`ls tools/convert-logging-timestamp*zip`

	extract_archive "${CONVERTER_NAME}"

        CONVERTER_SCRIPT=`ls convert-logging-timestamp*/bin/convert-logging-timestamp`

	# some basic tests with the tools
	if ! (${CONVERTER_SCRIPT} --timestamps 1283156545581511026 1283156546127117246 | grep "Mon, 30 Aug 2010 08:22:25.581 +0000 (UTC)"); then
		error "Unexpected result executing convert-logging-timestamp"
		exit 1
	fi

        # now perform some trace analysis tests and compare results with reference data
	for testset in "kieker-20100830-082225522-UTC-example-plots" "kieker-20141008-101258768-UTC-example-plots" \
		"kieker-20141008-101258768-UTC-repairEventBasedTraces-example-plots" \
	    "kieker-20141009-160413833-UTC-operationExecutionsConstructors-example-plots" \
	    "kieker-20141009-163010944-UTC-constructor-events-example-plots"; do
	    ARCHDIR=$(pwd)
	    create_subdir_n_cd
	    REFERENCE_OUTPUT_DIR="${ARCHDIR}/examples/userguide/ch5--trace-monitoring-aspectj/testdata/${testset}"
	    PLOT_SCRIPT="${ARCHDIR}/examples/userguide/ch5--trace-monitoring-aspectj/testdata/${testset}.sh"

	    if ! test -x ${PLOT_SCRIPT}; then
		error "${PLOT_SCRIPT} does not exist or is not executable"
		exit 1
	    fi

	    information "Executing ${PLOT_SCRIPT} ${ARCHDIR} ."

	    if ! ${PLOT_SCRIPT} "${ARCHDIR}" "."; then # passing kieker dir and output dir
		error "${PLOT_SCRIPT} returned with error"
		exit 1
	    fi

            information "Output directory ${REFERENCE_OUTPUT_DIR}"

	    for f in $(ls "${REFERENCE_OUTPUT_DIR}" | egrep "(dot$|pic$|html$|txt$)"); do
		information "Comparing to reference file $f ... "
		if test -z "$f"; then
		    error "File $f does not exist or is empty"
		    exit 1;
		else
		    # Note that this is a hack because sometimes the line order differs
		    (cat "$f" | sort) > left.tmp
		    (cat "${REFERENCE_OUTPUT_DIR}/$f" | sort) > right.tmp
		    if test "$f" = "traceDeploymentEquivClasses.txt" || test "$f" = "traceAssemblyEquivClasses.txt"; then
			# only the basic test already performed because the assignment to classes is not deterministic
		        information "OK"
		        continue;
		    fi
		    if ! diff left.tmp right.tmp; then
		        error "Detected deviation between files: '$f', '${REFERENCE_OUTPUT_DIR}/${f}'"
		        exit 1
		    else
		        information "OK"
		    fi
		fi
	    done

	    # Return to archive base dir
	    cd ${ARCHDIR}
	done

	# TODO: test examples ...
}

##
## "main"
##

TMP_ZIP_DIR=zip
TMP_TGZ_DIR=tgz

#
## binary releases
#
information "--------------------------------"
information "Binary release"
information "--------------------------------"

assert_dir_exists ${BASE_TMP_DIR}
change_dir "${BASE_TMP_DIR}"
BASE_TMP_DIR_ABS=$(pwd)

change_dir "${BASE_TMP_DIR_ABS}"
create_subdir_n_cd
DIR=$(pwd)

information "Binary ZIP"
BINZIP=$(ls ../../${DIST_RELEASE_DIR}/*-binaries.zip)
extract_archive_to ${BINZIP} ${TMP_ZIP_DIR}

information "Binary TGZ"
BINTGZ=$(ls ../../${DIST_RELEASE_DIR}/*-binaries.tar.gz)
extract_archive_to ${BINTGZ} ${TMP_TGZ_DIR}

diff -r "${TMP_TGZ_DIR}" "${TMP_ZIP_DIR}"
DIFF_BIN_RESULT=$?

# cleanup temporary folders we created for the comparison
rm -rf "${TMP_BINZIP_DIR}" "${TMP_GZ_DIR}"

if [ ${DIFF_BIN_RESULT} -eq 0 ]; then
  information "The content of both binary archives is identical."
  check_bin_archive "${BINTGZ}"
else
  error "The content of both binary archives is NOT identical."
  exit 1
fi
rm -rf "${DIR}"

# end
