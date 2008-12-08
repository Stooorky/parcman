#!/bin/bash

E_ARGSLIB_SHORT_PARAM=65 # se un opzione breve che necessita di essere valorizzata non lo e`.
E_ARGSLIB_SHORT_MSG="Option needs value."
E_ARGSLIB_USAGE_OPTIONS=66 # se il vettore delle opzioni non esiste o e` vuoto.
E_ARGSLIB_USAGE_OPTIONS_MSG="Vettore opzioni non settato oppure vuoto."
E_ARGSLIB_USAGE_HELPS=67 # se il vettore degli help delle opzioni non esiste o e` vuoto.
E_ARGSLIB_USAGE_HELPS_MSG="Vettore help delle opzioni non settato oppure vuoto."
E_ARGSLIB_USAGE_DEFAULTS=68 # se il vettore dei default delle opzioni non esiste o e` vuoto.
E_ARGSLIB_USAGE_DEFAULTS_MSG="Vettore default delle opzioni non settato oppure vuoto."


ARGSLIB_OPT_VALUE=""
function argslib_get_short_option()
{
	value=$(echo $1 | sed -n '/[-]\+/p')
	[[ ! -z "$value" ]] && echo $E_ARGSLIB_SHORT_MSG && echo "option='-a', value found='$value'" && exit $E_ARGSLIB_SHORT_PARAM
	ARGSLIB_OPT_VALUE="$1"
}

function argslib_get_long_option()
{
	ARGSLIB_OPT_VALUE=$(echo $1 | sed 's/--[a-zA-Z0-9.-_]*=//')
}

function argslib_usage()
{
	[[ ${#OPTIONS[*]} -eq 0 ]] && echo $E_ARGSLIB_USAGE_OPTIONS_MSG && exit $E_ARGSLIB_USAGE_OPTIONS
	[[ ${#HELPS[*]} -eq 0 ]] && echo $E_ARGSLIB_USAGE_HELPS_MSG && exit $E_ARGSLIB_USAGE_HELPS_MSG
	[[ ${#DEFAULTS[*]} -eq 0 ]] && echo $E_ARGSLIB_USAGE_DEFAULTS && exit $E_ARGSLIB_USAGE_DEFAULTS_MSG
	echo "Usage: $(basename $0) [options]"
	echo " where options are:"
	size=${#OPTIONS[*]}
	for (( i=0; i<${#OPTIONS[*]}; i++ )); do
		echo -n "  $i) ${OPTIONS[$i]}: ${HELPS[$i]}."
		[[ ! -z ${DEFAULTS[$i]} ]] && echo " [${DEFAULTS[$i]}]"
	done
	echo 
}

### for i in $@; do
### 	echo "param da analizzare: $i"
### 	case $i in 
### 	-a)
### 		shift
### 		argslib_get_short_option $1
### 		OPT_A="$ARGSLIB_OPT_VALUE"
### 		;;
### 	-b)
### 		shift
### 		argslib_get_short_option $1
### 		OPT_B="$ARGSLIB_OPT_VALUE"
### 		;;
### 	--aaa=*)
### 		argslib_get_long_option $i
### 		OPT_AAA="$ARGSLIB_OPT_VALUE"
### 		shift
### 		;;
### 	*)
### 		shift
### 		;;
### 	esac
### done
### 
### echo $OPT_A
### echo $OPT_B
### echo $OPT_AAA
