export EXTEXPATH=`pwd`
export PATH=$EXTEXPATH/util:$PATH

# ins Verzeichnis wechseln
cd $EXTEXPATH

# CLASSPATH erweitern
export CLASSPATH=.:/usr/lib/java/lib/tools.jar:bin/:src/de/dante/extex/config/

# jar-Dateien
for i in $EXTEXPATH/lib/*.jar ; do
        export CLASSPATH=$CLASSPATH:$i
done

# LD_LIBRARY_PATH setzen
#export LD_LIBRARY_PATH=$EXTEXPATH/lib:$LD_LIBRARY_PATH

xterm &
