# source this to get your environment set up
# adjust JAVA_HOME and JSDK_HOME to fit your needs

JAVA_HOME=/usr/java/jdk1.3
J2EE_HOME=/usr/j2sdkee1.2.1

DEVEL_HOME=`pwd`

CLASSPATH=$JAVA_HOME/jre/lib/rt.jar:$J2EE_HOME/lib/j2ee.jar
CLASSPATH=$CLASSPATH:$DEVEL_HOME/src:$DEVEL_HOME/demo

PATH=$PATH:$JAVA_HOME/bin
JAVA=$JAVA_HOME/bin/java

export JAVA JAVA_HOME PATH DEVEL_HOME CLASSPATH
