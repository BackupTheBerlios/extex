#!/bin/bash
#--------------------------------------------------------------------
# (c) 2003-2006 Gerd Neugebauer (gene@gerd-neugebauer.de)
#
#--------------------------------------------------------------------
# LOCALDIR contains the lcation where the build resides.
# Here all temporary files are created.
LOCALDIR="$HOME/build"
#--------------------------------------------------------------------
# Point to the Java SDK
export JAVA_HOME=$HOME/local/share/j2sdk1.4.2_09/
#--------------------------------------------------------------------
#
CVSDIR=":pserver:anonymous@cvs.extex.berlios.de:/cvsroot/extex"
#--------------------------------------------------------------------
#
MODULES="ExTeX"
#--------------------------------------------------------------------
#
LOG=$LOCALDIR/log
#--------------------------------------------------------------------
# 
export PATH=${JAVA_HOME}/bin:$PATH

mkdir -p $LOCALDIR
mkdir -p $LOG

cd $LOCALDIR
date >$LOG/export.log
cvs -q -r -d $CVSDIR checkout -P $MODULES >>$LOG/export.log 2>&1
cvs -q -r -d $CVSDIR update -P $MODULES >>$LOG/export.log 2>&1


source $LOCALDIR/ExTeX/util/Build/build.sh

#
