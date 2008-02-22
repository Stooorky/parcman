#!/bin/bash

compile ()
{
	cd $1
	bash compile.sh
	cd ..
}

echo "Global Compilation start."
echo


compile clientbootstrap

echo
echo "Global Compilation end."
