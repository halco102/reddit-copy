CREATE OR REPLACE FUNCTION rec_insert_temp_user_notifications()
  RETURNS trigger AS
$$
BEGIN
         INSERT INTO user_notifications_temp (users_id, posts_id)
         VALUES(NEW.users_id,NEW.posts_id);

    RETURN NEW;
END;
$$
LANGUAGE 'plpgsql';