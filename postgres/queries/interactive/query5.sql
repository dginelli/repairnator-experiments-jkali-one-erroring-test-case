select f_title, count(ps_postid)
from (
select f_title, f_forumid, f.k_person2id
from forum, forum_person,
 ( select k_person2id
   from knows
   where
   k_person1id = :Person
   union
   select k2.k_person2id
   from knows k1, knows k2
   where
   k1.k_person1id = :Person and k1.k_person2id = k2.k_person1id and k2.k_person2id <> :Person
 ) f
where f_forumid = fp_forumid and fp_personid = f.k_person2id and
      fp_creationdate >= :Date0
) tmp left outer join post
on tmp.f_forumid = ps_forumid and ps_creatorid = tmp.k_person2id
group by f_forumid, f_title
order by 2 desc, f_forumid
limit 20
