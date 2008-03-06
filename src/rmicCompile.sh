rmic -d ~/public_html/common/ databaseserver.DBServer
rmic -d ~/public_html/common/ parcmanserver.ParcmanServer

cp databaseserver/RemoteDBServer.class ~/public_html/common/databaseserver/
cp parcmanserver/RemoteParcmanServer.class ~/public_html/common/parcmanserver/


