# fuer die sh oder bash
##
# dies file einfach sourcen
# . ./Environment.sh
##
# Dies file sollte in dem Development-Root
# aufgerufen werden (.. da wo das Rules.make steht)
# .. sonst evtl. noch eigene Anpassungen

JAVALIB=/usr/local/share/java/lib
DEVEL_HOME=`pwd`

if [ ! -x $JAVA_HOME/bin/java ] ; then
	for d in  /usr/local/jdk1.2.2 \
		  /usr/local/lib/jdk/1.2.2 \
		  /usr/local/java \
		  /usr/java ;
       	do
		if [ -x $d/bin/java ] ; then
			JAVA_HOME=$d
			break
		fi
	done
fi

CLASSPATH=$JAVA_HOME/jre/lib/rt.jar:$JAVA_HOME/lib/dt.jar:$DEVEL_HOME/src:$DEVEL_HOME/demo

for f in $JAVALIB/*.jar ; do
    CLASSPATH=$CLASSPATH:$f
done

export DEVEL_HOME JAVA_HOME CLASSPATH
