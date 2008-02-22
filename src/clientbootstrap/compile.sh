#!/bin/bash

sourcefile=(ClientBootstrap.java)

dom="[CLIENTBOOTSTRAP]"

# Clean
echo "$dom Remove .class files"
rm *.class 2> /dev/null

# Compilation
echo "$dom Compile: ${sourcefile[@]}"
javac ${sourcefile[@]}

