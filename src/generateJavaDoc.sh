#!/bin/bash

rm -Rf doc 2> /dev/null

javadoc -d doc clientbootstrap/*.java \
plog/*.java \
remoteclient/*.java \
database/*.java \
database/beans/*.java \
database/exceptions/*.java \
database/xmlhandlers/*.java \
databaseserver/*.java \
remoteexceptions/*.java

