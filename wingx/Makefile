#
# $Id$
#
# see Rules.make
##

WINGX_DIR=wingx-`cat RELEASE`
WINGX_DEVEL_DIR=wingx-devel-`cat RELEASE`

fun: 

% :
	make -C src $@
	make -C demo $@

doc: dummy
	rm -rf doc/api ; mkdir doc/api ; \
	${JAVA_HOME}/bin/javadoc -d doc/api -windowtitle 'wingS eXtensions API' `find . -name "*.java" -exec dirname {} \; | sort | uniq | sed 's/\.\/\(src\/\|demo\/\)\?//g' | sed 's/\//./g'`

jar: dummy
	rm wingx.jar ; cd src ; \
	${JAVA_HOME}/bin/jar cvf ../wingx.jar `find . -name "*.class" -o -name "*.gif" -o -name "*.css"`

demojar: dummy
	rm wingx-demo.jar ; cd demo ; \
	${JAVA_HOME}/bin/jar cvf ../wingx-demo.jar `find . -name "*.class" -o -name "*.gif" -o -name "*.css"`

demo: dummy
	rm -rf demo/tomcat ;\
	mkdir demo/tomcat ;\
	mkdir demo/tomcat/WEB-INF ;\
	mkdir demo/tomcat/WEB-INF/classes ;\
	mkdir demo/tomcat/WEB-INF/lib ;\
	cp ${WINGS_HOME}/wings.jar demo/tomcat/WEB-INF/lib ;\
	cp wingx.jar demo/tomcat/WEB-INF/lib ;\
	cd demo ;\
	cp -P `find beaneditor iframe -name "*.class" -o -name "*.gif" -o -name "*.css" -o -name "*.thtml"` ../demo/tomcat/WEB-INF/classes ;\
	cd .. \;
	cp demo/demo.xml demo/tomcat/WEB-INF/web.xml

htdocs: dummy doc dist
	cd htdocs
	ln -s ../COPYING .
	ln -s ../doc/api

dist: fun doc jar demojar
	mkdir dist
	mkdir dist/lib
	mkdir dist/doc
	mkdir dist/demo
	mkdir dist/src
	cp wingx.jar dist/lib
	cp wingx-demo.jar dist/lib
	cp Makefile dist
	cp Rules.make dist
	cp Environment.sh dist
	cd ${DEVEL_HOME}/dist/doc ;\
	ln -s ../../doc/api .
	cp -r src/* ${DEVEL_HOME}/dist/src
	make -C dist/src cvs-clean
	cp -r demo/* ${DEVEL_HOME}/dist/demo
	make -C dist/demo cvs-clean
	ln -s dist ${WINGX_DIR}
	ln -s dist ${WINGX_DEVEL_DIR}
	tar -hcvf ${WINGX_DEVEL_DIR}.tar ${WINGX_DEVEL_DIR}
	tar -hcvf ${WINGX_DIR}.tar ${WINGX_DIR}
	tar --delete -v -f ${WINGX_DIR}.tar ${WINGX_DIR}/src
	rm ${WINGX_DIR} ${WINGX_DEVEL_DIR}
	gzip ${WINGX_DIR}.tar ${WINGX_DEVEL_DIR}.tar

distclean:
	rm -rf dist
	rm ${WINGX_DIR}.tar.gz ${WINGX_DEVEL_DIR}.tar.gz

