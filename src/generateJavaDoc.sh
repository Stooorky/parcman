#!/bin/bash

rm -Rf ../doc/api 2> /dev/null

javadoc -private -d ../doc/api clientbootstrap/*.java \
database/*.java \
database/beans/*.java \
database/exceptions/*.java \
database/xmlhandlers/*.java \
databaseserver/*.java \
indexingserver/*.java \
loginserver/*.java \
parcmanagent/*.java \
parcmanclient/*.java \
parcmanserver/*.java \
plog/*.java \
privilege/*.java \
pshell/*.java \
remoteclient/*.java \
remoteexceptions/*.java
