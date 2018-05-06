--26/12/2017
ALTER TABLE public.childrens ADD COLUMN "is_deleted" BOOLEAN DEFAULT FALSE;

--27/12/2017
ALTER TABLE public.tasks ADD COLUMN "is_deleted" BOOLEAN DEFAULT FALSE;

--29/12/2017

GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO earnituser;
GRANT ALL PRIVILEGES ON ALL TABLES  IN SCHEMA public TO earnituser;

ALTER TABLE public.goals ADD COLUMN "is_deleted" BOOLEAN DEFAULT FALSE;

ALTER TABLE public.tasks ADD COLUMN "should_lock_apps_if_task_overdue" BOOLEAN DEFAULT FALSE;

alter table parents add column fcm_token character varying(255);

alter table childrens add column fcm_token character varying(255);

alter table repitition_schedule add column start_time character varying(25);

alter table repitition_schedule add column end_time character varying(25);
