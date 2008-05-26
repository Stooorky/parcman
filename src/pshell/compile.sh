#!/bin/bash

sourcefile=(PShell.java \
		PShellData.java \
        PShellTest.java \
		)

clpath=`pwd`"/../"
dom="[PSHELL]"

# Clean
echo "$dom Remove .class files"
rm *.class 2> /dev/null

# Compilation
if [ "$1" != "clean" ]; then
	echo "$dom Compile: ${sourcefile[@]}"
	javac -classpath :$clpath ${sourcefile[@]}
fi
