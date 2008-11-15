#!/bin/bash

sourcefile=(RemoteParcmanClient.java \
	RemoteParcmanClientUser.java \
	ParcmanClient.java)

clpath=`pwd`"/../"
dom="[PARCMANCLIENT]"
FLAGS="-Xlint"

### FUNZIONI
local_compile()
{
	if [ "$1" != "" ]; then
		echo "$dom Compile with '$1' flags: ${sourcefile[@]}."
		javac "$1" -classpath :$clpath ${sourcefile[@]}
	else
		echo "$dom Compile: ${sourcefile[@]}."
		javac -classpath :$clpath ${sourcefile[@]}
	fi
}

local_clean()
{
	echo "$dom Remove '.class' files."
	rm *.class 2> /dev/null
}

# Compilation
if [ "$1" == "clean" ]; then
	local_clean 
elif [ "$1" == "xlint" ]; then
	echo "FLAGS: $1"
	local_compile $FLAGS
else
	local_compile
fi
