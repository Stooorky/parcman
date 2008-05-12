pkill rmid
pkill rmid
sleep 1

rmid -log $HOME/Desktop/log-rmid/ \
-J-Djava.rmi.server.codebase=file://$HOME/Desktop/svnkit/parcman/src/ \
-J-Djava.rmi.dgc.leaseValue=30000 \
-J-Djava.security.policy=policy &

