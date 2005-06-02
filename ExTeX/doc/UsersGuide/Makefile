#******************************************************************************
#* $Id: Makefile,v 1.5 2005/06/02 06:41:50 gene Exp $
#******************************************************************************
#* Author: Gerd Neugebauer
#*=============================================================================


all: extex-users.pdf

extex-users.pdf: cfg extex-users.tex

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
		-cp ../../src/java				\
		-o Configurations/configurations.tex		\
		-prim Configurations/primitives.tex		\
		../../src/java/config/*.xml 

clean:
	rm *.ilg *.out *.toc *.aux *.ilg *.log *.dvi *.ind

veryclean distclean: clean
	rm extex-users.pdf

#
