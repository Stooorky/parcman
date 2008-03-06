java -classpath :. \
-Djava.rmi.server.codebase=http://localhost:8003/~sarimisar/common/ \
-Djava.security.policy=policy \
-Dsetup.policy=setup/policy \
-Dsetup.dbDirectory=$HOME/Desktop/parcman/dbDirectory \
setup.Setup


