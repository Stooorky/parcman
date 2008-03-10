java -classpath :. \
-Djava.rmi.server.codebase=http://`hostname`:8001/common/ \
-Djava.security.policy=policy \
-Dsetup.implCodebase=file://$HOME/Desktop/svnkit/parcman/src/ \
-Dsetup.loginServerClass=loginserver.LoginServer \
-Dsetup.policyGroup=setup/policy \
-Dsetup.dbDirectory=$HOME/Desktop/svnkit/parcman/dbDirectory \
setup.Setup

