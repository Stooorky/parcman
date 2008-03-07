java -classpath :. \
-Djava.rmi.server.codebase=file://$HOME/public_html/common/ \
-Djava.security.policy=policy \
-Dsetup.implCodebase=file://$HOME/Desktop/parcman/src/ \
-Dsetup.loginServerClass=loginserver.LoginServer \
-Dsetup.policyGroup=setup/policy \
-Dsetup.dbDirectory=$HOME/Desktop/parcman/dbDirectory \
setup.Setup

