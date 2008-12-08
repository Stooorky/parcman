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
	slen=${#s}
	bt_get_term_width 
	width=$?
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

function bt___info__setup()
{
	echo -en "[info:$1] $2"
}

function bt___info__teardown()
{
	_OUT_=""
	if [ "$1" == "$BT_TYPE_EQ_ZERO" ];then
		bt_check_eq_zero "$2" "DONE" "ERROR"
	elif [ "$1" == "$BT_TYPE_GT_ZERO" ];then
		bt_check_gt_zero "$2" "DONE" "ERROR"
	elif [ "$1" == "$BT_TYPE_LT_ZERO" ];then
		bt_check_lt_zero "$2" "DONE" "ERROR"
	elif [ "$1" == "$BT_TYPE_EQ_TO" ];then
		bt_check_eq_to "$2" "$3" "DONE" "ERROR"
	else
		_OUT_="$2"
	fi
	bt_write_right_align "$_OUT_"
}

function bt___info()
{
	echo -e "[info:$1] $2"
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

function bt_exec()
{
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
	# $1=command, $2=message, $3=position
	bt_info_up "$2" "$3"
	bt_exec "$1"
	bt_info_down $BT_TYPE_EQ_ZERO "$?" 
}

function bt_info_nl()
{
	# $1=command, $2=message, $3=position
	bt_info_inline "$2" "$3"
	bt_exec "$1"
	bt_info_down $BT_TYPE_EQ_ZERO "$?" 
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
	# $1=path, $2=command, $3=position, $4=level
	if [ -z "$4" ]; then level=0; else level=$4; fi

	cd "$1"

	bt_info "$2" "exec $2 in $(basename $(pwd))" "$3"

	for node in *; do
		if [ -d "$node" ]; then
			(( level++ )) 
			bt_info_pre_order_exec "$node" "$2" "$3" $level
		fi
	done
	[[ $level -gt 0 ]] && (( level-- )) && cd ..
}

function bt_info_on_list_exec()
{
	# $1=list-command, $2=command, $3=message, $4=message-element, $5=position
	list="$1"

	[[ "${list[@]}" == "" ]] && bt_info_inline "$E_NOTHING_TO_DO_MSG" "$5" && return $E_NOTHING_TO_DO
	bt_info_inline "$3" "$5"
	for el in ${list[@]}; do
		bt_info "$2 $el" "$4 '$(basename $el)'" "$5"
	done
	# $1=type, $2=exit_status, $3=param
	bt_info_down "$BT_TYPE_NOP" "DONE"
}
function bt_info_on_list_exec_oneshot()
{
	# $1=list-command, $2=command, $3=message, $4=position
	list="$1"
	[[ "${list[@]}" == "" ]] && bt_info_inline "$E_NOTHING_TO_DO_MSG" "$4" && return $E_NOTHING_TO_DO

	bt_info "$2 $list" "$3" "$4"
}
