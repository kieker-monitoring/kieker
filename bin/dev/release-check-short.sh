#!/usr/bin/env bash

# include common variables and functions
source "$(dirname $0)/release-check-common.sh"

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


##
## "main"
##

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
assert_file_exists_regular ${BINZIP}
assert_no_duplicate_files_in_archive ${BINZIP}

BINTGZ=$(ls ../../${DIST_RELEASE_DIR}/*-binaries.tar.gz)
assert_file_exists_regular ${BINTGZ}
assert_no_duplicate_files_in_archive ${BINTGZ}

rm -rf ${DIR}

#
## source releases
#
change_dir "${BASE_TMP_DIR_ABS}"
create_subdir_n_cd
DIR=$(pwd)

SRCZIP=$(ls ../../${DIST_RELEASE_DIR}/*-sources.zip)
assert_file_exists_regular ${SRCZIP}
assert_no_duplicate_files_in_archive ${SRCZIP}

SRCTGZ=$(ls ../../${DIST_RELEASE_DIR}/*-sources.tar.gz)
assert_file_exists_regular ${SRCTGZ}
assert_no_duplicate_files_in_archive ${SRCTGZ}

rm -rf ${DIR}