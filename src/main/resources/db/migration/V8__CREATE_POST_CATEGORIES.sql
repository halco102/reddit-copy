create table post_categories(
    posts_id int8,
    categories_id int8,

    FOREIGN KEY (posts_id) REFERENCES posts(id),
    FOREIGN KEY (categories_id) REFERENCES categories(id)
);