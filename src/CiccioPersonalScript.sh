java -Dremoteclient.loginserveradress=//localhost:1098/LoginServer \
-Djava.security.policy=policy \
-Djava.rmi.dgc.leaseValue=30000 \
clientbootstrap.ClientBootstrap http://10.0.0.15:8003/~sarimisar/common remoteclient.RemoteClientUser
# clientbootstrap.ClientBootstrap http://localhost:8001/common/ remoteclient.RemoteClientUser

