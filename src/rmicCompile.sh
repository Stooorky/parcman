rmic -d ~/public_html/common/ databaseserver.DBServer
rmic -d ~/public_html/common/ parcmanserver.ParcmanServer
rmic -d ~/public_html/common/ loginserver.LoginServer

cp databaseserver/RemoteDBServer.class ~/public_html/common/databaseserver/
cp parcmanserver/RemoteParcmanServer.class ~/public_html/common/parcmanserver/
cp loginserver/RemoteLoginServer.class ~/public_html/common/loginserver/
cp remoteexceptions/*.class ~/public_html/common/remoteexceptions/

