CREATE TABLE posts(
  id int8 DEFAULT nextval('post_sequence') PRIMARY KEY ,
title varchar(255) NOT NULL ,
text varchar(255),
image_url varchar(800) NOT NULL ,
users_id int8 NOT NULL ,
allow_comments bool ,

FOREIGN KEY (users_id) REFERENCES users(id)
);