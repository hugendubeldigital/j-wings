#
# $Id$
#
# see Rules.make
##

fun: 

% :
	make -C src $@
	make -C demo $@

doc: dummy
	rm -rf doc/api ; mkdir doc/api ; \
	${JAVA_HOME}/bin/javadoc -d doc/api -windowtitle 'wingS API' `find . -name "*.java" -exec dirname {} \; | sort | uniq | sed 's/\.\/\(src\/\|demo\/\)\?//g' | sed 's/\//./g'`

jar: dummy
	rm wings.jar ; cd src ; \
	${JAVA_HOME}/bin/jar cvf ../wings.jar `find org -name "*.class" -o -name "*.gif" -o -name "*.css"`

