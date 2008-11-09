#!/bin/bash

sourcefile=(DB.java \
		DBFile.java \ 
		DBManager.java \ 
		DBSharings.java \ 
		DBUsers.java \ 
		)
clpath=`pwd`"/../"
dom="[DATABASE]"

# Clean
echo "$dom Remove .class files"
rm *.class 2> /dev/null
rm exceptions/*.class 2> /dev/null
rm beans/*.class 2> /dev/null
rm xmlhandlers/*.class 2> /dev/null

# Compilation
if [ "$1" != "clean" ]; then
	echo "$dom Compile: ${sourcefile[@]}"
	javac -Xlint -classpath :$clpath ${sourcefile[@]}
fi
