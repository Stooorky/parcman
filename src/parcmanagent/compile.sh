#!/bin/bash

sourcefile=(RemoteParcmanAgentServer.java \
	RemoteParcmanAgentClient.java \
        ParcmanAgent.java \
        UpdateList.java \
        exceptions/UpdateSharesListErrorException.java)
clpath=`pwd`"/../"
dom="[PARCMANAGENT]"

# Clean
echo "$dom Remove .class files"
rm *.class 2> /dev/null
rm exceptions/*.class 2> /dev/null

# Compilation
if [ "$1" != "clean" ]; then
	echo "$dom Compile: ${sourcefile[@]}"
	javac -classpath :$clpath ${sourcefile[@]}
fi
