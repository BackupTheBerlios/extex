#******************************************************************************
#* $Id: Makefile,v 1.2 2005/09/09 09:43:06 gene Exp $
#******************************************************************************
#* Author: Gerd Neugebauer
#*=============================================================================

MANUAL = extex-developers
DEPLOYDIR = ../../www/src/documentation/man
SRCDIR = ../../src/java

all: $(MANUAL).pdf

$(MANUAL).pdf:  $(MANUAL).tex howto $(MANUAL).toc
	@pdflatex $(MANUAL)

%.pdf: %.toc %.ind
	@pdflatex $*
	@pdflatex $*
	@rm $*.out

%.ind: %.idx

#	@makeindex -s doc.ist $*

%.idx: %.tex
	@pdflatex $*

%.toc: %.tex
	@pdflatex $*

deploy: $(MANUAL).pdf
	mkdir -p $(DEPLOYDIR)
	mv $(MANUAL).pdf $(DEPLOYDIR)

howto tmp/howto.tex:
	mkdir -p tmp
	perl bin/howto.pl -cp ../../src/java -out tmp/howto.tex

clean:
	rm -f *.ilg *.out *.toc *.aux *.ilg *.log *.dvi *.ind *~

veryclean distclean: clean
	rm -f $(MANUAL).pdf

#
