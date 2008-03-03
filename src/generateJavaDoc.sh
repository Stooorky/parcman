#!/bin/bash

javadoc -d doc clientbootstrap/*.java \
plog/*.java \
remoteclient/*.java \
database/*.java \
database/beans/*.java \
database/exceptions/*.java \
database/xmlhandlers/*.java
