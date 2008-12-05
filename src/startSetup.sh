#!/bin/bash
ADDRESS=10.0.0.4
PORT=8003
R_PATH="~sirio/common"

java -classpath :. \
-Djava.rmi.server.codebase=http://$ADDRESS:$PORT/$R_PATH/ \
-Djava.security.policy=policy \
-Djava.rmi.server.hostname=$ADDRESS \
-Dsetup.implCodebase=file://$HOME/programming/parcman/src/ \
-Dsetup.loginServerClass=loginserver.LoginServer \
-Dsetup.policyGroup=setup/policy \
-Dsetup.dbDirectory=$HOME/PAR/parcman/dbDirectory \
setup.Setup
# -Djava.rmi.server.codebase=http://`hostname`:8001/common/ \
#-Dsetup.implCodebase=file://$HOME/PAR/parcman/src/ \
# file://$HOME/Desktop/svnkit/parcman/src/
#-Djava.rmi.server.codebase=http://192.168.5.13:8003/~sarimisar/common/ \
#-Djava.rmi.server.hostname=192.168.5.13 \
