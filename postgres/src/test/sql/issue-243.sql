-- should display an ERROR and not also crash the connected ES node
select * from so_posts where zdb('so_posts', ctid) ==> '"';