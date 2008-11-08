#!/bin/bash

rm -Rf ../doc/api 2> /dev/null

javadoc -d ../doc/api clientbootstrap/*.java \
plog/*.java \
remoteclient/*.java \
database/*.java \
database/beans/*.java \
database/exceptions/*.java \
database/xmlhandlers/*.java \
databaseserver/*.java \
remoteexceptions/*.java \
loginserver/*.java \
parcmanclient/*.java \
parcmanserver/*.java \
indexingserver/*.java \
pshell/*.java
