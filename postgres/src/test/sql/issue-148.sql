CREATE TABLE issue148 (null_field text[]);
INSERT INTO issue148 VALUES (ARRAY['one', null, 'two']);
CREATE INDEX idxissue148 ON issue148 USING zombodb (zdb('issue148', ctid), zdb(issue148)) WITH (url='http://localhost:9200/');

SELECT * FROM issue148 WHERE zdb('issue148', ctid) ==> 'null_field:null';
DROP TABLE issue148;