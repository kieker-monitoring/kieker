#!/bin/bash

BASE_TMP_DIR="$(dirname $0)/../../tmp/"

function change_dir {
    echo "Changing dir to $1 ..."
    if ! cd ${1}; then
	echo "Failed to cd to '${1}'"
	exit 1
    fi
    echo "Current directory: $(pwd)"
}

# create tmp subdir in the current directory and change to it
function create_subdir_n_cd {
    TMPDIR=$(mktemp -d --tmpdir="$(pwd)")
    echo "Created temp dir '${TMPDIR}'"
    change_dir "${TMPDIR}"
}

# build with ant (target may be passed as $1)
function run_ant {
    if ! ant $1; then
	echo "Build failed"
	exit 1
    fi
}

# make sure that license for each jar included

# check byte-code version of jars

# make sure important files included

# assert expected number of files in dist/release

# assert expected number of jars in dist/

# assert that given string $1 contains expression $2 (regexp allowed)
function assert_string_contains {
    if ! (echo -n "$1" | grep -q "$2"); then
	echo "Expression '$2' not found in string"
	exit 1
    fi
}

# provide content list of archive
function print_archive_contents {
    if [ -z "$1" ]; then
	echo "No archive provided"
	exit 1
    fi
    
    if echo "$1" | grep "zip"; then
	unzip -l "$1" | awk '{ print $4 }'
	return
    fi

    if echo "$1" | grep "tar.gz"; then
	tar -tzf "$1"
	return
    fi
}

# extract archiv and change into directory
function extract_archive_n_cd {
    if [ -z "$1" ]; then
	echo "No archive provided"
	exit 1
    fi

    if echo "$1" | grep "zip"; then
	unzip -q "$1" 
    elif echo "$1" | grep "tar.gz"; then
	tar -xzf "$1"
    else
	echo "Archive '$1' is neither zip nor .tar.gz"
	exit 1
    fi 

    change_dir kieker-*
}

function assert_file_NOT_exists {
    echo -n "Asserting file '$1' does not exist ..."
    if test -e "$1"; then
	echo "File '$1' should not exist"
	exit 1
    fi
    echo OK
}

function assert_dir_exists {
    echo -n "Asserting '$1' is a directory ..."
    if ! test -d "$1"; then
	echo "File '$1' is missing or not a directory"
	exit 1
    fi
    echo OK
}

function assert_file_exists_regular {
    echo -n "Asserting '$1' is a regular file ..."
    if ! test -s "$1"; then
	echo "File '$1' is missing or not a regular file"
	exit 1
    fi
    echo OK
}

# Asserts the existence of files common to the src and bin releases
function assert_files_exist_common {
    assert_dir_exists "bin/"
    assert_dir_exists "doc/"
    assert_dir_exists "examples/"
    assert_dir_exists "lib/"
    assert_file_exists_regular "HISTORY"
    assert_file_exists_regular "LICENSE"
    assert_file_NOT_exists "build/"
    assert_file_NOT_exists "build-eclipse/"
    assert_file_NOT_exists "tmp/"
    assert_file_NOT_exists ".git/"
    assert_file_NOT_exists ".gitignore/"
    # check if LICENSE file for each jar
    for jar in $(find lib/ -name "*.jar"); do
	if (echo "$jar" | grep "noUpdateChecks"); then
	    continue # exclude  noUpdateChecks
	fi
	JAR_BASE=$(echo ${jar} | sed 's/\(.*\)\..*/\1/') # remove file extension
	assert_file_exists_regular "${JAR_BASE}.LICENSE"
    done
}

# Asserts the existence of files common to the src and bin releases
function assert_files_exist_src {
    assert_files_exist_common
    assert_dir_exists "model/"
    assert_dir_exists "src/"
    assert_dir_exists "src-gen/"
    assert_dir_exists "test/"
    assert_file_NOT_exists "dist/"
    assert_file_NOT_exists "META-INF/"
    assert_file_exists_regular "kieker-eclipse-cleanup.xml"
    assert_file_exists_regular "kieker-eclipse-formatter.xml"
    assert_file_exists_regular ".project"
    assert_file_exists_regular ".classpath"
}

# Asserts the existence of files common to the src and bin releases
function assert_files_exist_bin {
    assert_files_exist_common
    assert_file_exists_regular "doc/kieker-"*"_userguide.pdf"
    assert_dir_exists "dist/"
    assert_file_exists_regular $(ls "dist/kieker-"*".jar" | grep -v emf | grep -v aspectj ) # the core jar
    assert_file_exists_regular "dist/kieker-"*"_aspectj.jar"
    assert_file_exists_regular "dist/kieker-"*"_emf.jar"
    assert_file_NOT_exists "dist/release/"
    assert_file_NOT_exists "bin/dev/"
    assert_file_NOT_exists "doc/userguide/"
    assert_file_NOT_exists "model/"
}

function check_src_archive {
    if [ -z "$1" ]; then
	echo "No source archive provided"
	exit 1
    fi

    echo "Checking source archives '$1' ..."

    echo "Checking archive contents of '$1' ..."
    ARCHIVE_CONTENT=$(print_archive_contents "$1")
    # TODO: do so more with the ${ARCHIVE_CONTENT} than:
    assert_string_contains "${ARCHIVE_CONTENT}" "HISTORY"

    echo "Decompressing archive '$1' ..."
    extract_archive_n_cd "$1"
    touch $(basename "$1") # just to mark where this dir comes from

    assert_files_exist_src

    # now compile sources
    run_ant run-tests-junit
}

function check_bin_archive {
    if [ -z "$1" ]; then
	echo "No source archive provided"
	exit 1
    fi

    echo "Checking source archives '$1' ..."

    echo "Checking archive contents of '$1' ..."
    ARCHIVE_CONTENT=$(print_archive_contents "$1")
    # TODO: do so more with the ${ARCHIVE_CONTENT} than:
    assert_string_contains "${ARCHIVE_CONTENT}" "HISTORY"

    echo "Decompressing archive '$1' ..."
    extract_archive_n_cd "$1"
    touch $(basename "$1") # just to mark where this dir comes from

    assert_files_exist_bin
}

##
## "main" 
##
change_dir "${BASE_TMP_DIR}"
BASE_TMP_DIR_ABS=$(pwd)

change_dir "${BASE_TMP_DIR_ABS}"
create_subdir_n_cd
SRCZIP=$(ls ../../dist/release/*_sources.zip)
check_src_archive "${SRCZIP}"

change_dir "${BASE_TMP_DIR_ABS}"
create_subdir_n_cd
SRCTGZ=$(ls ../../dist/release/*_sources.tar.gz)
check_src_archive "${SRCTGZ}"

change_dir "${BASE_TMP_DIR_ABS}"
create_subdir_n_cd
BINZIP=$(ls ../../dist/release/*_binaries.zip)
check_bin_archive "${BINZIP}"

change_dir "${BASE_TMP_DIR_ABS}"
create_subdir_n_cd
BINTGZ=$(ls ../../dist/release/*_binaries.tar.gz)
check_bin_archive "${BINTGZ}"

# TOOD: check contents of remaining archives



