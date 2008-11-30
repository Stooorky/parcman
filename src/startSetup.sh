java -classpath :. \
-Djava.rmi.server.codebase=http://192.168.5.13:8003/~sarimisar/common/ \
-Djava.rmi.server.hostname=192.168.5.13 \
-Djava.security.policy=policy \
-Dsetup.implCodebase=file://$HOME/PAR/parcman/src/ \
-Dsetup.loginServerClass=loginserver.LoginServer \
-Dsetup.policyGroup=setup/policy \
-Dsetup.dbDirectory=$HOME/PAR/parcman/dbDirectory \
setup.Setup
# -Djava.rmi.server.codebase=http://`hostname`:8001/common/ \
# file://$HOME/Desktop/svnkit/parcman/src/
