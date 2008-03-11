#!/bin/bash

sourcefile=(ParcmanDBServerErrorRemoteException.java \
	ParcmanDBServerUserExistRemoteException.java \
	ParcmanDBServerUserNotValidRemoteException.java \
    ParcmanServerHackWarningRemoteException.java)

clpath=`pwd`"/../"
dom="[REMOTEEXCEPTIONS]"

# Clean
echo "$dom Remove .class files"
rm *.class 2> /dev/null

# Compilation
if [ "$1" != "clean" ]; then
	echo "$dom Compile: ${sourcefile[@]}"
	javac -classpath :$clpath ${sourcefile[@]}
fi
