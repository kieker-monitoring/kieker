export RED='\033[1;31m'
export WHITE='\033[1;37m'
export YELLOW='\033[1;33m'
export NC='\033[0m'

export ERROR="${RED}[error]${NC}"
export WARNING="${YELLOW}[warning]${NC}"
export INFO="${WHITE}[info]${NC}"


function error() {
	echo -e "${ERROR} $@"
}

function warning() {
	echo -e "${WARNING} $@"
}

function information() {
	echo -e "${INFO} $@"
}

# $1 = NAME, $2 = EXECUTABLE
function checkExecutable() {
	if [ "$2" == "" ] ; then
		error "$1 variable for executable not set."
		exit 1
	fi
	if [ ! -x "$2" ] ; then
		error "$1 not found at: $2"
		exit 1
	fi
}

# $1 = NAME, $2 = FILE
function checkFile() {
	if [ "$2" == "" ] ; then
		error "$1 variable for file not set."
		exit 1
	fi
	if [ ! -f "$2" ] ; then
		error "$1 not found at: $2"
		exit 1
	fi
}

# $1 = NAME, $2 = FILE
function checkDirectory() {
	if [ "$2" == "" ] ; then
		error "$1 directory variable not set."
		exit 1
	fi
	if [ ! -d "$2" ] ; then
		if [ "$3" == "create" ] ; then
			information "$1: directory does not exist, creating it"
		else
			error "$1: directory $2 does not exist."
			exit 1
		fi
	fi
}

