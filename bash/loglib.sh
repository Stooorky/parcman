#!/bin/bash

#+ namespace BT_|bt_

E_NOTHING_TO_DO=65
E_NOTHING_TO_DO_MSG="Nothing to do!"

BT_TYPE_EQ_ZERO="EQ_ZERO"
BT_TYPE_GT_ZERO="GT_ZERO"
BT_TYPE_LT_ZERO="LT_ZERO"
BT_TYPE_EQ_TO="EQ_TO"
BT_TYPE_NOP="NOP"

BT_OUTPUT_STDOUT="STDOUT"
BT_OUTPUT_FILE="FILE"
BT_OUTPUT_STDERR="STDERR"
BT_OUTOUT_LIST=( $BT_OUTPUT_STDOUT $BT_OUTPUT_STDERR, $BT_OUTPUT_FILE )
BT_OUTOUT=$OUTPUT_STDOUT

BT_NO_COLOR="\033[0m"
BT_RED="\033[1;31m"
BT_GREEN="\033[1;32m"
BT_BLUE="\033[1;34m"
BT_WHITE="\033[1;37m"
BT_NO_COLOR_SCARTO=7
BT_COLOR_SCARTO=10

BT_OUT_DONE="$BT_NO_COLOR[$BT_GREEN DONE $BT_NO_COLOR]"
BT_OUT_FAILED="$BT_NO_COLOR[$BT_RED FAILED $BT_NO_COLOR]"
BT_OUT_SCARTO=$(( $BT_NO_COLOR_SCARTO + $BT_NO_COLOR_SCARTO + $BT_COLOR_SCARTO ))

BT_OUT_TYPE_NORMAL=0
BT_OUT_TYPE_NOERROR=1
BT_OUT_TYPE_NOOUT=-1

BT_LEVEL_INFO=$BT_BLUE"info"$BT_NO_COLOR
BT_LEVEL_INFO_SCARTO=$(( $BT_COLOR_SCARTO + $BT_NO_COLOR_SCARTO ))

BT_WRITE_INFO_SCARTO=$(( $BT_OUT_SCARTO + $BT_LEVEL_INFO_SCARTO ))

function bt_get_term_height()
{
	dim=$(stty size)
	return ${dim%% *}
}

function bt_get_term_width()
{
	dim=$(stty size)
	return ${dim#* }
}

function bt_in_array()
{
	list=$1
	element="$2"
	for el in list;do
		[[ "$el" == "$element" ]] && return 0
	done
	return -1
}

function bt_write_right_align()
{
	s="$1"
	slen=$(( ${#s} - $2 ))
	bt_get_term_width 
	width=$?
	#echo "$s, $2, $slen, $width"
	echo -en "\033[""$width""D"
	echo -e "\033[""$(( $width - $slen ))""C""$s"
}

function bt_check_eq_zero()
{
	if [ $1 -eq 0 ];then
		_OUT_="$2"
	else
		_OUT_="$3"
	fi
}

function bt_check_eq_to()
{
	if [ $1 -eq $2 ];then
		_OUT_="$3"
	else
		_OUT_="$4"
	fi
}

function bt___info__setup()
{
	echo -en "$BT_WHITE[$BT_LEVEL_INFO:$BT_WHITE$1]$BT_NO_COLOR $2"
}

function bt___info__teardown()
{
	_OUT_=""
	if [ "$1" == "$BT_TYPE_EQ_ZERO" ];then
		bt_check_eq_zero "$2" "$BT_OUT_DONE" "$BT_OUT_FAILED"
	elif [ "$1" == "$BT_TYPE_GT_ZERO" ];then
		bt_check_gt_zero "$2" "$BT_OUT_DONE" "$BT_OUT_FAILED"
	elif [ "$1" == "$BT_TYPE_LT_ZERO" ];then
		bt_check_lt_zero "$2" "$BT_OUT_DONE" "$BT_OUT_FAILED"
	elif [ "$1" == "$BT_TYPE_EQ_TO" ];then
		bt_check_eq_to "$2" "$3" "$BT_OUT_DONE" "$BT_OUT_FAILED"
	else
		_OUT_="$2"
	fi
	bt_write_right_align "$_OUT_" $BT_OUT_SCARTO
}

function bt___info()
{
	echo -e "$BT_WHITE[$BT_LEVEL_INFO:$BT_WHITE$1]$BT_NO_COLOR $2"
}


function bt__log_setup()
{
	POSITION="$1"
	LEVEL="$2"
	MESSAGE="$3"

	method="bt___""$LEVEL""__setup"
	$method "$POSITION" "$MESSAGE"
}

function bt__log_teardown()
{
	LEVEL="$1"
	TYPE="$2"
	EXIT_STATUS="$3"
	PARAM="$4"

	method="bt___""$LEVEL""__teardown"
	$method "$TYPE" "$EXIT_STATUS" "$PARAM"
}

function bt__log()
{
	POSITION="$1"
	LEVEL="$2"
	MESSAGE="$3"

	method="bt___""$LEVEL"
	$method "$POSITION" "$MESSAGE"
}

function bt_info_up()
{
	# $1=message, $2=position
	bt__log_setup "$2" "info" "$1"
}

function bt_info_down()
{
	# $1=type, $2=exit_status, $3=param
	bt__log_teardown "info" "$1" "$2" "$3"
}

function bt_info_inline()
{
	# $1=message, $2=position
	bt__log "$2" "info" "$1"
}

#function bt_set_output()1
#{
#	bt_in_array $BT_OUTOUT_LIST "$1"
#	[[ $? -eq 0 ]] && BT_OUTPUT="$1" && return 0
#	return -1
#}

function bt_exec_noout()
{
	# < 0
	$1 &> /dev/null
}

function bt_exec_noerror()
{
	# > 0
	$1 2> /dev/null
}

function bt_exec()
{
	# = 0
	$1 
}

function bt_exec_in_xterm()
{
	time=0
	if [ ! -z "$2" ];then
		time=$2
	fi
	xterm -e "$1;sleep $time"

}

function bt_info()
{
	# $1=command, $2=message, $3=position, $4=noerror, $5=check-type, $6=check-param
	if [ -z "$5" ];then check=$BT_TYPE_EQ_ZERO; else check=$5; fi
	bt_info_up "$2" "$3"
	if [ -z "$4" ];then t=0; else t=$4; fi
	if [ $t -eq $BT_OUT_TYPE_NOERROR ];then m="bt_exec_noerror"; elif [ $t -eq $BT_OUT_TYPE_NOOUT ];then m="bt_exec_noout"; else m="bt_exec"; fi
	$m "$1"
	bt_info_down $check "$?" "$6"
}

function bt_info_nl()
{
	# $1=command, $2=message, $3=position, $4=noerror, $5=check-type, $6=check-param
	if [ -z "$5" ];then check=$BT_TYPE_EQ_ZERO; else check=$5; fi
	bt_info_inline "$2" "$3"
	if [ -z "$4" ];then t=0; else t=$4; fi
	if [ $t -eq $BT_OUT_TYPE_NOERROR ];then m="bt_exec_noerror"; elif [ $t -eq $BT_OUT_TYPE_NOOUT ];then m="bt_exec_noout"; else m="bt_exec"; fi
	$m "$1"
	bt_info_down $check "$?" "$6"
}

function bt_info_in_xterm()
{
	# $1=command, $2=message, $3=position, $4=sleep_time
	bt_info_up "$2" "$3"
	bt_exec_in_xterm "$1" $4
	bt_info_down $BT_TYPE_EQ_ZERO "$?" 
}

function bt_info_pre_order_exec()
{
	# $1=path, $2=command, $3=position, $4=level, $noerror
	if [ -z "$4" ]; then level=0; else level=$4; fi

	cd "$1"

	bt_info "$2" "exec $2 in $(basename $(pwd))" "$3" $4

	for node in *; do
		if [ -d "$node" ]; then
			(( level++ )) 
			bt_info_pre_order_exec "$node" "$2" "$3" $level $4
		fi
	done
	[[ $level -gt 0 ]] && (( level-- )) && cd ..
}

function bt_info_on_list_exec()
{
	# $1=list, $2=command, $3=message, $4=message-element, $5=position, $6=noerror
	local list
	list=( `echo "$1"` )

	[[ "${list[@]}" == "" ]] && bt_info_inline "$E_NOTHING_TO_DO_MSG" "$5" && return $E_NOTHING_TO_DO
	bt_info_inline "$3" "$5"
	for el in ${list[@]}; do
		bt_info "$2 $el" "$4 '$(basename $el)'" "$5" $6
	done
	# $1=type, $2=exit_status, $3=param
	bt_info_down "$BT_TYPE_NOP" $BT_OUT_DONE 
}
function bt_info_on_list_exec_oneshot()
{
	# $1=list-command, $2=command, $3=message, $4=position, $5=noerror
	[[ "$1" == "" ]] && bt_info_inline "$E_NOTHING_TO_DO_MSG" "$4" && return $E_NOTHING_TO_DO

	local list
	list=( `echo "$1"` )

	bt_info "$2 $1" "$3" "$4" $5
}
