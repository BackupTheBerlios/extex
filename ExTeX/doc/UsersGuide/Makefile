#******************************************************************************
#* $Id: Makefile,v 1.12 2006/10/13 13:32:19 gene Exp $
#******************************************************************************
#* Author: Gerd Neugebauer
#*=============================================================================

MANUAL = extex-users
DEPLOYDIR = ../../www/src/documentation/man
SRCDIR = ../../src/java

all: $(MANUAL).pdf

extex-users.pdf: cfg syntax $(MANUAL).tex

%.pdf: %.toc %.ind
	@pdflatex $*
	@pdflatex $*
	@rm $*.out

%.ind: %.idx
	@makeindex -s doc.ist $*

%.idx: %.tex
	@pdflatex $*

%.toc: %.tex
	@pdflatex $*

tmp/configurations.tex cfg: bin/primitives.pl
	mkdir -p tmp
	perl bin/primitives.pl				\
		-cp   $(SRCDIR)				\
		-sets tmp/configurations.tex		\
		-prim tmp/primitives.tex		\
		$(SRCDIR)/config/*.xml 

tmp/syntax.tex syntax: bin/syntax.pl
	mkdir -p tmp
	perl bin/syntax.pl				\
		-cp   $(SRCDIR)				\
		-out  tmp/syntax.tex

deploy: $(MANUAL).pdf
	mkdir -p $(DEPLOYDIR)
	mv $(MANUAL).pdf $(DEPLOYDIR)

clean:
	rm -f *.ilg *.out *.toc *.aux *.ilg *.log *.dvi *.ind *~

veryclean distclean: clean
	rm -f $(MANUAL).pdf

#
