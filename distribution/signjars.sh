#!/bin/sh

mkdir lib

#            jlGuiSpx.jar    wordlisttrainerCURRENT.jar  

BINDIR=/home/kjellw/workspace/word_list_trainer/lib
#1
JARNAME=wordlisttrainerCURRENT.jar

cp $BINDIR/$JARNAME ./lib/wordlisttrainer$1.jar

jarsigner -keystore ../wltkeystore -storepass Latuv5rit -keypass Latuv5rit  ./lib/wordlisttrainer$1.jar wltkey 
#2
JARNAME=derbyclient.jar

cp $BINDIR/$JARNAME ./lib/$JARNAME

jarsigner -keystore ../wltkeystore -storepass Latuv5rit -keypass Latuv5rit  ./lib/$JARNAME wltkey
#3
JARNAME=derby.jar

cp $BINDIR/$JARNAME ./lib/$JARNAME

jarsigner -keystore ../wltkeystore -storepass Latuv5rit -keypass Latuv5rit  ./lib/$JARNAME wltkey
#4
JARNAME=derbynet.jar

cp $BINDIR/$JARNAME ./lib/$JARNAME

jarsigner -keystore ../wltkeystore -storepass Latuv5rit -keypass Latuv5rit  ./lib/$JARNAME wltkey
#5
JARNAME=derbyrun.jar

cp $BINDIR/$JARNAME ./lib/$JARNAME

jarsigner -keystore ../wltkeystore -storepass Latuv5rit -keypass Latuv5rit  ./lib/$JARNAME wltkey 
#6
JARNAME=derbytools.jar

cp $BINDIR/$JARNAME ./lib/$JARNAME

jarsigner -keystore ../wltkeystore -storepass Latuv5rit -keypass Latuv5rit  ./lib/$JARNAME wltkey
#7
JARNAME=formsrt.jar

cp $BINDIR/$JARNAME ./lib/$JARNAME

jarsigner -keystore ../wltkeystore -storepass Latuv5rit -keypass Latuv5rit  ./lib/$JARNAME wltkey
#8
JARNAME=jspeex.jar

cp $BINDIR/$JARNAME ./lib/$JARNAME

jarsigner -keystore ../wltkeystore -storepass Latuv5rit -keypass Latuv5rit  ./lib/$JARNAME wltkey
#9
JARNAME=xstream-1.2.1.jar

cp $BINDIR/$JARNAME ./lib/$JARNAME

jarsigner -keystore ../wltkeystore -storepass Latuv5rit -keypass Latuv5rit  ./lib/$JARNAME wltkey
