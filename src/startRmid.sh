#rmid -stop
rm -rf $HOME/PAR/log-rmid/*
sleep 1

rmid -log $HOME/PAR/log-rmid/ \
-J-Djava.rmi.server.codebase=file://$HOME/public_html/common/ \
-J-Djava.rmi.dgc.leaseValue=30000 \
-J-Djava.security.policy=policy &
#file://$HOME/Desktop/svnkit/parcman/src/ \
#http://10.0.0.15:8003/~sarimisar/common/ 
