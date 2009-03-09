#!/bin/sh

java -cp lib/derbynet.jar:lib/derby.jar org.apache.derby.drda.NetworkServerControl shutdown -h 192.168.1.102
