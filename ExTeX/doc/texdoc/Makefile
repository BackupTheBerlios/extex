#******************************************************************************
#* $Id: Makefile,v 1.1 2003/12/28 00:03:17 gene Exp $
#******************************************************************************
#* Author: Gerd Neugebauer
#*=============================================================================

OPT    =  -v
TEXTEX = /home/TeX/knuth/tex/tex.tex

all:
	perl texdoc $(OPT) $(TEXTEX)

ci:
	ci -l texdoc Makefile

clean:
	rm -f TeX_*.html *~ *.bak
