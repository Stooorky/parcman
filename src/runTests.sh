#!/bin/bash

#  execute recursively an action
#+ parameters: 
#+ $1 the initial path
#+ $2 the action
#+ $3 the path to come back at the and of recursion
#+ $4 PRIVATE counter of recursion
function exec_recursively()
{
	path=$1
	action=$2
	come_back_path=$3
	if [ -z "$4" ]
	then
		count=0
	else
		count=$4
	fi

	# go into path
	cd $path

	# execute the action 
	eval $action
	
	# apply recursion
	for dir in *
	do
		if [ -d $dir ]
		then
			(( count++ ))
			exec_recursively $dir $action $come_back_path $count
		fi
	done

	# come back
	if [ "$come_back_path" != "" ] && [ "$count" -eq 0 ]
	then
		cd $come_back_path
	else 
		(( count-- ))
		cd ..
	fi
}

# print the usage schema for the script
function usage()
{
	echo "Usage:"
	echo "./runTests.sh -clean path/to/test/test1.java [ path/to/test/test2.java ... ]"
	echo "./runTests.sh -run path/to/test/test1.java [ path/to/test/test2.java ... ]"
	echo "./runTests.sh -compile path/to/test/test1.java [ path/to/test/test2.java ... ]"
	echo "./runTests.sh -all path/to/test/test1.java [ path/to/test/test2.java ... ]"
}

# print function 
function print()
{
	echo -e "$PRINT_COLOR$@$PRINT_BASE_COLOR"
}

# print error function 
function print_error()
{
	echo -e "$PRINT_ERROR_COLOR$@$PRINT_BASE_COLOR"
}

#  compile a test 
#+ parameters:
#+ $1 test to compile
function compile()
{
	echo 
	print "compiling test: '$1'..."
	javac -classpath :./:./tests/testwithjunit/junit-4.4.jar $1
}

#  clean a test path
#+ parameters:
#+ $1 test to clean
function clean()
{
	# extract the file name and the base path to the file
	path_field=( `echo $1 | awk '{split ($0,path_element,"/"); i=1; path=""; path_len=length(path_element); while (i<=path_len) { if (i == path_len) print path_element[i]; else path=path path_element[i] "/"; i++ }; print path  }'` )
	echo
	print "removing test: '$1' and his classes..."
	exec_recursively ${path_field[1]} "rm *.class" `pwd` 2> /dev/null
	#exec_recursively ${path_field[1]} "ls" `pwd`
}

#  run a test
#+ parameters:
#+ $1 test to run
function run() 
{
	t=`echo $1 | sed 's/\//./g' | sed 's/.java//'`
	#test=`echo $test | sed 's/.java//'`

	echo 
	print "run test: '$t'..."
	java -classpath :./:./tests/testwithjunit/junit-4.4.jar org.junit.runner.JUnitCore $t 
}

PRINT_COLOR="\033[1;32m"
PRINT_BASE_COLOR="\033[0m"
PRINT_ERROR_COLOR="\033[1;31m"

E_NO_ARGS=1
E_NO_ENOUGH_ARGS=2
E_NOT_VALID_TEST=3
E_NOT_VALID_COMMAND=4

echo "Running tests with JUnit 4"
echo "--------------------------"

if [ "${#@}" -lt 2 ]
then 
	echo 
	print_error "Error: not enough arguments!"
	usage
	exit "$E_NO_ENOUGH_ARGS"
fi

command=$1
if [ "${command:0:1}" != "-" ]
then
	echo 
	print_error "Error: command not found!"
	usage
	exit $E_NO_ARGS
fi

# rimuovo il comando dalla lista dei comandi
params=`echo $@ | sed 's/-[a-z]*//'`

# controllo che i test esistano
for test in "$params"
do
	if [ ! -e $test ] 
	then
		echo
		print_error "Error: not valid test '$test'!"
		echo "       the file named '$test' not exists!"
		exit $E_NOT_VALID_TEST
	fi
done

if [ "$command" == "-clean" ]
then
	for test in "$params"
	do
		clean $test
	done
elif [ "$command" == "-all" ] 
then
	for test in "$params"
	do
		clean $test
		compile $test
		run $test
	done
elif [ "$command" == "-run" ] 
then
	for test in "$params"
	do
		run $test
	done
elif [ "$command" == "-compile" ]
then
	for test in "$params"
	do
		compile $test
	done
else
	echo
	print_error "Error: not valid command!"
	usage
	exit $E_NOT_VALID_COMMAND
fi
