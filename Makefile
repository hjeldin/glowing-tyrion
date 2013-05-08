COMMONFOLDER = /home/accounts/studenti/id096092/public_html/common/
LOGFOLDER = /home/accounts/studenti/id096092/workspace/glowing-tyrion/log
CURRIP = $(shell ifconfig | sed -n '2 p' | cut -d: -f2 | cut -d\  -f1)
SRVIP = $(CURRIP)
all: FORCE
	@echo "Usage:"
	@echo "	make compile    : compile source code"
	@echo "	make keystore   : create keystores"
	@echo "	make run-server : run server"
	@echo "	make run-client : run client"

compile-run: compile publish run-server FORCE
	

compile: FORCE
	rmic -classpath ./bin -d $(COMMONFOLDER) servers.LoginServer
	rmic -classpath ./bin -d $(COMMONFOLDER) servers.GameServer  

publish: FORCE
	rmid -log $(LOGFOLDER) -J-Djava.rmi.server.codebase=http://$(CURRIP):8000/common/ -J-Djava.security.policy=$(COMMONFOLDER)rmid.policy -port 5551 &
	python -m SimpleHTTPServer &
	mv ./bin/* $(COMMONFOLDER)
	mv -rf $(COMMONFOLDER)bootstrap/* ./bin/bootstrap/
	mv -rf $(COMMONFOLDER)servers/Setup* ./bin/servers/

keystore: FORCE
	/bin/rm -f server.keystore client.keystore
	keytool -genkey -keyalg RSA -keystore server.keystore -dname "CN=Server, OU=Bar, O=Foo, L=Some, ST=Where, C=UN" -storepass server -keypass server
	keytool -genkey -keyalg RSA -keystore client.keystore -dname "CN=Client, OU=Bar, O=Foo, L=Some, ST=Where, C=UN" -storepass client -keypass client
	keytool -export -rfc -keystore server.keystore -alias mykey -file server.public-key -storepass server
	keytool -export -rfc -keystore client.keystore -alias mykey -file client.public-key -storepass client
	keytool -import -trustcacerts -alias client -keystore server.keystore -file client.public-key -storepass server
	keytool -import -trustcacerts -alias server -keystore client.keystore -file server.public-key -storepass client

run-server:
	@sleep 1
	#java -classpath :$(COMMONFOLDER):$(COMMONFOLDER)../bin/ -Djava.rmi.server.codebase=http://$(CURRIP):8000/common/ -Dfilu.impl.codebase=http://$(CURRIP):8000/common/ -Djava.security.policy=$(COMMONFOLDER)setup.policy -Dfilu.classeserver=filu.ServerCassa -Dfilu.policy=$(COMMONFOLDER)group.policy -Djavax.net.ssl.trustStore=server.keystore -Djavax.net.ssl.keyStore=server.keystore  -Djavax.net.ssl.keyStorePassword=server filu.Setup

run-client:
	#java -classpath :$(COMMONFOLDER)../bin/ -Djava.security.policy=$(COMMONFOLDER)group.policy -Djavax.net.ssl.trustStore=client.keystore -Djavax.net.ssl.keyStore=client.keystore  -Djavax.net.ssl.keyStorePassword=client filu.Boot $(SRVIP)
	
unpublish:
	pkill python
	pkill rmid
	
clean: FORCE
	rm -f ./bin/*.class .Rem* *~

FORCE:
