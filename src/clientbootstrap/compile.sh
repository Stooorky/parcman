#!/bin/bash

# Sourcefiles list
sourcefile=(ClientBootstrap.java)

clpath=`pwd`"/../"
dom="[CLIENTBOOTSTRAP]"

# Clean
echo "$dom Remove .class files"
rm *.class 2> /dev/null

# Compilation
if [ "$1" != "clean" ]; then
	echo "$dom Compile: ${sourcefile[@]}"
	javac -classpath :$clpath ${sourcefile[@]}
fi
