#******************************************************************************
#* $Id: Makefile,v 1.3 2003/12/30 08:35:59 gene Exp $
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
