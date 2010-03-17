SHELL=/bin/bash

DOCBASEFN=paper
TITLEBASEFN=TRTitelA4
TITLE1BASEFN=TRTitelA4-p1

BIBGEN_SH=${HOME}/svn_work/phd_vanhoorn/bin/bibs-gen.sh
ZIPFILE=$(DOCBASEFN).zip
ZIPCONTENT=beamerthemeWarsaw.sty makefile $(DOCBASEFN).tex bibliography.bib $(DOCBASEFN).pdf style.inc.tex figures/*

doitall: clean document

nupslides: document
	pdfnup --nup 2x3 --offset ".25cm .25cm" --delta ".25cm .5cm" --frame true --scale 0.9 $(DOCBASEFN).pdf
	pdfnup --nup 2x2 --offset ".25cm .25cm" --delta ".25cm .5cm" --frame true --scale 0.9 $(DOCBASEFN).pdf

title:
	latex $(TITLEBASEFN)
	dviselect -i $(TITLEBASEFN).dvi -o $(TITLE1BASEFN).dvi 1
	dvips -ta4 -o $(TITLE1BASEFN).ps $(TITLE1BASEFN).dvi
	ps2pdf $(TITLE1BASEFN).ps $(TITLE1BASEFN).pdf

document:
	pdflatex $(DOCBASEFN)
	makeindex $(DOCBASEFN).nlo -s nomencl.ist -o $(DOCBASEFN).nls
#	if [ -x "$(BIBGEN_SH)" ]; then $(BIBGEN_SH) $$(find -name "*.aux")  > bibliography.bib; fi
	bibtex $(DOCBASEFN)
	pdflatex $(DOCBASEFN)
	cp $(DOCBASEFN).bbl $(DOCBASEFN).orig.bbl && cat $(DOCBASEFN).orig.bbl | sed s/"\\\bibitem\[Boskovic"/"\\\pagebreak\\\bibitem[Boskovic"/g > $(DOCBASEFN).bbl 
	pdflatex $(DOCBASEFN)
#	latex -src-specials $(DOCBASEFN)

zip:	document
	if [ -f $(ZIPFILE) ]; then rm $(ZIPFILE); fi
	zip $(ZIPFILE) $(ZIPCONTENT)

clean:
	for f in aux bbl blg glo idx log toc snm out nav bak vrb backup; do \
	if [ -z "$$(ls *.$$f)" ]; then true; else rm *.$$f; fi \
	done