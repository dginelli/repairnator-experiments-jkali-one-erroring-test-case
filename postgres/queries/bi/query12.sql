/* Q12. Trending Posts
\set date '\'2011-07-22T00:00:00.000+00:00\''::timestamp
\set likeThreshold 400
 */
SELECT m.ps_postid AS "message.id"
     , m.ps_creationdate AS "message.creationDate"
     , c.p_firstname AS "creator.firstName"
     , c.p_lastname AS "creator.lastName"
     , count(*) as likeCount
  FROM post m
     , person c -- creator
     , likes l
 WHERE 1=1
    -- join
   AND m.ps_creatorid = c.p_personid
   AND m.ps_postid = l.l_postid
    -- filter
   AND m.ps_creationdate > :date
 GROUP BY m.ps_postid
        , m.ps_creationdate
        , c.p_firstname
        , c.p_lastname
HAVING count(*) > :likeThreshold
 ORDER BY likeCount DESC, m.ps_postid
 LIMIT 100
;
