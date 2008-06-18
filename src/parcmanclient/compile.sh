#!/bin/bash

sourcefile=(RemoteParcmanClient.java \
	RemoteParcmanClientUser.java \
	ParcmanClient.java)

clpath=`pwd`"/../"
dom="[PARCMANCLIENT]"

# Clean
echo "$dom Remove .class files"
rm *.class 2> /dev/null

# Compilation
if [ "$1" != "clean" ]; then
	echo "$dom Compile: ${sourcefile[@]}"
	javac -classpath :$clpath ${sourcefile[@]}
fi
