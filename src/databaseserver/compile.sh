#!/bin/bash

sourcefile=(RemoteDBServer.java \
		DBServer.java)

clpath=`pwd`"/../"
dom="[DATABASESERVER]"
FLAGS="-Xlint"

# Clean
echo "$dom Remove .class files"
rm *.class 2> /dev/null
rm exceptions/*.class 2> /dev/null

# Compilation
if [ "$1" != "clean" ]; then
	echo "$dom Compile: ${sourcefile[@]}"
	javac -Xlint -classpath :$clpath ${sourcefile[@]}
fi
