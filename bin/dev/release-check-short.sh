#!/usr/bin/env bash

# include common variables and functions
source "$(dirname $0)/release-check-common.sh"

KIEKER_VERSION="1.14"

# lists the files included in an archive without extracting it
function cat_archive_content {
	if [ -z "$1" ]; then
		echo "No archive provided"
		exit 1
	fi

	if echo "$1" | grep "zip"; then
		unzip -l "$1" | awk '{ print $4 }' |grep -v "^$"
	elif echo "$1" | grep "tar.gz"; then
		tar -tzf "$1" |grep -v "^$"
	else
		echo "Archive '$1' is neither zip nor .tar.gz"
		exit 1
	fi
}

function assert_no_duplicate_files_in_archive {
    echo -n "Making sure that no duplicate files in '$1' ..."
    (cat_archive_content $1 | sort) > tmp.content.original
    (cat_archive_content $1 | sort | uniq) > tmp.content.original.uniq
    if ! diff tmp.content.original tmp.content.original.uniq; then
	echo "Archive contains duplicate files."
	exit 1
    fi
    echo OK
}

function assert_all_sh_scripts_executable {
    for sh in $(find -name "*.sh"); do
	echo -n "Checking for exectuable flag: $sh ... "
	if ! test -x $sh; then
	    echo " not executable"
	    exit 1
	fi
	echo OK
    done
}

function assert_zip_file_content_exist {
    echo -n "Asserting zip file '$1' contains the following files: '$2' ..."
    if ! test -s "$1"; then
	echo "File '$1' is missing or not a regular file"
	exit 1
    fi
    CONTENTS=$(unzip -l $1)
    for p in $2; do
	if ! (echo ${CONTENTS} | grep -q "$p"); then
	    echo "'$p' not found in '$1'"
	    exit 1
	fi
    done
    echo OK
}

function assert_zip_file_content_contains {
    echo -n "Asserting file '$2' in zip file '$1' contains the following pattern: '$3' ..."
    if ! test -s "$1"; then
	echo "File '$1' is missing or not a regular file"
	exit 1
    fi
    CONTENT=$(unzip -c $1 $2)
    if ! (echo ${CONTENT} | grep -q "$3"); then
	echo "'$3' not found in '$2' (itself contained in '$1')"
	exit 1
    fi

    echo OK
}

function assert_file_NOT_exists_recursive {
	echo -n "Asserting '$1' does not exist as file or directory in any of the subdirs ..."
	NUM_DIRS=$(find -name "$1" | wc -l)
	if [ ${NUM_DIRS} -gt 0 ]; then
	    echo "$1 exists: $(find -name "$1")"
	    exit 1
	fi
	echo OK
}

# Asserts the existence of files common to the src and bin releases
function assert_files_exist_common {
	assert_dir_exists "bin/"
	assert_file_exists_regular "bin/logging.properties"
	assert_file_exists_regular "bin/logging.debug.properties"
	assert_file_exists_regular "bin/logging.verbose.properties"
	assert_file_NOT_exists "examples/JavaEEServletContainerExample/jetty/javadoc/"
	assert_dir_exists "lib/"
	assert_dir_exists "lib/framework-libs/"
	assert_file_exists_regular "lib/sigar/libsigar-x86-linux.so"
	assert_file_exists_regular "lib/sigar/libsigar-amd64-linux.so"
	assert_file_exists_regular "lib/sigar/sigar-amd64-winnt.dll"
	assert_file_exists_regular "lib/sigar/sigar-x86-winnt.dll"
	assert_file_exists_regular "lib/sigar/sigar-x86-winnt.lib"
	assert_file_exists_regular "README"
	assert_file_exists_regular "HISTORY"
	assert_file_exists_regular "LICENSE"
	assert_file_NOT_exists "build/tmp/"
	assert_file_NOT_exists "build-eclipse/"
	assert_file_NOT_exists "tmp/"
	assert_file_NOT_exists ".git/"
	assert_file_NOT_exists ".gitignore/"

	assert_file_NOT_exists_recursive "*.log"

	echo -n "Make sure that no class files included (only exception is inside WEB-INF/classes) ..."
	NUM_CLASS=$(find -name "*.class" | grep -v "WEB-INF/classes" | wc -l)
	if [ ${NUM_CLASS} -gt 0 ]; then
	    echo ".class files included: $(find -name "*.class" | grep -v "WEB-INF/classes")"
	    exit 1
	fi
	echo OK

	# check if LICENSE file for each jar
	for jar in $(find lib/ -name "*.jar"); do
		JAR_BASE=$(echo ${jar} | sed 's/\(.*\)\..*/\1/') # remove file extension
		assert_file_exists_regular "${JAR_BASE}.LICENSE"
	done

	# Make sure that required infos included in each LICENSE file in lib/ (excluding subdirs)
	for info in "Project" "Description" "License" "Required by"; do
	    for l in lib/*.LICENSE; do
		echo -n "Asserting '$l' contains '${info}' information .. "
		if ! (grep -q "${info}:" $l); then
		    echo "'${info}' missing in $l";
		    exit 1
		fi;
		echo "OK"
	    done
	done

	echo -n "Making sure that no references to old Kieker Jars included (note that we cannot check inside binary files) ..."
	if (grep -R "kieker-[[:digit:]].*\.jar" * | grep -v "Binary" |  grep -Ev "kieker-${KIEKER_VERSION}((\\\\)?-[[:alpha:]]+)?\.jar"); then
	    # Don't ask why results not dumped to stdout above
	    echo "Found old version string. Add/correct replacement regexp in Gradle file?"
	    echo "Due to a strange issue with the grep above, please use the grep regexp above to see where the problem is."
	    exit 1
	fi
	echo OK

	# make sure that specified AspectJ version matches the present files
	assert_file_exists_regular "lib/aspectjrt-${aspectjversion}.jar"
	assert_file_exists_regular "lib/aspectjweaver-${aspectjversion}.jar"

	echo "Making sure that for each gradle script, the Gradle wrapper environment exists ..."
	for d in $(find -name "build.gradle" -exec dirname {} \;); do
	    assert_file_exists_regular $d/gradlew
	    assert_file_exists_regular $d/gradle/
	    assert_file_exists_regular $d/gradlew.bat
	done
	echo OK

	echo "Making sure that for each Gradle wrapper environment, a gradle script exists ..."
	for d in $(find -name "gradlew.bat" -exec dirname {} \;); do
	    assert_file_exists_regular $d/build.gradle
	done
	echo OK
}

# Asserts the existence of files in the src release
function assert_files_exist_src {
	assert_files_exist_common
	assert_file_exists_regular "Jenkinsfile"
	assert_dir_exists "lib/static-analysis/"
	assert_file_NOT_exists "dist/"
	assert_file_NOT_exists_recursive "build"
	assert_dir_NOT_exists "javadoc"

	assert_file_NOT_exists "META-INF/"

	assert_file_NOT_exists "kieker-examples/userguide/ch2--manual-instrumentation/lib/*.jar"
	assert_file_NOT_exists "kieker-examples/userguide/ch3-4--custom-components/lib/*.jar"
	assert_file_NOT_exists "kieker-examples/userguide/ch5--trace-monitoring-aspectj/lib/*.jar"
	assert_file_NOT_exists "kieker-examples/userguide/appendix-JMS/lib/*.jar"
	assert_file_NOT_exists "kieker-examples/userguide/appendix-Sigar/lib/*.jar"

	assert_file_exists_regular "kieker-examples/JavaEEServletContainerExample/build.gradle"
	assert_file_exists_regular "kieker-examples/JavaEEServletContainerExample/livedemo-source/"
	assert_file_NOT_exists "kieker-examples/JavaEEServletContainerExample/jetty/webapps/jpetstore/WEB-INF/lib/kieker-*.jar"

	assert_file_exists_regular "config/javadoc-header/javadoc.css"
	assert_file_exists_regular "config/javadoc-header/kieker-javadoc-header.png"
	assert_file_exists_regular "kieker-eclipse.importorder"
	assert_file_exists_regular "kieker-eclipse-cleanup.xml"
	assert_file_exists_regular "kieker-eclipse-formatter.xml"
	assert_file_NOT_exists ".project"
	assert_file_NOT_exists ".classpath"
	assert_file_exists_regular ".checkstyle"
	assert_file_exists_regular ".pmd"
	assert_dir_exists ".settings/"
	assert_file_exists_regular "kieker-documentation/README-bin"
	assert_file_exists_regular "kieker-documentation/README-src"
}

# Asserts the existence of files in the bin release and some basic checks on the Kieker jars
function assert_files_exist_bin {
	assert_files_exist_common
	assert_file_exists_regular "doc/kieker-"*"-userguide.pdf"

	echo -n "Making sure (recursively) that 'build' only exists with build/libs/ ..."
	if find | grep "/build/" | grep -v "build/libs"; then
	    exit 1
	fi

	assert_dir_exists "${DIST_JAR_DIR}"
	MAIN_JAR=$(ls "${DIST_JAR_DIR}/kieker-"*".jar" | grep -v emf | grep -v aspectj )
	assert_file_NOT_exists "META-INF/"
	assert_file_exists_regular ${MAIN_JAR}
	assert_file_exists_regular "${DIST_JAR_DIR}/kieker-"*"-aspectj.jar"
	assert_zip_file_content_exist "${DIST_JAR_DIR}/kieker-"*"-aspectj.jar" " org/aspectj"
	assert_zip_file_content_exist "${DIST_JAR_DIR}/kieker-"*"-aspectj.jar" " aj/"
	assert_zip_file_content_contains "${DIST_JAR_DIR}/kieker-"*"-aspectj.jar" "META-INF/MANIFEST.MF" "Premain-Class: org.aspectj.weaver.loadtime.Agent"
	assert_file_exists_regular "${DIST_JAR_DIR}/kieker-"*"-emf.jar"
	assert_zip_file_content_exist "${DIST_JAR_DIR}/kieker-"*"-emf.jar" " org/eclipse/"
	assert_dir_exists "examples/"
	assert_file_exists_regular "examples/kieker.monitoring.example.properties"
	assert_file_exists_regular "examples/kieker.monitoring.adaptiveMonitoring.example.conf"
	assert_file_exists_regular "examples/userguide/ch2--manual-instrumentation/lib/kieker-"*"-emf.jar"
	assert_file_exists_regular "examples/userguide/ch3-4--custom-components/lib/kieker-"*"-emf.jar"
	assert_file_exists_regular "examples/userguide/ch5--trace-monitoring-aspectj/lib/kieker-"*"-aspectj.jar"
	assert_file_exists_regular "examples/userguide/appendix-JMS/lib/kieker-"*"-emf.jar"
	assert_file_exists_regular "examples/userguide/appendix-JMS/lib/commons-logging-"*".jar"
	assert_file_exists_regular "examples/userguide/appendix-Sigar/lib/kieker-"*"-emf.jar"
	assert_file_exists_regular "examples/userguide/appendix-Sigar/lib/sigar-"*".jar"
	assert_file_exists_regular "examples/userguide/appendix-Sigar/lib/libsigar-"*".so"
	assert_file_exists_regular "examples/userguide/appendix-Sigar/lib/sigar-"*".dll"
	assert_file_exists_regular "examples/userguide/appendix-Sigar/lib/sigar-"*".lib"

	assert_file_exists_regular "examples/userguide/appendix-JMS/.classpath"
	assert_file_exists_regular "examples/userguide/ch2--manual-instrumentation/.classpath"
	assert_file_exists_regular "examples/userguide/ch2--bookstore-application/.classpath"
	assert_file_exists_regular "examples/userguide/appendix-Sigar/.classpath"
	assert_file_exists_regular "examples/userguide/ch5--trace-monitoring-aspectj/.classpath"
	assert_file_exists_regular "examples/userguide/ch3-4--custom-components/.classpath"

	assert_file_exists_regular "examples/JavaEEServletContainerExample/build.gradle"
	assert_file_NOT_exists "examples/JavaEEServletContainerExample/livedemo-source/"
	assert_file_exists_regular "examples/JavaEEServletContainerExample/jetty/kieker.monitoring.properties"
	assert_file_exists_regular "examples/JavaEEServletContainerExample/jetty/webapps/jpetstore/WEB-INF/lib/kieker-"*"-aspectj.jar"
	assert_file_exists_regular "examples/JavaEEServletContainerExample/jetty/webapps/jpetstore/WEB-INF/lib/kieker-"*"-aspectj.jar.LICENSE"

	echo "Making sure that for each .project, a '.classpath' and a '.settings/org.eclipse.jdt.core.prefs' exists ..."
	for d in $(find -name ".project" -exec dirname {} \;); do
	    assert_file_exists_regular $d/.classpath
	    assert_file_exists_regular $d/.settings/org.eclipse.jdt.core.prefs
	done
	echo OK

	assert_file_NOT_exists "lib/static-analysis/"
	assert_file_NOT_exists "dist/"
	assert_file_NOT_exists "bin/dev/release-check*"
	assert_file_NOT_exists "doc/userguide/"
	assert_file_NOT_exists "src/"
	assert_file_NOT_exists "test/"
	assert_file_NOT_exists "kieker-eclipse.importorder"
	assert_file_NOT_exists "kieker-eclipse-cleanup.xml"
	assert_file_NOT_exists "kieker-eclipse-formatter.xml"
	assert_file_NOT_exists ".project"
	assert_file_NOT_exists ".classpath"
	assert_file_NOT_exists ".checkstyle"
	assert_file_NOT_exists ".pmd"
	assert_file_NOT_exists ".settings/"
	assert_file_NOT_exists "kieker-documentation/README-bin"
	assert_file_NOT_exists "kieker-documentation/README-src"
}

function check_src_archive {
	if [ -z "$1" ]; then
		echo "No source archive provided"
		exit 1
	fi

    assert_file_exists_regular "$1"
    assert_no_duplicate_files_in_archive "$1"

	echo "Decompressing archive '$1' ..."
	extract_archive_n_cd "$1"
	touch $(basename "$1") # just to mark where this dir comes from

    assert_files_exist_src
	assert_all_sh_scripts_executable
}



function check_bin_archive {
	if [ -z "$1" ]; then
		echo "No source archive provided"
		exit 1
	fi

    assert_file_exists_regular "$1"
    assert_no_duplicate_files_in_archive "$1"

	echo "Decompressing archive '$1' ..."
	extract_archive_n_cd "$1"
	touch $(basename "$1") # just to mark where this dir comes from

    assert_files_exist_bin
	assert_all_sh_scripts_executable
}

##
## "main"
##

aspectjversion="$(grep "libAspectjVersion = " gradle.properties | sed s/.*=.//g)"

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
check_bin_archive ${BINZIP}
rm -rf ${DIR}

change_dir "${BASE_TMP_DIR_ABS}"
create_subdir_n_cd
DIR=$(pwd)
BINTGZ=$(ls ../../${DIST_RELEASE_DIR}/*-binaries.tar.gz)
check_bin_archive ${BINTGZ}
rm -rf ${DIR}


#
## source releases
#
change_dir "${BASE_TMP_DIR_ABS}"
create_subdir_n_cd
DIR=$(pwd)
SRCZIP=$(ls ../../${DIST_RELEASE_DIR}/*-sources.zip)
check_src_archive ${SRCZIP}
rm -rf ${DIR}

change_dir "${BASE_TMP_DIR_ABS}"
create_subdir_n_cd
DIR=$(pwd)
SRCTGZ=$(ls ../../${DIST_RELEASE_DIR}/*-sources.tar.gz)
check_src_archive ${SRCTGZ}
rm -rf ${DIR}
