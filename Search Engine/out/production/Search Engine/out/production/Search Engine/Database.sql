DROP DATABASE IF EXISTS SearchEngine;
CREATE DATABASE SearchEngine;
DROP TABLE IF EXISTS SearchEngine.urls;
CREATE TABLE SearchEngine.urls(
    id INT AUTO_INCREMENT PRIMARY KEY,
    url varchar(2048) NOT NULL,
    crawldate date default null,
    titles Text,
    indexed    bool default 0
);
INSERT INTO searchengine.urls (`url`) VALUES ('https://www.msn.com/');
INSERT INTO searchengine.urls (`url`) VALUES ('https://www.yahwordoo.com/');
INSERT INTO searchengine.urls (`url`) VALUES ('https://www.reddit.com/');
INSERT INTO searchengine.urls (`url`) VALUES ('https://www.geeksforgeeks.org/');
INSERT INTO searchengine.urls (`url`) VALUES ('https://www.imdb.com/');
INSERT INTO searchengine.urls (`url`) VALUES ('https://www.spotify.com/eg-en/');
INSERT INTO searchengine.urls (`url`) VALUES ('https://www.gamespot.com/');
INSERT INTO searchengine.urls (`url`) VALUES ('https://www.skysports.com/');
INSERT INTO searchengine.urls (`url`) VALUES ('https://www.google.com/');

-- SELECT * FROM searchengine.urls where crawldate = NULL;
SET SQL_SAFE_UPDATES = 0;
UPDATE searchengine.urls SET crawldate = current_date() WHERE url = 'https://www.google.com/';
DROP TABLE IF EXISTS SearchEngine.searched;
CREATE TABLE SearchEngine.searched(
id int(11) auto_increment primary key,
word text,
count int(11)
);
DROP TABLE IF EXISTS SearchEngine.Words;
CREATE TABLE SearchEngine.Words(
   stem varchar(256),
   TF double,
   preview text,
   URLID INT,
   primary key (stem,URLID),
   foreign key(URLID) references URLs(id)
);
DROP DATABASE IF EXISTS content;
CREATE DATABASE content;
DROP TABLE IF EXISTS content.urlcontent;
CREATE TABLE content.urlcontent(
 id INT AUTO_INCREMENT PRIMARY KEY,
 url varchar(2048) not null,
 content longtext
);
