#!/bin/bash

for f in *.dat; do 
    (cat $f \
	| sed 's/\(Catalog.*\)\(pc-vanhoorn\)/\1websrv1/g' \
	| sed 's/\(CRM.*\)\(pc-vanhoorn\)/\1websrv1/g' \
	| sed 's/\(Bookstore.*\)\(pc-vanhoorn\)/\1apprv1/g' \
	> $f.original) \
	&& \
	cp $f.original $f
done 