SHELL=/bin/bash

DOCBASEFN=kieker-developerguide

doitall: clean document

document:
	pdflatex $(DOCBASEFN)
#	makeindex $(DOCBASEFN)
#	bibtex $(DOCBASEFN)
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
	if [ -z "$$(ls *.$$f)" ]; then true; else rm *.$$f; fi \
	done