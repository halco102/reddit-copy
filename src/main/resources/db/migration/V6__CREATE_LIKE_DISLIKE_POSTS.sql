CREATE TABLE post_likes_dislikes(

    is_like boolean,
    users_id int8 NOT NULL ,
    posts_id int8 NOT NULL,

    FOREIGN KEY (users_id) REFERENCES users(id),
    FOREIGN KEY (posts_id) REFERENCES posts(id)
);