java -classpath :. \
-Djava.rmi.server.codebase=http://`hostname`:8001/common/ \
-Djava.security.policy=policy \
-Dsetup.policy=setup/policy \
-Dsetup.dbDirectory=$HOME/Desktop/parcman/dbDirectory \
setup.Setup


