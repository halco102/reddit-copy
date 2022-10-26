CREATE OR REPLACE TRIGGER ins_same_values_user_notifications
  AFTER INSERT
  ON user_notifications
  FOR EACH ROW
  EXECUTE PROCEDURE rec_insert_temp_user_notifications();