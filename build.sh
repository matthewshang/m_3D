#!/bin/bash

cd /Volumes/mshang/GoFlex\ Home\ Public/matthew/m_3D/
javac -d bin -sourcepath src src/client/*.java
javac -d bin -sourcepath src src/core/*.java
javac -d bin -sourcepath src src/objects/*.java
javac -d bin -sourcepath src src/server/*.java