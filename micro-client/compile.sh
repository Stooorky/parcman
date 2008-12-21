#!/bin/bash

E_INLCUDE_ARGSLIB=65
E_INLCUDE_ARGSLIB_MSG="Impossibile includere argslib.sh"
E_INLCUDE_LOGLIB=66
E_INLCUDE_LOGLIB_MSG="Impossibile includere loglib.sh"
E_COMMAND_INVALID=67
E_COMMAND_INVALID_MSG="Comando non valido"

#LOG_POSITION=$(basename $0)
LOG_POSITION="micro-client.$(basename $0)"

# include argslib.sh
. ../bash/argslib.sh 2> /dev/null
[[ $? -gt 0 ]] && echo $E_INLCUDE_ARGSLIB_MSG && exit $E_INLCUDE_ARGSLIB

# include loglib.sh
. ../bash/loglib.sh 2> /dev/null
[[ $? -gt 0 ]] && echo $E_INLCUDE_LOGLIB_MSG && exit $E_INLCUDE_LOGLIB

P_JAVAC=/usr/bin/javac
P_CMD="compile"
P_SRC_DIR="src"
P_JAVAC_OPTS="-classpath $(pwd)/$P_SRC_DIR"

OPTIONS=( --javac --javac-opts --command --help )
HELPS=( "java compiler path" "options for java compiler" "command to do [compile|clean]" "prints help page" )
DEFAULTS=( "$P_JAVAC" "$P_JAVAC_OPTS" "$P_CMD" "" )

for i in $@; do
	case $i in 
	--javac=*)
		argslib_get_long_option $i
		shift
		P_JAVAC="$ARGSLIB_OPT_VALUE"
		;;
	--javac-opts=*)
		argslib_get_long_option $i
		shift
		P_JAVAC_OPTS="$ARGSLIB_OPT_VALUE"
		;;
	--command=*)
		argslib_get_long_option $i
		shift
		P_CMD="$ARGSLIB_OPT_VALUE"
		;;
	--help)
		argslib_usage
		shift
		exit 0
		;;
	*)
		shift
		;;
	esac
done

function compile()
{
	bt_info_on_list_exec "$(find . | grep .java | grep -v .svn)" "$P_JAVAC $P_JAVAC_OPTS" "compile micro-client" "compile" $LOG_POSITION 
}

function clean()
{
	bt_info_on_list_exec "$(find . | grep .class | grep -v .svn)" "rm -rf" "clean micro-client" "remove" $LOG_POSITION 
}

case $P_CMD in 
	"compile")
		compile 
		;;
	"clean")
		clean
		;;
	*)
		echo $E_COMMAND_INVALID_MSG "($P_CMD)" && exit $E_COMMAND_INVALID
		;;
esac
