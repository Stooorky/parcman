#!/bin/bash

sourcefile=(PLog.java)

clpath=`pwd`"/../"
dom="[PLOG]"

# Clean
echo "$dom Remove .class files"
rm *.class 2> /dev/null

# Compilation
echo "$dom Compile: ${sourcefile[@]}"
javac -classpath :$clpath ${sourcefile[@]}
