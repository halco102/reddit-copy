create table user_notifications(
users_id int8,
posts_id int8,

foreign key (users_id) references users(id),
foreign key (posts_id) references posts(id)
);