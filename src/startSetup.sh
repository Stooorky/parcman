java -classpath :. \
-Djava.rmi.server.codebase=http://192.168.1.103:8003/~sarimisar/common/ \
-Djava.security.policy=policy \
-Dsetup.implCodebase=file://$HOME/PAR/parcman/src/ \
-Dsetup.loginServerClass=loginserver.LoginServer \
-Dsetup.policyGroup=setup/policy \
-Dsetup.dbDirectory=$HOME/PAR/parcman/dbDirectory \
setup.Setup
# -Djava.rmi.server.codebase=http://`hostname`:8001/common/ \
# file://$HOME/Desktop/svnkit/parcman/src/
