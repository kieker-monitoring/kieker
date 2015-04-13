#!/bin/bash

# Script that either starts or stops Rserve

BINDIR=$(cd "$(dirname "$0")"; pwd)/

case "$1" in
    start)
	echo -n "Trying to start Rserve..."
	R CMD BATCH "${BINDIR}/RserveStart.R" RserveStart.log --vanilla &
	RET=$?
	if [ $RET = 0 ] && (ps ax | grep -i "rserve --slave" | grep -qv grep); then
		echo "done."
		exit 0
	else
		echo "failed (exit status >0 or Rserve could not be started)."
		exit 1
	fi
        ;;
    stop)
	echo -n "Trying to stop Rserve..."
	if pkill Rserve; then
	    echo "done."
	else 
	    echo "failed"
	    exit 1
	fi
        ;;
    *)
        ## If no parameters are given, print which are available.
        echo "Usage: $0 {start|stop}"
        exit 0
        ;;
esac
