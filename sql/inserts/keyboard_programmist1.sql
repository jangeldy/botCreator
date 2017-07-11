/*
Образец запроса для создания клавиатуры

DO $$
DECLARE
  keyboardId int = 1; --> // нужно задать id в ручную
  keyboard_row_last_id1 int;
  keyboard_row_last_id2 int;
BEGIN
  -- // запись клавиатуры
  INSERT INTO keyboard (id, description, inline) VALUES (keyboardId, 'Для благодарностей', FALSE );

  -- // запись строк клавиатуры
  INSERT INTO keyboard_row (id_keyboard) VALUES (keyboardId) RETURNING id INTO keyboard_row_last_id1;     -- // строка 1
  INSERT INTO keyboard_row (id_keyboard) VALUES (keyboardId) RETURNING id INTO keyboard_row_last_id2;     -- // строка 2

  -- // запись кнопок клавиатуры
  INSERT INTO keyboard_button (id_keyboard_row, text, url, json, rq_contact, rq_location, id_access_level) VALUES (keyboard_row_last_id1, 'ButtonName1', null, '{"step":stepName}', null, null, null);
  INSERT INTO keyboard_button (id_keyboard_row, text, url, json, rq_contact, rq_location, id_access_level) VALUES (keyboard_row_last_id1, 'ButtonName2', null, '{"step":stepName}', null, null, null);
  INSERT INTO keyboard_button (id_keyboard_row, text, url, json, rq_contact, rq_location, id_access_level) VALUES (keyboard_row_last_id2, 'ButtonName3', null, null, null, null, null);
END $$;
*/

