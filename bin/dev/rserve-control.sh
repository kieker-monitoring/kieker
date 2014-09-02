#!/bin/bash

# Script that either starts or stops Rserve

case "$1" in
    start)
	echo -n "Trying to start Rserve..."
	R CMD Rserve --vanilla &
	#R CMD Rserve.dbg --vanilla > /tmp/rserve.dbg.log &
	RET=$?
	if [ $RET = 0 ] && (ps ax | grep -i "rserve --no-save" | grep -qv grep); then
		echo "done."
	else
		echo "failed (exit status >0 or Rserve could not be started)."
	fi
	exit 1
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
