#!/bin/bash

E_ARGSLIB_SHORT_PARAM=65 # se un opzione breve che necessita di essere valorizzata non lo e`.
E_ARGSLIB_SHORT_MSG="Option needs value."

ARGSLIB_OPT_VALUE=""
function argslib_get_short_option()
{
	value=$(echo $1 | sed -n '/[-]\+/p')
	[[ ! -z "$value" ]] && echo $E_ARGSLIB_SHORT_MSG && echo "option='-a', value found='$value'" && exit $E_ARGSLIB_SHORT_PARAM
	ARGSLIB_OPT_VALUE="$1"
}

function argslib_get_long_option()
{
	ARGSLIB_OPT_VALUE=$(echo $1 | sed 's/--[a-zA-Z0-9-._]*=//')
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
