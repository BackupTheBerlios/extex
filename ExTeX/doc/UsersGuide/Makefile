#******************************************************************************
#* $Id: Makefile,v 1.6 2005/06/02 08:02:25 gene Exp $
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
	@perl Configurations/analyzeConfig			\
		-cp $(SRCDIR)					\
		-out  Configurations/configurations.tex		\
		-prim Configurations/primitives.tex		\
		$(SRCDIR)/config/*.xml 

deploy: $(MANUAL).pdf
	mkdir -p $(DEPLOYDIR)
	mv $(MANUAL).pdf $(DEPLOYDIR)

clean:
	rm -f *.ilg *.out *.toc *.aux *.ilg *.log *.dvi *.ind *~

veryclean distclean: clean
	rm -f $(MANUAL).pdf

#
