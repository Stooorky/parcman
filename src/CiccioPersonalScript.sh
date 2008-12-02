java -Dremoteclient.loginserveradress=//localhost:1098/LoginServer \
-Djava.security.policy=policy \
-Djava.rmi.dgc.leaseValue=30000 \
clientbootstrap.ClientBootstrap file:://$HOME/public_html/common remoteclient.RemoteClientUser
#clientbootstrap.ClientBootstrap http://192.168.1.103:8003/~sarimisar/common/ remoteclient.RemoteClientUser
