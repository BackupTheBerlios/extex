#******************************************************************************
#* $Id: Makefile,v 1.2 2003/12/29 11:39:34 mgn Exp $
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
