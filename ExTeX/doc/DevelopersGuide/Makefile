#******************************************************************************
#* $Id: Makefile,v 1.3 2006/01/14 12:53:50 mgn Exp $
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
	rm -f *.ilg *.out *.toc *.aux *.ilg *.log *.dvi *.ind *~ *backup

veryclean distclean: clean
	rm -f $(MANUAL).pdf

#
