#!/bin/bash

sourcefile=(ParcmanDBServerErrorRemoteException.java \
	ParcmanDBServerUserExistRemoteException.java \
	ParcmanDBServerUserNotExistRemoteException.java \
	ParcmanDBServerUserNotValidRemoteException.java \
	ParcmanServerHackWarningRemoteException.java \
	ParcmanDBServerShareExistRemoteException.java \
	ParcmanDBServerShareNotValidRemoteException.java \
	ParcmanServerUserIsConnectRemoteException.java \
    IndexingServerRequestAfterTimeOutRemoteException.java )

clpath=`pwd`"/../"
dom="[REMOTEEXCEPTIONS]"

# Clean
echo "$dom Remove .class files"
rm *.class 2> /dev/null

# Compilation
if [ "$1" != "clean" ]; then
	echo "$dom Compile: ${sourcefile[@]}"
	javac -Xlint -classpath :$clpath ${sourcefile[@]}
fi
