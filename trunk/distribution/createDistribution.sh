#!/bin/sh
mkdir files
cd files

../signjars.sh $1

echo  -n '<?xml version="1.0" encoding="UTF-8"?>
<jnlp spec="0.2 1.0" codebase="http://www.cs.umu.se/~kjellw/wltws" href="http://www.cs.umu.se/~kjellw/wltws/wordlisttrainer' > wordlisttrainer$1.jnlp
echo -n $1  >> wordlisttrainer$1.jnlp
echo -n '.jnlp">
        <information>
                <title>Word List Trainer</title>

                <vendor>Kjell Winblad</vendor>

                <homepage href="http://wordlisttrainer.googlecode.com"/>
                
                <description>Word List Trainer is a tool for foreign language learning. A frequently used technique when learning words is to use tables with the words in the mother language in one of the columns and the foreign words in the other. Word List Trainer makes it easy to create this kind of tables and to attach sounds to the words. The word lists can be exported to sound files that can be downloaded to e.g. mp3 players. It is possible to save the word lists in either a local database or a network database and to copy the word lists to and from the databases. This is a handy functionality when students are studying together and are learning the same words.</description>

                <description kind="short">This is a program that is made for language learning and that makes it easy to create and share word lists with the source language in one column and the language that shall be learned in the other as well as recording and attaching sounds to every word.</description>

                <description kind="tooltip">Word List Trainer</description>

                <icon href="href="http://www.cs.umu.se/~kjellw/wltws/lib/icon.png" kind="default"/>

                <offline-allowed/>

        </information>

        <security>
                <all-permissions/>
        </security>

        <resources arch="" os="">
                <j2se version="1.6+" href="http://java.sun.com/products/autodl/j2se"/>
                <j2se version="1.6+"/>
                <jar href="http://wordlisttrainer.googlecode.com/files/wordlisttrainer' >> wordlisttrainer$1.jnlp
		echo -n $1 >> wordlisttrainer$1.jnlp
		echo -n '.jar"/>
                <jar href="http://www.cs.umu.se/~kjellw/wltws/lib/derbyclient.jar"/>
                <jar href="http://www.cs.umu.se/~kjellw/wltws/lib/derby.jar"/>
                <jar href="http://www.cs.umu.se/~kjellw/wltws/lib/derbynet.jar"/>
                <jar href="http://www.cs.umu.se/~kjellw/wltws/lib/derbyrun.jar"/>
                <jar href="http://www.cs.umu.se/~kjellw/wltws/lib/derbytools.jar"/>
                <jar href="http://www.cs.umu.se/~kjellw/wltws/lib/formsrt.jar"/>
                <jar href="http://www.cs.umu.se/~kjellw/wltws/lib/jspeex.jar"/>
                <jar href="http://www.cs.umu.se/~kjellw/wltws/lib/xstream-1.2.1.jar"/>
        </resources>
        <application-desc main-class="org.wlt.gui.WordListTrainer"/>
</jnlp>'   >> wordlisttrainer$1.jnlp

echo -n '<a href="http://www.cs.umu.se/~kjellw/wltws' >> links
echo -n $1 >> links
echo -n '.jnlp">Version ' >> links
echo -n $1 >> links
echo '</a><br>' >> links

#Sends everything to the server

echo 'Write your cs umu password'

scp -r . kjellw@salt.cs.umu.se:~/public_html/wltws

echo 'Upload to google code'

cd ..

/home/kjellw/workspace/word_list_trainer/distribution/googlecode_upload.py -s "Jar-file used when lunching the program with java web start" -p wordlisttrainer -u kjellwinblad -w $2 files/lib/wordlisttrainer$1.jar

/home/kjellw/workspace/word_list_trainer/distribution/googlecode_upload.py -s "Java Web Start-file. This files contains instructions for installing and running the program. Open this with a Java Web Start implementation. (The command javaws on most systems)" -p wordlisttrainer -u kjellwinblad -w $2 files/wordlisttrainer$1.jnlp

echo 'Create zip-file'

mkdir word_list_trainer_$1

mkdir word_list_trainer_$1/bin

cp README.txt word_list_trainer_$1

cp files/lib/* word_list_trainer_$1/bin

tar -cvvzf word_list_trainer_$1.tar.gz word_list_trainer_$1

mv word_list_trainer_$1 word_list_trainer

zip -r word_list_trainer word_list_trainer

mv word_list_trainer word_list_trainer_$1

mv word_list_trainer.zip word_list_trainer_$1.zip

/home/kjellw/workspace/word_list_trainer/distribution/googlecode_upload.py -s "This file contains all binary files necessary to run the program" -p wordlisttrainer -u kjellwinblad -w $2 word_list_trainer_$1.zip

/home/kjellw/workspace/word_list_trainer/distribution/googlecode_upload.py -s "This file contains all binary files necessary to run the program" -p wordlisttrainer -u kjellwinblad -w $2 word_list_trainer_$1.tar.gz

echo 'Test the word list trainer program'

/usr/lib/jvm/java-6-sun-1.6.0.10/bin/javaws files/wordlisttrainer$1.jnlp

