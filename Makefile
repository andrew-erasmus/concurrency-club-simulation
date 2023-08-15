# Makefile for the club simulation assignment program 
# Andrew Erasmus
# 15 August 2023

JAVAC=/usr/bin/javac
.SUFFIXES: .java .class
SRCDIR=src
BINDIR=bin
PACKAGE=clubSimulation

# Locate all Java files under the package directory
# and generate the list of class files to be compiled
JAVA_FILES := $(shell find $(SRCDIR)/$(PACKAGE) -name '*.java')
CLASSES := $(patsubst $(SRCDIR)/%.java, %.class, $(JAVA_FILES))
CLASS_FILES := $(addprefix $(BINDIR)/, $(CLASSES))

# Rule to compile Java files in the package directory
$(BINDIR)/%.class: $(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

default: $(CLASS_FILES)
	
clean:
	rm -rf $(BINDIR)

run: $(CLASS_FILES)
	java -cp $(BINDIR) $(PACKAGE).ClubSimulation $(ARGS)


