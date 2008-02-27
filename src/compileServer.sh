#!/bin/bash

compile ()
{
	cd $1
	bash compile.sh
	cd ..
}

echo "Global Compilation start."
echo


compile remoteclient
compile plog

echo
echo "Global Compilation end."
