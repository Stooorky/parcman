#!/bin/bash

sourcefile=(DBManager.java \
	DBUsers.java \
	beans/UserBean.java \
	xmlhandlers/UserContentHandler.java)

clpath=`pwd`"/../"
dom="[REMOTECLIENT]"

# Clean
echo "$dom Remove .class files"
rm *.class 2> /dev/null

# Compilation
echo "$dom Compile: ${sourcefile[@]}"
javac -classpath :$clpath ${sourcefile[@]}

