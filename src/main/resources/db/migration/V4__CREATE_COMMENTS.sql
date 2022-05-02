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