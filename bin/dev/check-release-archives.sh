#!/bin/bash

BASE_TMP_DIR="$(dirname $0)/../../tmp/"

# create tmp subdir in the current directory ant return name
function create_subdir {
    mktemp -d --tmpdir=.
}

# build with ant
function run_ant {
    if ! ant; then
	echo "Build failed"
	exit 1
    fi
}

# make sure that license for each jar included

# check byte-code version of jars

# make sure important files included

# assert expected number of files in dist/release

# assert expected number of jars in dist

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

function check_src_archive {
    if [ -z "$1" ]; then
	echo "No source archive provided"
	exit 1
    fi

    echo "Checking source archives '$1' ..."

    echo "Checking archive contents of '$1' ..."
    ARCHIVE_CONTENT=$(print_archive_contents "$1")
    assert_string_contains "${ARCHIVE_CONTENT}" "HISTORY"
    # TODO: more files

    # TODO: assert string does NOT contain (e.g., ~ etc.)

    # TODO: assert LICENSE for each jar in lib/**/
}

function check_bin_archive {
    if [ -z "$1" ]; then
	echo "No source archive provided"
	exit 1
    fi

    echo "Checking source archives '$1' ..."

    echo "Checking archive contents of '$1' ..."
    ARCHIVE_CONTENT=$(print_archive_contents "$1")
    assert_string_contains "${ARCHIVE_CONTENT}" "HISTORY"
    # TODO: more files

    # TODO: assert string does NOT contain (e.g., ~ etc.)

    # TODO: assert LICENSE for each jar in lib/**/
}

##
## "main" 
##
if ! cd ${BASE_TMP_DIR}; then
    echo "Failed to cd to '${BASE_TMP_DIR}'"
    exit 1
fi
echo "Current directory: $(pwd)"

SRCZIP=$(ls ../dist/release/*_sources.zip)
check_src_archive "${SRCZIP}"
SRCTGZ=$(ls ../dist/release/*_sources.tar.gz)
check_src_archive "${SRCTGZ}"

BINZIP=$(ls ../dist/release/*_binaries.zip)
check_bin_archive "${BINZIP}"
BINTGZ=$(ls ../dist/release/*_binaries.tar.gz)
check_bin_archive "${BINZIP}"

# TOOD: example archives



