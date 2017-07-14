create table command
(
  id SERIAL PRIMARY KEY NOT NULL,
  command_text varchar(200),
  handling_class varchar(200) NOT NULL,
  step varchar(200) NOT NULL,
  description varchar(200)
);
CREATE UNIQUE INDEX command_step_uindex ON command (step);


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
