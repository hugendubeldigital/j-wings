#!/bin/sh

if [ "$JAVA_HOME" == "" ]
then
  echo "please set \$JAVA_HOME"
  exit
fi

## resolve links - $0 may be a link
PRG=$0
progname=`basename $0`

while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '.*/.*' > /dev/null; then
      PRG="$link"
  else
      PRG="`dirname $PRG`/$link"
  fi
done

if [ "$WINGS_HOME" == "" ]
then
  export WINGS_HOME=`dirname "$PRG"`/..
fi

CLASSPATH=.:$WINGS_HOME/build/class
for f in $WINGS_HOME/lib/*.jar
do
  CLASSPATH=$CLASSPATH:$f
done
export CLASSPATH

echo $CLASSPATH
$JAVA_HOME/bin/java org.wings.recorder.Simulator $@
