pkill rmid
pkill rmid
sleep 1

rmid -log $HOME/Desktop/log-rmid/ \
-J-Djava.rmi.server.codebase=file://$HOME/Desktop/parcman/src/ \
-J-Djava.security.policy=policy &

