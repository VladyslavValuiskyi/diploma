CREATE SEQUENCE IF NOT EXISTS sq__user_id;

CREATE TABLE IF NOT EXISTS users (
  id           INT8 NOT NULL DEFAULT nextval('sq__user_id') PRIMARY KEY,
  password     VARCHAR(50) NOT NULL,
  username        VARCHAR(256) ,
  enabled      BOOLEAN      DEFAULT TRUE,
  name         VARCHAR(255) DEFAULT NULL
);

ALTER TABLE ONLY users ADD CONSTRAINT x1users UNIQUE username;

CREATE TABLE IF NOT EXISTS authorities (
  user_id  INT8 NOT NULL,
  authority VARCHAR(50) NOT NULL,
  CONSTRAINT fk_authorities_users FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT ix_authority UNIQUE (user_id, authority)
);

-- to change field type
alter table  pictures alter base_64 type text;
