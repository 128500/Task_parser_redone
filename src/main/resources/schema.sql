CREATE TABLE tires
(
  tire_id          SERIAL NOT NULL
    CONSTRAINT tires_pkey
    PRIMARY KEY,
  type             VARCHAR(30),
  manufacturer     VARCHAR(40),
  model            VARCHAR(60),
  width            INTEGER,
  height           INTEGER,
  diameter         NUMERIC(5, 1),
  season           VARCHAR(20),
  load_index       VARCHAR(7),
  speed_index      VARCHAR(3),
  strengthen       BOOLEAN,
  studded          BOOLEAN,
  additional_param VARCHAR(10),
  country          VARCHAR(50),
  year             INTEGER,
  balance          INTEGER,
  price            NUMERIC(7, 2)
);