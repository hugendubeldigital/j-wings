#
# $Id$
# precondition: JAVA_HOME, DEVEL_HOME and CLASSPATH must be
# set.
#
# Currently the following libraries are required:
# - Swing 1.1
# - Java Servlet Development Kit (JSDK) 2.0  see http://java.sun.com/
# - ACME                                     see http://www.acme.com/java/
# - Apache JSSI > 1.1.2                      see http://java.apache.org/
#
##

#--- try to guess target, if it is not given
ifndef MAINTARGET
	ifndef JTARGETS
		ifndef JSOURCES
			JSOURCES=$(wildcard *.java)
		endif
		JTARGETS=$(JSOURCES:.java=.class)
	endif
	MAINTARGET=$(JTARGETS)
endif


#--- Jikes, fast java compiler from IBM
# comment out if not available
ifndef JIKES
	JIKES=jikes
endif

#--- use jikes as default if it exists
ifdef JIKES
	JAVAC=$(JIKES) -deprecation +E +P
else
	JAVAC=$(JAVA_HOME)/bin/javac -depend
	DEPAWK=awk -f ${DEVEL_HOME}/scripts/depend.awk
endif

JAVA=$(JAVA_HOME)/bin/java

# first Target is 'world', so we don't get confused with
# the first Target included from .depend

	# just use the dependencies, if we've the fast
	# jikes
ifdef JIKES
world : deptest
else
	# build the stuff ignoring .depend
	# MAINTARGET may have other targets defined so
	# build them afterwards
world : nodep $(MAINTARGET)
endif

# GNUmake specific test if .depend-file exists
ifeq (.depend,$(wildcard .depend))
include .depend
deptest : fun 
else
deptest : local-dep
	$(MAKE) fun
endif

fun : $(MAINTARGET)

# create *.class without known dependencies; useful for
# SUN javac and if we've to make the whole bunch of
# files; for few changed files, known dependencies
# and jikes available the normal way is faster
nodep :
	$(JAVAC) $(JFLAGS) *.java

#--- Rule to make %.class from %.java
%.class : %.java
	$(JAVAC) $(JFLAGS) $<

#-- default 'clean'/'depend'-actions
clean : local-clean
depend : local-dep
proper : local-proper

local-proper : local-clean
	rm -f *.u .depend

##
# Es interessieren nur die Abhängigkeiten von 
# class-files, denn die Abhängigkeit
# foobar.java -> foobar.class ist schon in der
# impliziten Regel oben erfasst (%.class : %.java).
# jikes trägt sonst auch files ein, die es gar nicht
# gibt, vor allem in irgendwelchen libraries.
# Daher werden mit dem SED-script alle .java-Abhängigkeiten
# entfernt
##
local-dep: dummy
ifdef JIKES
	$(JIKES) +E +F +B +M *.java
	cat *.u | sed '/.java$$/d' > .depend ; rm -f *.u
else
	$(DEPAWK) -I${DEVEL_HOME} *.java > .depend
endif

local-clean : dummy
	rm -f *.class *~

dummy :
