connect 'jdbc:derby://localhost:1527/DB;create=true;user=me;password=mine';

DROP TABLE WORDS;

DROP TABLE WORD_LISTS;

DROP TABLE WORD_LIST_WORDS;

CREATE TABLE WORD_LIST_WORDS (
  id INTEGER GENERATED ALWAYS AS IDENTITY,
  word_list_id INTEGER,
  wordAID INTEGER,
  wordBID INTEGER,
PRIMARY KEY(id));


CREATE TABLE WORD_LISTS (
  id INTEGER GENERATED ALWAYS AS IDENTITY,
  word_list_name CLOB,
  languageA CLOB,
  languageB CLOB,
  PRIMARY KEY(id));




CREATE TABLE WORDS (
  id INTEGER GENERATED ALWAYS AS IDENTITY,
  word CLOB,
  language CLOB,
  wordcomment CLOB,
  sound BLOB,
PRIMARY KEY(id));




