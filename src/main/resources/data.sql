CREATE SEQUENCE user_sequence START 1 INCREMENT 1;
CREATE SEQUENCE post_sequence START 1 INCREMENT 1;
CREATE SEQUENCE comment_sequence START 1 INCREMENT 1;


CREATE TABLE users(
id int8 DEFAULT nextval('user_sequence') PRIMARY KEY ,
username varchar(255) NOT NULL UNIQUE,
password varchar(255) NOT NULL ,
email varchar(255) NOT NULL UNIQUE,
created_at DATE NOT NULL ,
image_url varchar(255) NOT NULL
);

CREATE TABLE posts(
  id int8 DEFAULT nextval('post_sequence') PRIMARY KEY ,
title varchar(255) NOT NULL ,
text varchar(255),
image_url varchar(255) NOT NULL ,
users_id int8 NOT NULL ,
allow_comments bool ,

FOREIGN KEY (users_id) REFERENCES users(id)
);


CREATE TABLE comments(
  id int8 DEFAULT nextval('comment_sequence') PRIMARY KEY  ,
text varchar(255) NOT NULL ,
created_at DATE,
edited DATE ,
posts_id int8 NOT NULL ,
users_id int8 NOT NULL ,
parent_comment_id int8,

FOREIGN KEY (posts_id) REFERENCES posts(id),
FOREIGN KEY (users_id) REFERENCES users(id)
);
