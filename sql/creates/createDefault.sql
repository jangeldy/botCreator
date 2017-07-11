create table command
(
  id SERIAL PRIMARY KEY NOT NULL,
  command_text varchar(200),
  handling_class varchar(200) NOT NULL,
  step varchar(200),
  description varchar(200)
);


CREATE TABLE access_level
(
  id INTEGER PRIMARY KEY NOT NULL,
  name VARCHAR(200),
  enum_name VARCHAR(200) NOT NULL
);
CREATE UNIQUE INDEX access_level_enum_name_uindex ON access_level (enum_name);


CREATE TABLE users
(
  id SERIAL PRIMARY KEY NOT NULL,
  user_name VARCHAR(200),
  user_surname VARCHAR(200),
  chat_id BIGINT NOT NULL,
  id_access_level INTEGER,
  CONSTRAINT users_access_level_id_fk FOREIGN KEY (id_access_level) REFERENCES access_level (id)
);
CREATE UNIQUE INDEX users_chat_id_uindex ON users (chat_id);


CREATE TABLE keyboard
(
  id INTEGER PRIMARY KEY NOT NULL,
  description VARCHAR(500),
  inline BOOLEAN
);

CREATE TABLE keyboard_row
(
  id SERIAL PRIMARY KEY NOT NULL,
  id_keyboard INTEGER,
  CONSTRAINT keyboard_row_keyboard_id_fk FOREIGN KEY (id_keyboard) REFERENCES keyboard (id)
);

CREATE TABLE keyboard_button
(
  id SERIAL PRIMARY KEY NOT NULL,
  id_keyboard_row INTEGER,
  text VARCHAR(200),
  url VARCHAR(1000),
  json VARCHAR(1000),
  rq_contact BOOLEAN,
  rq_location BOOLEAN,
  id_access_level INTEGER,
  CONSTRAINT keyboard_button_keyboard_row_id_fk FOREIGN KEY (id_keyboard_row) REFERENCES keyboard_row (id),
  CONSTRAINT keyboard_button_access_level_id_fk FOREIGN KEY (id_access_level) REFERENCES access_level (id)
);