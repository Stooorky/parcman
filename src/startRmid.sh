#!/bin/bash
ADDRESS=10.0.0.4
PORT=8003
R_PATH="~sirio/common"

#echo "address="$ADDRESS",port="$PORT", path="$R_PATH

rmid -stop
rm -rf $HOME/PAR/log-rmid/*
sleep 1

rmid -log $HOME/PAR/log-rmid/ \
-J-Djava.rmi.server.codebase=http://$ADDRESS:$PORT/$R_PATH/ \
-J-Djava.rmi.server.hostname=$ADDRESS \
-J-Djava.rmi.dgc.leaseValue=30000 \
-J-Djava.security.policy=policy &
#file://$HOME/Desktop/svnkit/parcman/src/ \
#http://10.0.0.15:8003/~sarimisar/common/ 
#-J-Djava.rmi.server.hostname=192.168.5.13 \
