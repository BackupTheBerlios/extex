#******************************************************************************
#* $Id: Makefile,v 1.11 2005/06/20 10:31:05 gene Exp $
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
	perl bin/primitives.pl				\
		-cp   $(SRCDIR)				\
		-sets tmp/configurations.tex		\
		-prim tmp/primitives.tex		\
		$(SRCDIR)/config/*.xml 

tmp/syntax.tex syntax: bin/syntax.pl
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
