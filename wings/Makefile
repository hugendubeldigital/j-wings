#
# $Id$
#
# see Rules.make
##

WINGS_DIR=wings-`cat RELEASE`
WINGS_DEVEL_DIR=wings-devel-`cat RELEASE`

fun: 

% :
	make -C src $@
	make -C demo $@

javadoc: dummy
	rm -rf doc/api ; mkdir doc/api ; \
	${JAVA_HOME}/bin/javadoc -d doc/api -windowtitle 'wingS API' `find . -name "*.java" -exec dirname {} \; | sort | uniq | sed 's/\.\/\(src\/\|demo\/\)\?//g' | sed 's/\//./g'`

jar: dummy
	rm wings.jar ; cd src ; \
	${JAVA_HOME}/bin/jar cvf ../wings.jar `find . -name "*.class" -o -name "*.gif" -o -name "*.css"`

demojar: dummy
	rm wings-demo.jar ; cd demo ; \
	${JAVA_HOME}/bin/jar cvf ../wings-demo.jar `find . -name "*.class" -o -name "*.gif" -o -name "*.css"`

htdocs: dummy javadoc
	cd htdocs ;\
	ln -s ../doc/api .

dist: fun javadoc jar demojar
	mkdir dist
	mkdir dist/lib
	mkdir dist/doc
	mkdir dist/demo
	mkdir dist/src
	cp wings.jar dist/lib
	cp wings-demo.jar dist/lib
	cp Makefile dist
	cp Rules.make dist
	cp Environment.sh dist
	cd ${DEVEL_HOME}/dist/doc ;\
	ln -s ../../htdocs/faq.html . ;\
	ln -s ../../htdocs/styles.css . ;\
	ln -s ../../doc/api .
	cp -r src/* ${DEVEL_HOME}/dist/src
	make -C dist/src cvs-clean
	cp -r demo/* ${DEVEL_HOME}/dist/demo
	make -C dist/demo cvs-clean
	ln -s dist ${WINGS_DIR}
	ln -s dist ${WINGS_DEVEL_DIR}
	tar -hcvf ${WINGS_DEVEL_DIR}.tar ${WINGS_DEVEL_DIR}
	tar -hcvf ${WINGS_DIR}.tar ${WINGS_DIR}
	tar --delete -v -f ${WINGS_DIR}.tar ${WINGS_DIR}/src
	rm ${WINGS_DIR} ${WINGS_DEVEL_DIR}
	gzip ${WINGS_DIR}.tar ${WINGS_DEVEL_DIR}.tar

distclean:
	rm -rf dist
	rm ${WINGS_DIR}.tar.gz ${WINGS_DEVEL_DIR}.tar.gz

