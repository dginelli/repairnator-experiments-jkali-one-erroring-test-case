CREATE OR REPLACE FUNCTION test_issue250(user_query text, expected_message text) RETURNS text LANGUAGE plpgsql AS $$
DECLARE
  errmsg text;
BEGIN
  BEGIN
    select id from so_Posts where zdb('so_posts', ctid) ==> user_query;
  EXCEPTION WHEN OTHERS THEN
    GET STACKED DIAGNOSTICS errmsg = MESSAGE_TEXT;

    IF errmsg ILIKE '%' || expected_message || '%' THEN
      RETURN 'got proper error';
    ELSE
      -- not the right error
      RAISE EXCEPTION '%', errmsg;
    END IF;
  END;

  RAISE EXCEPTION 'Expected an error from ES but didn not receive it';
END;
$$;

SELECT test_issue250('id:~"^.*"', 'Can only use regexp queries on keyword and text fields');
SELECT test_issue250('id:42*', 'Can only use prefix queries on keyword and text fields');

DROP FUNCTION test_issue250(text, text);