# source this to get your environment set up
# adjust JAVA_HOME and JSDK_HOME to fit your needs

JAVA_HOME=/usr/local/jdk1.3
JSDK_HOME=/home/httpd/classes
WINGS_HOME=/home/hengels/wings

DEVEL_HOME=`pwd`

CLASSPATH=$JAVA_HOME/jre/lib/rt.jar:$JSDK_HOME/servlet-2.0.jar
CLASSPATH=$CLASSPATH:$DEVEL_HOME/src:$DEVEL_HOME/demo:$WINGS_HOME/src

PATH=$PATH:$JAVA_HOME/bin
JAVA=$JAVA_HOME/bin/java

export JAVA JAVA_HOME PATH DEVEL_HOME CLASSPATH
