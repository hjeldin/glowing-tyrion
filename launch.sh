#!/bin/sh

SERVERIP=157.27.241.56
PROJ_DIR=/home/accounts/studenti/id092097/workspace/glowing-tyrion
CODEBASE_DIR=http://$SERVERIP:8000/common/
POLICY_DIR=/home/accounts/studenti/id092097/workspace/glowing-tyrion
LOG_DIR=/home/accounts/studenti/id092097/workspace/glowing-tyrion/log

export CLASSPATH=$POLICY_DIR

 pkill rmiregistry
 pkill tnameserv
 #pkill python
 #pkill rmid
 pkill java

sleep 1

rmiregistry 2222 &
tnameserv -ORBInitialPort 5555 &

sleep 1

cd $POLICY_DIR/../
#python -m SimpleHTTPServer &

cd $PROJ_DIR

#rmid -log $LOG_DIR -J-Djava.rmi.server.codebase=$CODEBASE_DIR -J-Djava.security.policy=$POLICY_DIR/policy -port 5551 &


sleep 4

cd $PROJ_DIR

pwd

java -Djava.security.policy=$POLICY_DIR/policy -Djavax.net.ssl.trustStore=$PROJ_DIR/server.keystore \
-Djavax.net.ssl.keyStore=$PROJ_DIR/server.keystore -Djavax.net.ssl.trustStorePassword=server \
-Djavax.net.ssl.keyStorePassword=server -Djava.rmi.server.codebase=$CODEBASE_DIR \
-classpath :bin servers.RmiRegistry &

sleep 3

cd $PROJ_DIR

java -classpath :bin/:lib/gson-2.2.3.jar -Djava.rmi.server.codebase=$CODEBASE_DIR \
-Dservers.impl.codebase=$CODEBASE_DIR -Djava.security.policy=$POLICY_DIR/policy \
-Dservers.classeserver=servers.GameServer -Dservers.policy=$POLICY_DIR/policy \
-Djavax.net.ssl.trustStore=$PROJ_DIR/server.keystore -Djavax.net.ssl.keyStore=$PROJ_DIR/server.keystore \
-Djavax.net.ssl.keyStorePassword=server -Djava.rmi.activation.port=5551 \
-Djavax.net.ssl.trustStorePassword=server servers.SetupGame &

sleep 1

cd $PROJ_DIR
     
java -classpath :bin/ -Djava.rmi.server.codebase=$CODEBASE_DIR -Djava.rmi.codebase=$CODEBASE_DIR -Dservers.impl.codebase=$CODEBASE_DIR \
     -Djava.security.policy=$POLICY_DIR/policy -Dservers.classeserver=servers.LoginServer \
     -Dservers.policy=$POLICY_DIR/policy -Djavax.net.ssl.trustStore=$PROJ_DIR/server.keystore \
     -Djavax.net.ssl.keyStore=$PROJ_DIR/server.keystore -Djavax.net.ssl.keyStorePassword=server -Djava.rmi.activation.port=5551 \
     servers.SetupLogin &

sleep 1

cd $PROJ_DIR

java -Djava.security.policy=$POLICY_DIR/policy -Djava.rmi.server.codebase=$CODEBASE_DIR \
-Djavax.net.ssl.trustStore=$PROJ_DIR/server.keystore \
-Djavax.net.ssl.keyStore=$PROJ_DIR/server.keystore -Djavax.net.ssl.trustStorePassword=server -Djavax.net.ssl.keyStorePassword=server \
-classpath :bin/ proxies.ProxyServer
