#!/bin/bash

cd /Volumes/mshang/GoFlex\ Home\ Public/matthew/m_3D/

echo -n "Enter mode > "
read input

if [ $input == "server" ]; then
	java -classpath bin:./bin server.GameServer	
elif [ $input == "client" ]; then
	java -classpath bin:./bin client.GameClient
else
	echo "Command not recognized"
fi