#!/bin/bash

sourcefile=(DBManager.java \
	DBUsers.java \
	DB.java \
	DBFile.java \
	beans/UserBean.java \
	xmlhandlers/UserContentHandler.java \
	exceptions/ParcmanDBDirectoryMalformedException.java \
	exceptions/ParcmanDBNotCreateException.java \
	exceptions/ParcmanDBErrorException.java \
	exceptions/ParcmanDBUserExistException.java \
	exceptions/ParcmanDBUserNotValidException.java)

clpath=`pwd`"/../"
dom="[DATABASE]"


# Clean
echo "$dom Remove .class files"
rm *.class 2> /dev/null
rm beans/*.class 2> /dev/null
rm xmlhandlers/*.class 2> /dev/null
rm exceptions/*.class 2> /dev/null

# Compilation
if [ "$1" != "clean" ]; then
	echo "$dom Compile: ${sourcefile[@]}"
	javac -Xlint -classpath :$clpath ${sourcefile[@]}
fi

