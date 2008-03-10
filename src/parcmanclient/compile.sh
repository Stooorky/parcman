#!/bin/bash

sourcefile=(RemoteParcmanClient.java \
	ParcmanClient.java)

clpath=`pwd`"/../"
dom="[PARCMANCLIENT]"

# Clean
echo "$dom Remove .class files"
rm *.class 2> /dev/null

# Compilation
if [ "$1" != "clean" ]; then
    echo "$dom Compile: ${sourcefile[@]}"
    javac -Xlint -classpath :$clpath ${sourcefile[@]}
fi
