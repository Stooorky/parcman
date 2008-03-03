#!/bin/bash

sourcefile=(RemoteClient.java \
	RemoteClientUser.java \
	RemoteClientAdmin.java)

clpath=`pwd`"/../"
dom="[REMOTECLIENT]"

# Clean
echo "$dom Remove .class files"
rm *.class 2> /dev/null

# Compilation
if [ "$1" != "clean" ]; then
    echo "$dom Compile: ${sourcefile[@]}"
    javac -classpath :$clpath ${sourcefile[@]}
fi

