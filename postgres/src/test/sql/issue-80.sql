CREATE TABLE tas_test_table
(
  pk_id bigserial NOT NULL,
  start_date_text text,
  end_date_text text,
  duration text,
  CONSTRAINT tas_test_table_pkey PRIMARY KEY (pk_id)
);

INSERT INTO tas_test_table VALUES (1, '1/1/1999', '12/31/1999', '364');
INSERT INTO tas_test_table VALUES (2, '1/1/1999', '2/3/1999', '33');
INSERT INTO tas_test_table VALUES (3, '12/1/1999', '12/31/1999', '30');
INSERT INTO tas_test_table VALUES (4, '2/5/2015', '12/31/2016', '695');
INSERT INTO tas_test_table VALUES (5, '1/1/1999', 'UNKNOWN', NULL);
INSERT INTO tas_test_table VALUES (6, '2/1/2016', '2/2/2016', '1');

CREATE INDEX idx_tas_test ON tas_test_table USING zombodb(zdb('tas_test_table', ctid), zdb(tas_test_table)) WITH (url='http://localhost:9200/');

SELECT assert(zdb_estimate_count('public.tas_test_table', 'end_date_text =[["12/31/1999","2/3/1999", "12/31/2016", "UNKNOWN", "2/2/2016"]]'), 6, 'double-bracket');
SELECT assert(zdb_estimate_count('public.tas_test_table', 'end_date_text =["12/31/1999","2/3/1999", "12/31/2016", "UNKNOWN", "2/2/2016"]'), 6, 'single-bracket');

DROP TABLE tas_test_table;