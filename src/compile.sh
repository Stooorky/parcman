#!/bin/bash

# Funzione di compilazione package
compile ()
{
	cd $1
	bash compile.sh $2
	cd ..
}

# Funzione di clean package
clean ()
{
    cd $1
    bash compile.sh clean
    cd ..
}

#####################
# Clean
#####################
if [ "$1" = "clean" ]; then
    if [ "$2" = "server" ]; then
        echo "Clean Server .class files."
        clean remoteclient
        clean plog
        echo "Done."
        exit
    fi

    if [ "$2" = "client" ]; then
        echo "Clean Client .class files."
        clean clientbootstrap
        clean plog
        echo "Done."
        exit
    fi

    if [ "$2" = "tests" ]; then
        echo "Clean Tests .class files."
        clean tests
        echo "Done."
        exit
    fi

    if [ "$2" = "dbmanager" ]; then
        echo "Clean Dbmanager  .class files."
        clean dbmanager
        clean plog
        echo "Done."
        exit
    fi

    if [ "$2" = "all" ]; then
        echo "Clean All .class files."
        clean clientbootstrap
        clean plog
        clean remoteclient
        clean tests
        clean database
        echo "Done."
        exit
    fi
fi

#####################
# Compile Server
#####################
if [ "$1" = "server" ]; then
    echo "Compilation Server start."
    compile remoteclient
    compile plog
    echo "Done."
    exit
fi

#####################
# Compile Client
#####################
if [ "$1" = "client" ]; then
    echo "Compilation Client start."
    compile clientbootstrap
    compile plog
    echo "Done."
    exit
fi

#####################
# Compile DBmanager
#####################
if [ "$1" = "database" ]; then
    echo "Compilation Database start."
    compile database
    compile plog
    echo "Done."
    exit
fi

#####################
# Compile Tests
#####################
if [ "$1" = "tests" ]; then
    echo "Compilation Client start."
    compile tests 
    echo "Done."
    exit
fi

#####################
# Compile All
#####################
if [ "$1" = "all" ]; then
    echo "Compile All start."
    compile clientbootstrap
    compile plog
    compile remoteclient
    compile tests
    compile database
    echo "Done."
    exit
fi

#####################
# USE
#####################
echo "[USE] For Clean:"
echo "       $0 clean <server, client, tests, database, all>"
echo "      For Compile:"
echo "       $0 <server, client, tests, database, all>"

