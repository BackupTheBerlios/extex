#******************************************************************************
#* $Id: Makefile,v 1.7 2005/06/06 08:06:56 gene Exp $
#******************************************************************************
#* Author: Gerd Neugebauer
#*=============================================================================

MANUAL = extex-users
DEPLOYDIR = ../../www/src/documentation/man
SRCDIR = ../../src/java

all: $(MANUAL).pdf

extex-users.pdf: cfg $(MANUAL).tex

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

Configurations/configurations.tex cfg:
	@perl bin/analyzeConfig				\
		-cp   $(SRCDIR)					\
		-out  tmp/configurations.tex	\
		-prim tmp/primitives.tex		\
		$(SRCDIR)/config/*.xml 

deploy: $(MANUAL).pdf
	mkdir -p $(DEPLOYDIR)
	mv $(MANUAL).pdf $(DEPLOYDIR)

clean:
	rm -f *.ilg *.out *.toc *.aux *.ilg *.log *.dvi *.ind *~

veryclean distclean: clean
	rm -f $(MANUAL).pdf

#
