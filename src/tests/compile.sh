#!/bin/bash

sourcefile=(RunTests.java \
	Test.java \
	TestDBManager.java \
    TestSetup.java)

clpath=`pwd`"/../"
dom="[TESTS]"

# Clean
echo "$dom Remove .class files"
rm *.class 2> /dev/null

# Compilation
if [ "$1" != "clean" ]; then
    echo "$dom Compile: ${sourcefile[@]}"
    javac -Xlint -classpath :$clpath ${sourcefile[@]}
fi
