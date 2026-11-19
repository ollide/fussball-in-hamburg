CREATE TABLE IF NOT EXISTS zips_nearby (
  -- id: zip + distance in m (eg. 2035910000
  id VARCHAR(11) NOT NULL PRIMARY KEY,
  nearby CLOB NOT NULL
);