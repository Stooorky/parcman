
rmid -log $HOME/Desktop/log-rmid/ \
-J-Djava.rmi.server.codebase=http://`hostname`:8001/common/ \
-J-Djava.security.policy=policy &

