java -classpath :. \
-Djava.rmi.server.codebase=http://10.0.0.13:8003/~sarimisar/common/ \
-Djava.security.policy=policy \
-Djava.rmi.server.hostname=10.0.0.13 \
-Dsetup.implCodebase=file://$HOME/PAR/parcman/src/ \
-Dsetup.loginServerClass=loginserver.LoginServer \
-Dsetup.policyGroup=setup/policy \
-Dsetup.dbDirectory=$HOME/PAR/parcman/dbDirectory \
setup.Setup
# -Djava.rmi.server.codebase=http://`hostname`:8001/common/ \
# file://$HOME/Desktop/svnkit/parcman/src/
#-Djava.rmi.server.codebase=http://192.168.5.13:8003/~sarimisar/common/ \
#-Djava.rmi.server.hostname=192.168.5.13 \
