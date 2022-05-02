CREATE Table likes_dislikes_comments(
    is_like boolean,
    comments_id int8 NOT NULL,
    users_id int8 NOT NULL ,

    FOREIGN KEY (comments_id) REFERENCES comments(id),
    FOREIGN KEY (users_id) REFERENCES users(id)
);

