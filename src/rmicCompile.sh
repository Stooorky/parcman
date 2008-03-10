#rmic -d ~/public_html/common/ databaseserver.DBServer
rmic -d ~/public_html/common/ parcmanserver.ParcmanServer
rmic -d ~/public_html/common/ loginserver.LoginServer

#cp databaseserver/RemoteDBServer.class ~/public_html/common/databaseserver/
mkdir ~/public_html/common/parcmanserver/ 2> /dev/null
cp parcmanserver/RemoteParcmanServer.class ~/public_html/common/parcmanserver/
cp loginserver/RemoteLoginServer.class ~/public_html/common/loginserver/
mkdir ~/public_html/common/remoteexceptions/ 2> /dev/null
cp remoteexceptions/*.class ~/public_html/common/remoteexceptions/
mkdir ~/public_html/common/parcmanclient/ 2> /dev/null
cp parcmanclient/*.class ~/public_html/common/parcmanclient/
mkdir ~/public_html/common/remoteclient/ 2> /dev/null
cp remoteclient/*.class ~/public_html/common/remoteclient/
