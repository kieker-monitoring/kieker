#!/bin/bash

# Script that either starts or stops Rserve

case "$1" in
    start)
	echo -n "Trying to start Rserve..."
	R CMD Rserve --vanilla &
	#R CMD Rserve.dbg --vanilla > /tmp/rserve.dbg.log &
	RET=$?
	if [ $RET = 0 ]; then
		echo "done."
	else
		echo "failed (exit status: $RET)."
	fi
	exit $RET
        ;;
    stop)
	echo -n "Trying to stop Rserve..."
	pkill Rserve
	echo "done."
	exit 0
        ;;
    *)
        ## If no parameters are given, print which are available.
        echo "Usage: $0 {start|stop}"
        exit 0
        ;;
esac
