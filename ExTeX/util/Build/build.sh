#!/bin/bash
#--------------------------------------------------------------------
# (c) 2003-2006 Gerd Neugebauer (gene@gerd-neugebauer.de)
#

if [ "$LOCALDIR" == "" ]; then
    echo "LOCALDIR is undefined"
    exit 1
fi
if [ "$LOG" == "" ]; then
    echo "LOG is undefined"
    exit 1
fi
if [ "$JAVA_HOME" == "" ]; then
    echo "JAVA_HOME is undefined"
    exit 1
fi
if [ "$INSTALLDIR" == "" ]; then
    echo "INSTALLDIR is undefined"
    exit 1
fi

#--------------------------------------------------------------------
#
export PATH=${JAVA_HOME}/bin:$PATH

mkdir -p $LOCALDIR $LOG $INSTALLDIR $INSTALLDIR/snapshot

#--------------------------------------------------------------------
#
cd $LOCALDIR/ExTeX
echo > .extex-test<<EOF
#
texmf.path=/usr/share/texmf-tetex
#
EOF
date >$LOG/installer.log
sh build installer >>$LOG/installer.log 2>&1
if [ -e target/ExTeX-setup.jar ]
then
#    scp -q target/ExTeX-setup.jar $INSTALLDIR/snapshot
    cp target/ExTeX-setup.jar $INSTALLDIR/snapshot
else
    cat $LOG/installer.log
fi


#--------------------------------------------------------------------
#
cd $LOCALDIR/ExTeX
date >$LOG/javadoc.log
sh build javadoc >>$LOG/javadoc.log 2>&1
#scp -q -r target/javadoc $INSTALLDIR/snapshot
cp -r target/javadoc $INSTALLDIR/snapshot
#cp -r target/javadoc ~/public_html/


#--------------------------------------------------------------------
#
cd $LOCALDIR/ExTeX
date >$LOG/test.log
sh build clean junitreport >> $LOG/test.log 2>&1
cp -r target/www/reports $INSTALLDIR/
#cp -r target/www/reports ~/public_html/


#--------------------------------------------------------------------
#
cd $LOCALDIR/ExTeX/doc/DevelopersGuide
date >$LOG/doc.log
make >> $LOG/doc.log 2>&1
cd $LOCALDIR/ExTeX/doc/UsersGuide
make >> $LOG/doc.log 2>&1


#--------------------------------------------------------------------
#
date >$LOG/www.log
cd $LOCALDIR/ExTeX/www
make >>$LOG/www.log 2>&1
#scp -q -r ../target/www/* $INSTALLDIR/
cp -r ../target/www/* $INSTALLDIR/


#
