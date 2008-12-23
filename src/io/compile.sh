#!/bin/bash

sourcefile=(Enum.java \
		IO.java \
		IOColor.java \
		IOConstants.java \
		IOProperties.java \
		Logger.java \
		LoggerConstants.java \
		LoggerLevel.java \ 
		LoggerProperties.java \ 
		Pair.java \
		PropertyManager.java \
		LoggerTest.java \
		)
		#exceptions/InOutException.java \
		#exceptions/ListIsEmptyException.java \
clpath=`pwd`"/../"
dom="[IO]"

### FUNZIONI
local_compile()
{
	if [ "$1" != "" ]; then
		echo "$dom Compile with '$1' flags: ${sourcefile[@]}."
		javac "$1" -classpath :$clpath ${sourcefile[@]} 
	else
		echo "$dom Compile: ${sourcefile[@]}."
		javac -classpath :$clpath ${sourcefile[@]} 2> /dev/null
	fi
	cd exceptions
	bash compile.sh "$1"
	cd ..
}

local_clean()
{
	echo "$dom Remove '.class' files."
	rm *.class 2> /dev/null
	rm exceptions/*.class 2> /dev/null
}

# Compilation
if [ "$1" == "clean" ]; then
	local_clean 
elif [ "$1" == "xlint" ]; then
	echo "FLAGS: $2"
	local_compile "$2"
else
	local_compile
fi
