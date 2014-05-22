#!/bin/bash

# Script that either starts or stops Rserve
case "$1" in
    start)
	echo -n "Trying to start Rserve..."
	R CMD Rserve --no-save --vanilla &
	#R CMD Rserve.dbg --no-save --vanilla > /tmp/rserve.dbg.log &
	echo "done."
	exit 0
        ;;
    stop)
	echo -n "Trying to stop Rserve..."
	pkill Rserve
	echo "done."
	exit 0
        ;;
    *)
        ## If no parameters are given, print which are avaiable.
        echo "Usage: $0 {start|stop}"
        exit 1
        ;;
esac
