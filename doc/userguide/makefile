SHELL=/bin/bash

DOCBASEFN=kieker-userguide
BIBGEN_SH=../../../../../../phd_vanhoorn/bin/bibs-gen.sh


doitall: clean document

document:
#	java -jar make-libraries.jar libraries_descriptions ../../lib/ Libraries.tex
#	./make-libraries.sh
	pdflatex $(DOCBASEFN)
#	makeindex $(DOCBASEFN)
#	if [ -x "$(BIBGEN_SH)" ]; then $(BIBGEN_SH) $$(find -name "*.aux")  > bibliography.bib; fi
	bibtex $(DOCBASEFN)
#	makeindex -s sprmindx.sty $(DOCBASEFN).idx
#	makeindex $(DOCBASEFN).nlo -s nomencl.ist -o $(DOCBASEFN).nls
#	makeindex $(DOCBASEFN).idx
#	bibtex $(DOCBASEFN)
#	for f in $$(ls | grep -E "bu[[:digit:]]+\.aux"); do \
#	if ! grep -q "\\citation" $$f; then continue; fi; \
#	bibtex $$f; \
##	if test $$? -ne 0;  then exit 1; fi \
#	done
	pdflatex $(DOCBASEFN)
	pdflatex $(DOCBASEFN)

clean:
	for f in aux bbl blg glo idx log toc snm out nav bak vrb backup; do \
	rm -f *.$$f ; \
	done
