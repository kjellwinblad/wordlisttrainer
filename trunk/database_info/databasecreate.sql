connect 'jdbc:derby:LocalDB;create=true';

connect 'jdbc:derby://localhost:1527/NetworkDB;create=true;user=me;password=mine';

DROP TABLE WORDS;

DROP TABLE WORD_LISTS;

DROP TABLE WORD_LIST_WORDS;

CREATE TABLE WORD_LIST_WORDS (
  id INTEGER GENERATED ALWAYS AS IDENTITY,
  word_list_id INTEGER,
  wordAID INTEGER,
  wordBID INTEGER,
  dbVersion INTEGER,
PRIMARY KEY(id));


CREATE TABLE WORD_LISTS (
  id INTEGER GENERATED ALWAYS AS IDENTITY,
  word_list_name CLOB,
  languageA CLOB,
  languageB CLOB,
  dbVersion INTEGER,
  PRIMARY KEY(id));




CREATE TABLE WORDS (
  id INTEGER GENERATED ALWAYS AS IDENTITY,
  word CLOB,
  language CLOB,
  wordcomment CLOB,
  sound BLOB,
  dbVersion INTEGER,
PRIMARY KEY(id));




