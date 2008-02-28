#!/bin/bash

# Sourcefiles list
sourcefile=(ClientBootstrap.java)

clpath=`pwd`"/../"
dom="[CLIENTBOOTSTRAP]"

# Clean
echo "$dom Remove .class files"
rm *.class 2> /dev/null

# Compilation
echo "$dom Compile: ${sourcefile[@]}"
javac -classpath :$clpath ${sourcefile[@]}
