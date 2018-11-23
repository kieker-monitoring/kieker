#!/usr/bin/env bash

BASE_TMP_DIR="$(dirname $0)/../../build/"
DIST_RELEASE_DIR="build/distributions/"
DIST_JAR_DIR="build/libs/"

export RED='\033[1;31m'
export WHITE='\033[1;37m'
export YELLOW='\033[1;33m'
export NC='\033[0m'

if [ "$TERM" == "xterm-256color" ]; then
	# interactive shell - use colors
	export ERROR="${RED}[error]${NC}"
	export WARNING="${YELLOW}[warning]${NC}"
	export INFO="${WHITE}[info]${NC}"
else
        # not interactive shell
	export ERROR="[error]"
	export WARNING="[warning]"
	export INFO="[info]"
fi

function error() {
	echo -e "${ERROR} $@"
}

function warning() {
	echo -e "${WARNING} $@"
}

function information() {
	echo -e "${INFO} $@"
}

function change_dir {
	information "Changing dir to $1 ..."
	if ! cd ${1}; then
		error "Failed to cd to '${1}'"
		exit 1
	fi
	information "Current directory: $(pwd)"
}

# create tmp subdir in the current directory and change to it
function create_subdir_n_cd {
	TMPDIR=$(mktemp -d --tmpdir="$(pwd)")
	information "Created temp dir '${TMPDIR}'"
	change_dir "${TMPDIR}"
}

function assert_dir_exists {
	information "Asserting '$1' is a directory ..."
	if ! test -d "$1"; then
		error "Directory '$1' is missing or not a directory"
		exit 1
	fi
	information OK
}

function assert_file_exists_regular {
	information "Asserting '$1' is a regular file ..."
	if ! test -s "$1"; then
		error "File '$1' is missing or not a regular file"
		exit 1
	fi
	information OK
}

function assert_dir_NOT_exists {
	information "Asserting '$1' is a directory ..."
	if test -d "$1"; then
		error "Directory '$1' should not exist"
		exit 1
	fi
	information OK
}

function assert_file_NOT_exists {
	information "Asserting file '$1' does not exist ..."
	if test -e "$1"; then
		error "File '$1' should not exist"
		exit 1
	fi
	information OK
}

# extract archive
function extract_archive {
	if [ -z "$1" ]; then
		error "No archive provided"
		exit 1
	fi

	if echo "$1" | grep "zip"; then
		unzip -q "$1"
	elif echo "$1" | grep "tar.gz"; then
		tar -xzf "$1"
	else
		error "Archive '$1' is neither zip nor .tar.gz"
		exit 1
	fi
}

# extract archive and change into directory
function extract_archive_n_cd {
	extract_archive $1

	change_dir kieker-*
}
