java -classpath :. \
-Djava.rmi.server.codebase=http://10.0.0.15:8003/~sarimisar/common/ \
-Djava.security.policy=policy \
-Dsetup.implCodebase=file://$HOME/Desktop/svnkit/parcman/src/ \
-Dsetup.loginServerClass=loginserver.LoginServer \
-Dsetup.policyGroup=setup/policy \
-Dsetup.dbDirectory=$HOME/Desktop/svnkit/parcman/dbDirectory \
setup.Setup
# -Djava.rmi.server.codebase=http://`hostname`:8001/common/ \
# file://$HOME/Desktop/svnkit/parcman/src/
