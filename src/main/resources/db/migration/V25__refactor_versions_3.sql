START TRANSACTION;

ALTER TABLE `branch` ADD `project_id` bigint(20) NOT NULL DEFAULT '-1';

ALTER TABLE branch
ADD FOREIGN KEY (project_id) REFERENCES `project`(id);

COMMIT;