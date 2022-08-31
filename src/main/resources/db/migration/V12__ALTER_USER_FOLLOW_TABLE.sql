ALTER TABLE user_follow ADD
   FOREIGN KEY(person_id) REFERENCES users(id);

ALTER TABLE user_follow ADD
   FOREIGN KEY(follow_id) REFERENCES users(id);