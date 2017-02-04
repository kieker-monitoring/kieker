#!/usr/bin/env bash

# include common variables and functions
source "$(dirname $0)/release-check-common.sh"

# 51=Java 1.6
# 52=Java 1.7
# 53=Java 1.8
# 54=Java 1.9
javaVersion="major version: 52"

# build with ant (target may be passed as $1)
function run_gradle {
	echo "Trying to invoke gradle with target '$1' ..."
	if ! ./gradlew -S $1; then
		echo "Gradle build failed"
		exit 1
	fi
}

# extract archive to a specific location
function extract_archive_to {
	if [ -z "$1" ]; then
		echo "No archive provided"
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
		echo "Archive '$1' is neither zip nor .tar.gz"
		exit 1
	fi
}

function check_src_archive {
	if [ -z "$1" ]; then
		echo "No source archive provided"
		exit 1
	fi

	echo "Decompressing archive '$1' ..."
	extract_archive_n_cd "$1"
	touch $(basename "$1") # just to mark where this dir comes from

	# Making sure that no JavaDoc warnings reported by the `javadoc` tool
	echo -n "Making sure that no JavaDoc warnings (ignoring generated sources) ..."
	if (run_gradle apidoc | grep -v "src-gen" | grep "warning -"); then
	    echo "One or more JavaDoc warnings"
	    exit 1
	fi
	echo "OK"

	# now build release from source (including checks and tests)
	run_gradle distribute
	# make sure that the expected files are present
	assert_dir_exists "${DIST_JAR_DIR}"
	assert_dir_exists "${DIST_RELEASE_DIR}"
	assert_file_exists_regular $(ls "${DIST_JAR_DIR}/kieker-"*".jar" | grep -v emf | grep -v aspectj ) # the core jar
	assert_file_exists_regular "${DIST_JAR_DIR}/kieker-"*"-aspectj.jar"
	assert_file_exists_regular "${DIST_JAR_DIR}/kieker-"*"-emf.jar"
	assert_file_NOT_exists "${DIST_JAR_DIR}/kieker-monitoring-servlet-"*".war"

	# check bytecode version of classes contained in jar
	echo "Making sure that bytecode version of class in jar is $javaVersion"
	MAIN_JAR=$(ls "${DIST_JAR_DIR}/kieker-"*".jar" | grep -v emf | grep -v aspectj)
	assert_file_exists_regular ${MAIN_JAR}

	VERSION_CLASS=$(find build -name "Version.class" | grep "kieker-common")
	assert_file_exists_regular "${VERSION_CLASS}"
	if ! javap -verbose ${VERSION_CLASS} | grep "${javaVersion}"; then
		echo "Unexpected bytecode version"
		exit 1
	fi
	echo "OK"
}

function check_bin_archive {
	if [ -z "$1" ]; then
		echo "No source archive provided"
		exit 1
	fi

	echo "Decompressing archive '$1' ..."
	extract_archive_n_cd "$1"
	touch $(basename "$1") # just to mark where this dir comes from

	# check bytecode version of classes contained in jar
	echo "Making sure that bytecode version of class in jar is $javaVersion"
	MAIN_JAR=$(ls "${DIST_JAR_DIR}/kieker-"*".jar" | grep -v emf | grep -v aspectj)
	assert_file_exists_regular ${MAIN_JAR}
	VERSION_CLASS_IN_JAR=$(unzip -l	 ${MAIN_JAR} | grep Version.class | awk '{ print $4 }')
	unzip "${MAIN_JAR}" "${VERSION_CLASS_IN_JAR}"
	assert_file_exists_regular "${VERSION_CLASS_IN_JAR}"
	
	versionClassDisassembled="$(javap -verbose ${VERSION_CLASS_IN_JAR} )"
	echo "${versionClassDisassembled}"
	
	fileResult="$(file ${VERSION_CLASS_IN_JAR})"
	echo "${fileResult}"
	
	bytecodeVersion=$(javap -verbose ${VERSION_CLASS_IN_JAR} | grep "${javaVersion}")

	if ! javap -verbose ${VERSION_CLASS_IN_JAR} | grep "${javaVersion}"; then
		echo "Unexpected bytecode version: ${bytecodeVersion}"
		exit 1
	fi
	echo "OK"

	# some basic tests with the tools
	if ! (bin/convertLoggingTimestamp.sh --timestamps 1283156545581511026 1283156546127117246 | grep "Mon, 30 Aug 2010 08:22:25.581 +0000 (UTC)"); then
		echo "Unexpected result executinǵ bin/convertLoggingTimestamp.sh"
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
		echo "${PLOT_SCRIPT} does not exist or is not executable"
		exit 1
	    fi
	    if ! ${PLOT_SCRIPT} "${ARCHDIR}" "."; then # passing kieker dir and output dir
		echo "${PLOT_SCRIPT} returned with error"
		exit 1
	    fi
	    for f in $(ls "${REFERENCE_OUTPUT_DIR}" | egrep "(dot$|pic$|html$|txt$)"); do 
		echo -n "Comparing to reference file $f ... "
		if test -z "$f"; then
		    echo "File $f does not exist or is empty"
		    exit 1;
		fi
		# Note that this is a hack because sometimes the line order differs
		(cat "$f" | sort) > left.tmp
		(cat "${REFERENCE_OUTPUT_DIR}/$f" | sort) > right.tmp
		if test "$f" = "traceDeploymentEquivClasses.txt" || test "$f" = "traceAssemblyEquivClasses.txt"; then
			# only the basic test already performed because the assignment to classes is not deterministic
		    echo "OK"
		    continue;
		fi
		if ! diff --context=5	 left.tmp right.tmp; then
		    echo "Detected deviation between files: '$f', '${REFERENCE_OUTPUT_DIR}/${f}'"
		    exit 1
		else 
		    echo "OK"
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
assert_dir_exists ${BASE_TMP_DIR}
change_dir "${BASE_TMP_DIR}"
BASE_TMP_DIR_ABS=$(pwd)

change_dir "${BASE_TMP_DIR_ABS}"
create_subdir_n_cd
DIR=$(pwd)

BINZIP=$(ls ../../${DIST_RELEASE_DIR}/*-binaries.zip)
extract_archive_to ${BINZIP} ${TMP_ZIP_DIR}

BINTGZ=$(ls ../../${DIST_RELEASE_DIR}/*-binaries.tar.gz)
extract_archive_to ${BINTGZ} ${TMP_TGZ_DIR}

diff -r ${TMP_TGZ_DIR} ${TMP_ZIP_DIR}
DIFF_BIN_RESULT=$?

# cleanup temporary folders we created for the comparison
rm -rf ${TMP_BINZIP_DIR} ${TMP_GZ_DIR}

if [ ${DIFF_BIN_RESULT} -eq 0 ]; then
  echo "The content of both binary archives is identical."
  check_bin_archive "${BINTGZ}"
else
  echo "The content of both binary archives is NOT identical."
  exit 1
fi
rm -rf ${DIR}

#
## source releases
#
change_dir "${BASE_TMP_DIR_ABS}"
create_subdir_n_cd
DIR=$(pwd)

SRCZIP=$(ls ../../${DIST_RELEASE_DIR}/*-sources.zip)
extract_archive_to ${SRCZIP} ${TMP_ZIP_DIR}

SRCTGZ=$(ls ../../${DIST_RELEASE_DIR}/*-sources.tar.gz)
extract_archive_to ${SRCTGZ} ${TMP_TGZ_DIR}

diff -r ${TMP_TGZ_DIR} ${TMP_ZIP_DIR}
DIFF_SRC_RESULT=$?

if [ ${DIFF_SRC_RESULT} -eq 0 ]; then
  echo "The content of both source archives is identical."
  check_src_archive "${SRCTGZ}"
else
  echo "The content of both source archives is NOT identical."
  exit 1
fi
rm -rf ${DIR}